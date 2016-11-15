package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;

import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

/**
 * A function that evaluates global image edge features.
 */
public interface EdgeEvaluationFunction extends EvaluationFunction {

    /**
     * Processes given BGRA format images.
     * @param images images in BGRA format
     * @return processed images
     */
    default Mat[] processBGRA(Mat[] images) {
        Mat[] copy = ImageUtils.getImagesCopy(images);
        copy = preprocess(copy);
        copy = postprocess(copy);
        ImageUtils.multiplyTransparencies(images, copy);
        return copy;
    }

    /**
     * Post-processes given images by converting them back to BGRA format.
     * @param images images in grayscale format
     * @return processed images
     */
    default Mat[] postprocess(Mat[] images) {
        convertType(images, Imgproc.COLOR_GRAY2BGRA);
        return images;
    }

    /**
     * Pre-processes given images by converting them from BGRA to grayscale format.
     * @param images images in BGRA format
     * @return processed images
     */
    Mat[] preprocess(Mat[] images);

    /**
     * Processes given images by converting them from BGR to grayscale format.
     * @param images images in BGR format
     * @return processed images
     */
    Mat[] process(Mat[] images);

}
