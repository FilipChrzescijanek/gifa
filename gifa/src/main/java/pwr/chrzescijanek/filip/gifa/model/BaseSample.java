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
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.DRAG;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.E;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.N;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.NE;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.NIL;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.NW;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.S;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.SE;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.SW;
import static pwr.chrzescijanek.filip.gifa.controller.SharedState.SampleSelection.W;

public abstract class BaseSample extends Ellipse {

	static final String STROKE_STYLE = "-fx-stroke-type: inside;\n" +
			"    -fx-stroke-width: 2;\n" +
			"    -fx-stroke-dash-array: 12 2 4 2;\n" +
			"    -fx-stroke-dash-offset: 6;\n" +
			"    -fx-stroke-line-cap: butt;";

	public final Rectangle sampleArea = new Rectangle();

	final ImageData imageData;
	final SharedState state;
	final PanelViewFactory panelViewFactory;

	double xBound;
	double yBound;
	private double startX;
	private double startY;

	BaseSample( final ImageData imageData, double x, double y, double radiusX, double radiusY,
				final SharedState state, final PanelViewFactory panelViewFactory,
				final double xBound, final double yBound ) {
		super(x, y, radiusX, radiusY);
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

	public void setSample( double x, double y, double radiusX, double radiusY, final double xBound, final double yBound ) {
		setCenterX(x);
		setCenterY(y);
		setRadiusX(radiusX);
		setRadiusY(radiusY);
		this.xBound = xBound;
		this.yBound = yBound;
		recalculateTranslates();
	}

	abstract void handleSingleClick( final MouseEvent event );

	abstract void handleDoubleClick( final MouseEvent event );

	abstract int getIndexOf();

	void moveCenterBy( double dX, double dY) {
		drag(dX, dY);
		recalculateTranslates();
	}

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
		sampleArea.xProperty().bind(this.centerXProperty().subtract(this.radiusXProperty()));
		sampleArea.yProperty().bind(this.centerYProperty().subtract(this.radiusYProperty()));
		sampleArea.widthProperty().bind(this.radiusXProperty().multiply(2.0));
		sampleArea.heightProperty().bind(this.radiusYProperty().multiply(2.0));
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

	abstract void bindProperties( final ImageData imageData );

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
		setOnMouseClicked(event -> {
			handleSingleClick(event);
			handleDoubleClick(event);
		});
	}

	private void onMouseMoved() {
		sampleArea.setOnMouseMoved(this::checkForResize);
		this.setOnMouseMoved(this::checkForResizeOrDrag);
	}

	private void checkForResizeOrDrag( final MouseEvent event ) {
		if ( isSelected() ) {
			double dX = event.getX() - sampleArea.getX();
			double dY = event.getY() - sampleArea.getY();
			if ( !checkForResize(dX, dY) && dX > 10 / getScaleX() && dY > 10 / getScaleY()
					&& sampleArea.getWidth() - dX > 10 / getScaleX() && sampleArea.getHeight() - dY > 10 / getScaleY() ) {
				getScene().setCursor(MOVE);
				state.setSampleSelection(DRAG);
			}
		}
	}

	private void checkForResize( final MouseEvent event ) {
		if ( isSelected() ) {
			double dX = event.getX() - sampleArea.getX();
			double dY = event.getY() - sampleArea.getY();
			checkForResize(dX, dY);
		}
	}

	private boolean isSelected() {return state.selectedRectangle.get() == this || state.selectedSample.get() == this;}

	private boolean checkForResize( final double dX, final double dY ) {
		if ( sampleArea.getWidth() - dX < 7 / getScaleX() && sampleArea.getHeight() - dY < 7 / getScaleY() ) {
			getScene().setCursor(SE_RESIZE);
			state.setSampleSelection(SE);
		} else if ( dX < 7 / getScaleX() && dY < 7 / getScaleY() ) {
			getScene().setCursor(NW_RESIZE);
			state.setSampleSelection(NW);
		} else if ( sampleArea.getWidth() - dX < 7 / getScaleX() && dY < 7 / getScaleY() ) {
			getScene().setCursor(NE_RESIZE);
			state.setSampleSelection(NE);
		} else if ( dX < 7 / getScaleX() && sampleArea.getHeight() - dY < 7 / getScaleY() ) {
			getScene().setCursor(SW_RESIZE);
			state.setSampleSelection(SW);
		} else if ( sampleArea.getWidth() - dX < 7 / getScaleX() ) {
			getScene().setCursor(E_RESIZE);
			state.setSampleSelection(E);
		} else if ( dX < 7 / getScaleX() ) {
			getScene().setCursor(W_RESIZE);
			state.setSampleSelection(W);
		} else if ( sampleArea.getHeight() - dY < 7 / getScaleY() ) {
			getScene().setCursor(S_RESIZE);
			state.setSampleSelection(S);
		} else if ( dY < 7 / getScaleY() ) {
			getScene().setCursor(N_RESIZE);
			state.setSampleSelection(N);
		} else {
			setNoSelection();
			return false;
		}
		return true;
	}

