package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.COLOR_BGRA2BGR;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.extractChannel;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.otsuThreshold;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.sobelDerivative;

/**
 * Provides method to calculate TBA.
 */
public class SobelValue implements EdgeEvaluationFunction {

	private final int kernelSize;

	/**
	 * Constructs new SobelValue with given kernel size.
	 *
	 * @param kernelSize Sobel derivative kernel size
	 */
	public SobelValue(final int kernelSize) {
		this.kernelSize = kernelSize;
	}

	@Override
	public Mat[] preprocess(final Mat[] images) {
		convertType(images, COLOR_BGRA2BGR);
		convertType(images, COLOR_BGR2HSV);
		final Mat[] values = extractChannel(images, 2);
		final Mat[] filtered = sobelDerivative(values, kernelSize);
		return otsuThreshold(filtered);
	}

	@Override
	public Mat[] process(final Mat[] images) {
		convertType(images, COLOR_BGR2HSV);
		final Mat[] values = extractChannel(images, 2);
		final Mat[] filtered = sobelDerivative(values, kernelSize);
		return otsuThreshold(filtered);
	}

	@Override
	public double[] evaluate(final Mat[] images) {
		return calculateMeans(images, 0);
	}
}
