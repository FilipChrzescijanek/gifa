package pwr.chrzescijanek.filip.gifa.core.function;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;

public class SobelValue implements EdgeEvaluationFunction {

	private int kernelSize;

	public SobelValue( final int kernelSize) {
		this.kernelSize = kernelSize;
	}

	@Override
	public Mat[] preprocess( final Mat[] images ) {
		ImageUtils.convertType(images, Imgproc.COLOR_BGRA2BGR);
		ImageUtils.convertType(images, Imgproc.COLOR_BGR2HSV);
		Mat[] values = ImageUtils.extractChannel(images, 2);
		Mat[] result = ImageUtils.sobelDerivative(values, kernelSize);
		return result;
	}

	@Override
	public Mat[] process( final Mat[] images ) {
		ImageUtils.convertType(images, Imgproc.COLOR_BGR2HSV);
		Mat[] values = ImageUtils.extractChannel(images, 2);
		Mat[] result = ImageUtils.sobelDerivative(values, kernelSize);
		return result;
	}

	@Override
	public double[] evaluate( final Mat[] images ) {
		return FunctionUtils.calculateMeans(images, 0);
	}
}
