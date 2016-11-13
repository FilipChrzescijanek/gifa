package pwr.chrzescijanek.filip.gifa.model.image;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.model.sample.BaseSampleFactory;

import javax.inject.Inject;

public class ImageDataFactory {

	private BaseSampleFactory baseSampleFactory;

	@Inject
	public ImageDataFactory( final BaseSampleFactory baseSampleFactory) { this.baseSampleFactory = baseSampleFactory; }

	public ImageToAlignData createImageToAlignData(final Image image, final Mat imageData ) {
		return new ImageToAlignData(image, imageData, baseSampleFactory);
	}

	public SamplesImageData createSamplesImageData(final Image image, final Mat imageData ) {
		return new SamplesImageData(image, imageData);
	}
}
