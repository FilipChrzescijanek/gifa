package pwr.chrzescijanek.filip.gifa.core.function.hsv.mean;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV_FULL;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

/**
 * Provides method to calculate value mean.
 */
public final class MeanValue implements EvaluationFunction {

	/**
	 * Default constructor.
	 */
	public MeanValue() {}

	@Override
	public double[] evaluate(final Mat[] images) {
		convertType(images, COLOR_BGR2HSV_FULL);
		return calculateMeans(images, 2);
	}

}
