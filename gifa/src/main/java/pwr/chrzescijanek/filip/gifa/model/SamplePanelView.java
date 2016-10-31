package pwr.chrzescijanek.filip.gifa.model;

import javafx.scene.image.Image;

public class SamplePanelView extends PanelView {

	SamplePanelView( final Image image, final Sample sample ) {
		super(image, sample);
	}

	@Override
	void setOnMouseClicked() {
		setOnMouseClicked(event -> {
				sample.state.zoom = true;
				sample.state.selectedSample.set(sample);
		});
	}

	@Override
	void setOnMouseDragged() {
		setOnMouseDragged(event -> {
			if (event.isControlDown()) {
				moveSampleCenter(event);
			} else {
				final int index = sample.getIndexOf();
				final double deltaX = -( event.getX() - startX );
				final double deltaY = -( event.getY() - startY );
				startX = event.getX();
				startY = event.getY();
				sample.state.samplesImages.values()
						.forEach(img -> img.samples.get(index).moveCenterBy(deltaX, deltaY));
			}
		});
	}

	@Override
	void setOnScroll() {
		setOnScroll(event -> {
			if ( sample.state.rotate.get() ) {
				if ( event.isControlDown() ) {
					((Sample) sample).rotate(event);
				} else {
					final int index = sample.getIndexOf();
					sample.state.samplesImages.values()
							.forEach(img -> img.samples.get(index).updateRotation(event));
					event.consume();
				}
		}
		});
	}

}
