package pwr.chrzescijanek.filip.gifa.core.function.rgb.mean;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;

/**
 * Provides method to calculate red mean.
 */
public final class MeanRed implements EvaluationFunction {

    /**
     * Default constructor.
     */
    public MeanRed() {
    }

    @Override
    public double[] evaluate(final Mat[] images) {
        return calculateMeans(images, 2);
    }
}
