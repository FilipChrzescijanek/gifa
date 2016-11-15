package pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.linear;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateLinearFuzzinesses;

/**
 * Provides method to calculate red linear fuzziness.
 */
public final class LinearFuzzinessRed implements EvaluationFunction {

    /**
     * Default constructor.
     */
    public LinearFuzzinessRed() {
    }

    @Override
    public double[] evaluate(final Mat[] images) {
        return calculateLinearFuzzinesses(images, 2);
    }
}
