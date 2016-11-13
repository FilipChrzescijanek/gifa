package pwr.chrzescijanek.filip.gifa.core.generator;

import com.sun.javafx.UnmodifiableArrayList;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EdgeEvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.util.Result;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.DoubleStream;

import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.extractMeaningfulPixels;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.getImagesCopy;

public class DataGenerator {

    private final Map<String, EvaluationFunction> functions;
    private final Map<String, Mat[]> preprocessedImages = new HashMap<>();

    DataGenerator(final Map<String, EvaluationFunction> functions) {
        this.functions = Collections.unmodifiableMap(functions);
    }

    public Map<String, Mat[]> getPreprocessedImages() {
        return new TreeMap<>(preprocessedImages);
    }

    public Result generateData(final Mat[] images, final List<String> names) {
        preprocessImages(images);
        final Mat[] adjustedImages = extractMeaningfulPixels(images);
        return new Result(names, calculateStatistics(adjustedImages));
    }

    private void preprocessImages(Mat[] images) {
        functions.entrySet().stream().forEach(
                entry -> {
                    final EvaluationFunction function = entry.getValue();
                    if (function instanceof EdgeEvaluationFunction && !preprocessedImages.containsKey(entry.getKey()))
                        preprocessedImages.put(entry.getKey(), ((EdgeEvaluationFunction) function).processBGRA(images));
                }
        );
    }

    private Map<String, UnmodifiableArrayList<Double>> calculateStatistics(final Mat[] images) {
        Map<String, UnmodifiableArrayList<Double>> results = new TreeMap<>();
        functions.entrySet().stream().forEach(entry -> evaluate(images, results, entry));
        return results;
    }

    private void evaluate(Mat[] images, Map<String, UnmodifiableArrayList<Double>> results, Entry<String, EvaluationFunction> entry) {
        final EvaluationFunction function = entry.getValue();
        if (function instanceof EdgeEvaluationFunction) {
            results.put(entry.getKey(), boxResult(function.evaluate(((EdgeEvaluationFunction) function).process(getImagesCopy(images)))));
        } else {
            results.put(entry.getKey(), boxResult(function.evaluate(getImagesCopy(images))));
        }
    }

    private UnmodifiableArrayList<Double> boxResult(double[] results) {
        Double[] boxed = DoubleStream.of(results).boxed().toArray(Double[]::new);
        return new UnmodifiableArrayList<>(boxed, boxed.length);
    }

}
