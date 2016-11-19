package pwr.chrzescijanek.filip.gifa.core.function.edge;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EdgeEvaluationFunction;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.COLOR_BGRA2GRAY;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.otsu;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.scharr;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.xorPairs;

/**
 * Provides method to calculate differences in edge detection using Scharr operator.
 */
public class Scharr implements EdgeEvaluationFunction {

	/**
	 * Default constructor.
	 */
	public Scharr() {}

	@Override
	public Mat[] preprocess(final Mat[] images) {
		convertType(images, COLOR_BGRA2GRAY);
		final Mat[] filtered = scharr(images);
		final Mat[] binary = otsu(filtered);
		return concat(stream(binary), stream(xorPairs(binary))).toArray(Mat[]::new);
	}

	@Override
	public Mat[] process(final Mat[] images) {
		convertType(images, COLOR_BGR2GRAY);
		final Mat[] filtered = scharr(images);
		final Mat[] binary = otsu(filtered);
		return xorPairs(binary);
	}

	@Override
	public double[] evaluate(final Mat[] images) {
		return calculateMeans(images, 0);
	}
}
