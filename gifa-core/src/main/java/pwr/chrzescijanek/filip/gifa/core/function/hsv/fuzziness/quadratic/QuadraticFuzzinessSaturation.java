package pwr.chrzescijanek.filip.gifa.core.function.hsv.fuzziness.quadratic;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV_FULL;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateQuadraticFuzzinesses;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

/**
 * Provides method to calculate saturation quadratic fuzziness.
 */
public final class QuadraticFuzzinessSaturation implements EvaluationFunction {

	/**
	 * Default constructor.
	 */
	public QuadraticFuzzinessSaturation() {}

	@Override
	public double[] evaluate(final Mat[] images) {
		convertType(images, COLOR_BGR2HSV_FULL);
		return calculateQuadraticFuzzinesses(images, 1);
	}
}
