package pwr.chrzescijanek.filip.gifa.model.panel;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import pwr.chrzescijanek.filip.gifa.model.image.ImageToAlignData;
import pwr.chrzescijanek.filip.gifa.model.image.Triangle;
import pwr.chrzescijanek.filip.gifa.model.sample.Vertex;

/**
 * Represents single panel view that shows vertex.
 */
public class VertexPanelView extends PanelView {

	/**
	 * Constructs a new VertexPanelView given JavaFX image and sample.
	 * @param image JavaFX image
	 * @param sample vertex that will be shown
     */
	protected VertexPanelView(final Image image, final Vertex sample ) {
		super(image, sample);
	}

	@Override
	protected void setOnMouseClicked() {
		setOnMouseClicked(event -> {
			this.sample.state.selectedVertex.set((Vertex) sample);
			this.sample.state.zoomVertex.set(true);
			bindToNewPosition(event);
		});
	}

	@Override
	protected void setOnMouseDragged() {
		setOnMouseDragged(this::moveSampleCenter);
	}

	@Override
	protected void setOnScroll() { }

	private void bindToNewPosition( final MouseEvent event ) {
		final int index = sample.getIndexOf();
		final Triangle triangle = ( (ImageToAlignData) sample.imageData ).triangle;
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
