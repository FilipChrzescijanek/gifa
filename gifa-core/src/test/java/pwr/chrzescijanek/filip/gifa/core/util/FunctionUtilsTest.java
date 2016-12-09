package pwr.chrzescijanek.filip.gifa.core.util;

import org.junit.Before;
import org.junit.Test;

import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateEntropy;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateLinearFuzziness;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateMean;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateQuadraticFuzziness;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateStdDeviation;
import static pwr.chrzescijanek.filip.gifa.core.util.FunctionUtils.calculateVariance;

public class FunctionUtilsTest {

	private static final double EPSILON = 0.000000001;

	private final byte[] image = new byte[12];

	@Before
	public void setUp() throws Exception {
		setUpImage();
	}

	private void setUpImage() {
		image[0] = -1;
		image[1] = -1;
		image[2] = -1;
		image[3] = 56;
		image[4] = 34;
		image[5] = 20;
		image[6] = 100;
		image[7] = -100;
		image[8] = -90;
		image[9] = 120;
		image[10] = 10;
		image[11] = 80;
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkForException() throws Exception {
		assertEquals(0.0, calculateMean(image, 3, 3), EPSILON);
	}

	@Test
	public void calculateMeanTest() throws Exception {
		assertEquals(132.75 / 255.0, calculateMean(image, 3, 0), EPSILON);
		assertEquals(113.75 / 255.0, calculateMean(image, 3, 1), EPSILON);
		assertEquals(130.25 / 255.0, calculateMean(image, 3, 2), EPSILON);
	}

	@Test
	public void calculateVarianceTest() throws Exception {
		assertEquals(88_283 / 1_040_400.0, calculateVariance(image, 3, 0), EPSILON);
		assertEquals(155_443 / 1_040_400.0, calculateVariance(image, 3, 1), EPSILON);
		assertEquals(126_083 / 1_040_400.0, calculateVariance(image, 3, 2), EPSILON);
	}

	@Test
	public void calculateStdDeviationTest() throws Exception {
		assertEquals(sqrt(88_283 / 1_040_400.0), calculateStdDeviation(image, 3, 0), EPSILON);
		assertEquals(sqrt(155_443 / 1_040_400.0), calculateStdDeviation(image, 3, 1), EPSILON);
		assertEquals(sqrt(126_083 / 1_040_400.0), calculateStdDeviation(image, 3, 2), EPSILON);
	}

	@Test
	public void calculateLinearFuzzinessTest() throws Exception {
		assertEquals(138.0 / 255.0, calculateLinearFuzziness(image, 3, 0), EPSILON);
		assertEquals(71.5 / 255.0, calculateLinearFuzziness(image, 3, 1), EPSILON);
		assertEquals(94.5 / 255.0, calculateLinearFuzziness(image, 3, 2), EPSILON);
	}

	@Test
	public void calculateQuadraticFuzzinessTest() throws Exception {
		assertEquals(sqrt(27_536 / 65_025.0), calculateQuadraticFuzziness(image, 3, 0), EPSILON);
		assertEquals(sqrt(11_057 / 65_025.0), calculateQuadraticFuzziness(image, 3, 1), EPSILON);
		assertEquals(sqrt(14_721 / 65_025.0), calculateQuadraticFuzziness(image, 3, 2), EPSILON);
	}

	@Test
	public void calculateEntropyTest() throws Exception {
		assertEquals(2.0, calculateEntropy(image, 3, 0), EPSILON);
		assertEquals(2.0, calculateEntropy(image, 3, 1), EPSILON);
		assertEquals(2.0, calculateEntropy(image, 3, 2), EPSILON);
	}

}