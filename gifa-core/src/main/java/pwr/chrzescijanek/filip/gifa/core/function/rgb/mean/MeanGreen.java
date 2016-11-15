package pwr.chrzescijanek.filip.gifa.core.function.rgb.mean;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;

/**
 * Provides method to calculate green mean.
 */
public final class MeanGreen implements EvaluationFunction {

    /**
     * Default constructor.
     */
    public MeanGreen() {
    }

    @Override
    public double[] evaluate(final Mat[] images) {
        return calculateMeans(images, 1);
    }
}
