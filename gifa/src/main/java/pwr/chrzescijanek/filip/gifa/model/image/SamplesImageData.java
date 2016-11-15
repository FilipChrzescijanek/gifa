package pwr.chrzescijanek.filip.gifa.model.image;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.model.sample.Sample;

/**
 * Represents aligned images that can be compared using user-defined samples.
 */
public class SamplesImageData extends ImageData {

	/**
	 * User-defined samples.
	 */
	public final ObservableList<Sample> samples = FXCollections.observableArrayList();

	/**
	 * Constructs a new SamplesImageData with given JavaFX image and OpenCV image.
	 *
	 * @param image     JavaFX image
	 * @param imageData OpenCV image
	 */
	protected SamplesImageData(final Image image, final Mat imageData) {
		super(image, imageData);
	}

	/**
	 * Adds given sample
	 *
	 * @param sample sample to be added
	 */
	public void add(final Sample sample) { this.samples.add(sample); }

}
