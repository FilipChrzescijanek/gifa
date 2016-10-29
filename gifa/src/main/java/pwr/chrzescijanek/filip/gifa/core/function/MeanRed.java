package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils;

public final class MeanRed implements EvaluationFunction {

	@Override
	public double[] evaluate( final Mat[] images) {
		return FunctionUtils.calculateMeans(images, 2);
	}
}
