package pwr.chrzescijanek.filip.gifa;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MeanValue implements EvaluationFunction {

	@Override
	public double[] evaluate( final Mat[] images, final boolean[] mask ) {
		StatisticsCalculator.convertType(images, Imgproc.COLOR_RGB2HSV_FULL);
		return StatisticsCalculator.calculateMeans(images, mask, 2);
	}

}
