package pwr.chrzescijanek.filip.gifa.example;

import pwr.chrzescijanek.filip.gifa.core.generator.DataGeneratorFactory;
import pwr.chrzescijanek.filip.gifa.core.generator.FunctionManager;
import pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils;
import pwr.chrzescijanek.filip.gifa.example.function.MeanBlue;
import pwr.chrzescijanek.filip.gifa.example.function.MeanGreen;
import pwr.chrzescijanek.filip.gifa.example.function.MeanRed;

public class CustomFunctionManager implements FunctionManager {

    @Override
    public void manageFunctions(DataGeneratorFactory dataGeneratorFactory) {
        dataGeneratorFactory.clearAvailableFunctions();
        dataGeneratorFactory.injectFunction("Mean Red (lambda)", mats -> FunctionUtils.calculateMeans(mats, 2));
        dataGeneratorFactory.injectFunction("Mean Red", new MeanRed());
        dataGeneratorFactory.injectFunction("Mean Green", new MeanGreen());
        dataGeneratorFactory.injectFunction("Mean Blue", new MeanBlue());
    }

}
