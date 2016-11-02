package pwr.chrzescijanek.filip.gifa.core.function.rgb.variance;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateStdDeviations;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateVariances;

public final class VarianceBlue implements EvaluationFunction {
	@Override
	public double[] evaluate( final Mat[] images ) {
		return calculateVariances(images, 0);
	}
}
