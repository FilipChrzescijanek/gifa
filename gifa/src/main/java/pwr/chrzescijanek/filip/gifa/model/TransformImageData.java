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
				this, image.getWidth() * 2 / 5, image.getHeight() / 5, image.getWidth() / 5, image.getHeight() / 5);
		vertexSamples[1] = sampleFactory.createNewVertexSample(
				this, image.getWidth() * 1 / 5, image.getHeight() * 3 / 5, image.getWidth() / 5, image.getHeight() / 5);
		vertexSamples[2] = sampleFactory.createNewVertexSample(
				this, image.getWidth() * 3 / 5, image.getHeight() * 3 / 5, image.getWidth() / 5, image.getHeight() / 5);
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
		triangle.pointsProperty[0].bind(vertexSamples[0].xProperty().add(vertexSamples[0].widthProperty().divide(2.0)));
		triangle.pointsProperty[1].bind(vertexSamples[0].yProperty().add(vertexSamples[0].heightProperty().divide(2.0)));
		triangle.pointsProperty[2].bind(vertexSamples[1].xProperty().add(vertexSamples[1].widthProperty().divide(2.0)));
		triangle.pointsProperty[3].bind(vertexSamples[1].yProperty().add(vertexSamples[1].heightProperty().divide(2.0)));
		triangle.pointsProperty[4].bind(vertexSamples[2].xProperty().add(vertexSamples[2].widthProperty().divide(2.0)));
		triangle.pointsProperty[5].bind(vertexSamples[2].yProperty().add(vertexSamples[2].heightProperty().divide(2.0)));
	}
}
