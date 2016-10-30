package pwr.chrzescijanek.filip.gifa.model;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import pwr.chrzescijanek.filip.gifa.controller.SharedState;

public class VertexSamplePanelView extends PanelView {

	VertexSamplePanelView( final Image image, final VertexSample sample ) {
		super(image, sample);
		setOnMouseClicked(event -> {
			SharedState.INSTANCE.zoom = true;
			SharedState.INSTANCE.selectedRectangle.set(this.sample);
			bindToNewPosition(event);
		});
	}

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
					sample.xProperty().add(0)
					: sample.xProperty().add(sample.widthProperty().divide(getBoundsInParent().getWidth() / event.getX()));
	}

	private DoubleBinding getHeightBinding( final MouseEvent event ) {
		return Math.abs(event.getY()) < 0.00001 ?
				sample.yProperty().add(0)
				: sample.yProperty().add(sample.heightProperty().divide(getBoundsInParent().getHeight() / event.getY()));
	}

}
