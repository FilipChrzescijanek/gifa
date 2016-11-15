package pwr.chrzescijanek.filip.gifa.model.sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import pwr.chrzescijanek.filip.gifa.model.image.ImageData;
import pwr.chrzescijanek.filip.gifa.model.panel.PanelViewFactory;
import pwr.chrzescijanek.filip.gifa.util.SharedState;

import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.E_RESIZE;
import static javafx.scene.Cursor.MOVE;
import static javafx.scene.Cursor.NE_RESIZE;
import static javafx.scene.Cursor.NW_RESIZE;
import static javafx.scene.Cursor.N_RESIZE;
import static javafx.scene.Cursor.SE_RESIZE;
import static javafx.scene.Cursor.SW_RESIZE;
import static javafx.scene.Cursor.S_RESIZE;
import static javafx.scene.Cursor.W_RESIZE;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.DRAG;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.E;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.N;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.NE;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.NIL;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.NW;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.S;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.SE;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.SW;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.W;

/**
 * Represents basic sample.
 */
public abstract class BasicSample extends Ellipse {

	/**
	 * Sample stroke style.
	 */
	protected static final String STROKE_STYLE = "-fx-stroke-type: inside;\n" +
	                                             "    -fx-stroke-width: 2;\n" +
	                                             "    -fx-stroke-dash-array: 12 2 4 2;\n" +
	                                             "    -fx-stroke-dash-offset: 6;\n" +
	                                             "    -fx-stroke-line-cap: butt;";

	/**
	 * Represents current resize/drag method.
	 */
	protected static SampleSelection sampleSelection = NIL;

	/**
	 * Rectangle area bounding sample.
	 */
	public final Rectangle sampleArea = new Rectangle();

	/**
	 * Shared state.
	 */
	public final SharedState state;

	/**
	 * Image data.
	 */
	public final ImageData imageData;

	/**
	 * Panel views factory.
	 */
	protected final PanelViewFactory panelViewFactory;

	/**
	 * X bound.
	 */
	protected double xBound;

	/**
	 * Y bound.
	 */
	protected double yBound;

	/**
	 * Drag start X position.
	 */
	protected double startX;

	/**
	 * Drag start Y position.
	 */
	protected double startY;

	/**
	 * Constructs a new BasicSample on given image, with given position, size, shared state,
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
	protected BasicSample(final ImageData imageData, final double x, final double y, final double radiusX,
	                      final double radiusY, final SharedState state, final PanelViewFactory panelViewFactory,
	                      final double xBound, final double yBound) {
		super(x, y, radiusX, radiusY);
		this.imageData = imageData;
		this.state = state;
		this.panelViewFactory = panelViewFactory;
		this.xBound = xBound;
		this.yBound = yBound;
		bindToBounds();
		addListeners();
		setStyle();
		bindProperties();
	}

	private void bindToBounds() {
		defaultBindings();
		sampleArea.translateXProperty().bind(this.translateXProperty());
		sampleArea.translateYProperty().bind(this.translateYProperty());
		sampleArea.scaleXProperty().bind(this.scaleXProperty());
		sampleArea.scaleYProperty().bind(this.scaleYProperty());
	}

	private void addListeners() {
		onScaleChanged();
		onMousePressed();
		onMouseClicked();
		onMouseMoved();
		onMouseDragged();
		onDragExited();
		onMouseReleased();
		onMouseExited();
	}

	private void setStyle() {
		setStyle(STROKE_STYLE);
		sampleArea.setStyle(STROKE_STYLE);
		setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		setStroke(Color.color(1.0, 1.0, 1.0, 0.8));
		sampleArea.setFill(Color.color(0.0, 0.0, 0.0, 0.0));
		sampleArea.setStroke(Color.color(1.0, 1.0, 1.0, 0.9));
	}

	/**
	 * Handles property bindings.
	 */
	protected abstract void bindProperties();

	/**
	 * Sets default property bindings.
	 */
	protected void defaultBindings() {
		sampleArea.xProperty().bind(this.centerXProperty().subtract(this.radiusXProperty()));
		sampleArea.yProperty().bind(this.centerYProperty().subtract(this.radiusYProperty()));
		sampleArea.widthProperty().bind(this.radiusXProperty().multiply(2.0));
		sampleArea.heightProperty().bind(this.radiusYProperty().multiply(2.0));
	}

	private void onScaleChanged() {
		scaleXProperty().addListener((observable, oldValue, newValue) -> {
			setScaleY(newValue.doubleValue());
			recalculateTranslates();
		});
	}

	private void onMousePressed() {
		sampleArea.setOnMousePressed(this::setStartPosition);
		this.setOnMousePressed(this::setStartPosition);
	}

	private void onMouseClicked() {
		setOnMouseClicked(event -> {
			handleSingleClick(event);
			handleDoubleClick(event);
		});
	}

	private void onMouseMoved() {
		sampleArea.setOnMouseMoved(this::checkForResize);
		this.setOnMouseMoved(this::checkForResizeOrDrag);
	}

