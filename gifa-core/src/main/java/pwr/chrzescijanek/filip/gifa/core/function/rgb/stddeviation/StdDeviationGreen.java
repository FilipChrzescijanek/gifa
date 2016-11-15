package pwr.chrzescijanek.filip.gifa.core.function.rgb.stddeviation;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateStdDeviations;

/**
 * Provides method to calculate green standard deviation.
 */
public final class StdDeviationGreen implements EvaluationFunction {

	/**
	 * Default constructor.
	 */
	public StdDeviationGreen() {}

	@Override
	public double[] evaluate(final Mat[] images) {
		return calculateStdDeviations(images, 1);
	}
}
