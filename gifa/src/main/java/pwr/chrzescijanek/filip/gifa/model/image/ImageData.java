package pwr.chrzescijanek.filip.gifa.model.image;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.opencv.core.Mat;

public abstract class ImageData {

	public ObjectProperty<Image> image = new SimpleObjectProperty<>(null);
	public final DoubleProperty scale = new SimpleDoubleProperty(1.0);
	public final DoubleProperty vScrollPos = new SimpleDoubleProperty(0.5);
	public final DoubleProperty hScrollPos = new SimpleDoubleProperty(0.5);

	public final Mat imageData;

	protected ImageData( final Image image, final Mat imageData ) {
		this.image.set(image);
		this.imageData = imageData;
	}

}
