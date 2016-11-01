package pwr.chrzescijanek.filip.gifa.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import org.opencv.core.Mat;

public abstract class ImageData {

	public Image image;
	public Mat imageData;
	public final DoubleProperty scale = new SimpleDoubleProperty(1.0);
	public double vScrollPos = 0.5;
	public double hScrollPos = 0.5;

	ImageData( final Image image, final Mat imageData ) {
		this.image = image;
		this.imageData = imageData;
	}

}
