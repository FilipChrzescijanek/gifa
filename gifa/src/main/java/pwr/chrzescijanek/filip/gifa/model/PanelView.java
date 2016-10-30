package pwr.chrzescijanek.filip.gifa.model;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;

public abstract class PanelView extends ImageView {

	BaseSample sample;
	Ellipse clip;

	PanelView( final Image image, final BaseSample sample ) {
		super(image);
		this.sample = sample;
		setViewport();
		setPreserveRatio(true);
		setOnMouseClicked();
		createClip();
	}

	private void setViewport() {Rectangle2D rectangle2D = new Rectangle2D(sample.getX(), sample.getY(), sample.getWidth(), sample.getHeight());
		setViewport(rectangle2D);
		onXPropertyChanged();
		onYPropertyChanged();
		onWidthPropertyChanged();
		onHeightPropertyChanged();
	}

	private void onXPropertyChanged() {
		sample.xProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(newValue.doubleValue(), sample.getY(), sample.getWidth(), sample.getHeight());
			setViewport(rec2D);
		});
	}

	private void onYPropertyChanged() {
		sample.yProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(sample.getX(), newValue.doubleValue(), sample.getWidth(), sample.getHeight());
			setViewport(rec2D);
		});
	}

	private void onWidthPropertyChanged() {
		sample.widthProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(sample.getX(), sample.getY(), newValue.doubleValue(), sample.getHeight());
			setViewport(rec2D);
		});
	}

	private void onHeightPropertyChanged() {
		sample.heightProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(sample.getX(), sample.getY(), sample.getWidth(), newValue.doubleValue());
			setViewport(rec2D);
		});
	}

	private void setOnMouseClicked() {
		setOnMouseClicked(event -> {
			sample.state.zoom = true;
			sample.state.selectedSample.set(sample);
		});
	}

	private void createClip() {
		clip = new Ellipse();
		clip.centerXProperty().bind(clip.radiusXProperty());
		clip.centerYProperty().bind(clip.radiusYProperty());
		clip.radiusXProperty().bind(getRadiusXBinding());
		clip.radiusYProperty().bind(getRadiusYBinding());
		setClip(clip);
	}

	private DoubleBinding getRadiusXBinding() {
		return new DoubleBinding() {
			{
				super.bind(viewportProperty(), fitWidthProperty(), fitHeightProperty());
			}

			@Override
			protected double computeValue() {
				return getFitWidth() / getFitHeight() > getViewport().getWidth() / getViewport().getHeight() ?
						( getFitHeight() * getViewport().getWidth() / getViewport().getHeight() ) / 2
						: getFitWidth() / 2;
			}
		};
	}

	private DoubleBinding getRadiusYBinding() {
		return new DoubleBinding() {
			{
				super.bind(viewportProperty(), fitWidthProperty(), fitHeightProperty());
			}
			@Override
			protected double computeValue() {
				return getFitWidth() / getFitHeight() > getViewport().getWidth() / getViewport().getHeight() ?
						getFitHeight() / 2
						: ( getFitWidth() * getViewport().getHeight() / getViewport().getWidth() ) / 2;
			}
		};
	}

}
