package pwr.chrzescijanek.filip.gifa.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

public class Injector {

	private static final Map<Class<?>, Object> components = new WeakHashMap<>();
	private static final Set<Object> viewModels = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Function<Class<?>, Object> supplier = createInstanceSupplier();

	private static Function< Class< ? >, Object > createInstanceSupplier() {
		return (c) -> {
			try {
				return c.newInstance();
			} catch (InstantiationException | IllegalAccessException ex) {
				throw new IllegalStateException("Could not instantiate: " + c, ex);
			}
		};
	}

	public static <T> T instantiateViewModel( Class<T> clazz) {
		return instantiateViewModel(clazz, f -> null);
	}

	public static <T> T instantiateViewModel( Class<T> clazz, Function<String, Object> context) {
		@SuppressWarnings("unchecked")
		T viewModel = registerExistingAndInject((T) supplier.apply(clazz));
		Field[] fields = clazz.getDeclaredFields();
		Arrays.stream(fields)
				.filter(field -> field.isAnnotationPresent(Inject.class))
				.forEach(field -> {
					Object value = context.apply(field.getName());
					if (Objects.nonNull(value) )
						injectIntoField(field, viewModel, value);
				});
		return viewModel;
	}

	@SuppressWarnings("unchecked")
	public static <T> T instantiateModelOrService(Class<T> clazz) {
		T product = (T) components.get(clazz);
		if (product == null) {
			product = injectAndInitialize((T) supplier.apply(clazz));
			components.put(clazz, product);
		}
		return clazz.cast(product);
	}

	private static <T> T registerExistingAndInject(T instance) {
		T product = injectAndInitialize(instance);
		viewModels.add(product);
		return product;
	}

	private static <T> T injectAndInitialize(T product) {
		injectMembers(product);
		initialize(product);
		return product;
	}

	private static void injectMembers(final Object instance) {
		injectMembers(instance.getClass(), instance);
	}

	private static void injectMembers(Class<? extends Object> clazz, final Object instance) throws SecurityException {
		Field[] fields = clazz.getDeclaredFields();
		Arrays.stream(fields)
				.filter(field -> field.isAnnotationPresent(Inject.class))
				.filter(field -> isNotPrimitiveOrString(field.getType()))
				.forEach(field -> {
					Object value = instantiateModelOrService(field.getType());
					if (Objects.nonNull(value))
						injectIntoField(field, instance, value);
				});
		Class<? extends Object> superclass = clazz.getSuperclass();
		if (Objects.nonNull(superclass))
			injectMembers(superclass, instance);
	}

	private static boolean isNotPrimitiveOrString(Class<?> type) {
		return !type.isPrimitive() && !type.isAssignableFrom(String.class);
	}

	private static void injectIntoField(final Field field, final Object instance, final Object value) throws SecurityException {
		AccessController.doPrivileged((PrivilegedAction<?>) () -> {
			boolean wasAccessible = field.isAccessible();
			try {
				field.setAccessible(true);
				field.set(instance, value);
				return null;
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				throw new IllegalStateException(String.format("Cannot set value %s to field %s", value, field), ex);
			} finally {
				field.setAccessible(wasAccessible);
			}
		});
	}

	private static void initialize(Object instance) {
		Class<? extends Object> clazz = instance.getClass();
		invokeMethodWithAnnotation(clazz, instance, PostConstruct.class);
	}

	private static void invokeMethodWithAnnotation(Class<?> clazz, final Object instance, final Class<? extends Annotation> annotationClass) throws SecurityException {
		Method[] declaredMethods = clazz.getDeclaredMethods();
		Arrays.stream(declaredMethods)
				.filter(method -> method.isAnnotationPresent(annotationClass))
				.forEach(method -> AccessController.doPrivileged((PrivilegedAction<?>) () -> {
					boolean wasAccessible = method.isAccessible();
					try {
						method.setAccessible(true);
						return method.invoke(instance);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						throw new IllegalStateException(String.format("Problem invoking method %s annotated with %s", method, annotationClass), ex);
					} finally {
						method.setAccessible(wasAccessible);
					}
				}));
		Class<?> superclass = clazz.getSuperclass();
		if (Objects.nonNull(superclass))
			invokeMethodWithAnnotation(superclass, instance, annotationClass);
	}

	public static void reset() {
		components.values().stream().forEach(Injector::destroy);
		viewModels.stream().forEach(Injector::destroy);
		components.clear();
		viewModels.clear();
	}

	private static void destroy(Object instance) {
		Class<? extends Object> clazz = instance.getClass();
		invokeMethodWithAnnotation(clazz, instance, PreDestroy.class);
	}
}
