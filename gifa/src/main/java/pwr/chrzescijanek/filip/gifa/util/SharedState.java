package pwr.chrzescijanek.filip.gifa.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import pwr.chrzescijanek.filip.gifa.model.image.ImageToAlignData;
import pwr.chrzescijanek.filip.gifa.model.image.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.sample.Sample;
import pwr.chrzescijanek.filip.gifa.model.sample.Vertex;

import java.util.LinkedHashMap;

import static javafx.collections.FXCollections.observableMap;

/**
 * Represents application's state that is shared between components.
 */
public class SharedState {

	/**
	 * Images to be aligned.
	 */
	public final ObservableMap<String, ImageToAlignData> imagesToAlign = observableMap(new LinkedHashMap<>());

	/**
	 * Aligned images that can be compared using user-defined samples.
	 */
	public final ObservableMap<String, SamplesImageData> samplesImages = observableMap(new LinkedHashMap<>());

	/**
	 * Currently selected sample.
	 */
	public final ObjectProperty<Sample> selectedSample = new SimpleObjectProperty<>(null);

	/**
	 * Currently selected triangle vertex.
	 */
	public final ObjectProperty<Vertex> selectedVertex = new SimpleObjectProperty<>(null);

	/**
	 * Indicates if sample should be zoomed.
	 */
	public final BooleanProperty zoomSample = new SimpleBooleanProperty(false);

	/**
	 * Indicates if triangle vertex should be zoomed.
	 */
	public final BooleanProperty zoomVertex = new SimpleBooleanProperty(false);

	/**
	 * Indicates if samples can be rotated.
	 */
	public final BooleanProperty rotate = new SimpleBooleanProperty(false);

	/**
	 * Default constructor.
	 */
	public SharedState() { }

}
