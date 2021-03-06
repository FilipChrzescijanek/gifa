package pwr.chrzescijanek.filip.gifa.core.generator;

import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.function.edge.Canny;
import pwr.chrzescijanek.filip.gifa.core.function.edge.Scharr;
import pwr.chrzescijanek.filip.gifa.core.function.edge.Sobel;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.entropy.EntropyHue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.entropy.EntropySaturation;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.entropy.EntropyValue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.fuzziness.linear.LinearFuzzinessHue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.fuzziness.linear.LinearFuzzinessSaturation;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.fuzziness.linear.LinearFuzzinessValue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.fuzziness.quadratic.QuadraticFuzzinessHue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.fuzziness.quadratic.QuadraticFuzzinessSaturation;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.fuzziness.quadratic.QuadraticFuzzinessValue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.mean.MeanHue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.mean.MeanSaturation;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.mean.MeanValue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.stddeviation.StdDeviationHue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.stddeviation.StdDeviationSaturation;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.stddeviation.StdDeviationValue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.variance.VarianceHue;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.variance.VarianceSaturation;
import pwr.chrzescijanek.filip.gifa.core.function.hsv.variance.VarianceValue;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.entropy.EntropyBlue;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.entropy.EntropyGreen;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.entropy.EntropyRed;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.linear.LinearFuzzinessBlue;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.linear.LinearFuzzinessGreen;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.linear.LinearFuzzinessRed;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.quadratic.QuadraticFuzzinessBlue;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.quadratic.QuadraticFuzzinessGreen;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.fuzziness.quadratic.QuadraticFuzzinessRed;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.mean.MeanBlue;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.mean.MeanGreen;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.mean.MeanRed;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.stddeviation.StdDeviationBlue;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.stddeviation.StdDeviationGreen;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.stddeviation.StdDeviationRed;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.variance.VarianceBlue;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.variance.VarianceGreen;
import pwr.chrzescijanek.filip.gifa.core.function.rgb.variance.VarianceRed;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Provides factory method for creating DataGenerator class instances.
 */
public class DataGeneratorFactory {

	private final Map<String, EvaluationFunction> availableFunctions = new TreeMap<>();

	private final Map<String, EvaluationFunction> chosenFunctions = new TreeMap<>();

	/**
	 * Default constructor, injects basic functions.
	 */
	public DataGeneratorFactory() {
		injectBasicFunctions();
	}

	/**
	 * Injects basic functions.
	 */
	public void injectBasicFunctions() {
		injectRedFunctions();
		injectGreenFunctions();
		injectBlueFunctions();
		injectHueFunctions();
		injectSaturationFunctions();
		injectValueFunctions();
		injectEdgeDifferenceFunctions();
	}

	/**
	 * Injects basic red value evaluating functions.
	 */
	public void injectRedFunctions() {
		injectFunction("Red mean", new MeanRed());
		injectFunction("Red variance", new VarianceRed());
		injectFunction("Red std. deviation", new StdDeviationRed());
		injectFunction("Red entropy", new EntropyRed());
		injectFunction("Red linear fuzziness", new LinearFuzzinessRed());
		injectFunction("Red quadratic fuzziness", new QuadraticFuzzinessRed());
	}

	/**
	 * Injects basic green value evaluating functions.
	 */
	public void injectGreenFunctions() {
		injectFunction("Green mean", new MeanGreen());
		injectFunction("Green variance", new VarianceGreen());
		injectFunction("Green std. deviation", new StdDeviationGreen());
		injectFunction("Green entropy", new EntropyGreen());
		injectFunction("Green linear fuzziness", new LinearFuzzinessGreen());
		injectFunction("Green quadratic fuzziness", new QuadraticFuzzinessGreen());
	}

	/**
	 * Injects basic blue value evaluating functions.
	 */
	public void injectBlueFunctions() {
		injectFunction("Blue mean", new MeanBlue());
		injectFunction("Blue variance", new VarianceBlue());
		injectFunction("Blue std. deviation", new StdDeviationBlue());
		injectFunction("Blue entropy", new EntropyBlue());
		injectFunction("Blue linear fuzziness", new LinearFuzzinessBlue());
		injectFunction("Blue quadratic fuzziness", new QuadraticFuzzinessBlue());
	}

	/**
	 * Injects basic hue evaluating functions.
	 */
	public void injectHueFunctions() {
		injectFunction("Hue mean", new MeanHue());
		injectFunction("Hue variance", new VarianceHue());
		injectFunction("Hue std. deviation", new StdDeviationHue());
		injectFunction("Hue entropy", new EntropyHue());
		injectFunction("Hue linear fuzziness", new LinearFuzzinessHue());
		injectFunction("Hue quadratic fuzziness", new QuadraticFuzzinessHue());
	}

	/**
	 * Injects basic saturation evaluating functions.
	 */
	public void injectSaturationFunctions() {
		injectFunction("Saturation mean", new MeanSaturation());
		injectFunction("Saturation variance", new VarianceSaturation());
		injectFunction("Saturation std. deviation", new StdDeviationSaturation());
		injectFunction("Saturation entropy", new EntropySaturation());
		injectFunction("Saturation linear fuzziness", new LinearFuzzinessSaturation());
		injectFunction("Saturation quadratic fuzziness", new QuadraticFuzzinessSaturation());
	}

	/**
	 * Injects basic value evaluating functions.
	 */
	public void injectValueFunctions() {
		injectFunction("Value mean", new MeanValue());
		injectFunction("Value variance", new VarianceValue());
		injectFunction("Value std. deviation", new StdDeviationValue());
		injectFunction("Value entropy", new EntropyValue());
		injectFunction("Value linear fuzziness", new LinearFuzzinessValue());
		injectFunction("Value quadratic fuzziness", new QuadraticFuzzinessValue());
	}

	/**
	 * Injects basic edge differences evaluating functions.
	 */
	public void injectEdgeDifferenceFunctions() {
		injectFunction("Edge differences - Canny detector", new Canny());
		injectFunction("Edge differences - Scharr operator", new Scharr());
		injectFunction("Edge differences - Sobel operator", new Sobel(3));
	}

	/**
	 * Injects given function with given name.
	 *
	 * @param name     function name
	 * @param function function to be injected
	 */
	public void injectFunction(final String name, final EvaluationFunction function) {
		availableFunctions.put(name, function);
	}

	/**
	 * Constructs a new DataGenerator with chosen functions.
	 *
	 * @return new DataGenerator class instance
	 */
	public DataGenerator createGenerator() {
		return new DataGenerator(chosenFunctions);
	}


	/**
	 * Clears available functions.
	 */
	public void clearAvailableFunctions() {
		availableFunctions.clear();
	}

	/**
	 * @return available functions
	 */
	public Set<String> getAvailableFunctionsNames() {
		return new TreeSet<>(availableFunctions.keySet());
	}

	/**
	 * Chooses all available functions.
	 */
	public void chooseAllAvailableFunctions() {
		chosenFunctions.putAll(availableFunctions);
	}

	/**
	 * Chooses function with given name.
	 *
	 * @param name function name
	 */
	public void chooseFunction(final String name) {
		chosenFunctions.put(name, availableFunctions.get(name));
	}

	/**
	 * Deselects function with given name.
	 *
	 * @param name function name
	 */
	public void deselectFunction(final String name) {
		chosenFunctions.remove(name);
	}

	/**
	 * Clears chosen functions.
	 */
	public void clearChosenFunctions() {
		chosenFunctions.clear();
	}

}
