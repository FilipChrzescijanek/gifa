package pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.quadratic;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateLinearFuzzinesses;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateQuadraticFuzzinesses;

public final class QuadraticFuzzinessRed implements EvaluationFunction {
	@Override
	public double[] evaluate( final Mat[] images ) {
		return calculateQuadraticFuzzinesses(images, 2);
	}
}
