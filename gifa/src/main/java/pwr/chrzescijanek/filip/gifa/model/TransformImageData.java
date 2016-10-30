package pwr.chrzescijanek.filip.gifa.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.opencv.core.Mat;

import static pwr.chrzescijanek.filip.gifa.model.BaseSample.STROKE_STYLE;

public class TransformImageData extends ImageData {

	public final VertexSample[] vertexSamples = new VertexSample[3];
	public final Triangle triangle;
	public final ObjectProperty<WritableImage> writableImage = new SimpleObjectProperty<>(null);

	TransformImageData( final Image image, final Mat imageData, final SampleFactory sampleFactory ) {
		super(image, imageData);
		initializeVertexSamples(image, sampleFactory);
		writableImage.set(
				new WritableImage(this.image.getPixelReader(), (int) this.image.getWidth(), (int) this.image.getHeight())
		);
		triangle = new Triangle(image.getWidth(), image.getHeight());
		initializeTriangle();
	}

	private void initializeVertexSamples( final Image image, final SampleFactory sampleFactory ) {
		vertexSamples[0] = sampleFactory.createNewVertexSample(
				this, image.getWidth() * 1 / 2, image.getHeight() * 3 / 10, image.getWidth() / 10, image.getHeight() / 10);
		vertexSamples[1] = sampleFactory.createNewVertexSample(
				this, image.getWidth() * 3 / 10, image.getHeight() * 7 / 10, image.getWidth() / 10, image.getHeight() / 10);
		vertexSamples[2] = sampleFactory.createNewVertexSample(
				this, image.getWidth() * 7 / 10, image.getHeight() * 7 / 10, image.getWidth() / 10, image.getHeight() / 10);
	}

	private void initializeTriangle() {
		triangle.setFill(Color.color(1.0, 1.0, 1.0, 0.35));
		triangle.setStroke(Color.color(1.0, 1.0, 1.0, 0.6));
		triangle.setStyle(STROKE_STYLE);
		bindPoints();
		bindScale();
	}

	private void bindScale() {
		triangle.scaleXProperty().bind(scale);
	}

	private void bindPoints() {
		triangle.pointsProperty[0].bind(vertexSamples[0].centerXProperty());
		triangle.pointsProperty[1].bind(vertexSamples[0].centerYProperty());
		triangle.pointsProperty[2].bind(vertexSamples[1].centerXProperty());
		triangle.pointsProperty[3].bind(vertexSamples[1].centerYProperty());
		triangle.pointsProperty[4].bind(vertexSamples[2].centerXProperty());
		triangle.pointsProperty[5].bind(vertexSamples[2].centerYProperty());
	}
}
