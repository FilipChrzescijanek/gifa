package pwr.chrzescijanek.filip.gifa.core.function.rgb.stddeviation;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateStdDeviations;

public final class StdDeviationGreen implements EvaluationFunction {
	@Override
	public double[] evaluate( final Mat[] images ) {
		return calculateStdDeviations(images, 1);
	}
}
