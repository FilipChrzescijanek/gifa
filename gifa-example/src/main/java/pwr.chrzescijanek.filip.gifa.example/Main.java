package pwr.chrzescijanek.filip.gifa.example;

import pwr.chrzescijanek.filip.gifa.core.generator.DataGeneratorFactory;
import pwr.chrzescijanek.filip.gifa.inject.Injector;
import pwr.chrzescijanek.filip.gifa.util.ControllerUtils;

public class Main {

	public static void main(final String[] args) {
		final CustomFunctionManager cfm = new CustomFunctionManager();
		cfm.manageFunctions(Injector.instantiateComponent(DataGeneratorFactory.class));
		pwr.chrzescijanek.filip.gifa.Main.main();
	}

}
