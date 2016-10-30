package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;

public final class MeanRed implements EvaluationFunction {

	@Override
	public double[] evaluate( final Mat[] images) {
		return calculateMeans(images, 2);
	}
}
