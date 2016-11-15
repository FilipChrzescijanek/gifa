package pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.linear;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateLinearFuzzinesses;

/**
 * Provides method to calculate green linear fuzziness.
 */
public final class LinearFuzzinessGreen implements EvaluationFunction {

    /**
     * Default constructor.
     */
    public LinearFuzzinessGreen() {
    }

    @Override
    public double[] evaluate(final Mat[] images) {
        return calculateLinearFuzzinesses(images, 1);
    }
}
