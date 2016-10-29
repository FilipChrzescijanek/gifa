package pwr.chrzescijanek.filip.gifa.core.generator;

import com.sun.javafx.UnmodifiableArrayList;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.DoubleStream;

public class DataGenerator {

	private final Map< String, EvaluationFunction > functions;

	DataGenerator( final Map< String, EvaluationFunction > functions ) {
		this.functions = Collections.unmodifiableMap(functions);
	}

	public Map<String, UnmodifiableArrayList<Double>> generateData( final Mat[] images ) {
		final Mat[] adjustedImages = ImageUtils.extractMeaningfulPixels(images);
		return calculateStatistics(adjustedImages);
	}

	private Map<String, UnmodifiableArrayList<Double>> calculateStatistics( final Mat[] images ) {
		Map<String, UnmodifiableArrayList<Double>> results = new TreeMap<>();
		for ( Map.Entry<String, EvaluationFunction> e : functions.entrySet() ) {
			final double[] result = e.getValue().evaluate(ImageUtils.getImagesCopy(images));
			results.put(e.getKey(), boxResult(result));
		}
		return results;
	}

	private UnmodifiableArrayList<Double> boxResult( double[] results) {
		Double[] boxed = DoubleStream.of(results).boxed().toArray(Double[]::new);
		return new UnmodifiableArrayList<>(boxed, boxed.length);
	}

}
