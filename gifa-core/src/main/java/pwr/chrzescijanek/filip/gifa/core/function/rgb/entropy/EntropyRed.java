package pwr.chrzescijanek.filip.gifa.core.function.rgb.entropy;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateEntropies;

/**
 * Provides method to calculate red entropy.
 */
public final class EntropyRed implements EvaluationFunction {

	/**
	 * Default constructor.
	 */
	public EntropyRed() {}

	@Override
	public double[] evaluate(final Mat[] images) {
		return calculateEntropies(images, 2);
	}
}
