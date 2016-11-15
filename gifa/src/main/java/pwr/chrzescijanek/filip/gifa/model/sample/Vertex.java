package pwr.chrzescijanek.filip.gifa.model.sample;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import pwr.chrzescijanek.filip.gifa.model.image.ImageToAlignData;
import pwr.chrzescijanek.filip.gifa.model.image.Triangle;
import pwr.chrzescijanek.filip.gifa.model.panel.PanelViewFactory;
import pwr.chrzescijanek.filip.gifa.model.panel.VertexPanelView;
import pwr.chrzescijanek.filip.gifa.util.SharedState;
import pwr.chrzescijanek.filip.gifa.view.FXView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.initializeController;
import static pwr.chrzescijanek.filip.gifa.util.StageUtils.showStage;

/**
 * Represents triangle vertex.
 */
public class Vertex extends BasicSample {

	private final List<List<NumberChangeListener>> vertexChangeListeners = new ArrayList<>();

	private final List<List<ColorChangeListener>> strokeChangeListeners = new ArrayList<>();

	/**
	 * Constructs a new Vertex on given image, with given position, size, shared state,
	 * panel views factory and bounds.
	 *
	 * @param imageData        image
	 * @param x                X position
	 * @param y                Y position
	 * @param radiusX          X radius
	 * @param radiusY          Y radius
	 * @param state            shared state
	 * @param panelViewFactory panel views factory
	 * @param xBound           X bound
	 * @param yBound           Y bound
	 */
	protected Vertex(final ImageToAlignData imageData, final double x, final double y, final double radiusX,
	                 final double radiusY, final SharedState state, final PanelViewFactory panelViewFactory,
	                 final double xBound, final double yBound) {
		super(imageData, x, y, radiusX, radiusY, state, panelViewFactory, xBound, yBound);
	}

	@Override
	protected void bindProperties() {
		scaleXProperty().bind(imageData.scale);
		sampleArea.visibleProperty().bind(Bindings.equal(state.selectedVertex, this));
	}

	@Override
	protected void handleSingleClick(final MouseEvent event) {
		state.selectedVertex.set(this);
		if (event.isAltDown()) state.zoomVertex.set(true);
	}

