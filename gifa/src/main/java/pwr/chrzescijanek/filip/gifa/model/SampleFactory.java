package pwr.chrzescijanek.filip.gifa.model;

import javafx.scene.image.Image;
import pwr.chrzescijanek.filip.gifa.controller.SharedState;

import javax.inject.Inject;

public class SampleFactory {

	private SharedState state;
	private PanelViewFactory panelViewFactory;

	@Inject
	public SampleFactory( final PanelViewFactory panelViewFactory ) {
		this.panelViewFactory = panelViewFactory;
		this.state = SharedState.INSTANCE; }

	public Sample createNewSample( SamplesImageData imageData, double x, double y, double width, double height ) {
		final Image image = imageData.image;
		Sample sample = new Sample(imageData, x, y, width, height, state, panelViewFactory, image.getWidth(), image.getHeight());
		imageData.add(sample);
		return sample;
	}

	public VertexSample createNewVertexSample( TransformImageData imageData, double x, double y, double width, double height ) {
		final Image image = imageData.image;
		VertexSample sample = new VertexSample(imageData, x, y, width, height, state, panelViewFactory, image.getWidth(), image.getHeight());
		return sample;
	}
}
