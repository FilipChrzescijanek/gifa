package pwr.chrzescijanek.filip.gifa.inject;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Injector {

	private static final Map< Class< ? >, Object > components = new WeakHashMap<>();
	private static final Function< Class< ? >, Object > supplier = createInstanceSupplier();

	private Injector() { }

	private static Function< Class< ? >, Object > createInstanceSupplier() {
		return ( c ) -> {
			try {
				final List< ? extends Constructor< ? > > constructors = getConstructors(c);
				checkForTooManyConstructors(c, constructors);
				if ( constructors.isEmpty() ) return c.newInstance();
				return createInstance(constructors.get(0));
			} catch ( InstantiationException | IllegalAccessException | InvocationTargetException ex ) {
				throw new IllegalStateException("Could not instantiate: " + c, ex);
			}
		};
	}

	private static List< ? extends Constructor< ? > > getConstructors( final Class< ? > c ) {
		return Arrays.stream(c.getConstructors()).filter(r -> r.isAnnotationPresent(Inject.class))
				.collect(Collectors.toList());
	}

	private static void checkForTooManyConstructors( final Class< ? > c, final List< ? extends Constructor< ? > > constructors ) throws
			InstantiationException {
		if ( constructors.size() > 1 ) throw new InstantiationException(
				String.format("Found more than one constructor annotated with @Inject in %s class", c.getName())
		);
	}

	private static Object createInstance( final Constructor< ? > constructor ) throws InstantiationException, IllegalAccessException,
			InvocationTargetException {
		final Object[] values = instantiateParameters(constructor);
		return constructor.newInstance(values);
	}

	private static Object[] instantiateParameters( final Constructor< ? > constructor ) {
		Parameter[] parameters = constructor.getParameters();
		return Arrays.stream(parameters)
				.filter(parameter -> checkIfNotPrimitiveOrString(parameter.getType()))
				.map(parameter -> instantiateComponent(parameter.getType()))
				.toArray();
	}

	public static < T > T instantiate( Class< T > clazz ) {
		@SuppressWarnings( "unchecked" )
		T instance = injectFields((T) supplier.apply(clazz));
		return instance;
	}

	@SuppressWarnings( "unchecked" )
	public static < T > T instantiateComponent( Class< T > clazz ) {
		T product = (T) components.get(clazz);
		if ( product == null ) {
			product = injectFields((T) supplier.apply(clazz));
			components.put(clazz, product);
		}
		return clazz.cast(product);
	}

	public static void reset() {
		components.clear();
	}

	private static < T > T injectFields( final T instance ) {
		injectFields(instance.getClass(), instance);
		return instance;
	}

	private static void injectFields( Class< ? > clazz, final Object instance ) {
		Field[] fields = clazz.getDeclaredFields();
		Arrays.stream(fields)
				.filter(field -> field.isAnnotationPresent(Inject.class))
				.filter(field -> checkIfNotPrimitiveOrString(field.getType()))
				.forEach(field -> {
					Object value = instantiateComponent(field.getType());
					if ( Objects.nonNull(value) )
						injectField(field, instance, value);
				});
		Class< ? > superclass = clazz.getSuperclass();
		if ( Objects.nonNull(superclass) )
			injectFields(superclass, instance);
	}

	private static boolean checkIfNotPrimitiveOrString( Class< ? > type ) {
		return !type.isPrimitive() && !type.isAssignableFrom(String.class);
	}

	private static void injectField( final Field field, final Object instance, final Object value ) {
		AccessController.doPrivileged((PrivilegedAction< ? >) () -> {
			boolean wasAccessible = field.isAccessible();
			try {
				field.setAccessible(true);
				field.set(instance, value);
				return null;
			} catch ( IllegalArgumentException | IllegalAccessException ex ) {
				throw new IllegalStateException(String.format("Could not set value %s to field %s", value, field), ex);
			} finally {
				try {
					field.setAccessible(wasAccessible);
				} catch ( SecurityException e ) {
					e.printStackTrace();
				}
			}
		});
	}
}
