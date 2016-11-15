package pwr.chrzescijanek.filip.gifa.model.image;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.model.sample.BasicSampleFactory;

import javax.inject.Inject;

/**
 * Provides factory methods for creating ImageData subclasses instances.
 */
public class ImageDataFactory {

	private final BasicSampleFactory basicSampleFactory;

	/**
	 * Constructs a new ImageDataFactory with given samples factory.
	 *
	 * @param basicSampleFactory samples factory
	 */
	@Inject
	public ImageDataFactory(final BasicSampleFactory basicSampleFactory) {
		this.basicSampleFactory = basicSampleFactory;
	}

	/**
	 * Constructs a new ImageToAlignData with given JavaFX image and OpenCV image.
	 *
	 * @param image     JavaFX image
	 * @param imageData OpenCV image
	 * @return new ImageToAlignData class instance
	 */
	public ImageToAlignData createImageToAlignData(final Image image, final Mat imageData) {
		return new ImageToAlignData(image, imageData, basicSampleFactory);
	}

	/**
	 * Constructs a new SamplesImageData with given JavaFX image and OpenCV image.
	 *
	 * @param image     JavaFX image
	 * @param imageData OpenCV image
	 * @return new SamplesImageData class instance
	 */
	public SamplesImageData createSamplesImageData(final Image image, final Mat imageData) {
		return new SamplesImageData(image, imageData);
	}

}
