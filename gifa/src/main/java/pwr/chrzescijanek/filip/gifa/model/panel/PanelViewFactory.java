package pwr.chrzescijanek.filip.gifa.model.panel;

import javafx.scene.image.Image;
import pwr.chrzescijanek.filip.gifa.model.sample.Sample;
import pwr.chrzescijanek.filip.gifa.model.sample.Vertex;

public class PanelViewFactory {

	public PanelViewFactory() {	}

	public SamplePanelView createSamplePanelView(Image image, Sample sample) {
		return new SamplePanelView(image, sample);
	}

	public VertexPanelView createVertexPanelView(Image image, Vertex sample) {
		return new VertexPanelView(image, sample);
	}
}