	private void onMouseDragged() {
		sampleArea.setOnMouseDragged(this::resizeAndPlace);
		this.setOnMouseDragged(this::resizeAndPlace);
	}

	private void onDragExited() {
		sampleArea.setOnDragExited(event -> setNoSelection());
		this.setOnDragExited(event -> setNoSelection());
	}

	private void onMouseReleased() {
		this.setOnMouseReleased(event -> setNoSelection());
		sampleArea.setOnMouseReleased(event -> setNoSelection());
	}

	private void onMouseExited() {
		sampleArea.setOnMouseExited(event -> getScene().setCursor(DEFAULT));
		this.setOnMouseExited(event -> getScene().setCursor(DEFAULT));
	}

	/**
	 * Recalculates sample translates.
	 */
	protected void recalculateTranslates() {
		setTranslateX((xBound * 0.5 * (getScaleX() - 1.0)) -
		              (xBound * 0.5 - getCenterX()) * (getScaleX() - 1.0));
		setTranslateY((yBound * 0.5 * (getScaleY() - 1.0)) -
		              (yBound * 0.5 - getCenterY()) * (getScaleY() - 1.0));
	}

	/**
	 * Mouse single click handler.
	 *
	 * @param event mouse event
	 */
	protected abstract void handleSingleClick(final MouseEvent event);

	/**
	 * Mouse double click handler.
	 *
	 * @param event mouse event
	 */
	protected abstract void handleDoubleClick(final MouseEvent event);

	private void setNoSelection() {
		sampleSelection = NIL;
		getScene().setCursor(DEFAULT);
	}

	/**
	 * @return index of sample
	 */
	public abstract int getIndexOf();

	/**
	 * Sets sample position, size and bounds.
	 *
	 * @param x       new X position
	 * @param y       new Y position
	 * @param radiusX new X radius
	 * @param radiusY new Y radius
	 * @param xBound  new X bound
	 * @param yBound  new Y bound
	 */
	public void setSample(final double x, final double y, final double radiusX, final double radiusY,
	                      final double xBound, final double yBound) {
		setCenterX(x);
		setCenterY(y);
		setRadiusX(radiusX);
		setRadiusY(radiusY);
		this.xBound = xBound;
		this.yBound = yBound;
		recalculateTranslates();
	}

	/**
	 * Moves sample by given distances.
	 *
	 * @param dX X distance
	 * @param dY Y distance
	 */
	public void moveCenterBy(final double dX, final double dY) {
		drag(dX, dY);
		recalculateTranslates();
	}

	private void drag(final double deltaX, final double deltaY) {
		if (!(sampleArea.getX() + deltaX + sampleArea.getWidth() > xBound || sampleArea.getX() + deltaX < 0
		      || sampleArea.getY() + deltaY + sampleArea.getHeight() > yBound || sampleArea.getY() + deltaY < 0)) {
			setCenterX(getCenterX() + deltaX);
			setCenterY(getCenterY() + deltaY);
		}
	}

	/**
	 * Sets drag start position based on given mouse event.
	 *
	 * @param event mouse event
	 */
	protected void setStartPosition(final MouseEvent event) {
		startX = event.getSceneX() / getScaleX();
		startY = event.getSceneY() / getScaleY();
	}

	/**
	 * Resizes and replaces sample based on given mouse event.
	 *
	 * @param event mouse event
	 */
	protected void resizeAndPlace(final MouseEvent event) {
		final double deltaX = updateStartX(event);
		final double deltaY = updateStartY(event);
		resizeShape(deltaX, deltaY);
		recalculateTranslates();
	}

	/**
	 * Updates drag start X position based on given mouse event.
	 *
	 * @param event mouse event
	 * @return the distance between new and old drag start X positions
	 */
	protected double updateStartX(final MouseEvent event) {
		final double deltaX = event.getSceneX() / getScaleX() - startX;
		startX = event.getSceneX() / getScaleX();
		return deltaX;
	}

	/**
	 * Updates drag start Y position based on given mouse event.
	 *
	 * @param event mouse event
	 * @return the distance between new and old drag start Y positions
	 */
	protected double updateStartY(final MouseEvent event) {
		final double deltaY = event.getSceneY() / getScaleY() - startY;
		startY = event.getSceneY() / getScaleY();
		return deltaY;
	}

	private void resizeShape(final double deltaX, final double deltaY) {
		switch (sampleSelection) {
			case NW:
				resizeNW(deltaX, deltaY);
				break;
			case NE:
				resizeNE(deltaX, deltaY);
				break;
			case SE:
				resizeSE(deltaX, deltaY);
				break;
			case SW:
				resizeSW(deltaX, deltaY);
				break;
			case W:
				resizeW(deltaX);
				break;
			case E:
				resizeE(deltaX);
				break;
			case S:
				resizeS(deltaY);
				break;
			case N:
				resizeN(deltaY);
				break;
			case DRAG:
				drag(deltaX, deltaY);
				break;
			default:
				break;
		}
	}

