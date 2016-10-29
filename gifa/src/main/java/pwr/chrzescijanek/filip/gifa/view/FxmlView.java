package pwr.chrzescijanek.filip.gifa.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import pwr.chrzescijanek.filip.gifa.inject.Injector;
import pwr.chrzescijanek.filip.gifa.inject.ViewModelFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.ResourceBundle.getBundle;

public class FxmlView extends Pane {

	private final ObjectProperty< Object > viewModelProperty;
	private final URL resource;
	private final ResourceBundle bundle;
	private final Function< String, Object > injectionContext;

	private FXMLLoader fxmlLoader;

	public FxmlView( String path ) {
		this(path, path.substring(path.lastIndexOf('/')), f -> null);
	}

	public FxmlView( String path, String bundleName ) {
		this(path, bundleName, f -> null);
	}

	public FxmlView( String path, String bundleName, Function< String, Object > injectionContext ) {
		this.injectionContext = injectionContext;
		this.viewModelProperty = new SimpleObjectProperty<>();
		this.resource = getClass().getResource(path);
		this.bundle = getResourceBundle(bundleName);
	}

	private static ResourceBundle getResourceBundle( String name ) {
		try {
			return getBundle(name);
		} catch ( MissingResourceException ex ) {
			return null;
		}
	}

	public Parent getView() {
		this.initializeFXMLLoader();
		return fxmlLoader.getRoot();
	}

	private void initializeFXMLLoader() {
		if ( Objects.isNull(this.fxmlLoader) ) {
			this.fxmlLoader = this.loadSynchronously(resource, bundle);
			this.viewModelProperty.set(this.fxmlLoader.getController());
		}
	}

	private FXMLLoader loadSynchronously( final URL resource, ResourceBundle bundle ) throws IllegalStateException {
		final FXMLLoader loader = new FXMLLoader(resource, bundle);
		ViewModelFactory factory = discover();
		Callback< Class< ? >, Object > controllerFactory =
				( Class< ? > clazz ) -> factory.instantiateViewModel(clazz, this.injectionContext);
		loader.setControllerFactory(controllerFactory);
		try {
			loader.load();
		} catch ( IOException ex ) {
			throw new IllegalStateException("Cannot load " + resource.getPath(), ex);
		}
		return loader;
	}

	private ViewModelFactory discover() {
		Iterable< ViewModelFactory > discoveredFactories = ViewModelFactory.discover();
		List< ViewModelFactory > factories = StreamSupport
				.stream(discoveredFactories.spliterator(), false)
				.collect(Collectors.toList());
		if ( factories.isEmpty() )
			return Injector::instantiateViewModel;
		if ( factories.size() == 1 )
			return factories.get(0);
		throw new IllegalStateException("More than one ViewModelFactories discovered!\n"
				+ factories.toString());
	}

	public Object getViewModel() {
		this.initializeFXMLLoader();
		return this.viewModelProperty.get();
	}

	public ResourceBundle getResourceBundle() {
		return this.bundle;
	}

}