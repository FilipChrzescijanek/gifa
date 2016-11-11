package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;

public class Scharr implements EdgeEvaluationFunction {

	@Override
	public Mat[] preprocess( final Mat[] images ) {
		ImageUtils.convertType(images, Imgproc.COLOR_BGRA2GRAY);
		Mat[] filtered = ImageUtils.scharrDerivative(images);
		Mat[] result = ImageUtils.otsuThreshold(filtered);
		return result;
	}

	@Override
	public Mat[] process( final Mat[] images ) {
		ImageUtils.convertType(images, Imgproc.COLOR_BGR2GRAY);
		Mat[] filtered = ImageUtils.scharrDerivative(images);
		Mat[] result = ImageUtils.otsuThreshold(filtered);
		return result;
	}

	@Override
	public double[] evaluate( final Mat[] images ) {
		return FunctionUtils.calculateMeans(images, 0);
	}
}
