package pwr.chrzescijanek.filip.gifa.view.panel;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;
import pwr.chrzescijanek.filip.gifa.model.sample.BasicSample;

/**
 * Represents single panel view.
 */
public abstract class PanelView extends ImageView {

	/**
	 * Associated sample.
	 */
	protected final BasicSample sample;

	/**
	 * Viewport clip.
	 */
	protected final Ellipse clip = new Ellipse();

	/**
	 * Current scale.
	 */
	protected final DoubleProperty scale = new SimpleDoubleProperty(1.0);

	/**
	 * Drag start X position.
	 */
	protected double startX;

	/**
	 * Drag start Y position.
	 */
	protected double startY;

	/**
	 * Constructs a new PanelView given JavaFX image and sample.
	 *
	 * @param image  JavaFX image
	 * @param sample sample that will be shown
	 */
	protected PanelView(final Image image, final BasicSample sample) {
		super(image);
		this.sample = sample;
		addListeners();
		setViewport();
		bindScale();
		setPreserveRatio(true);
		createClip();
	}

	private void addListeners() {
		setOnMousePressed();
		setOnMouseClicked();
		setOnMouseDragged();
		setOnScroll();
	}

	private void setViewport() {
		final Rectangle2D rectangle2D = new Rectangle2D(sample.sampleArea.getX(), sample.sampleArea.getY(),
		                                                sample.sampleArea.getWidth(), sample.sampleArea.getHeight());
		setViewport(rectangle2D);
		onXPropertyChanged();
		onYPropertyChanged();
		onWidthPropertyChanged();
		onHeightPropertyChanged();
	}

	private void bindScale() {
		scale.bind(new DoubleBinding() {
			{
				super.bind(viewportProperty(), fitWidthProperty(), fitHeightProperty());
			}

			@Override
			protected double computeValue() {
				final double width = getViewport().getWidth();
				final double height = getViewport().getHeight();
				return getFitWidth() / getFitHeight() > width / height ?
						getFitHeight() / height
						: getFitWidth() / width;
			}
		});
	}

	private void createClip() {
		clip.centerXProperty().bind(sample.sampleArea.widthProperty().divide(2.0).multiply(scale));
		clip.centerYProperty().bind(sample.sampleArea.heightProperty().divide(2.0).multiply(scale));
		clip.radiusXProperty().bind(sample.radiusXProperty());
		clip.radiusYProperty().bind(sample.radiusYProperty());
		clip.scaleXProperty().bind(scale);
		clip.scaleYProperty().bind(scale);
		clip.rotateProperty().bind(sample.rotateProperty());
		setClip(clip);
	}

	private void setOnMousePressed() {
		setOnMousePressed(event -> {
			startX = event.getX();
			startY = event.getY();
		});
	}

	/**
	 * Handles mouse single click.
	 */
	protected abstract void setOnMouseClicked();

	/**
	 * Handles mouse drag.
	 */
	protected abstract void setOnMouseDragged();

	/**
	 * Handles scroll event.
	 */
	protected abstract void setOnScroll();

	private void onXPropertyChanged() {
		sample.sampleArea.xProperty().addListener((observable, oldValue, newValue) -> {
			final Rectangle2D rec2D = new Rectangle2D(newValue.doubleValue(), sample.sampleArea.getY(),
			                                          sample.sampleArea.getWidth(), sample.sampleArea.getHeight());
			setViewport(rec2D);
		});
	}

	private void onYPropertyChanged() {
		sample.sampleArea.yProperty().addListener((observable, oldValue, newValue) -> {
			final Rectangle2D rec2D = new Rectangle2D(sample.sampleArea.getX(), newValue.doubleValue(),
			                                          sample.sampleArea.getWidth(), sample.sampleArea.getHeight());
			setViewport(rec2D);
		});
	}

	private void onWidthPropertyChanged() {
		sample.sampleArea.widthProperty().addListener((observable, oldValue, newValue) -> {
			final Rectangle2D rec2D = new Rectangle2D(sample.sampleArea.getX(), sample.sampleArea.getY(),
			                                          newValue.doubleValue(), sample.sampleArea.getHeight());
			setViewport(rec2D);
		});
	}

	private void onHeightPropertyChanged() {
		sample.sampleArea.heightProperty().addListener((observable, oldValue, newValue) -> {
			final Rectangle2D rec2D = new Rectangle2D(sample.sampleArea.getX(), sample.sampleArea.getY(),
			                                          sample.sampleArea.getWidth(), newValue.doubleValue());
			setViewport(rec2D);
		});
	}

	/**
	 * Moves sample center by the difference between given mouse event position and drag start position.
	 *
	 * @param event mouse event
	 */
	protected void moveSampleCenter(final MouseEvent event) {
		final double deltaX = -(event.getX() - startX);
		final double deltaY = -(event.getY() - startY);
		startX = event.getX();
		startY = event.getY();
		sample.moveCenterBy(deltaX, deltaY);
	}

}
