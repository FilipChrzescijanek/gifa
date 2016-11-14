package pwr.chrzescijanek.filip.gifa.util;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import pwr.chrzescijanek.filip.gifa.model.sample.Sample;
import pwr.chrzescijanek.filip.gifa.model.image.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.image.ImageToAlignData;
import pwr.chrzescijanek.filip.gifa.model.sample.Vertex;

import java.util.LinkedHashMap;

public class SharedState {

	public final ObservableMap< String, ImageToAlignData> imagesToAlign = FXCollections.observableMap(new LinkedHashMap<>());
	public final ObservableMap< String, SamplesImageData> samplesImages = FXCollections.observableMap(new LinkedHashMap<>());
	public final ObjectProperty<Sample> selectedSample = new SimpleObjectProperty<>(null);
	public final ObjectProperty<Vertex> selectedVertex = new SimpleObjectProperty<>(null);
	public final BooleanProperty zoomSample = new SimpleBooleanProperty(false);
	public final BooleanProperty zoomVertex = new SimpleBooleanProperty(false);
	public final BooleanProperty rotate = new SimpleBooleanProperty(false);

}
