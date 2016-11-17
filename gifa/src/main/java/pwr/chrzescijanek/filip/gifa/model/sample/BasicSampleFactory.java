package pwr.chrzescijanek.filip.gifa.model.sample;

import javafx.scene.image.Image;
import pwr.chrzescijanek.filip.gifa.model.image.ImageToAlignData;
import pwr.chrzescijanek.filip.gifa.model.image.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.util.SharedState;

import javax.inject.Inject;

/**
 * Provides factory methods for creating BasicSample subclasses instances.
 */
public class BasicSampleFactory {

	private final SharedState state;

	/**
	 * Constructs a new BasicSampleFactory with given shared state.
	 *
	 * @param state shared state
	 */
	@Inject
	public BasicSampleFactory(final SharedState state) {
		this.state = state;
	}

	/**
	 * Constructs a new Sample on given image, with given position and size.
	 *
	 * @param imageData image
	 * @param x         X position
	 * @param y         Y position
	 * @param radiusX   X radius
	 * @param radiusY   Y radius
	 * @return new Sample class instance
	 */
	public Sample createNewSample(final SamplesImageData imageData, final double x, final double y,
	                              final double radiusX, final double radiusY) {
		final Image image = imageData.image.get();
		final Sample sample = new Sample(imageData, x, y, radiusX, radiusY, state,
		                                 image.getWidth(), image.getHeight());
		imageData.add(sample);
		return sample;
	}

	/**
	 * Constructs a new Vertex on given image, with given position and size.
	 *
	 * @param imageData image
	 * @param x         X position
	 * @param y         Y position
	 * @param radiusX   X radius
	 * @param radiusY   Y radius
	 * @return new Vertex class instance
	 */
	public Vertex createNewVertex(final ImageToAlignData imageData, final double x, final double y,
	                              final double radiusX, final double radiusY) {
		final Image image = imageData.image.get();
		return new Vertex(imageData, x, y, radiusX, radiusY, state, image.getWidth(), image.getHeight());
	}

}
