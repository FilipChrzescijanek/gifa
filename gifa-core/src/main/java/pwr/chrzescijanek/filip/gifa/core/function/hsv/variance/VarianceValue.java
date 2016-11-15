package pwr.chrzescijanek.filip.gifa.core.function.hsv.variance;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV_FULL;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateVariances;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

/**
 * Provides method to calculate value variance.
 */
public final class VarianceValue implements EvaluationFunction {

	/**
	 * Default constructor.
	 */
	public VarianceValue() {}

	@Override
	public double[] evaluate(final Mat[] images) {
		convertType(images, COLOR_BGR2HSV_FULL);
		return calculateVariances(images, 2);
	}

}
