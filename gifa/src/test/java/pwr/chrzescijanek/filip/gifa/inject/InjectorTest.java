package pwr.chrzescijanek.filip.gifa.inject;

import org.junit.After;
import org.junit.Test;
import pwr.chrzescijanek.filip.gifa.controller.Controller;
import pwr.chrzescijanek.filip.gifa.core.generator.DataGeneratorFactory;
import pwr.chrzescijanek.filip.gifa.model.image.ImageDataFactory;
import pwr.chrzescijanek.filip.gifa.model.sample.BasicSampleFactory;
import pwr.chrzescijanek.filip.gifa.util.SharedState;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static pwr.chrzescijanek.filip.gifa.inject.Injector.instantiate;
import static pwr.chrzescijanek.filip.gifa.inject.Injector.instantiateComponent;

public class InjectorTest {

	@After
	public void tearDown() throws Exception {
		Injector.reset();
	}

	@Test
	public void instantiateComponentTest() throws Exception {
		final ControllerMock firstMock = instantiateComponent(ControllerMock.class);
		final ControllerMock secondMock = instantiateComponent(ControllerMock.class);
		assertEquals(firstMock, secondMock);
		assertMockFieldsNotNull(firstMock, secondMock);
	}

	private void assertMockFieldsNotNull(final ControllerMock... mocks) {
		for (final ControllerMock mock : mocks) {
			assertNotNull(mock.getSharedState());
			assertNotNull(mock.getDataGeneratorFactory());
			assertNotNull(mock.getBaseSampleFactory());
			assertNotNull(mock.getImageDataFactory());
		}
	}

	@Test
	public void instantiateTest() throws Exception {
		final ControllerMock firstMock = instantiate(ControllerMock.class);
		final ControllerMock secondMock = instantiate(ControllerMock.class);
		assertNotEquals(firstMock, secondMock);
		assertMockFieldsNotNull(firstMock, secondMock);
	}

	private class ControllerMock extends Controller {

		@Inject
		public ControllerMock(final SharedState state, final DataGeneratorFactory generatorFactory,
		                      final BasicSampleFactory basicSampleFactory, final ImageDataFactory imageDataFactory) {
			super(state, generatorFactory, basicSampleFactory, imageDataFactory);
		}

		SharedState getSharedState() {
			return state;
		}

		DataGeneratorFactory getDataGeneratorFactory() {
			return dataGeneratorFactory;
		}

		BasicSampleFactory getBaseSampleFactory() {
			return basicSampleFactory;
		}

		ImageDataFactory getImageDataFactory() {
			return imageDataFactory;
		}
	}

}