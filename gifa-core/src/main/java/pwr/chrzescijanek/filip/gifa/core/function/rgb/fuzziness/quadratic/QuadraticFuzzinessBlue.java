package pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.quadratic;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateQuadraticFuzzinesses;

/**
 * Provides method to calculate blue quadratic fuzziness.
 */
public final class QuadraticFuzzinessBlue implements EvaluationFunction {

    /**
     * Default constructor.
     */
    public QuadraticFuzzinessBlue() {
    }

    @Override
    public double[] evaluate(final Mat[] images) {
        return calculateQuadraticFuzzinesses(images, 0);
    }
}
