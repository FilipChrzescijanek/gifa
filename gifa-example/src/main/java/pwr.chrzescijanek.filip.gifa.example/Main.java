package pwr.chrzescijanek.filip.gifa.example;

import pwr.chrzescijanek.filip.gifa.core.generator.DataGeneratorFactory;
import pwr.chrzescijanek.filip.gifa.inject.Injector;

public class Main {

    public static void main(String[] args) {
        CustomFunctionManager cfm = new CustomFunctionManager();
        cfm.manageFunctions(Injector.instantiateComponent(DataGeneratorFactory.class));
        pwr.chrzescijanek.filip.gifa.Main.main();
    }
}
