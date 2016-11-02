package pwr.chrzescijanek.filip.gifa.core.function.rgb.entropy;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateEntropies;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateVariances;

public final class EntropyBlue implements EvaluationFunction {
	@Override
	public double[] evaluate( final Mat[] images ) {
		return calculateEntropies(images, 0);
	}
}
