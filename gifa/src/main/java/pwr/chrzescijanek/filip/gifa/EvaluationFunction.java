package pwr.chrzescijanek.filip.gifa;

import org.opencv.core.Mat;

@FunctionalInterface
public interface EvaluationFunction {

	public double[] evaluate( final Mat[] images, final boolean[] mask );

}
