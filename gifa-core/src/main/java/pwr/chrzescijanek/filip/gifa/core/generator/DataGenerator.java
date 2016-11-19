package pwr.chrzescijanek.filip.gifa.core.generator;

import com.sun.javafx.UnmodifiableArrayList;
import javafx.util.Pair;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EdgeEvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.util.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.DoubleStream;

import static java.util.Collections.unmodifiableMap;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.extractMeaningfulPixels;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.getImagesCopy;

/**
 * Provides methods for calculating results based on global image features evaluation.
 */
public class DataGenerator {

	private final Map<String, EvaluationFunction> functions;

	private final Map<String, Pair<String[], Mat[]>> preprocessedImages = new HashMap<>();

	DataGenerator(final Map<String, EvaluationFunction> functions) {
		this.functions = unmodifiableMap(functions);
	}

	/**
	 * @return preprocessed images
	 */
	public Map<String, Pair<String[], Mat[]>> getPreprocessedImages() {
		return new TreeMap<>(preprocessedImages);
	}

	/**
	 * Evaluates global features of given images with given names.
	 *
	 * @param images images to be evaluated
	 * @param names  associated image names
	 * @return result of evaluation
	 */
	public Result generateData(final Mat[] images, final List<String> names) {
		final List<String> pairs = getPairNames(names);
		preprocessImages(images, pairs);
		final Mat[] adjustedImages = extractMeaningfulPixels(images);
		final List<List<String>> imageNames = getImageNames(names, pairs);
		return new Result(imageNames, calculateStatistics(adjustedImages));
	}

	private List<String> getPairNames(final List<String> names) {
		final List<String> pairs = new ArrayList<>(names);
		for (int i = 0; i < names.size(); i++)
			for (int j = i + 1; j < names.size(); j++)
				pairs.add(names.get(i) + " x " + names.get(j));
		return pairs;
	}

	private void preprocessImages(final Mat[] images, final List<String> pairs) {
		functions.entrySet().stream().forEach(
				entry -> {
					final EvaluationFunction function = entry.getValue();
					if (function instanceof EdgeEvaluationFunction && !preprocessedImages.containsKey(entry.getKey()))
						preprocessedImages.put(entry.getKey(),
						                       new Pair<>(pairs.toArray(new String[0]),
						                                  ((EdgeEvaluationFunction) function).processBGRA(images)));
				});
	}

	private List<List<String>> getImageNames(final List<String> names, final List<String> pairs) {
		final List<List<String>> imageNames = new ArrayList<>();
		functions.entrySet().stream().forEach(entry -> {
			final EvaluationFunction function = entry.getValue();
			if (function instanceof EdgeEvaluationFunction)
				imageNames.add(pairs.subList(names.size(), pairs.size()));
			else
				imageNames.add(names);
		});
		return imageNames;
	}

	private Map<String, UnmodifiableArrayList<Double>> calculateStatistics(final Mat[] images) {
		final Map<String, UnmodifiableArrayList<Double>> results = new TreeMap<>();
		functions.entrySet().stream().forEach(entry -> evaluate(images, results, entry));
		return results;
	}

	private void evaluate(final Mat[] images, final Map<String, UnmodifiableArrayList<Double>> results,
	                      final Entry<String, EvaluationFunction> entry) {
		final EvaluationFunction function = entry.getValue();
		if (function instanceof EdgeEvaluationFunction)
			results.put(entry.getKey(), boxResult(
					function.evaluate(((EdgeEvaluationFunction) function).process(getImagesCopy(images)))));
		else
			results.put(entry.getKey(), boxResult(function.evaluate(getImagesCopy(images))));
	}

	private UnmodifiableArrayList<Double> boxResult(final double[] results) {
		final Double[] boxed = DoubleStream.of(results).boxed().toArray(Double[]::new);
		return new UnmodifiableArrayList<>(boxed, boxed.length);
	}

}
