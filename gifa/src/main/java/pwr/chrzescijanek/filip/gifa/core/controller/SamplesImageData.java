package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class SamplesImageData extends ImageData {

	public final List<RectangleOfInterest> rectangles = new ArrayList<>();

	public SamplesImageData( final Image image, final Mat imageData ) {
		super(image, imageData);
	}

	public void add(RectangleOfInterest r) {
		r.setStyle(STROKE_STYLE);
		r.setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		r.setStroke(Color.color(1.0, 1.0, 1.0, 0.8));
		final int index = rectangles.size();
		r.setOnMouseClicked(event -> {
			State.INSTANCE.selectedSample.set(r);
			registerDoubleClickListener(event, index);
		});
		final List< List< ImageView > > views = State.INSTANCE.sampleImageViews;
		if ( views.size() == index) {
			views.add(new ArrayList<>());
		}
		final ImageView imageView = new ImageView();
		imageView.setImage(image);
		Rectangle2D rectangle2D = new Rectangle2D(r.getX(), r.getY(), r.getWidth(), r.getHeight());
		imageView.setViewport(rectangle2D);
		r.xProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(newValue.doubleValue(), r.getY(), r.getWidth(), r.getHeight());
			imageView.setViewport(rec2D);
		});
		r.yProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(r.getX(), newValue.doubleValue(), r.getWidth(), r.getHeight());
			imageView.setViewport(rec2D);
		});
		r.widthProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(r.getX(), r.getY(), newValue.doubleValue(), r.getHeight());
			imageView.setViewport(rec2D);
		});
		r.heightProperty().addListener(( observable, oldValue, newValue ) -> {
			Rectangle2D rec2D = new Rectangle2D(r.getX(), r.getY(), r.getWidth(), newValue.doubleValue());
			imageView.setViewport(rec2D);
		});
		imageView.setOnMouseClicked(event -> {
					State.INSTANCE.selectedSample.set(r);
				});
		views.get(index).add(imageView);
		this.rectangles.add(r);
	}

	private void registerDoubleClickListener( final MouseEvent event, final int i ) {
		if(event.getButton().equals(MouseButton.PRIMARY)){
			if(event.getClickCount() > 1){
				final Stage newStage = new Stage();
				final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gifa-samples-panel.fxml"));
				Parent root = null;
				try {
					root = loader.load();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
				final SamplesPanelController controller = loader.getController();
				newStage.setTitle("gifa");
				newStage.getIcons().clear();
				newStage.getIcons().add(new Image(getClass().getResourceAsStream( "/icon-small.png" )));
				Scene scene = new Scene(root, 640, 480);
				Preferences prefs = Preferences.userNodeForPackage(Main.class);
				String theme = prefs.get(Controller.THEME_PREFERENCE_KEY, Controller.THEME_LIGHT);
				if (theme.equals(Controller.THEME_LIGHT)) {
					scene.getStylesheets().add(Controller.THEME_LIGHT);
				} else {
					scene.getStylesheets().add(Controller.THEME_DARK);
				}
				newStage.setScene(scene);
				newStage.show();
				controller.setIndex(i);
				controller.placeImages();
			}
		}
	}
}
