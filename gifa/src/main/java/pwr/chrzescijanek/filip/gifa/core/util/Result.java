package pwr.chrzescijanek.filip.gifa.core.util;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class Result {

	public final List<String> imageNames = new ArrayList<>();
	public final Map<String, double[]> results = new TreeMap<>();
	public final Image resultImage;

	public Result( final Image resultImage ) {this.resultImage = resultImage;}

	public void putResults(Map<String, double[]> results) {
		this.results.putAll(results);
	}
}
