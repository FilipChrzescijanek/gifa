package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;

import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

public class Sobel implements EdgeEvaluationFunction {

    private int kernelSize;

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
