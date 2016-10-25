package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.Point;

public class ImageData {

	public static final String STROKE_STYLE = "-fx-stroke-type: inside;\n" +
			"    -fx-stroke-width: 2;\n" +
			"    -fx-stroke-dash-array: 12 2 4 2;\n" +
			"    -fx-stroke-dash-offset: 6;\n" +
			"    -fx-stroke-line-cap: butt;";
	public Image image;
	public Mat imageData;
	public double scale = 1.0;
	public double vScrollPos = 0.0;
	public double hScrollPos = 0.0;

	public ImageData( final Image image, final Mat imageData ) {
		this.image = image;
		this.imageData = imageData;
	}

}
