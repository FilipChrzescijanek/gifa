package pwr.chrzescijanek.filip.gifa.model.panel;

import javafx.scene.image.Image;
import pwr.chrzescijanek.filip.gifa.model.sample.Sample;
import pwr.chrzescijanek.filip.gifa.model.sample.Vertex;

/**
 * Provides factory methods for creating PanelView subclasses instances
 */
public class PanelViewFactory {

	/**
	 * Default constructor.
	 */
	public PanelViewFactory() {	}

	/**
	 * Constructs a new SamplePanelView given JavaFX image and sample.
	 * @param image JavaFX image
	 * @param sample user-defined sample that will be shown
     * @return new SamplePanelView class instance
     */
	public SamplePanelView createSamplePanelView(Image image, Sample sample) {
		return new SamplePanelView(image, sample);
	}

	/**
	 * Constructs a new VertexPanelView given JavaFX image and sample.
	 * @param image JavaFX image
	 * @param sample vertex that will be shown
     * @return new VertexPanelView class instance
     */
	public VertexPanelView createVertexPanelView(Image image, Vertex sample) {
		return new VertexPanelView(image, sample);
	}

}