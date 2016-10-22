package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.MapChangeListener;
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

public class TransformImageData extends ImageData {

	public final RectangleOfInterest[] rectangles = new RectangleOfInterest[3];
	public final Triangle triangle = new Triangle();
	private int x, y;

	public TransformImageData( final Image image, final Mat imageData ) {
		super(image, imageData);
		rectangles[0] = new RectangleOfInterest(image.getWidth() * 2 / 5, image.getHeight() / 5, image.getWidth() / 5, image.getHeight() / 5 );
		rectangles[1] = new RectangleOfInterest(image.getWidth() * 1 / 5, image.getHeight() * 3 / 5, image.getWidth() / 5, image.getHeight() / 5 );
		rectangles[2] = new RectangleOfInterest(image.getWidth() * 3 / 5, image.getHeight() * 3 / 5, image.getWidth() / 5, image.getHeight() / 5 );
		triangle.pointsProperty[0].bind(rectangles[0].xProperty().add(rectangles[0].widthProperty().divide(2.0)));
		triangle.pointsProperty[1].bind(rectangles[0].yProperty().add(rectangles[0].heightProperty().divide(2.0)));
		triangle.pointsProperty[2].bind(rectangles[1].xProperty().add(rectangles[1].widthProperty().divide(2.0)));
		triangle.pointsProperty[3].bind(rectangles[1].yProperty().add(rectangles[1].heightProperty().divide(2.0)));
		triangle.pointsProperty[4].bind(rectangles[2].xProperty().add(rectangles[2].widthProperty().divide(2.0)));
		triangle.pointsProperty[5].bind(rectangles[2].yProperty().add(rectangles[2].heightProperty().divide(2.0)));

		rectangles[0].setOnMouseClicked(event -> {
			State.INSTANCE.selectedRectangle.set(rectangles[0]);
			registerDoubleClickListener(event, 0);
		});
		rectangles[1].setOnMouseClicked(event -> {
			State.INSTANCE.selectedRectangle.set(rectangles[1]);
			registerDoubleClickListener(event, 1);
		});
		rectangles[2].setOnMouseClicked(event -> {
			State.INSTANCE.selectedRectangle.set(rectangles[2]);
			registerDoubleClickListener(event, 2);
		});


		triangle.setFill(Color.color(1.0, 1.0, 1.0, 0.35));
		triangle.setStroke(Color.color(1.0, 1.0, 1.0, 0.6));
		triangle.setStyle(STROKE_STYLE);
		rectangles[0].setStyle(STROKE_STYLE);
		rectangles[1].setStyle(STROKE_STYLE);
		rectangles[2].setStyle(STROKE_STYLE);
		rectangles[0].setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		rectangles[0].setStroke(Color.color(1.0, 1.0, 1.0, 0.8));
		rectangles[1].setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		rectangles[1].setStroke(Color.color(1.0, 1.0, 1.0, 0.8));
		rectangles[2].setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		rectangles[2].setStroke(Color.color(1.0, 1.0, 1.0, 0.8));

		int i = 0;
		for (RectangleOfInterest r : rectangles) {
			final int index = i;
			final List< List< ImageView > > views = State.INSTANCE.imageViews;
			if ( views.size() == i) {
				views.add(new ArrayList<>());
			}
			final ImageView imageView = new ImageView();
			WritableImage img = new WritableImage(this.image.getPixelReader(), (int) this.image.getWidth(), (int) this.image.getHeight());
			imageView.setImage(img);
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
				State.INSTANCE.selectedRectangle.set(r);
				((WritableImage) imageView.getImage()).getPixelWriter().setColor(x, y,
						this.image.getPixelReader().getColor(x, y));
				triangle.pointsProperty[index * 2].unbind();
				triangle.pointsProperty[index * 2 + 1].unbind();
				DoubleBinding widthBinding = Math.abs(event.getX()) < 0.00001 ?
					r.xProperty().add(0)
					: r.xProperty().add(r.widthProperty().divide(imageView.getBoundsInParent().getWidth() / event.getX()));
				DoubleBinding heightBinding = Math.abs(event.getY()) < 0.00001 ?
						r.yProperty().add(0)
						: r.yProperty().add(r.heightProperty().divide(imageView.getBoundsInParent().getHeight() / event.getY()));
				triangle.pointsProperty[index * 2].bind(widthBinding);
				triangle.pointsProperty[index * 2 + 1].bind(heightBinding);
				this.x = (int) triangle.pointsProperty[index * 2].get();
				this.y = (int) triangle.pointsProperty[index * 2 + 1].get();
				((WritableImage) imageView.getImage()).getPixelWriter().setColor(x, y, (Color) r.getStroke());
				triangle.recalculateTranslates(imageView.getImage().getWidth(), imageView.getImage().getHeight());
			});
			views.get(i).add(imageView);
			i++;
		}
	}

	private void registerDoubleClickListener( final MouseEvent event, final int i ) {
		if(event.getButton().equals(MouseButton.PRIMARY)){
			if(event.getClickCount() > 1){
				final Stage newStage = new Stage();
				final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gifa-panel.fxml"));
				Parent root = null;
				try {
					root = loader.load();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
				final PanelController controller = loader.getController();
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
				State.INSTANCE.transformImages.addListener((MapChangeListener< ? super String, ? super TransformImageData >) c -> {
					controller.placeImages();
				});
			}
		}
	}

	public void resetRectangles() {
		rectangles[0].setX(image.getWidth() * 2 / 5);
		rectangles[0].setY(image.getHeight() / 5);
		rectangles[1].setX(image.getWidth() / 5);
		rectangles[1].setY(image.getHeight() * 3 / 5);
		rectangles[2].setX(image.getWidth() * 3 / 5);
		rectangles[2].setY(image.getHeight() * 3 / 5);
		rectangles[0].setWidth(image.getWidth() / 5);
		rectangles[0].setHeight(image.getHeight() / 5);
		rectangles[1].setWidth(image.getWidth() / 5);
		rectangles[1].setHeight(image.getHeight() / 5);
		rectangles[2].setWidth(image.getWidth() / 5);
		rectangles[2].setHeight(image.getHeight() / 5);
	}
}
