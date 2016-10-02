package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;

public enum MeanValue implements EvaluationFunction {

	INSTANCE;

	@Override
	public double[] evaluate( final Mat[] images, final boolean[] mask ) {
		ImageUtils.convertType(images, Imgproc.COLOR_RGB2HSV_FULL);
		return FunctionUtils.calculateMeans(images, mask, 2);
	}

}
