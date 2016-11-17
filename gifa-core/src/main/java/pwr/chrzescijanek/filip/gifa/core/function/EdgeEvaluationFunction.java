package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;

import static org.opencv.imgproc.Imgproc.COLOR_GRAY2BGRA;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.getImagesCopy;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.multiplyTransparencies;

/**
 * A function that evaluates differences between images' edges.
 */
public interface EdgeEvaluationFunction extends EvaluationFunction {

	/**
	 * Processes given BGRA format images.
	 *
	 * @param images images in BGRA format
	 * @return processed images
	 */
	default Mat[] processBGRA(final Mat[] images) {
		Mat[] copy = getImagesCopy(images);
		copy = preprocess(copy);
		copy = postprocess(copy);
		multiplyTransparencies(images, copy);
		return copy;
	}

	/**
	 * Pre-processes given images by converting them from BGRA to grayscale format.
	 *
	 * @param images images in BGRA format
	 * @return processed images
	 */
	Mat[] preprocess(Mat[] images);

	/**
	 * Post-processes given images by converting them back to BGRA format.
	 *
	 * @param images images in grayscale format
	 * @return processed images
	 */
	default Mat[] postprocess(final Mat[] images) {
		convertType(images, COLOR_GRAY2BGRA);
		return images;
	}

	/**
	 * Processes given images by converting them from BGR to grayscale format.
	 *
	 * @param images images in BGR format
	 * @return processed images
	 */
	Mat[] process(Mat[] images);

}
