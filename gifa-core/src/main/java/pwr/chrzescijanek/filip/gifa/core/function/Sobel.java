package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;

import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

/**
 * Provides method to calculate TBA.
 */
public class Sobel implements EdgeEvaluationFunction {

    private int kernelSize;

    /**
     * Constructs new Sobel with given kernel size.
     * @param kernelSize Sobel derivative kernel size
     */
    public Sobel(final int kernelSize) {
        this.kernelSize = kernelSize;
    }

    @Override
    public Mat[] preprocess(final Mat[] images) {
        convertType(images, Imgproc.COLOR_BGRA2GRAY);
        Mat[] filtered = ImageUtils.sobelDerivative(images, kernelSize);
        Mat[] result = ImageUtils.otsuThreshold(filtered);
        return result;
    }

    @Override
    public Mat[] process(final Mat[] images) {
        convertType(images, Imgproc.COLOR_BGR2GRAY);
        Mat[] filtered = ImageUtils.sobelDerivative(images, kernelSize);
        Mat[] result = ImageUtils.otsuThreshold(filtered);
        return result;
    }

    @Override
    public double[] evaluate(final Mat[] images) {
        return FunctionUtils.calculateMeans(images, 0);
    }
}
