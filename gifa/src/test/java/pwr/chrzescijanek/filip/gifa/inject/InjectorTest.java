package pwr.chrzescijanek.filip.gifa.inject;

import org.junit.After;
import org.junit.Test;
import pwr.chrzescijanek.filip.gifa.controller.Controller;
import pwr.chrzescijanek.filip.gifa.core.generator.DataGeneratorFactory;
import pwr.chrzescijanek.filip.gifa.model.image.ImageDataFactory;
import pwr.chrzescijanek.filip.gifa.model.sample.BaseSampleFactory;
import pwr.chrzescijanek.filip.gifa.util.SharedState;

import javax.inject.Inject;

import static org.junit.Assert.*;
import static pwr.chrzescijanek.filip.gifa.inject.Injector.instantiate;
import static pwr.chrzescijanek.filip.gifa.inject.Injector.instantiateComponent;

public class InjectorTest {

    private class ControllerMock extends Controller {

        SharedState getSharedState() {
            return state;
        }

        DataGeneratorFactory getDataGeneratorFactory() {
            return dataGeneratorFactory;
        }

        BaseSampleFactory getBaseSampleFactory() {
            return baseSampleFactory;
        }

        ImageDataFactory getImageDataFactory() {
            return imageDataFactory;
        }

        @Inject
        public ControllerMock(SharedState state, DataGeneratorFactory generatorFactory, BaseSampleFactory baseSampleFactory, ImageDataFactory imageDataFactory) {
            super(state, generatorFactory, baseSampleFactory, imageDataFactory);
        }
    }

    @After
    public void tearDown() throws Exception {
        Injector.reset();
    }

    @Test
    public void instantiateComponentTest() throws Exception {
        ControllerMock firstMock = instantiateComponent(ControllerMock.class);
        ControllerMock secondMock = instantiateComponent(ControllerMock.class);
        assertEquals(firstMock, secondMock);
        assertMockFieldsNotNull(firstMock, secondMock);
    }

    @Test
    public void instantiateTest() throws Exception {
        ControllerMock firstMock = instantiate(ControllerMock.class);
        ControllerMock secondMock = instantiate(ControllerMock.class);
        assertNotEquals(firstMock, secondMock);
        assertMockFieldsNotNull(firstMock, secondMock);
    }

    private void assertMockFieldsNotNull(ControllerMock... mocks) {
        for (ControllerMock mock : mocks) {
            assertNotNull(mock.getSharedState());
            assertNotNull(mock.getDataGeneratorFactory());
            assertNotNull(mock.getBaseSampleFactory());
            assertNotNull(mock.getImageDataFactory());
        }
    }

}