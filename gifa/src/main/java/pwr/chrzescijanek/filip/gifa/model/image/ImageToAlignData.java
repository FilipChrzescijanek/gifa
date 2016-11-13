package pwr.chrzescijanek.filip.gifa.model.image;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.model.sample.BaseSampleFactory;
import pwr.chrzescijanek.filip.gifa.model.sample.Vertex;

public class ImageToAlignData extends ImageData {

	protected static final String STROKE_STYLE = "-fx-stroke-type: inside;\n" +
			"    -fx-stroke-width: 2;\n" +
			"    -fx-stroke-dash-array: 12 2 4 2;\n" +
			"    -fx-stroke-dash-offset: 6;\n" +
			"    -fx-stroke-line-cap: butt;";

	public final ObjectProperty<WritableImage> writableImage = new SimpleObjectProperty<>(null);

	public final Vertex[] vertices = new Vertex[3];
	public final Triangle triangle;

	protected ImageToAlignData(final Image image, final Mat imageData, final BaseSampleFactory baseSampleFactory) {
		super(image, imageData);
		initializeVertices(baseSampleFactory);
		writableImage.set(
				new WritableImage(
						this.image.get().getPixelReader(),
						(int) this.image.get().getWidth(),
						(int) this.image.get().getHeight())
		);
		triangle = new Triangle(image.getWidth(), image.getHeight());
		initializeTriangle();
	}

	public void recalculateVertices() {
		Image image = this.image.get();
		vertices[0].setSample(image.getWidth() * 1 / 2, image.getHeight() * 3 / 10, image.getWidth() / 10, image.getHeight() / 10,
				image.getWidth(), image.getHeight());
		vertices[1].setSample(image.getWidth() * 3 / 10, image.getHeight() * 7 / 10, image.getWidth() / 10, image.getHeight() / 10,
				image.getWidth(), image.getHeight());
		vertices[2].setSample(image.getWidth() * 7 / 10, image.getHeight() * 7 / 10, image.getWidth() / 10, image.getHeight() / 10,
				image.getWidth(), image.getHeight());
	}

	private void initializeVertices(final BaseSampleFactory baseSampleFactory) {
		Image image = this.image.get();
		vertices[0] = baseSampleFactory.createNewVertex(
				this, image.getWidth() * 1 / 2, image.getHeight() * 3 / 10, image.getWidth() / 10, image.getHeight() / 10);
		vertices[1] = baseSampleFactory.createNewVertex(
				this, image.getWidth() * 3 / 10, image.getHeight() * 7 / 10, image.getWidth() / 10, image.getHeight() / 10);
		vertices[2] = baseSampleFactory.createNewVertex(
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
		triangle.pointsProperty[0].bind(vertices[0].centerXProperty());
		triangle.pointsProperty[1].bind(vertices[0].centerYProperty());
		triangle.pointsProperty[2].bind(vertices[1].centerXProperty());
		triangle.pointsProperty[3].bind(vertices[1].centerYProperty());
		triangle.pointsProperty[4].bind(vertices[2].centerXProperty());
		triangle.pointsProperty[5].bind(vertices[2].centerYProperty());
	}

}
