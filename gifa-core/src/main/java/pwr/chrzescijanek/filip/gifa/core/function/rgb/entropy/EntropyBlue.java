package pwr.chrzescijanek.filip.gifa.core.function.rgb.entropy;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateEntropies;

/**
 * Provides method to calculate blue entropy.
 */
public final class EntropyBlue implements EvaluationFunction {

	/**
	 * Default constructor.
	 */
	public EntropyBlue() {}

	@Override
	public double[] evaluate(final Mat[] images) {
		return calculateEntropies(images, 0);
	}
}
