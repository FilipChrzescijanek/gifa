package pwr.chrzescijanek.filip.gifa.model.sample;

import javafx.scene.image.Image;
import pwr.chrzescijanek.filip.gifa.util.SharedState;
import pwr.chrzescijanek.filip.gifa.model.image.ImageToAlignData;
import pwr.chrzescijanek.filip.gifa.model.image.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.panel.PanelViewFactory;

import javax.inject.Inject;

/**
 * Provides factory methods for creating BasicSample subclasses instances.
 */
public class BaseSampleFactory {

	private SharedState state;
	private PanelViewFactory panelViewFactory;

	/**
	 * Constructs a new BaseSampleFactory with given shared state and panel views factory.
	 * @param state shared state
	 * @param panelViewFactory panel views factory
     */
	@Inject
	public BaseSampleFactory(final SharedState state, final PanelViewFactory panelViewFactory ) {
		this.state = state;
		this.panelViewFactory = panelViewFactory;
	}

	/**
	 * Constructs a new Sample on given image, with given position and size.
	 * @param imageData image
	 * @param x X position
	 * @param y Y position
	 * @param radiusX X radius
	 * @param radiusY Y radius
     * @return new Sample class instance
     */
	public Sample createNewSample(SamplesImageData imageData, double x, double y, double radiusX, double radiusY ) {
		final Image image = imageData.image.get();
		Sample sample = new Sample(imageData, x, y, radiusX, radiusY, state, panelViewFactory, image.getWidth(), image.getHeight());
		imageData.add(sample);
		return sample;
	}

	/**
	 * Constructs a new Vertex on given image, with given position and size.
	 * @param imageData image
	 * @param x X position
	 * @param y Y position
	 * @param radiusX X radius
	 * @param radiusY Y radius
     * @return new Vertex class instance
     */
	public Vertex createNewVertex(ImageToAlignData imageData, double x, double y, double radiusX, double radiusY ) {
		final Image image = imageData.image.get();
		Vertex vertex = new Vertex(imageData, x, y, radiusX, radiusY, state, panelViewFactory, image.getWidth(), image.getHeight());
		return vertex;
	}

}
