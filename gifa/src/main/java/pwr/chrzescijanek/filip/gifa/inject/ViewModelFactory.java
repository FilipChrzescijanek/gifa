package pwr.chrzescijanek.filip.gifa.inject;

import java.util.ServiceLoader;
import java.util.function.Function;

@FunctionalInterface
public interface ViewModelFactory {

	<T> T instantiateViewModel(Class<T> clazz, Function<String, Object> injectionContext);

	static Iterable<ViewModelFactory> discover() {
		return ServiceLoader.load(ViewModelFactory.class);
	}

}
