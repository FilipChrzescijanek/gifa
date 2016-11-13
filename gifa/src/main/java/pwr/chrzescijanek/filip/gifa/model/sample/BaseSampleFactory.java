package pwr.chrzescijanek.filip.gifa.model.sample;

import javafx.scene.image.Image;
import pwr.chrzescijanek.filip.gifa.controller.SharedState;
import pwr.chrzescijanek.filip.gifa.model.image.ImageToAlignData;
import pwr.chrzescijanek.filip.gifa.model.image.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.panel.PanelViewFactory;

import javax.inject.Inject;

public class BaseSampleFactory {

	private SharedState state;
	private PanelViewFactory panelViewFactory;

	@Inject
	public BaseSampleFactory(final SharedState state, final PanelViewFactory panelViewFactory ) {
		this.state = state;
		this.panelViewFactory = panelViewFactory;
	}

	public Sample createNewSample(SamplesImageData imageData, double x, double y, double radiusX, double radiusY ) {
		final Image image = imageData.image.get();
		Sample sample = new Sample(imageData, x, y, radiusX, radiusY, state, panelViewFactory, image.getWidth(), image.getHeight());
		imageData.add(sample);
		return sample;
	}

	public Vertex createNewVertex(ImageToAlignData imageData, double x, double y, double radiusX, double radiusY ) {
		final Image image = imageData.image.get();
		Vertex vertex = new Vertex(imageData, x, y, radiusX, radiusY, state, panelViewFactory, image.getWidth(), image.getHeight());
		return vertex;
	}

}
