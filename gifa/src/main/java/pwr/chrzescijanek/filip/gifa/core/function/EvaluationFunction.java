package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;

@FunctionalInterface
public interface EvaluationFunction {

	double[] evaluate( final Mat[] images, final boolean[] mask );

}
