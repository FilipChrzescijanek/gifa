package pwr.chrzescijanek.filip.gifa.core.function.edge;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import pwr.chrzescijanek.filip.gifa.core.function.EdgeEvaluationFunction;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.COLOR_BGRA2GRAY;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.otsu;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.sobel;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.xor;

/**
 * Provides method to calculate differences in edge detection using Sobel operator.
 */
public class Sobel implements EdgeEvaluationFunction {

	private final int kernelSize;

	/**
	 * Constructs new Sobel with given kernel size.
	 *
	 * @param kernelSize Sobel derivative kernel size
	 */
	public Sobel(final int kernelSize) {
		this.kernelSize = kernelSize;
	}

	@Override
	public Mat[] preprocess(final Mat[] images) {
		convertType(images, COLOR_BGRA2GRAY);
		final Mat[] filtered = sobel(images, kernelSize);
		final Mat[] binary = otsu(filtered);
		return xor(binary);
	}

	@Override
	public Mat[] process(final Mat[] images) {
		convertType(images, COLOR_BGR2GRAY);
		final Mat[] filtered = sobel(images, kernelSize);
		final Mat[] binary = otsu(filtered);
		return xor(binary);
	}

	@Override
	public double[] evaluate(final Mat[] images) {
		return calculateMeans(images, 0);
	}
}
