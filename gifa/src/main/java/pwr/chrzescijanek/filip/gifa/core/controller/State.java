package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.BarChart;
import pwr.chrzescijanek.filip.gifa.core.util.Result;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public enum State {

	INSTANCE;

	public final Map<String, ImageData> images = new TreeMap<>();
	public final ObjectProperty<Result> result = new SimpleObjectProperty<>(null);
	public final ObjectProperty<List<BarChart<String, Number>>> charts = new SimpleObjectProperty<>(null);

}
