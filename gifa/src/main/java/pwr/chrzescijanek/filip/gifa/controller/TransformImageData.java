package pwr.chrzescijanek.filip.gifa.controller;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import pwr.chrzescijanek.filip.gifa.view.FxmlView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class TransformImageData extends ImageData {

	public final RectangleOfInterest[] rectangles = new RectangleOfInterest[3];
	public final Triangle triangle = new Triangle();
	public final ObjectProperty<WritableImage> writableImage = new SimpleObjectProperty<>(null);

	public TransformImageData( final Image image, final Mat imageData ) {
		super(image, imageData);
		WritableImage img = new WritableImage(this.image.getPixelReader(), (int) this.image.getWidth(), (int) this.image.getHeight());
		writableImage.set(img);
		rectangles[0] = new RectangleOfInterest(image.getWidth() * 2 / 5, image.getHeight() / 5, image.getWidth() / 5, image.getHeight() / 5, image);
		rectangles[1] = new RectangleOfInterest(image.getWidth() * 1 / 5, image.getHeight() * 3 / 5, image.getWidth() / 5, image.getHeight() / 5, image);
		rectangles[2] = new RectangleOfInterest(image.getWidth() * 3 / 5, image.getHeight() * 3 / 5, image.getWidth() / 5, image.getHeight() / 5, image);

		triangle.setFill(Color.color(1.0, 1.0, 1.0, 0.35));
		triangle.setStroke(Color.color(1.0, 1.0, 1.0, 0.6));
		triangle.setStyle(STROKE_STYLE);
		rectangles[0].setStyle(STROKE_STYLE);
		rectangles[1].setStyle(STROKE_STYLE);
		rectangles[2].setStyle(STROKE_STYLE);
		rectangles[0].sample.setStyle(STROKE_STYLE);
		rectangles[1].sample.setStyle(STROKE_STYLE);
		rectangles[2].sample.setStyle(STROKE_STYLE);
		rectangles[0].sample.setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		rectangles[0].sample.setStroke(Color.color(1.0, 1.0, 1.0, 0.8));
		rectangles[1].sample.setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		rectangles[1].sample.setStroke(Color.color(1.0, 1.0, 1.0, 0.8));
		rectangles[2].sample.setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		rectangles[2].sample.setStroke(Color.color(1.0, 1.0, 1.0, 0.8));
		rectangles[0].setFill(Color.color(0.0, 0.0, 0.0, 0.0));
		rectangles[0].setStroke(Color.color(1.0, 1.0, 1.0, 0.9));
		rectangles[1].setFill(Color.color(0.0, 0.0, 0.0, 0.0));
		rectangles[1].setStroke(Color.color(1.0, 1.0, 1.0, 0.9));
		rectangles[2].setFill(Color.color(0.0, 0.0, 0.0, 0.0));
		rectangles[2].setStroke(Color.color(1.0, 1.0, 1.0, 0.9));

		triangle.pointsProperty[0].addListener(( observable, oldValue, newValue ) -> {
			int oldX = oldValue.intValue();
			int y = triangle.pointsProperty[1].intValue();
			int newX = newValue.intValue();
			changeColor(oldX, y, newX, y, (Color) rectangles[0].sample.getStroke());
		});
		triangle.pointsProperty[1].addListener(( observable, oldValue, newValue ) -> {
			int oldY = oldValue.intValue();
			int x = triangle.pointsProperty[0].intValue();
			int newY = newValue.intValue();
			changeColor(x, oldY, x, newY, (Color) rectangles[0].sample.getStroke());
		});
		triangle.pointsProperty[2].addListener(( observable, oldValue, newValue ) -> {
			int oldX = oldValue.intValue();
			int y = triangle.pointsProperty[3].intValue();
			int newX = newValue.intValue();
			changeColor(oldX, y, newX, y, (Color) rectangles[1].sample.getStroke());
		});
		triangle.pointsProperty[3].addListener(( observable, oldValue, newValue ) -> {
			int oldY = oldValue.intValue();
			int x = triangle.pointsProperty[2].intValue();
			int newY = newValue.intValue();
			changeColor(x, oldY, x, newY, (Color) rectangles[1].sample.getStroke());
		});
		triangle.pointsProperty[4].addListener(( observable, oldValue, newValue ) -> {
			int oldX = oldValue.intValue();
			int y = triangle.pointsProperty[5].intValue();
			int newX = newValue.intValue();
			changeColor(oldX, y, newX, y, (Color) rectangles[2].sample.getStroke());
		});
		triangle.pointsProperty[5].addListener(( observable, oldValue, newValue ) -> {
			int oldY = oldValue.intValue();
			int x = triangle.pointsProperty[4].intValue();
			int newY = newValue.intValue();
			changeColor(x, oldY, x, newY, (Color) rectangles[2].sample.getStroke());
		});
		rectangles[0].sample.strokeProperty().addListener(( observable, oldValue, newValue ) -> {
			int x = triangle.pointsProperty[0].intValue();
			int y = triangle.pointsProperty[1].intValue();
			changeColor(x, y, x, y, (Color) newValue);
		});
		rectangles[1].sample.strokeProperty().addListener(( observable, oldValue, newValue ) -> {
			int x = triangle.pointsProperty[2].intValue();
			int y = triangle.pointsProperty[3].intValue();
			changeColor(x, y, x, y, (Color) newValue);
		});
		rectangles[2].sample.strokeProperty().addListener(( observable, oldValue, newValue ) -> {
			int x = triangle.pointsProperty[4].intValue();
			int y = triangle.pointsProperty[5].intValue();
			changeColor(x, y, x, y, (Color) newValue);
		});

		triangle.pointsProperty[0].bind(rectangles[0].xProperty().add(rectangles[0].widthProperty().divide(2.0)));
		triangle.pointsProperty[1].bind(rectangles[0].yProperty().add(rectangles[0].heightProperty().divide(2.0)));
		triangle.pointsProperty[2].bind(rectangles[1].xProperty().add(rectangles[1].widthProperty().divide(2.0)));
		triangle.pointsProperty[3].bind(rectangles[1].yProperty().add(rectangles[1].heightProperty().divide(2.0)));
		triangle.pointsProperty[4].bind(rectangles[2].xProperty().add(rectangles[2].widthProperty().divide(2.0)));
		triangle.pointsProperty[5].bind(rectangles[2].yProperty().add(rectangles[2].heightProperty().divide(2.0)));

		rectangles[0].sample.setOnMouseClicked(event -> {
			if (event.isAltDown())
				State.INSTANCE.zoom = true;
			State.INSTANCE.selectedRectangle.set(rectangles[0]);
			rectangles[0].setVisible(true);
			rectangles[1].setVisible(false);
			rectangles[2].setVisible(false);
			registerDoubleClickListener(event, 0);
		});
		rectangles[1].sample.setOnMouseClicked(event -> {
			if (event.isAltDown())
				State.INSTANCE.zoom = true;
			State.INSTANCE.selectedRectangle.set(rectangles[1]);
			rectangles[1].setVisible(true);
			rectangles[0].setVisible(false);
			rectangles[2].setVisible(false);
			registerDoubleClickListener(event, 1);
		});
		rectangles[2].sample.setOnMouseClicked(event -> {
			if (event.isAltDown())
				State.INSTANCE.zoom = true;
			State.INSTANCE.selectedRectangle.set(rectangles[2]);
			rectangles[2].setVisible(true);
			rectangles[0].setVisible(false);
			rectangles[1].setVisible(false);
			registerDoubleClickListener(event, 2);
		});

		int i = 0;
		for (RectangleOfInterest r : rectangles) {
			r.sample.setOnMouseDragged(event -> {
				final double deltaX = event.getX() - State.INSTANCE.x;
				final double deltaY = event.getY() - State.INSTANCE.y;
				State.INSTANCE.x = event.getX();
				State.INSTANCE.y = event.getY();
				r.resizeShape(deltaX, deltaY);
				recalculateForTransformImage(r, r.getScaleX());
			});

			r.setOnMouseDragged(event -> {
				final double deltaX = event.getX() - State.INSTANCE.x;
				final double deltaY = event.getY() - State.INSTANCE.y;
				State.INSTANCE.x = event.getX();
				State.INSTANCE.y = event.getY();
				r.resizeShape(deltaX, deltaY);
				recalculateForTransformImage(r, r.getScaleX());
			});
			final int index = i;
			final List< List< ImageView > > views = State.INSTANCE.imageViews;
			if ( views.size() == i) {
				views.add(new ArrayList<>());
			}
			final ImageView view = new ImageView();
			view.imageProperty().bind(writableImage);
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
				State.INSTANCE.zoom = true;
				State.INSTANCE.selectedRectangle.set(r);
				triangle.pointsProperty[index * 2].unbind();
				triangle.pointsProperty[index * 2 + 1].unbind();
				DoubleBinding widthBinding = Math.abs(event.getX()) < 0.00001 ?
					r.xProperty().add(0)
					: r.xProperty().add(r.widthProperty().divide(view.getBoundsInParent().getWidth() / event.getX()));
				DoubleBinding heightBinding = Math.abs(event.getY()) < 0.00001 ?
						r.yProperty().add(0)
						: r.yProperty().add(r.heightProperty().divide(view.getBoundsInParent().getHeight() / event.getY()));
				triangle.pointsProperty[index * 2].bind(widthBinding);
				triangle.pointsProperty[index * 2 + 1].bind(heightBinding);
				triangle.recalculateTranslates(view.getImage().getWidth(), view.getImage().getHeight());
			});
			views.get(i).add(view);
			i++;
		}
	}

	private void changeColor( final int oldX, final int oldY, final int newX, final int newY, final Color color ) {
		if (oldX < image.getWidth() && oldY < image.getHeight())
			writableImage.get().getPixelWriter().setColor(oldX, oldY,
					this.image.getPixelReader().getColor(oldX, oldY));
		if (newX < image.getWidth() && newY < image.getHeight())
		writableImage.get().getPixelWriter().setColor(newX, newY, color);
	}

	private void recalculateForTransformImage( final RectangleOfInterest rectangle, final double scale ) {
		triangle.setTranslateX(( image.getWidth() * 0.5 * ( scale - 1.0 ) ) -
				( image.getWidth() * 0.5 - triangle.getTriangleMiddleX() ) * ( scale - 1.0 ));
		triangle.setTranslateY(( image.getHeight() * 0.5 * ( scale - 1.0 ) ) -
				( image.getHeight() * 0.5 - triangle.getTriangleMiddleY() ) * ( scale - 1.0 ));
		rectangle.setTranslateX(( image.getWidth() * 0.5 * ( scale - 1.0 ) ) -
					( image.getWidth() * 0.5 - ( rectangle.getX() + rectangle.getWidth() * 0.5 ) ) * ( scale - 1.0 ));
		rectangle.setTranslateY(( image.getHeight() * 0.5 * ( scale - 1.0 ) ) -
					( image.getHeight() * 0.5 - ( rectangle.getY() + rectangle.getHeight() * 0.5 ) ) * ( scale - 1.0 ));
	}

	private void registerDoubleClickListener( final MouseEvent event, final int i ) {
		if(event.getButton().equals(MouseButton.PRIMARY)){
			if(event.getClickCount() > 1){
				final Stage newStage = new Stage();
				FxmlView appView = new FxmlView("/gifa-panel.fxml");
				final Parent root = appView.getView();
				final PanelController controller = (PanelController) appView.getViewModel();
				controller.setIndex(i);
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
