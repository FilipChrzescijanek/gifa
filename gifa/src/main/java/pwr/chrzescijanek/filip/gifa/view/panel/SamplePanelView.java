package pwr.chrzescijanek.filip.gifa.view.panel;

import javafx.scene.image.Image;
import pwr.chrzescijanek.filip.gifa.model.sample.Sample;

/**
 * Represents single panel view that shows user-defined sample.
 */
public class SamplePanelView extends PanelView {

	/**
	 * Constructs a new SamplePanelView given JavaFX image and sample.
	 *
	 * @param image  JavaFX image
	 * @param sample user-defined sample that will be shown
	 */
	protected SamplePanelView(final Image image, final Sample sample) {
		super(image, sample);
	}

	@Override
	protected void setOnMouseClicked() {
		setOnMouseClicked(event -> {
			sample.state.selectedSample.set((Sample) sample);
			sample.state.zoomSample.set(true);
		});
	}

	@Override
	protected void setOnMouseDragged() {
		setOnMouseDragged(event -> {
			if (event.isControlDown()) {
				moveSampleCenter(event);
			}
			else {
				final int index = sample.getIndexOf();
				final double deltaX = -(event.getX() - startX);
				final double deltaY = -(event.getY() - startY);
				startX = event.getX();
				startY = event.getY();
				sample.state.samplesImages.values()
				                          .forEach(img -> img.samples.get(index).moveCenterBy(deltaX, deltaY));
			}
		});
	}

	@Override
	protected void setOnScroll() {
		setOnScroll(event -> {
			if (sample.state.rotate.get()) {
				if (event.isControlDown()) {
					((Sample) sample).rotate(event);
				}
				else {
					final int index = sample.getIndexOf();
					sample.state.samplesImages.values()
					                          .forEach(img -> img.samples.get(index).updateRotation(event));
					event.consume();
				}
			}
		});
	}

}
