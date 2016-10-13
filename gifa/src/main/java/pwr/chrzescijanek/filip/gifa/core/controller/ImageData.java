package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.Point;

public final class ImageData {

	public final Image image;
	public final Mat imageData;
	public final RectangleOfInterest rectangle;
	public final Triangle triangle;
	public double scale = 1.0;

	public ImageData( final Image image, final Mat imageData ) {
		this.image = image;
		this.imageData = imageData;
		double quarterWidth = image.getWidth() / 4;
		double quarterHeight = image.getHeight() / 4;
		double halfWidth = image.getWidth() / 2;
		double halfHeight = image.getHeight() / 2;
		this.rectangle = new RectangleOfInterest(quarterWidth, quarterHeight, halfWidth, halfHeight);
		this.triangle = new Triangle(
				new Point(halfWidth, quarterHeight),
				new Point(quarterWidth, quarterHeight + halfHeight),
				new Point(quarterWidth + halfWidth, quarterHeight + halfHeight));
	}

}
