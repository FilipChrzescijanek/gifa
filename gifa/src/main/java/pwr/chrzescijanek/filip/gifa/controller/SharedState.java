package pwr.chrzescijanek.filip.gifa.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import pwr.chrzescijanek.filip.gifa.core.util.Result;
import pwr.chrzescijanek.filip.gifa.model.BaseSample;
import pwr.chrzescijanek.filip.gifa.model.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.TransformImageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public enum SharedState {

	INSTANCE;

	public ObjectProperty< BaseSample > selectedSample = new SimpleObjectProperty<>(null);
	public ObjectProperty< BaseSample > selectedRectangle = new SimpleObjectProperty<>(null);
	public final ObservableMap< String, TransformImageData > transformImages = FXCollections.observableMap(new LinkedHashMap<>());
	public final ObservableMap< String, SamplesImageData > samplesImages = FXCollections.observableMap(new LinkedHashMap<>());
	public final List< List< ImageView > > imageViews = new ArrayList<>();
	public final ObjectProperty< List< Result > > results = new SimpleObjectProperty<>(null);
	public final ObjectProperty< List< List< XYChart.Series > > > series = new SimpleObjectProperty<>(new ArrayList<>());
	public final ObjectProperty< List< List< BarChart< String, Number > > > > charts = new SimpleObjectProperty<>(new ArrayList<>());
	public final ObjectProperty< List< LineChart< String, Number > > > samplesCharts = new SimpleObjectProperty<>(new ArrayList<>());
	private SampleSelection sampleSelection = SampleSelection.NIL;
	public List< Map< String, Color > > seriesColors = new ArrayList<>();
	public Map< String, Color > resultsSeriesColors = new HashMap<>();
	public boolean zoom;

	public void setSampleSelection( final SampleSelection sampleSelection ) {
		this.sampleSelection = sampleSelection;
	}

	public SampleSelection getSampleSelection() {
		return sampleSelection;
	}

	public enum SampleSelection {
		NIL, NW, NE, SW, SE, N, S, W, E, DRAG
	}

}
