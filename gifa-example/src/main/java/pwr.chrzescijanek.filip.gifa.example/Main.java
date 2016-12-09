package pwr.chrzescijanek.filip.gifa.example;

import pwr.chrzescijanek.filip.gifa.core.generator.DataGeneratorFactory;
import pwr.chrzescijanek.filip.gifa.core.generator.FunctionManager;
import pwr.chrzescijanek.filip.gifa.example.function.MeanBlue;
import pwr.chrzescijanek.filip.gifa.example.function.MeanGreen;
import pwr.chrzescijanek.filip.gifa.example.function.MeanRed;
import pwr.chrzescijanek.filip.gifa.inject.Injector;

import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMeans;

public class Main {

	public static void main(final String[] args) {
		final FunctionManager fm = getFunctionManager();
		fm.manageFunctions(Injector.instantiateComponent(DataGeneratorFactory.class));
		pwr.chrzescijanek.filip.gifa.Main.main();
	}

	private static FunctionManager getFunctionManager() {
		return dataGeneratorFactory -> {
			dataGeneratorFactory.clearAvailableFunctions();
			dataGeneratorFactory.injectFunction("Mean Red (lambda)", mats -> calculateMeans(mats, 2));
			dataGeneratorFactory.injectFunction("Mean Red", new MeanRed());
			dataGeneratorFactory.injectFunction("Mean Green", new MeanGreen());
			dataGeneratorFactory.injectFunction("Mean Blue", new MeanBlue());
		};
	}

}
