package pwr.chrzescijanek.filip.gifa.generator;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.controller.Controller;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.function.MeanValue;
import pwr.chrzescijanek.filip.gifa.core.function.StdDeviationValue;
import pwr.chrzescijanek.filip.gifa.core.utils.FunctionUtils;
import pwr.chrzescijanek.filip.gifa.core.utils.ImageUtils;
import pwr.chrzescijanek.filip.gifa.core.utils.ResultImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DataGenerator {

	INSTANCE;

	private final List< EvaluationFunction > functions = new ArrayList<>();

	public void generateData( final Mat[] images, final MatOfPoint2f[] points, final Controller c ) {
		assert ( images.length == points.length );
		ImageUtils.performAffineTransformations(images, points, true);
		final ResultImage resultImage = ImageUtils.getResultImage(images);
		Image image = ImageUtils.createImage(resultImage.data, resultImage.width, resultImage.height, resultImage.channels);
		c.setImage(image);
		ImageUtils.convertType(images, Imgproc.COLOR_BGRA2BGR);
		performDataGeneration(images, resultImage.mask);
	}

	private void performDataGeneration( final Mat[] images, final boolean[] mask ) {
		initializeWithBasicFunctions();
		calculateStatistics(images, mask);
	}

	public void initializeWithBasicFunctions() {
		clearFunctions();
		injectBasicFunctions();
	}

	public void clearFunctions() {
		functions.clear();
	}

	private void injectBasicFunctions() {
		injectFunction(new MeanValue());
		injectFunction(new StdDeviationValue());
		injectFunction(( images, mask ) -> FunctionUtils.calculateMeans(images, mask, 2));
	}

	public void injectFunction( final EvaluationFunction function ) {
		functions.add(function);
	}

	private void calculateStatistics( final Mat[] images, final boolean[] mask ) {
		for ( EvaluationFunction f : functions ) {
			System.out.println(Arrays.toString(
					f.evaluate(ImageUtils.getImagesCopy(images), ImageUtils.getMaskCopy(mask))));
		}
	}

}
