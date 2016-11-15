package pwr.chrzescijanek.filip.gifa.core.function.rgb.entropy;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateEntropies;

/**
 * Provides method to calculate green entropy.
 */
public final class EntropyGreen implements EvaluationFunction {

	/**
	 * Default constructor.
	 */
	public EntropyGreen() {}

	@Override
	public double[] evaluate(final Mat[] images) {
		return calculateEntropies(images, 1);
	}
}
