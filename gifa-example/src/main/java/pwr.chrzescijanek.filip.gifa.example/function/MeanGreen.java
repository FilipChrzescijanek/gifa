package pwr.chrzescijanek.filip.gifa.example.function;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;

public class MeanGreen implements EvaluationFunction {

	public double[] evaluate(final Mat[] images) {
		return calculateMeans(images, 1);
	}

}
