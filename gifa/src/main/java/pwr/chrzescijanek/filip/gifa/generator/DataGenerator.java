package pwr.chrzescijanek.filip.gifa.generator;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.controller.ImageData;
import pwr.chrzescijanek.filip.gifa.core.controller.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.function.MeanValue;
import pwr.chrzescijanek.filip.gifa.core.function.StdDeviationValue;
import pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;
import pwr.chrzescijanek.filip.gifa.core.util.Result;
import pwr.chrzescijanek.filip.gifa.core.util.ResultImage;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public enum DataGenerator {

	INSTANCE;

	private final Map< String, EvaluationFunction > availableFunctions = new TreeMap<>();
	private final Map< String, EvaluationFunction > chosenFunctions = new TreeMap<>();

	DataGenerator() {
		injectBasicFunctions();
	}

	public void injectBasicFunctions() {
		injectFunction("Mean value", MeanValue.INSTANCE);
		injectFunction("StdDev value", StdDeviationValue.INSTANCE);
		injectFunction("Mean red", ( images, mask ) -> FunctionUtils.calculateMeans(images, mask, 2));
	}

	public SamplesImageData[] transform( final Mat[] images, final MatOfPoint2f[] points, int interpolation) {
		assert ( images.length == points.length );
		ImageUtils.performAffineTransformations(images, points, interpolation);
		return Arrays.stream(images)
				.map(i ->
						new SamplesImageData(
								ImageUtils.createImage(
										ImageUtils.getImageData(i), i.width(), i.height(), i.channels(), PixelFormat.getByteBgraPreInstance()
								), i
						)
				).toArray(SamplesImageData[]::new);
	}

	public Result generateData( final Mat[] images ) {
//		assert ( transformImages.length == points.length );
		final ResultImage resultImage = ImageUtils.getResultImage(images);
//		Image image = ImageUtils.createImage(resultImage.data, resultImage.width, resultImage.height, resultImage.channels,
//				PixelFormat.getByteBgraPreInstance());
		Result result = new Result();//image);
		ImageUtils.convertType(images, Imgproc.COLOR_BGRA2BGR);
		result.putResults(calculateStatistics(images, resultImage.mask));
		return result;
	}

	public void injectFunction( final String key, final EvaluationFunction function ) {
		availableFunctions.put(key, function);
	}

	private Map<String, double[]> calculateStatistics( final Mat[] images, final boolean[] mask ) {
		Map<String, double[]> results = new TreeMap<>();
		for ( Map.Entry<String, EvaluationFunction> e : chosenFunctions.entrySet() ) {
			results.put(e.getKey(), e.getValue().evaluate(ImageUtils.getImagesCopy(images), ImageUtils.getMaskCopy(mask)));
		}
		return results;
	}

	public Set<String> getAllAvailableFunctions() { return availableFunctions.keySet(); }

	public void deselectFunction (final String key) { chosenFunctions.remove(key); }

	public void chooseFunction( final String key ) {
		chosenFunctions.put(key, availableFunctions.get(key));
	}

	public void chooseAllAvailableFunctions() {
		chosenFunctions.putAll(availableFunctions);
	}

	public void clearAvailableFunctions() {
		availableFunctions.clear();
	}

	public void clearChosenFunctions() {
		chosenFunctions.clear();
	}

}
