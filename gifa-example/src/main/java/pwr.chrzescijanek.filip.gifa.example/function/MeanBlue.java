package pwr.chrzescijanek.filip.gifa.example.function;

import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils;

public class MeanBlue implements EvaluationFunction {

    public double[] evaluate(Mat[] images) {
        return FunctionUtils.calculateMeans(images, 0);
    }

}
