package pwr.chrzescijanek.filip.gifa.core.function;

import org.opencv.core.Mat;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2HSV_FULL;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateEntropies;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.convertType;

public class EntropyHue implements EvaluationFunction {

	@Override
	public double[] evaluate( final Mat[] images ) {
		convertType(images, COLOR_RGB2HSV_FULL);
		return calculateEntropies(images, 0);
	}
}
