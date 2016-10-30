package pwr.chrzescijanek.filip.gifa.model;

import javafx.scene.image.Image;
import org.opencv.core.Mat;

import javax.inject.Inject;

public class ImageDataFactory {

	private SampleFactory sampleFactory;

	@Inject
	public ImageDataFactory( final SampleFactory sampleFactory ) { this.sampleFactory = sampleFactory; }

	public TransformImageData createTransformImageData( final Image image, final Mat imageData ) {
		return new TransformImageData(image, imageData, sampleFactory);
	}

	public SamplesImageData createSamplesImageData( final Image image, final Mat imageData ) {
		return new SamplesImageData(image, imageData);
	}
}
