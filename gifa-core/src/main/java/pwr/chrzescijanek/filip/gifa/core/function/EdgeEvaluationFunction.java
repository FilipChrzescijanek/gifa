package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;

import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

public interface EdgeEvaluationFunction extends EvaluationFunction {

    default Mat[] processBGRA(Mat[] images) {
        Mat[] copy = ImageUtils.getImagesCopy(images);
        copy = preprocess(copy);
        copy = postprocess(copy);
        ImageUtils.multiplyTransparencies(images, copy);
        return copy;
    }

    default Mat[] postprocess(Mat[] images) {
        convertType(images, Imgproc.COLOR_GRAY2BGRA);
        return images;
    }

    Mat[] preprocess(Mat[] images);

    Mat[] process(Mat[] images);

}
