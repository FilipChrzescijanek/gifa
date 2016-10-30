package pwr.chrzescijanek.filip.gifa.model;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import pwr.chrzescijanek.filip.gifa.controller.PanelController;
import pwr.chrzescijanek.filip.gifa.controller.SharedState;
import pwr.chrzescijanek.filip.gifa.util.StageUtils;
import pwr.chrzescijanek.filip.gifa.view.FXView;

import java.util.List;

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
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.DRAG;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.E;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.N;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.NE;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.NIL;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.NW;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.S;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.SE;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.SW;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.RectangleSelection.W;

public abstract class BaseSample extends Rectangle {

	static final String STROKE_STYLE = "-fx-stroke-type: inside;\n" +
			"    -fx-stroke-width: 2;\n" +
			"    -fx-stroke-dash-array: 12 2 4 2;\n" +
			"    -fx-stroke-dash-offset: 6;\n" +
			"    -fx-stroke-line-cap: butt;";

	public final Ellipse sampleArea = new Ellipse();

	final ImageData imageData;
	final SharedState state;
	final PanelViewFactory panelViewFactory;

	double xBound;
	double yBound;
	private double startX;
	private double startY;

	BaseSample( final ImageData imageData, double x, double y, double width, double height,
				final SharedState state, final PanelViewFactory panelViewFactory,
				final double xBound, final double yBound ) {
		super(x, y, width, height);
		this.imageData = imageData;
		this.state = state;
		this.panelViewFactory = panelViewFactory;
		this.xBound = xBound;
		this.yBound = yBound;
		bindToBounds();
		addListeners();
		setStyle();
		bindProperties(this.imageData);
	}

