package pwr.chrzescijanek.filip.gifa.model.image;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.model.sample.BasicSampleFactory;
import pwr.chrzescijanek.filip.gifa.model.sample.Vertex;

/**
 * Represents image that will be aligned.
 */
public class ImageToAlignData extends ImageData {

	/**
	 * JavaFX writable image.
	 */
	public final ObjectProperty<WritableImage> writableImage = new SimpleObjectProperty<>(null);

	/**
	 * Samples connected to triangle vertices.
	 */
	public final Vertex[] vertices = new Vertex[3];

	/**
	 * Triangle used for superimposing.
	 */
	public final Triangle triangle;

	/**
	 * Constructs a new ImageToAlignData with given JavaFX image and OpenCV image, that uses samples
	 * factory for vertices initialization.
	 *
	 * @param image              JavaFX image
	 * @param imageData          OpenCV image
	 * @param basicSampleFactory BasicSample subclasses factory used for vertices initialization
	 */
	protected ImageToAlignData(final Image image, final Mat imageData, final BasicSampleFactory basicSampleFactory) {
		super(image, imageData);
		initializeVertices(basicSampleFactory);
		writableImage.set(
				new WritableImage(
						this.image.get().getPixelReader(),
						(int) this.image.get().getWidth(),
						(int) this.image.get().getHeight())
		);
		triangle = new Triangle(image.getWidth(), image.getHeight());
		initializeTriangle();
	}

	private void initializeVertices(final BasicSampleFactory basicSampleFactory) {
		final Image image = this.image.get();
		vertices[0] = basicSampleFactory.createNewVertex(this,
		                                                 image.getWidth() * 1 / 2,
		                                                 image.getHeight() * 3 / 10,
		                                                 image.getWidth() / 10,
		                                                 image.getHeight() / 10);
		vertices[1] = basicSampleFactory.createNewVertex(this,
		                                                 image.getWidth() * 3 / 10,
		                                                 image.getHeight() * 7 / 10,
		                                                 image.getWidth() / 10,
		                                                 image.getHeight()
		                                                 / 10);
		vertices[2] = basicSampleFactory.createNewVertex(this,
		                                                 image.getWidth() * 7 / 10,
		                                                 image.getHeight() * 7 / 10,
		                                                 image.getWidth() / 10,
		                                                 image.getHeight() / 10);
	}

	private void initializeTriangle() {
		bindPoints();
		bindScale();
	}

	private void bindPoints() {
		triangle.pointsProperty[0].bind(vertices[0].centerXProperty());
		triangle.pointsProperty[1].bind(vertices[0].centerYProperty());
		triangle.pointsProperty[2].bind(vertices[1].centerXProperty());
		triangle.pointsProperty[3].bind(vertices[1].centerYProperty());
		triangle.pointsProperty[4].bind(vertices[2].centerXProperty());
		triangle.pointsProperty[5].bind(vertices[2].centerYProperty());
	}

	private void bindScale() {
		triangle.scaleXProperty().bind(scale);
	}

	/**
	 * Puts triangle vertices onto their default positions.
	 */
	public void recalculateVertices() {
		final Image image = this.image.get();
		vertices[0].setSample(image.getWidth() * 1 / 2,
		                      image.getHeight() * 3 / 10,
		                      image.getWidth() / 10,
		                      image.getHeight() / 10,
		                      image.getWidth(), image.getHeight());
		vertices[1].setSample(image.getWidth() * 3 / 10,
		                      image.getHeight() * 7 / 10,
		                      image.getWidth() / 10,
		                      image.getHeight() / 10,
		                      image.getWidth(), image.getHeight());
		vertices[2].setSample(image.getWidth() * 7 / 10,
		                      image.getHeight() * 7 / 10,
		                      image.getWidth() / 10,
		                      image.getHeight() / 10,
		                      image.getWidth(), image.getHeight());
	}

}