	private void onMouseDragged() {
		sampleArea.setOnMouseDragged(this::resizeAndPlace);
		this.setOnMouseDragged(this::resizeAndPlace);
	}

	private void recalculateTranslates() {
		setTranslateX(( xBound * 0.5 * ( getScaleX() - 1.0 ) ) -
				( xBound * 0.5 - getCenterX() ) * ( getScaleX() - 1.0 ));
		setTranslateY(( yBound * 0.5 * ( getScaleY() - 1.0 ) ) -
				( yBound * 0.5 - getCenterY() ) * ( getScaleY() - 1.0 ));
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
		state.setSampleSelection(NIL);
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
		switch ( state.getSampleSelection() ) {
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
		final double newWidth = sampleArea.getWidth() - deltaX;
		final double newHeight = sampleArea.getHeight() - deltaY;

		if ( !( sampleArea.getX() + deltaX < 0 || sampleArea.getY() + deltaY < 0 ) ) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	private void resizeSE( final double deltaX, final double deltaY ) {
		final double newWidth = sampleArea.getWidth() + deltaX;
		final double newHeight = sampleArea.getHeight() + deltaY;

		if ( !( newWidth + sampleArea.getX() + deltaX > xBound || newHeight + sampleArea.getY() + deltaY > yBound ) ) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	private void resizeSW( final double deltaX, final double deltaY ) {
		final double newWidth = sampleArea.getWidth() - deltaX;
		final double newHeight = sampleArea.getHeight() + deltaY;

		if ( !( sampleArea.getX() + deltaX < 0 || newHeight + sampleArea.getY() + deltaY > yBound ) ) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	private void resizeNE( final double deltaX, final double deltaY ) {
		final double newWidth = sampleArea.getWidth() + deltaX;
		final double newHeight = sampleArea.getHeight() - deltaY;

		if ( !( newWidth + sampleArea.getX() + deltaX > xBound || sampleArea.getY() + deltaY < 0 ) ) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	private void resizeE( final double deltaX ) {
		final double newWidth = sampleArea.getWidth() + deltaX;

		if ( !( newWidth + sampleArea.getX() + deltaX > xBound ) ) {
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
		}
	}

	private void resizeW( final double deltaX ) {
		final double newWidth = sampleArea.getWidth() - deltaX;

		if ( !( sampleArea.getX() + deltaX < 0 ) ) {
			setCenterX(newWidth > 6 ? getCenterX() + deltaX / 2.0 : getCenterX());
			setRadiusX(newWidth > 6 ? newWidth / 2.0 : 3.0);
		}
	}

	private void resizeS( final double deltaY ) {
		final double newHeight =sampleArea. getHeight() + deltaY;

		if ( !( newHeight + sampleArea.getY() + deltaY > yBound ) ) {
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
		}
	}

	private void resizeN( final double deltaY ) {
		final double newHeight = sampleArea.getHeight() - deltaY;

		if ( !( sampleArea.getY() + deltaY < 0 ) ) {
			setCenterY(newHeight > 6 ? getCenterY() + deltaY / 2.0 : getCenterY());
			setRadiusY(newHeight > 6 ? newHeight / 2.0 : 3.0);
		}
	}

	private void drag( final double deltaX, final double deltaY ) {
		if ( !( sampleArea.getX() + deltaX + sampleArea.getWidth() > xBound || sampleArea.getX() + deltaX < 0
				|| sampleArea.getY() + deltaY + sampleArea.getHeight() > yBound || sampleArea.getY() + deltaY < 0 ) ) {
			setCenterX(getCenterX() + deltaX);
			setCenterY(getCenterY() + deltaY);
		}
	}

}
