package pwr.chrzescijanek.filip.gifa.core.function.hsv.entropy;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV_FULL;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateEntropies;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

/**
 * Provides method to calculate hue entropy.
 */
public final class EntropyHue implements EvaluationFunction {

	/**
	 * Default constructor.
	 */
	public EntropyHue() {}

	@Override
	public double[] evaluate(final Mat[] images) {
		convertType(images, COLOR_BGR2HSV_FULL);
		return calculateEntropies(images, 0);
	}
}
