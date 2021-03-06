package pwr.chrzescijanek.filip.gifa.core.generator;

/**
 * Interface that allows to manage DataGeneratorFactory functions.
 */
@FunctionalInterface
public interface FunctionManager {

	/**
	 * Manages functions of given factory.
	 *
	 * @param factory factory to be managed
	 */
	void manageFunctions(DataGeneratorFactory factory);

}
