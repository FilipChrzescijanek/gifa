package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class SamplesImageData extends ImageData {

	public final ObservableList< RectangleOfInterest > rectangles = FXCollections.observableArrayList();

	public SamplesImageData( final Image image, final Mat imageData ) {
		super(image, imageData);
	}

	public void add( RectangleOfInterest r ) {
		r.sample.setOnMouseDragged(event -> {
			resize(r, event);
		});
		r.setOnMouseDragged(event -> {
			resize(r, event);
		});
		r.setStyle(STROKE_STYLE);
		r.sample.setStyle(STROKE_STYLE);
		r.setFill(Color.color(0.0, 0.0, 0.0, 0.0));
		r.setStroke(Color.color(1.0, 1.0, 1.0, 0.9));
		r.sample.setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		r.sample.setStroke(Color.color(1.0, 1.0, 1.0, 0.8));
		r.sample.setOnMouseClicked(event -> {
			State.INSTANCE.selectedSample.set(r);
			rectangles.forEach(rec -> rec.setVisible(false));
			r.setVisible(true);
			registerDoubleClickListener(r, event, rectangles.indexOf(r));
		});
		final List< List< ImageView > > views = State.INSTANCE.sampleImageViews;
		final int index = rectangles.size();
		if ( views.size() == index ) {
			views.add(new ArrayList<>());
		}
		final ImageView view = new ImageView();
		view.setImage(image);
		Rectangle2D rectangle2D = new Rectangle2D(r.getX(), r.getY(), r.getWidth(), r.getHeight());
		view.setViewport(rectangle2D);
		r.xProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(newValue.doubleValue(), r.getY(), r.getWidth(), r.getHeight());
			view.setViewport(rec2D);
		});
		r.yProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(r.getX(), newValue.doubleValue(), r.getWidth(), r.getHeight());
			view.setViewport(rec2D);
		});
		r.widthProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(r.getX(), r.getY(), newValue.doubleValue(), r.getHeight());
			view.setViewport(rec2D);
		});
		r.heightProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(r.getX(), r.getY(), r.getWidth(), newValue.doubleValue());
			view.setViewport(rec2D);
		});
		view.setOnMouseClicked(event -> {
			State.INSTANCE.selectedSample.set(r);
		});
		views.get(index).add(view);
		this.rectangles.add(r);
	}

	private void resize( final RectangleOfInterest r, final MouseEvent event ) {
		if ( event.isControlDown() ) {
			final double deltaX = event.getX() - State.INSTANCE.x;
			final double deltaY = event.getY() - State.INSTANCE.y;
			State.INSTANCE.x = event.getX();
			State.INSTANCE.y = event.getY();
			r.resizeShape(deltaX, deltaY);
			recalculateForSamplesImage(r, r.getScaleX());
		} else {
			int i = -1;
			for ( SamplesImageData img : State.INSTANCE.samplesImages.values() ) {
				i = img.rectangles.indexOf(r);
				if ( i >= 0 ) break;
			}
			final int index = i;
			final double deltaX = event.getX() - State.INSTANCE.x;
			final double deltaY = event.getY() - State.INSTANCE.y;
			State.INSTANCE.x = event.getX();
			State.INSTANCE.y = event.getY();
			State.INSTANCE.samplesImages.values().forEach(
														img ->  {
															img.rectangles.get(index).resizeShape(deltaX, deltaY);
														}
												);
		}
	}

	private void registerDoubleClickListener( final RectangleOfInterest r, final MouseEvent event, final int i ) {
		if ( event.getButton().equals(MouseButton.PRIMARY) ) {
			if ( event.getClickCount() > 1 ) {
				final Stage newStage = new Stage();
				newStage.initOwner(r.getScene().getWindow());
				final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gifa-samples-panel.fxml"));
				Parent root = null;
				try {
					root = loader.load();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
				final SamplesPanelController controller = loader.getController();
				controller.setIndex(i);
				newStage.setTitle("gifa");
				newStage.getIcons().clear();
				newStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon-small.png")));
				Scene scene = new Scene(root, 640, 480);
				Preferences prefs = Preferences.userNodeForPackage(Main.class);
				String theme = prefs.get(Controller.THEME_PREFERENCE_KEY, Controller.THEME_LIGHT);
				if ( theme.equals(Controller.THEME_LIGHT) ) {
					scene.getStylesheets().add(Controller.THEME_LIGHT);
				} else {
					scene.getStylesheets().add(Controller.THEME_DARK);
				}
				newStage.setScene(scene);
				newStage.show();
				controller.placeImages();
				rectangles.addListener((ListChangeListener< ? super RectangleOfInterest >) c -> {
					while ( c.next() )
						if ( c.wasRemoved() && c.getRemoved().contains(r) )
							newStage.close();
				});
				State.INSTANCE.samplesImages.addListener((MapChangeListener< ? super String, ? super SamplesImageData >) c -> {
					newStage.close();
				});
			}
		}
	}

	private void recalculateForSamplesImage( final RectangleOfInterest rectangle, final double scale ) {
		rectangle.setTranslateX(( image.getWidth() * 0.5 * ( scale - 1.0 ) ) -
				( image.getWidth() * 0.5 - ( rectangle.getX() + rectangle.getWidth() * 0.5 ) ) * ( scale - 1.0 ));
		rectangle.setTranslateY(( image.getHeight() * 0.5 * ( scale - 1.0 ) ) -
				( image.getHeight() * 0.5 - ( rectangle.getY() + rectangle.getHeight() * 0.5 ) ) * ( scale - 1.0 ));
	}
}
