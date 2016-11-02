package pwr.chrzescijanek.filip.gifa.core.function.hsv.mean;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2HSV_FULL;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

public final class MeanSaturation implements EvaluationFunction {
	@Override
	public double[] evaluate( final Mat[] images ) {
		convertType(images, COLOR_RGB2HSV_FULL);
		return calculateMeans(images, 1);
	}
}