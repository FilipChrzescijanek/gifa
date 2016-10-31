package pwr.chrzescijanek.filip.gifa.model;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import pwr.chrzescijanek.filip.gifa.controller.SharedState;

public class VertexSamplePanelView extends PanelView {

	VertexSamplePanelView( final Image image, final VertexSample sample ) {
		super(image, sample);
	}

	@Override
	void setOnMouseClicked() {
		setOnMouseClicked(event -> {
			SharedState.INSTANCE.zoom = true;
			SharedState.INSTANCE.selectedRectangle.set(this.sample);
			bindToNewPosition(event);
		});
	}

	@Override
	void setOnMouseDragged() {
		setOnMouseDragged(this::moveSampleCenter);
	}

	@Override
	void setOnScroll() { }

	private void bindToNewPosition( final MouseEvent event ) {
		final int index = sample.getIndexOf();
		final Triangle triangle = ( (TransformImageData) sample.imageData ).triangle;
		triangle.pointsProperty[index * 2].unbind();
		triangle.pointsProperty[index * 2 + 1].unbind();
		DoubleBinding widthBinding = getWidthBinding(event);
		DoubleBinding heightBinding = getHeightBinding(event);
		triangle.pointsProperty[index * 2].bind(widthBinding);
		triangle.pointsProperty[index * 2 + 1].bind(heightBinding);
	}

	private DoubleBinding getWidthBinding( final MouseEvent event ) {
		return Math.abs(event.getX()) < 0.00001 ?
				sample.sampleArea.xProperty().add(0)
				: sample.sampleArea.xProperty().add(sample.sampleArea.widthProperty().divide(getBoundsInParent().getWidth() / event.getX()));
	}

	private DoubleBinding getHeightBinding( final MouseEvent event ) {
		return Math.abs(event.getY()) < 0.00001 ?
				sample.sampleArea.yProperty().add(0)
				: sample.sampleArea.yProperty().add(sample.sampleArea.heightProperty().divide(getBoundsInParent().getHeight() / event.getY()));
	}
}
