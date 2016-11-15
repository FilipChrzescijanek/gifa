package pwr.chrzescijanek.filip.gifa.core.function.rgb.mean;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;

/**
 * Provides method to calculate blue mean.
 */
public final class MeanBlue implements EvaluationFunction {

	/**
	 * Default constructor.
	 */
	public MeanBlue() {}

	@Override
	public double[] evaluate(final Mat[] images) {
		return calculateMeans(images, 0);
	}
}
