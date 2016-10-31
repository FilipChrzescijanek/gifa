package pwr.chrzescijanek.filip.gifa.model;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;

public abstract class PanelView extends ImageView {

	final BaseSample sample;
	final Ellipse clip = new Ellipse();
	final DoubleProperty scale = new SimpleDoubleProperty(1.0);
	double startX;
	double startY;

	PanelView( final Image image, final BaseSample sample ) {
		super(image);
		this.sample = sample;
		addListeners();
		setViewport();
		bindScale();
		setPreserveRatio(true);
		createClip();
	}

	void moveSampleCenter( final MouseEvent event ) {
		final double deltaX = -( event.getX() - startX );
		final double deltaY = -( event.getY() - startY );
		startX = event.getX();
		startY = event.getY();
		sample.moveCenterBy(deltaX, deltaY);
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

	private void addListeners() {
		setOnMousePressed();
		setOnMouseClicked();
		setOnMouseDragged();
		setOnScroll();
	}

	private void setOnMousePressed() {
		setOnMousePressed(event -> {
			startX = event.getX();
			startY = event.getY();
		});
	}

	abstract void setOnMouseClicked();

	abstract void setOnMouseDragged();

	abstract void setOnScroll();

	private void setViewport() {
		Rectangle2D rectangle2D = new Rectangle2D(sample.sampleArea.getX(), sample.sampleArea.getY(),
				sample.sampleArea.getWidth(), sample.sampleArea.getHeight());
		setViewport(rectangle2D);
		onXPropertyChanged();
		onYPropertyChanged();
		onWidthPropertyChanged();
		onHeightPropertyChanged();
	}

	private void onXPropertyChanged() {
		sample.sampleArea.xProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(newValue.doubleValue(), sample.sampleArea.getY(),
					sample.sampleArea.getWidth(), sample.sampleArea.getHeight());
			setViewport(rec2D);
		});
	}

	private void onYPropertyChanged() {
		sample.sampleArea.yProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(sample.sampleArea.getX(), newValue.doubleValue(),
					sample.sampleArea.getWidth(), sample.sampleArea.getHeight());
			setViewport(rec2D);
		});
	}

	private void onWidthPropertyChanged() {
		sample.sampleArea.widthProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(sample.sampleArea.getX(), sample.sampleArea.getY(),
					newValue.doubleValue(), sample.sampleArea.getHeight());
			setViewport(rec2D);
		});
	}

	private void onHeightPropertyChanged() {
		sample.sampleArea.heightProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(sample.sampleArea.getX(), sample.sampleArea.getY(),
					sample.sampleArea.getWidth(), newValue.doubleValue());
			setViewport(rec2D);
		});
	}

	private void createClip() {
//		clip.centerXProperty().bind(clip.radiusXProperty().multiply(scale));
//		clip.centerYProperty().bind(clip.radiusYProperty().multiply(scale));
		clip.centerXProperty().bind(sample.sampleArea.widthProperty().divide(2.0).multiply(scale));
		clip.centerYProperty().bind(sample.sampleArea.heightProperty().divide(2.0).multiply(scale));
		clip.radiusXProperty().bind(sample.radiusXProperty());
		clip.radiusYProperty().bind(sample.radiusYProperty());
		clip.scaleXProperty().bind(scale);
		clip.scaleYProperty().bind(scale);
		clip.rotateProperty().bind(sample.rotateProperty());
		setClip(clip);
	}

}
