package pwr.chrzescijanek.filip.gifa.core.function.hsv.stddeviation;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV_FULL;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateStdDeviations;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

/**
 * Provides method to calculate hue standard deviation.
 */
public final class StdDeviationHue implements EvaluationFunction {

    /**
     * Default constructor.
     */
    public StdDeviationHue() {
    }

    @Override
    public double[] evaluate(final Mat[] images) {
        convertType(images, COLOR_BGR2HSV_FULL);
        return calculateStdDeviations(images, 0);
    }
}
