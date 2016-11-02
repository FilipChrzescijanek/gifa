package pwr.chrzescijanek.filip.gifa.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import pwr.chrzescijanek.filip.gifa.inject.Injector;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class FXView {

	private final ObjectProperty< Object > controllerProperty = new SimpleObjectProperty<>();
	private final URL resource;

	private FXMLLoader fxmlLoader;

	public FXView( String path ) {
		this.resource = getClass().getResource(path);
	}

	public Object getController() {
		this.initializeFXMLLoader();
		return this.controllerProperty.get();
	}

	public Parent getView() {
		this.initializeFXMLLoader();
		return fxmlLoader.getRoot();
	}

	private void initializeFXMLLoader() {
		if ( Objects.isNull(this.fxmlLoader) ) {
			this.fxmlLoader = this.load(resource);
			this.controllerProperty.set(this.fxmlLoader.getController());
		}
	}

	private FXMLLoader load( final URL resource ) throws IllegalStateException {
		final FXMLLoader loader = new FXMLLoader(resource);
		Callback< Class< ? >, Object > controllerFactory = Injector::instantiate;
		loader.setControllerFactory(controllerFactory);
		tryToLoad(loader);
		return loader;
	}

	private void tryToLoad( final FXMLLoader loader ) {
		try {
			loader.load();
		} catch ( IOException ex ) {
			throw new IllegalStateException("Could not load view: " + loader.getLocation().getPath(), ex);
		}
	}

}