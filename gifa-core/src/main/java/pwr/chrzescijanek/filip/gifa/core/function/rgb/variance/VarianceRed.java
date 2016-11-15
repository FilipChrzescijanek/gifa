package pwr.chrzescijanek.filip.gifa.core.function.rgb.variance;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateVariances;

/**
 * Provides method to calculate red variance.
 */
public final class VarianceRed implements EvaluationFunction {

    /**
     * Default constructor.
     */
    public VarianceRed() {
    }

    @Override
    public double[] evaluate(final Mat[] images) {
        return calculateVariances(images, 2);
    }
}