	private void resizeNW(final double deltaX, final double deltaY) {
		final double newWidth = sampleArea.getWidth() - deltaX;
		final double newHeight = sampleArea.getHeight() - deltaY;

		if (!(sampleArea.getX() + deltaX < 0 || sampleArea.getY() + deltaY < 0)) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	private void resizeNE(final double deltaX, final double deltaY) {
		final double newWidth = sampleArea.getWidth() + deltaX;
		final double newHeight = sampleArea.getHeight() - deltaY;

		if (!(newWidth + sampleArea.getX() + deltaX > xBound || sampleArea.getY() + deltaY < 0)) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	private void resizeSE(final double deltaX, final double deltaY) {
		final double newWidth = sampleArea.getWidth() + deltaX;
		final double newHeight = sampleArea.getHeight() + deltaY;

		if (!(newWidth + sampleArea.getX() + deltaX > xBound || newHeight + sampleArea.getY() + deltaY > yBound)) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	private void resizeSW(final double deltaX, final double deltaY) {
		final double newWidth = sampleArea.getWidth() - deltaX;
		final double newHeight = sampleArea.getHeight() + deltaY;

		if (!(sampleArea.getX() + deltaX < 0 || newHeight + sampleArea.getY() + deltaY > yBound)) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	private void resizeW(final double deltaX) {
		final double newWidth = sampleArea.getWidth() - deltaX;

		if (!(sampleArea.getX() + deltaX < 0)) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
		}
	}

	private void resizeE(final double deltaX) {
		final double newWidth = sampleArea.getWidth() + deltaX;

		if (!(newWidth + sampleArea.getX() + deltaX > xBound)) {
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
		}
	}

	private void resizeS(final double deltaY) {
		final double newHeight = sampleArea.getHeight() + deltaY;

		if (!(newHeight + sampleArea.getY() + deltaY > yBound)) {
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
		}
	}

	private void resizeN(final double deltaY) {
		final double newHeight = sampleArea.getHeight() - deltaY;

		if (!(sampleArea.getY() + deltaY < 0)) {
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	/**
	 * Resizes and replaces sample based on given distances.
	 *
	 * @param deltaX X distance
	 * @param deltaY Y distance
	 */
	protected void resizeAndPlace(final double deltaX, final double deltaY) {
		resizeShape(deltaX, deltaY);
		recalculateTranslates();
	}

	private void checkForResizeOrDrag(final MouseEvent event) {
		if (isSelected()) {
			final double dX = event.getX() - sampleArea.getX();
			final double dY = event.getY() - sampleArea.getY();
			if (!checkForResize(dX, dY) && dX > 10 / getScaleX() && dY > 10 / getScaleY()
			    && sampleArea.getWidth() - dX > 10 / getScaleX() && sampleArea.getHeight() - dY > 10 / getScaleY()) {
				getScene().setCursor(MOVE);
				sampleSelection = DRAG;
			}
		}
	}

	/**
	 * @return true if this sample is selected; false otherwise
	 */
	protected boolean isSelected() {
		return state.selectedVertex.get() == this || state.selectedSample.get() == this;
	}

	private boolean checkForResize(final double dX, final double dY) {
		if ((this instanceof Vertex || !state.rotate.get())) {
			if (sampleArea.getWidth() - dX < 7 / getScaleX() && sampleArea.getHeight() - dY < 7 / getScaleY()) {
				getScene().setCursor(SE_RESIZE);
				sampleSelection = SE;
			}
			else if (dX < 7 / getScaleX() && dY < 7 / getScaleY()) {
				getScene().setCursor(NW_RESIZE);
				sampleSelection = NW;
			}
			else if (sampleArea.getWidth() - dX < 7 / getScaleX() && dY < 7 / getScaleY()) {
				getScene().setCursor(NE_RESIZE);
				sampleSelection = NE;
			}
			else if (dX < 7 / getScaleX() && sampleArea.getHeight() - dY < 7 / getScaleY()) {
				getScene().setCursor(SW_RESIZE);
				sampleSelection = SW;
			}
			else if (sampleArea.getWidth() - dX < 7 / getScaleX()) {
				getScene().setCursor(E_RESIZE);
				sampleSelection = E;
			}
			else if (dX < 7 / getScaleX()) {
				getScene().setCursor(W_RESIZE);
				sampleSelection = W;
			}
			else if (sampleArea.getHeight() - dY < 7 / getScaleY()) {
				getScene().setCursor(S_RESIZE);
				sampleSelection = S;
			}
			else if (dY < 7 / getScaleY()) {
				getScene().setCursor(N_RESIZE);
				sampleSelection = N;
			}
			else {
				setNoSelection();
				return false;
			}
			return true;
		}
		else return false;
	}

	private void checkForResize(final MouseEvent event) {
		if (isSelected() && (this instanceof Vertex || !state.rotate.get())) {
			final double dX = event.getX() - sampleArea.getX();
			final double dY = event.getY() - sampleArea.getY();
			checkForResize(dX, dY);
		}
	}

	/**
	 * Represents available resize/drag methods.
	 */
	protected enum SampleSelection {
		NIL, NW, NE, SW, SE, N, S, W, E, DRAG
	}

}
