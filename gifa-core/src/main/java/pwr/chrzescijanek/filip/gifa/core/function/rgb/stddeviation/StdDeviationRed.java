package pwr.chrzescijanek.filip.gifa.core.function.rgb.stddeviation;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateStdDeviations;

/**
 * Provides method to calculate red standard deviation.
 */
public final class StdDeviationRed implements EvaluationFunction {

    /**
     * Default constructor.
     */
    public StdDeviationRed() {
    }

    @Override
    public double[] evaluate(final Mat[] images) {
        return calculateStdDeviations(images, 2);
    }
}