	public void setRectangle( double x, double y, double width, double height, final double xBound, final double yBound ) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		this.xBound = xBound;
		this.yBound = yBound;
	}

	abstract void handleSingleClick( final MouseEvent event );

	abstract void handleDoubleClick( final MouseEvent event );

	abstract int getIndexOf();

	void setStartPosition( final MouseEvent event ) {
		startX = event.getX();
		startY = event.getY();
	}

	void resizeAndPlace( final MouseEvent event ) {
		resizeShape(event);
		recalculateTranslates();
	}

	Stage getNewStage( String viewPath, String info, String title, List< ? extends PanelView > views ) {
		final Stage newStage = new Stage();
		FXView fxView = new FXView(viewPath);
		final PanelController controller = initializeController(info, views, fxView);
		showStage(newStage, fxView, controller, title);
		return newStage;
	}

	PanelController initializeController( final String info, final List< ? extends PanelView > views, final FXView fxView ) {
		final PanelController controller = (PanelController) fxView.getController();
		controller.setInfo(info);
		controller.setImageViews(views);
		return controller;
	}

	void showStage( final Stage newStage, final FXView fxView, final PanelController controller, final String title ) {
		Image icon = new Image(getClass().getResourceAsStream("/images/icon-small.png"));
		StageUtils.prepareStage(newStage, title, icon, fxView);
		controller.placeImages();
		newStage.show();
	}

	private void bindToBounds() {
		sampleArea.centerXProperty().bind(this.xProperty().add(this.widthProperty().divide(2.0)));
		sampleArea.centerYProperty().bind(this.yProperty().add(this.heightProperty().divide(2.0)));
		sampleArea.radiusXProperty().bind(this.widthProperty().divide(2.0));
		sampleArea.radiusYProperty().bind(this.heightProperty().divide(2.0));
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
		setFill(Color.color(0.0, 0.0, 0.0, 0.0));
		setStroke(Color.color(1.0, 1.0, 1.0, 0.9));
		sampleArea.setFill(Color.color(0.3, 0.3, 0.3, 0.5));
		sampleArea.setStroke(Color.color(1.0, 1.0, 1.0, 0.8));
	}

	private void bindProperties( final ImageData imageData ) {
		scaleXProperty().bind(imageData.scale);
		setVisible(false);
	}

	private void onScaleChanged() {
		scaleXProperty().addListener(( observable, oldValue, newValue ) -> {
			setScaleY(newValue.doubleValue());
			recalculateTranslates();
		});
	}

	private void onMousePressed() {
		sampleArea.setOnMousePressed(this::setStartPosition);
		this.setOnMousePressed(this::setStartPosition);
	}

	private void onMouseClicked() {
		sampleArea.setOnMouseClicked(event -> {
			handleSingleClick(event);
			handleDoubleClick(event);
		});
	}

	private void onMouseMoved() {
		sampleArea.setOnMouseMoved(this::checkForResizeOrDrag);
		this.setOnMouseMoved(this::checkForResize);
	}

	private void checkForResizeOrDrag( final MouseEvent event ) {
		if ( isSelected() ) {
			double dX = event.getX() - getX();
			double dY = event.getY() - getY();
			if ( !checkForResize(dX, dY) && dX > 14 / getScaleX() && dY > 14 / getScaleY()
					&& getWidth() - dX > 14 / getScaleX() && getHeight() - dY > 14 / getScaleY() ) {
				getScene().setCursor(MOVE);
				state.setRectangleSelection(DRAG);
			}
		}
	}

	private void checkForResize( final MouseEvent event ) {
		if ( isSelected() ) {
			double dX = event.getX() - getX();
			double dY = event.getY() - getY();
			checkForResize(dX, dY);
		}
	}

	private boolean isSelected() {return state.selectedRectangle.get() == this || state.selectedSample.get() == this;}

	private boolean checkForResize( final double dX, final double dY ) {
		if ( getWidth() - dX < 7 / getScaleX() && getHeight() - dY < 7 / getScaleY() ) {
			getScene().setCursor(SE_RESIZE);
			state.setRectangleSelection(SE);
		} else if ( dX < 7 / getScaleX() && dY < 7 / getScaleY() ) {
			getScene().setCursor(NW_RESIZE);
			state.setRectangleSelection(NW);
		} else if ( getWidth() - dX < 7 / getScaleX() && dY < 7 / getScaleY() ) {
			getScene().setCursor(NE_RESIZE);
			state.setRectangleSelection(NE);
		} else if ( dX < 7 / getScaleX() && getHeight() - dY < 7 / getScaleY() ) {
			getScene().setCursor(SW_RESIZE);
			state.setRectangleSelection(SW);
		} else if ( getWidth() - dX < 7 / getScaleX() ) {
			getScene().setCursor(E_RESIZE);
			state.setRectangleSelection(E);
		} else if ( dX < 7 / getScaleX() ) {
			getScene().setCursor(W_RESIZE);
			state.setRectangleSelection(W);
		} else if ( getHeight() - dY < 7 / getScaleY() ) {
			getScene().setCursor(S_RESIZE);
			state.setRectangleSelection(S);
		} else if ( dY < 7 / getScaleY() ) {
			getScene().setCursor(N_RESIZE);
			state.setRectangleSelection(N);
		} else return false;
		return true;
	}

	private void onMouseDragged() {
		sampleArea.setOnMouseDragged(this::resizeAndPlace);
		this.setOnMouseDragged(this::resizeAndPlace);
	}

	private void recalculateTranslates() {
		setTranslateX(( xBound * 0.5 * ( getScaleX() - 1.0 ) ) -
				( xBound * 0.5 - ( getX() + getWidth() * 0.5 ) ) * ( getScaleX() - 1.0 ));
		setTranslateY(( yBound * 0.5 * ( getScaleY() - 1.0 ) ) -
				( yBound * 0.5 - ( getY() + getHeight() * 0.5 ) ) * ( getScaleY() - 1.0 ));
	}

	private void onDragExited() {
		sampleArea.setOnDragExited(event -> setNoSelection());
		this.setOnDragExited(event -> setNoSelection());
	}

	private void onMouseReleased() {
		this.setOnMouseReleased(event -> setNoSelection());
		sampleArea.setOnMouseReleased(event -> setNoSelection());
	}

	private void setNoSelection() {
		state.setRectangleSelection(NIL);
		getScene().setCursor(DEFAULT);
	}

	private void onMouseExited() {
		sampleArea.setOnMouseExited(event -> getScene().setCursor(DEFAULT));
		this.setOnMouseExited(event -> getScene().setCursor(DEFAULT));
	}

	private void resizeShape( final MouseEvent event ) {
		final double deltaX = event.getX() - startX;
		final double deltaY = event.getY() - startY;
		startX = event.getX();
		startY = event.getY();
		switch ( state.getRectangleSelection() ) {
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

	private void resizeNW( final double deltaX, final double deltaY ) {
		final double newWidth = getWidth() - deltaX;
		final double newHeight = getHeight() - deltaY;

		if ( !( getX() + deltaX < 0 || getY() + deltaY < 0 ) ) {
			setX(newWidth > 6 ? getX() + deltaX : getX());
			setY(newHeight > 6 ? getY() + deltaY : getY());
			setWidth(newWidth > 6 ? newWidth : 6);
			setHeight(newHeight > 6 ? newHeight : 6);
		}
	}

	private void resizeSE( final double deltaX, final double deltaY ) {
		final double newWidth = getWidth() + deltaX;
		final double newHeight = getHeight() + deltaY;

		if ( !( newWidth + getX() + deltaX > xBound || newHeight + getY() + deltaY > yBound ) ) {
			setWidth(newWidth > 6 ? newWidth : 6);
			setHeight(newHeight > 6 ? newHeight : 6);
		}
	}

	private void resizeSW( final double deltaX, final double deltaY ) {
		final double newWidth = getWidth() - deltaX;
		final double newHeight = getHeight() + deltaY;

		if ( !( getX() + deltaX < 0 || newHeight + getY() + deltaY > yBound ) ) {
			setX(newWidth > 6 ? getX() + deltaX : getX());
			setWidth(newWidth > 6 ? newWidth : 6);
			setHeight(newHeight > 6 ? newHeight : 6);
		}
	}

	private void resizeNE( final double deltaX, final double deltaY ) {
		final double newWidth = getWidth() + deltaX;
		final double newHeight = getHeight() - deltaY;

		if ( !( newWidth + getX() + deltaX > xBound || getY() + deltaY < 0 ) ) {
			setY(newHeight > 6 ? getY() + deltaY : getY());
			setWidth(newWidth > 6 ? newWidth : 6);
			setHeight(newHeight > 6 ? newHeight : 6);
		}
	}

	private void resizeE( final double deltaX ) {
		final double newWidth = getWidth() + deltaX;

		if ( !( newWidth + getX() + deltaX > xBound ) ) {
			setWidth(newWidth > 6 ? newWidth : 6);
		}
	}

	private void resizeW( final double deltaX ) {
		final double newWidth = getWidth() - deltaX;

		if ( !( getX() + deltaX < 0 ) ) {
			setX(newWidth > 6 ? getX() + deltaX : getX());
			setWidth(newWidth > 6 ? newWidth : 6);
		}
	}

	private void resizeS( final double deltaY ) {
		final double newHeight = getHeight() + deltaY;

		if ( !( newHeight + getY() + deltaY > yBound ) ) {
			setHeight(newHeight > 6 ? newHeight : 6);
		}
	}

	private void resizeN( final double deltaY ) {
		final double newHeight = getHeight() - deltaY;

		if ( !( getY() + deltaY < 0 ) ) {
			setY(newHeight > 6 ? getY() + deltaY : getY());
			setHeight(newHeight > 6 ? newHeight : 6);
		}
	}

	private void drag( final double deltaX, final double deltaY ) {
		if ( !( getX() + deltaX + getWidth() > xBound || getX() + deltaX < 0
				|| getY() + deltaY + getHeight() > yBound || getY() + deltaY < 0 ) ) {
			setX(getX() + deltaX);
			setY(getY() + deltaY);
		}
	}

}
