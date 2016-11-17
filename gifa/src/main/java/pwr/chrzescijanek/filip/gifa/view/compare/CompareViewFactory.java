package pwr.chrzescijanek.filip.gifa.view.compare;

import javafx.scene.image.Image;
import pwr.chrzescijanek.filip.gifa.model.sample.Sample;
import pwr.chrzescijanek.filip.gifa.model.sample.Vertex;

/**
 * Provides factory methods for creating CompareView subclasses instances.
 */
public class CompareViewFactory {

	/**
	 * Default constructor.
	 */
	public CompareViewFactory() { }

	/**
	 * Constructs a new SampleCompareView given JavaFX image and sample.
	 *
	 * @param image  JavaFX image
	 * @param sample user-defined sample that will be shown
	 * @return new SampleCompareView class instance
	 */
	public SampleCompareView createSamplePanelView(final Image image, final Sample sample) {
		return new SampleCompareView(image, sample);
	}

	/**
	 * Constructs a new VertexCompareView given JavaFX image and sample.
	 *
	 * @param image  JavaFX image
	 * @param sample vertex that will be shown
	 * @return new VertexCompareView class instance
	 */
	public VertexCompareView createVertexPanelView(final Image image, final Vertex sample) {
		return new VertexCompareView(image, sample);
	}

}
