package pwr.chrzescijanek.filip.gifa.core.generator;

import javax.inject.Inject;

public abstract class FunctionManager {

	@Inject
	public FunctionManager( final DataGeneratorFactory factory) {
		manageFunctions(factory);
	}

	protected abstract void manageFunctions( final DataGeneratorFactory factory );

}
