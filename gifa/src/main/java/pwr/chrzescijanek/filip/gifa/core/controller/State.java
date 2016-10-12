package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import pwr.chrzescijanek.filip.gifa.core.util.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static pwr.chrzescijanek.filip.gifa.core.controller.State.TriangleSelection.*;

public enum State {

	INSTANCE;

	public final Map<String, ImageData> images = new TreeMap<>();
	public final ObjectProperty<Result> result = new SimpleObjectProperty<>(null);
	public final ObjectProperty<List<XYChart.Series>> series = new SimpleObjectProperty<>(new ArrayList<>());
	public final ObjectProperty<List< BarChart< String, Number > >> charts = new SimpleObjectProperty<>(new ArrayList<>());
	private TriangleSelection triangleSelection = NONE;
	private RectangleSelection rectangleSelection = RectangleSelection.NONE;
	public long x, y;

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
	}
}
