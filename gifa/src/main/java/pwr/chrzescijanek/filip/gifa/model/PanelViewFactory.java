package pwr.chrzescijanek.filip.gifa.model;

import javafx.scene.image.Image;

public class PanelViewFactory {

	public PanelViewFactory() {	}

	public SamplePanelView createSamplePanelView( Image image, Sample sample) {
		return new SamplePanelView(image, sample);
	}

	public VertexSamplePanelView createVertexSamplePanelView( Image image, VertexSample sample) {
		return new VertexSamplePanelView(image, sample);
	}
}
