package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;

/**
 * A function that evaluates global image features.
 */
@FunctionalInterface
public interface EvaluationFunction {

	/**
	 * Evaluates global features of given images.
	 *
	 * @param images images to be evaluated
	 * @return evaluation result
	 */
	double[] evaluate(Mat[] images);

}
