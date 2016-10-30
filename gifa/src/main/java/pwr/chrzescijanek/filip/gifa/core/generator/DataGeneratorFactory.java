package pwr.chrzescijanek.filip.gifa.core.generator;

import pwr.chrzescijanek.filip.gifa.core.function.EvaluationFunction;
import pwr.chrzescijanek.filip.gifa.core.function.MeanRed;
import pwr.chrzescijanek.filip.gifa.core.function.MeanValue;
import pwr.chrzescijanek.filip.gifa.core.function.StdDeviationValue;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DataGeneratorFactory {

	private final Map< String, EvaluationFunction > availableFunctions = new TreeMap<>();
	private final Map< String, EvaluationFunction > chosenFunctions = new TreeMap<>();

	public DataGeneratorFactory() {
		injectBasicFunctions();
	}

	public DataGenerator createGenerator() {
		return new DataGenerator(chosenFunctions);
	}

	public void injectBasicFunctions() {
		injectFunction("Mean value", new MeanValue());
		injectFunction("StdDev value", new StdDeviationValue());
		injectFunction("Mean red", new MeanRed());
	}

	public void injectFunction( final String key, final EvaluationFunction function ) {
		availableFunctions.put(key, function);
	}

	public void clearAvailableFunctions() {
		availableFunctions.clear();
	}

	public Set< String > getAvailableFunctionsNames() { return new TreeSet<>(availableFunctions.keySet()); }

	public void chooseAllAvailableFunctions() {
		chosenFunctions.putAll(availableFunctions);
	}

	public void chooseFunction( final String key ) {
		chosenFunctions.put(key, availableFunctions.get(key));
	}

	public void deselectFunction( final String key ) { chosenFunctions.remove(key); }

	public void clearChosenFunctions() {
		chosenFunctions.clear();
	}
}
