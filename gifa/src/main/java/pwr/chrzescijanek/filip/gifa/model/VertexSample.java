package pwr.chrzescijanek.filip.gifa.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import pwr.chrzescijanek.filip.gifa.controller.PanelController;
import pwr.chrzescijanek.filip.gifa.controller.SharedState;
import pwr.chrzescijanek.filip.gifa.view.FXView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VertexSample extends BaseSample {

	private interface NumberChangeListener extends ChangeListener< Number > { }

	private interface ColorChangeListener extends ChangeListener< Paint > { }

	private final List< List< NumberChangeListener > > vertexChangeListeners = new ArrayList<>();
	private final List< List< ColorChangeListener > > strokeChangeListeners = new ArrayList<>();

	VertexSample( final TransformImageData imageData, final double x, final double y, final double width, final double height,
				  final SharedState state, final PanelViewFactory panelViewFactory, final double xBound, final double yBound ) {
		super(imageData, x, y, width, height, state, panelViewFactory, xBound, yBound);
	}

	@Override
	int getIndexOf() {
		return Arrays.asList(( (TransformImageData) imageData ).vertexSamples).indexOf(this);
	}

	@Override
	void handleSingleClick( final MouseEvent event ) {
		if ( event.isAltDown() ) state.zoom = true;
		state.selectedRectangle.set(this);
		Arrays.stream(( (TransformImageData) imageData ).vertexSamples).forEach(s -> s.setVisible(false));
		setVisible(true);
	}

	@Override
	void handleDoubleClick( final MouseEvent event ) {
		if ( event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() > 1 ) {
			String viewPath = "/static/gifa-panel.fxml";
			String info = "Click on any image to move according triangle point. Drag to move the sample.";
			final int index = getIndexOf();
			String title = "Vertex #" + ( index + 1 );
			final List< VertexSamplePanelView > vertexSamplePanelViews = state.transformImages.values().stream()
					.map(img -> panelViewFactory.createVertexSamplePanelView(img.writableImage.get(), img.vertexSamples[index]))
					.collect(Collectors.toList());
			FXView fxView = new FXView(viewPath);
			final PanelController controller = initializeController(info, vertexSamplePanelViews, fxView);
			Stage newStage = new Stage();
			showStage(newStage, fxView, controller, title);
			postCreation(controller, newStage);
		}
	}

	private void postCreation( final PanelController controller, final Stage newStage ) {
		addListeners(controller);
		colorVertices();
		newStage.setOnCloseRequest(e -> removeChangeListeners());
	}

	private void addListeners( final PanelController controller ) {
		addImagesChangeListener(controller);
		initializeVertexChangeListeners();
		initializeStrokeChangeListeners();
		addVertexChangeListeners();
		addStrokeChangeListeners();
	}

	private void addImagesChangeListener( final PanelController controller ) {
		state.transformImages.addListener(
				(MapChangeListener< ? super String, ? super TransformImageData >) c -> controller.placeImages()
		);
	}

	private void addVertexChangeListeners() {
		int index = 0;
		for ( TransformImageData imageData : state.transformImages.values() ) {
			Triangle triangle = imageData.triangle;
			for ( int i = 0; i < triangle.pointsProperty.length; i++ )
				triangle.pointsProperty[i].addListener(vertexChangeListeners.get(index).get(i));
			index++;
		}
	}

	private void addStrokeChangeListeners() {
		int index = 0;
		for ( TransformImageData imageData : state.transformImages.values() ) {
			VertexSample[] vertexSamples = imageData.vertexSamples;
			for ( int i = 0; i < vertexSamples.length; i++ )
				vertexSamples[i].sampleArea.strokeProperty().addListener(strokeChangeListeners.get(index).get(i));
			index++;
		}
	}

	private void colorVertices() {
		for ( TransformImageData imageData : state.transformImages.values() ) {
			final DoubleProperty[] pointsProperty = imageData.triangle.pointsProperty;
			VertexSample[] vertexSamples = imageData.vertexSamples;
			final WritableImage writableImage = imageData.writableImage.get();
			for (int i = 0; i < vertexSamples.length; i++) {
				writableImage.getPixelWriter().setColor(
						pointsProperty[i * 2].intValue(), pointsProperty[i * 2 + 1].intValue(),
						(Color) vertexSamples[i].sampleArea.getStroke()
				);
			}
		}
	}

	private void changeColor( final TransformImageData imageData,
							  final int oldX, final int oldY, final int newX, final int newY, final Color color ) {
		final WritableImage writableImage = imageData.writableImage.get();
		if ( oldX < xBound && oldY < yBound )
			writableImage.getPixelWriter().setColor(oldX, oldY, imageData.image.getPixelReader().getColor(oldX, oldY));
		if ( newX < xBound && newY < yBound )
			writableImage.getPixelWriter().setColor(newX, newY, color);
	}

	private void removeChangeListeners() {
		removeVertexChangeListeners();
		removeStrokeChangeListeners();
	}

	private void removeVertexChangeListeners() {
		int index = 0;
		for ( TransformImageData imageData : state.transformImages.values() ) {
			Triangle triangle = imageData.triangle;
			for ( int i = 0; i < triangle.pointsProperty.length; i++ )
				triangle.pointsProperty[i].removeListener(vertexChangeListeners.get(index).get(i));
			index++;
		}
		vertexChangeListeners.clear();
	}

	private void removeStrokeChangeListeners() {
		int index = 0;
		for ( TransformImageData imageData : state.transformImages.values() ) {
			VertexSample[] vertexSamples = imageData.vertexSamples;
			for ( int i = 0; i < vertexSamples.length; i++ )
				vertexSamples[i].sampleArea.strokeProperty().removeListener(strokeChangeListeners.get(index).get(i));
			index++;
		}
		strokeChangeListeners.clear();
	}

	private void initializeVertexChangeListeners() {
		int index = 0;
		for ( TransformImageData imageData : state.transformImages.values() ) {
			vertexChangeListeners.add(new ArrayList<>());
			final List< NumberChangeListener > listeners = vertexChangeListeners.get(index);

			Triangle triangle = imageData.triangle;
			VertexSample[] vertexSamples = imageData.vertexSamples;

			listeners.add(( observable, oldValue, newValue ) -> {
				int oldX = oldValue.intValue();
				int y = triangle.pointsProperty[1].intValue();
				int newX = newValue.intValue();
				changeColor(imageData, oldX, y, newX, y, (Color) vertexSamples[0].sampleArea.getStroke());
			});
			listeners.add(( observable, oldValue, newValue ) -> {
				int oldY = oldValue.intValue();
				int x = triangle.pointsProperty[0].intValue();
				int newY = newValue.intValue();
				changeColor(imageData, x, oldY, x, newY, (Color) vertexSamples[0].sampleArea.getStroke());
			});
			listeners.add(( observable, oldValue, newValue ) -> {
				int oldX = oldValue.intValue();
				int y = triangle.pointsProperty[3].intValue();
				int newX = newValue.intValue();
				changeColor(imageData, oldX, y, newX, y, (Color) vertexSamples[1].sampleArea.getStroke());
			});
			listeners.add(( observable, oldValue, newValue ) -> {
				int oldY = oldValue.intValue();
				int x = triangle.pointsProperty[2].intValue();
				int newY = newValue.intValue();
				changeColor(imageData, x, oldY, x, newY, (Color) vertexSamples[1].sampleArea.getStroke());
			});
			listeners.add(( observable, oldValue, newValue ) -> {
				int oldX = oldValue.intValue();
				int y = triangle.pointsProperty[5].intValue();
				int newX = newValue.intValue();
				changeColor(imageData, oldX, y, newX, y, (Color) vertexSamples[2].sampleArea.getStroke());
			});
			listeners.add(( observable, oldValue, newValue ) -> {
				int oldY = oldValue.intValue();
				int x = triangle.pointsProperty[4].intValue();
				int newY = newValue.intValue();
				changeColor(imageData, x, oldY, x, newY, (Color) vertexSamples[2].sampleArea.getStroke());
			});

			index++;
		}
	}

	private void initializeStrokeChangeListeners() {
		int index = 0;
		for ( TransformImageData imageData : state.transformImages.values() ) {
			strokeChangeListeners.add(new ArrayList<>());
			final List< ColorChangeListener > listeners = strokeChangeListeners.get(index);

			Triangle triangle = imageData.triangle;

			listeners.add(( observable, oldValue, newValue ) -> {
				int x = triangle.pointsProperty[0].intValue();
				int y = triangle.pointsProperty[1].intValue();
				changeColor(imageData, x, y, x, y, (Color) newValue);
			});
			listeners.add(( observable, oldValue, newValue ) -> {
				int x = triangle.pointsProperty[2].intValue();
				int y = triangle.pointsProperty[3].intValue();
				changeColor(imageData, x, y, x, y, (Color) newValue);
			});
			listeners.add(( observable, oldValue, newValue ) -> {
				int x = triangle.pointsProperty[4].intValue();
				int y = triangle.pointsProperty[5].intValue();
				changeColor(imageData, x, y, x, y, (Color) newValue);
			});

			index++;
		}
	}

}
