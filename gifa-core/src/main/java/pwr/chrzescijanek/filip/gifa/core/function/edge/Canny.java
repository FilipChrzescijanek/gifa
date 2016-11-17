package pwr.chrzescijanek.filip.gifa.core.function.edge;

import javafx.util.Pair;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import pwr.chrzescijanek.filip.gifa.core.function.EdgeEvaluationFunction;

import java.util.Map;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.COLOR_BGRA2GRAY;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.canny;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.otsu;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.xor;

/**
 * Provides method to calculate differences in edge detection using Canny thresholding.
 */
public class Canny implements EdgeEvaluationFunction {

	/**
	 * Default constructor.
	 */
	public Canny() { }

	@Override
	public Mat[] preprocess(final Mat[] images) {
		convertType(images, COLOR_BGRA2GRAY);
		final Mat[] filtered = canny(images);
		final Mat[] binary = otsu(filtered);
		return xor(binary);
	}

	@Override
	public Mat[] process(final Mat[] images) {
		convertType(images, COLOR_BGR2GRAY);
		final Mat[] filtered = canny(images);
		final Mat[] binary = otsu(filtered);
		return xor(binary);
	}

	@Override
	public double[] evaluate(final Mat[] images) {
		return calculateMeans(images, 0);
	}

}
