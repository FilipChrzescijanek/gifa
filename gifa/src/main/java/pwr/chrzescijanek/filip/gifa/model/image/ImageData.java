package pwr.chrzescijanek.filip.gifa.model.image;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.opencv.core.Mat;

/**
 * Represents basic image data.
 */
public abstract class ImageData {

	/**
	 * JavaFX image.
	 */
	public ObjectProperty<Image> image = new SimpleObjectProperty<>(null);

	/**
	 * Current image scale.
	 */
	public final DoubleProperty scale = new SimpleDoubleProperty(1.0);

	/**
	 * Current vertical viewport position.
	 */
	public final DoubleProperty vScrollPos = new SimpleDoubleProperty(0.5);

	/**
	 * Current horizontal viewport position.
	 */
	public final DoubleProperty hScrollPos = new SimpleDoubleProperty(0.5);

	/**
	 * OpenCV image.
	 */
	public final Mat imageData;

	/**
	 * Construct a new ImageData with given JavaFX image and OpenCV image.
	 * @param image JavaFX image
	 * @param imageData OpenCV image
     */
	protected ImageData( final Image image, final Mat imageData ) {
		this.image.set(image);
		this.imageData = imageData;
	}

}
