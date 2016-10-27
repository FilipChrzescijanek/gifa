package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import pwr.chrzescijanek.filip.gifa.core.util.Result;

import java.util.*;
import java.util.stream.Collectors;

import static pwr.chrzescijanek.filip.gifa.core.controller.State.TriangleSelection.*;

public enum State {

	INSTANCE;

	public ObjectProperty<RectangleOfInterest> selectedSample = new SimpleObjectProperty<>(null);
	public ObjectProperty<RectangleOfInterest> selectedRectangle = new SimpleObjectProperty<>(null);
	public final ObservableMap<String, TransformImageData> transformImages = FXCollections.observableMap(new TreeMap<>());
	public final ObservableMap<String, SamplesImageData> samplesImages = FXCollections.observableMap(new TreeMap<>());
	public final List<List<ImageView>> imageViews = new ArrayList<>();
	public final ObservableList<List<ImageView>> sampleImageViews = FXCollections.observableArrayList();
	public final ObjectProperty<List<Result>> results = new SimpleObjectProperty<>(null);
	public final ObjectProperty<List<List<XYChart.Series>>> series = new SimpleObjectProperty<>(new ArrayList<>());
	public final ObjectProperty<List<List< BarChart< String, Number > >>> charts = new SimpleObjectProperty<>(new ArrayList<>());
	public final ObjectProperty<List< LineChart< String, Number > >> samplesCharts = new SimpleObjectProperty<>(new ArrayList<>());
	private TriangleSelection triangleSelection = NONE;
	private RectangleSelection rectangleSelection = RectangleSelection.NONE;
	public List<Map<String, Color >> seriesColors = new ArrayList<>();
	public Map<String, Color > resultsSeriesColors = new HashMap<>();
	public double x, y;
	public boolean dragStarted;
	public boolean zoom;

	public void setRectangleSelection( final RectangleSelection rectangleSelection ) {
		this.rectangleSelection = rectangleSelection;
	}

	public TriangleSelection getTriangleSelection() {
		return triangleSelection;
	}

	public RectangleSelection getRectangleSelection() {
		return rectangleSelection;
	}

	enum TriangleSelection {
		NONE, FIRST_POINT, SECOND_POINT, THIRD_POINT, MOVE
	}

	enum RectangleSelection {
		NONE, NW, NE, SW, SE, N, S, W, E, DRAG
	}

	public void setSelection(int index) {
		switch ( index ) {
			case 0: triangleSelection = FIRST_POINT; break;
			case 1: triangleSelection = SECOND_POINT; break;
			case 2: triangleSelection = THIRD_POINT; break;
			default: if (triangleSelection != MOVE) triangleSelection = NONE; break;
		}
	}

	public void setMoveTriangle() {
		triangleSelection = MOVE;
	}

	public void setNoSelection() {
		triangleSelection = NONE;
		rectangleSelection=RectangleSelection.NONE;
	}
}
