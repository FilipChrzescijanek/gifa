package pwr.chrzescijanek.filip.gifa.generator;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.controller.Controller;
import pwr.chrzescijanek.filip.gifa.core.controller.NullController;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.function.MeanValue;
import pwr.chrzescijanek.filip.gifa.core.function.StdDeviationValue;
import pwr.chrzescijanek.filip.gifa.core.utils.FunctionUtils;
import pwr.chrzescijanek.filip.gifa.core.utils.ImageUtils;
import pwr.chrzescijanek.filip.gifa.core.utils.ResultImage;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public enum DataGenerator {

	INSTANCE;

	private final Map< String, EvaluationFunction > availableFunctions = new TreeMap<>();
	private final Map< String, EvaluationFunction > chosenFunctions = new TreeMap<>();
	private Controller controller = new NullController();

	DataGenerator() {
		injectBasicFunctions();
	}

	public void injectBasicFunctions() {
		injectFunction("Mean value", MeanValue.INSTANCE);
		injectFunction("StdDev value", StdDeviationValue.INSTANCE);
		injectFunction("Mean red", ( images, mask ) -> FunctionUtils.calculateMeans(images, mask, 2));
	}

	public void generateData( final Mat[] images, final MatOfPoint2f[] points ) {
		assert ( images.length == points.length );
//		ImageUtils.performAffineTransformations(images);
		ImageUtils.performAffineTransformations(images, points);
		final ResultImage resultImage = ImageUtils.getResultImage(images);
		Image image = ImageUtils.createImage(resultImage.data, resultImage.width, resultImage.height, resultImage.channels);
		controller.setImage(image);
		ImageUtils.convertType(images, Imgproc.COLOR_BGRA2BGR);
		calculateStatistics(images, resultImage.mask);
	}

	public void injectFunction( final String key, final EvaluationFunction function ) {
		availableFunctions.put(key, function);
	}

	private void calculateStatistics( final Mat[] images, final boolean[] mask ) {
		for ( Map.Entry<String, EvaluationFunction> e : chosenFunctions.entrySet() ) {
			System.out.println(e.getKey() + ": " + Arrays.toString(
					e.getValue().evaluate(ImageUtils.getImagesCopy(images), ImageUtils.getMaskCopy(mask))));
		}
	}

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

	public void setController( final Controller controller ) {
		this.controller = controller;
	}

}