	@Override
	protected void handleDoubleClick(final MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() > 1) {
			final String viewPath = "/static/gifa-panel.fxml";
			final String info = "Click on any image to move according triangle point. Drag to move the sample.";
			final int index = getIndexOf();
			final String title = "Vertex #" + (index + 1);
			final List<VertexPanelView> vertexPanelViews =
					state.imagesToAlign.values().stream()
					                   .map(img -> panelViewFactory
							                   .createVertexPanelView(img.writableImage.get(), img.vertices[index]))
					                   .collect(Collectors.toList());
			final FXView fxView = new FXView(viewPath);
			final PanelController controller = initializeController(info, vertexPanelViews, fxView);
			final Stage newStage = new Stage();
			showStage(newStage, fxView, controller, title);
			postCreation(controller, newStage);
		}
	}

	@Override
	public int getIndexOf() {
		return Arrays.asList(((ImageToAlignData) imageData).vertices).indexOf(this);
	}

	private void postCreation(final PanelController controller, final Stage newStage) {
		addListeners(controller);
		colorVertices();
		newStage.setOnCloseRequest(e -> removeChangeListeners());
	}

	private void addListeners(final PanelController controller) {
		addImagesChangeListener(controller);
		initializeVertexChangeListeners();
		initializeStrokeChangeListeners();
		addVertexChangeListeners();
		addStrokeChangeListeners();
	}

	private void addImagesChangeListener(final PanelController controller) {
		state.imagesToAlign.addListener(
				(MapChangeListener<? super String, ? super ImageToAlignData>) c -> {
					removeChangeListeners();
					final int index = getIndexOf();
					final List<VertexPanelView> vertexPanelViews =
							state.imagesToAlign.values().stream()
							                   .map(img -> panelViewFactory
									                   .createVertexPanelView(
											                   img.writableImage.get(), img.vertices[index]))
							                   .collect(Collectors.toList());
					controller.setPanelViews(vertexPanelViews);
					initializeVertexChangeListeners();
					initializeStrokeChangeListeners();
					addVertexChangeListeners();
					addStrokeChangeListeners();
					Platform.runLater(controller::refresh);
				});
	}

	private void addVertexChangeListeners() {
		int index = 0;
		for (final ImageToAlignData imageData : state.imagesToAlign.values()) {
			if (index >= vertexChangeListeners.size()) break;
			final Triangle triangle = imageData.triangle;
			for (int i = 0; i < triangle.pointsProperty.length; i++)
				triangle.pointsProperty[i].addListener(vertexChangeListeners.get(index).get(i));
			index++;
		}
	}

	private void addStrokeChangeListeners() {
		int index = 0;
		for (final ImageToAlignData imageData : state.imagesToAlign.values()) {
			if (index >= strokeChangeListeners.size()) break;
			final Vertex[] vertices = imageData.vertices;
			for (int i = 0; i < vertices.length; i++)
				vertices[i].strokeProperty().addListener(strokeChangeListeners.get(index).get(i));
			index++;
		}
	}

	private void colorVertices() {
		for (final ImageToAlignData imageData : state.imagesToAlign.values()) {
			final DoubleProperty[] pointsProperty = imageData.triangle.pointsProperty;
			final Vertex[] vertices = imageData.vertices;
			final WritableImage writableImage = imageData.writableImage.get();
			for (int i = 0; i < vertices.length; i++) {
				writableImage.getPixelWriter().setColor(
						pointsProperty[i * 2].intValue(), pointsProperty[i * 2 + 1].intValue(),
						(Color) vertices[i].getStroke());
			}
		}
	}

	private void changeColor(final ImageToAlignData imageData,
	                         final int oldX, final int oldY, final int newX, final int newY, final Color color) {
		final WritableImage writableImage = imageData.writableImage.get();
		if (oldX < xBound && oldY < yBound)
			writableImage.getPixelWriter().setColor(oldX, oldY,
			                                        imageData.image.get().getPixelReader().getColor(oldX, oldY));
		if (newX < xBound && newY < yBound)
			writableImage.getPixelWriter().setColor(newX, newY, color);
	}

	private void removeChangeListeners() {
		removeVertexChangeListeners();
		removeStrokeChangeListeners();
	}

	private void removeVertexChangeListeners() {
		int index = 0;
		for (final ImageToAlignData imageData : state.imagesToAlign.values()) {
			if (index >= vertexChangeListeners.size()) break;
			final Triangle triangle = imageData.triangle;
			for (int i = 0; i < triangle.pointsProperty.length; i++)
				triangle.pointsProperty[i].removeListener(vertexChangeListeners.get(index).get(i));
			index++;
		}
		vertexChangeListeners.clear();
	}

	private void removeStrokeChangeListeners() {
		int index = 0;
		for (final ImageToAlignData imageData : state.imagesToAlign.values()) {
			if (index >= strokeChangeListeners.size()) break;
			final Vertex[] vertices = imageData.vertices;
			for (int i = 0; i < vertices.length; i++)
				vertices[i].strokeProperty().removeListener(strokeChangeListeners.get(index).get(i));
			index++;
		}
		strokeChangeListeners.clear();
	}

	private void initializeVertexChangeListeners() {
		vertexChangeListeners.clear();
		int index = 0;
		for (final ImageToAlignData imageData : state.imagesToAlign.values()) {
			vertexChangeListeners.add(new ArrayList<>());
			final List<NumberChangeListener> listeners = vertexChangeListeners.get(index);

			final Triangle triangle = imageData.triangle;
			final Vertex[] vertices = imageData.vertices;

			listeners.add((observable, oldValue, newValue) -> {
				final int oldX = oldValue.intValue();
				final int y = triangle.pointsProperty[1].intValue();
				final int newX = newValue.intValue();
				changeColor(imageData, oldX, y, newX, y, (Color) vertices[0].getStroke());
			});
			listeners.add((observable, oldValue, newValue) -> {
				final int oldY = oldValue.intValue();
				final int x = triangle.pointsProperty[0].intValue();
				final int newY = newValue.intValue();
				changeColor(imageData, x, oldY, x, newY, (Color) vertices[0].getStroke());
			});
			listeners.add((observable, oldValue, newValue) -> {
				final int oldX = oldValue.intValue();
				final int y = triangle.pointsProperty[3].intValue();
				final int newX = newValue.intValue();
				changeColor(imageData, oldX, y, newX, y, (Color) vertices[1].getStroke());
			});
			listeners.add((observable, oldValue, newValue) -> {
				final int oldY = oldValue.intValue();
				final int x = triangle.pointsProperty[2].intValue();
				final int newY = newValue.intValue();
				changeColor(imageData, x, oldY, x, newY, (Color) vertices[1].getStroke());
			});
			listeners.add((observable, oldValue, newValue) -> {
				final int oldX = oldValue.intValue();
				final int y = triangle.pointsProperty[5].intValue();
				final int newX = newValue.intValue();
				changeColor(imageData, oldX, y, newX, y, (Color) vertices[2].getStroke());
			});
			listeners.add((observable, oldValue, newValue) -> {
				final int oldY = oldValue.intValue();
				final int x = triangle.pointsProperty[4].intValue();
				final int newY = newValue.intValue();
				changeColor(imageData, x, oldY, x, newY, (Color) vertices[2].getStroke());
			});

			index++;
		}
	}

	private void initializeStrokeChangeListeners() {
		strokeChangeListeners.clear();
		int index = 0;
		for (final ImageToAlignData imageData : state.imagesToAlign.values()) {
			strokeChangeListeners.add(new ArrayList<>());
			final List<ColorChangeListener> listeners = strokeChangeListeners.get(index);

			final Triangle triangle = imageData.triangle;

			listeners.add((observable, oldValue, newValue) -> {
				final int x = triangle.pointsProperty[0].intValue();
				final int y = triangle.pointsProperty[1].intValue();
				changeColor(imageData, x, y, x, y, (Color) newValue);
			});
			listeners.add((observable, oldValue, newValue) -> {
				final int x = triangle.pointsProperty[2].intValue();
				final int y = triangle.pointsProperty[3].intValue();
				changeColor(imageData, x, y, x, y, (Color) newValue);
			});
			listeners.add((observable, oldValue, newValue) -> {
				final int x = triangle.pointsProperty[4].intValue();
				final int y = triangle.pointsProperty[5].intValue();
				changeColor(imageData, x, y, x, y, (Color) newValue);
			});

			index++;
		}
	}

	private interface NumberChangeListener extends ChangeListener<Number> {}

	private interface ColorChangeListener extends ChangeListener<Paint> {}

}
