package pwr.chrzescijanek.filip.gifa.controller;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class RectangleOfInterest extends Rectangle {

	public final Ellipse sample = new Ellipse();
	private Image image;

	public void setRectangle(double x, double y, double width, double height, Image image) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.image = image;
	}

	public RectangleOfInterest(double x, double y, double width, double height, Image image) {
		super(x, y, width, height);
		this.image = image;

		sample.centerXProperty().bind(this.xProperty().add(this.widthProperty().divide(2.0)));
		sample.centerYProperty().bind(this.yProperty().add(this.heightProperty().divide(2.0)));
		sample.radiusXProperty().bind(this.widthProperty().divide(2.0));
		sample.radiusYProperty().bind(this.heightProperty().divide(2.0));
		sample.translateXProperty().bind(this.translateXProperty());
		sample.translateYProperty().bind(this.translateYProperty());
		sample.scaleXProperty().bind(this.scaleXProperty());
		sample.scaleYProperty().bind(this.scaleYProperty());

		sample.setOnMousePressed(event -> {
			State.INSTANCE.x = event.getX();
			State.INSTANCE.y = event.getY();
		});

		this.setOnMousePressed(event -> {
			State.INSTANCE.x = event.getX();
			State.INSTANCE.y = event.getY();
		});

		sample.setOnMouseMoved(event -> {
			if (State.INSTANCE.selectedRectangle.get() == this || State.INSTANCE.selectedSample.get() == this) {
				double dX = event.getX() - this.getX();
				double dY = event.getY() - this.getY();
				if ( this.getWidth() - dX < 7 / this.getScaleX() && this.getHeight() - dY < 7 / this.getScaleY()
						&& State.INSTANCE.getRectangleSelection() != State.RectangleSelection.DRAG ) {
					this.getScene().setCursor(Cursor.SE_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.SE);
				} else if ( dX < 7 / this.getScaleX() && dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.NW_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.NW);
				} else if ( this.getWidth() - dX < 7 / this.getScaleX() && dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.NE_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.NE);
				} else if ( dX < 7 / this.getScaleX() && this.getHeight() - dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.SW_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.SW);
				} else if ( this.getWidth() - dX < 7 / this.getScaleX() ) {
					this.getScene().setCursor(Cursor.E_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.E);
				} else if ( dX < 7 / this.getScaleX() ) {
					this.getScene().setCursor(Cursor.W_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.W);
				} else if ( this.getHeight() - dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.S_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.S);
				} else if ( dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.N_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.N);
				} else if ( dX > 14 / this.getScaleX() && dY > 14 / this.getScaleY()
						&& this.getWidth() - dX > 10 / this.getScaleX() && this.getHeight() - dY > 10 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.MOVE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.DRAG);
				}
			}
		});

		this.setOnMouseMoved(event -> {
			if (State.INSTANCE.selectedRectangle.get() == this || State.INSTANCE.selectedSample.get() == this) {
				double dX = event.getX() - this.getX();
				double dY = event.getY() - this.getY();
				if ( this.getWidth() - dX < 7 / this.getScaleX() && this.getHeight() - dY < 7 / this.getScaleY()
						&& State.INSTANCE.getRectangleSelection() != State.RectangleSelection.DRAG ) {
					this.getScene().setCursor(Cursor.SE_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.SE);
				} else if ( dX < 7 / this.getScaleX() && dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.NW_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.NW);
				} else if ( this.getWidth() - dX < 7 / this.getScaleX() && dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.NE_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.NE);
				} else if ( dX < 7 / this.getScaleX() && this.getHeight() - dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.SW_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.SW);
				} else if ( this.getWidth() - dX < 7 / this.getScaleX() ) {
					this.getScene().setCursor(Cursor.E_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.E);
				} else if ( dX < 7 / this.getScaleX() ) {
					this.getScene().setCursor(Cursor.W_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.W);
				} else if ( this.getHeight() - dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.S_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.S);
				} else if ( dY < 7 / this.getScaleY() ) {
					this.getScene().setCursor(Cursor.N_RESIZE);
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.N);
				}
			}
		});

		sample.setOnDragExited(event -> {
			State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
			this.getScene().setCursor(Cursor.DEFAULT);
		});

		this.setOnDragExited(event -> {
			State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
			this.getScene().setCursor(Cursor.DEFAULT);
		});

		this.setOnMouseReleased(event -> {
			State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
			this.getScene().setCursor(Cursor.DEFAULT);
		});

		sample.setOnMouseReleased(event -> {
			State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
			this.getScene().setCursor(Cursor.DEFAULT);
		});

		sample.setOnMouseExited(event -> {
			this.getScene().setCursor(Cursor.DEFAULT);
		});

		this.setOnMouseExited(event -> {
			this.getScene().setCursor(Cursor.DEFAULT);
		});


	}

	public void resizeShape(final double deltaX, final double deltaY) {

		switch (State.INSTANCE.getRectangleSelection()) {
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
				resizeW(deltaX, deltaY);
				break;
			case E:
				resizeE(deltaX, deltaY);
				break;
			case S:
				resizeS(deltaX, deltaY);
				break;
			case N:
				resizeN(deltaX, deltaY);
				break;
			case DRAG:
				drag(deltaX, deltaY);
				break;
			default:
				break;
		}
	}

	public void resizeNW( final double deltaX, final double deltaY ) {

		final double newWidth = this.getWidth() - deltaX;
		final double newHeight = this.getHeight() - deltaY;

		if (!(this.getX() + deltaX < 0 || this.getY() + deltaY < 0)) {
			this.setX(newWidth > 6 ? this.getX() + deltaX : this.getX());
			this.setWidth(newWidth > 6 ? newWidth : 6);
			this.setY(newHeight > 6 ? this.getY() + deltaY : this.getY());
			this.setHeight(newHeight > 6 ? newHeight : 6);
		}
		
	}

	public void resizeSE( final double deltaX, final double deltaY ) {
		

		final double newWidth = this.getWidth() + deltaX;
		final double newHeight = this.getHeight() + deltaY;

		if (!(newWidth + this.getX() + deltaX > image.getWidth() - 0
				|| newHeight + this.getY() + deltaY > image.getHeight() - 0)) {
			this.setWidth(newWidth > 6 ? newWidth : 6);
			this.setHeight(newHeight > 6 ? newHeight : 6);
		}
		
	}

	public void resizeSW( final double deltaX, final double deltaY ) {

		final double newWidth = this.getWidth() - deltaX;
		final double newHeight = this.getHeight() + deltaY;

		if (!(this.getX() + deltaX < 0
				|| newHeight + this.getY() + deltaY > image.getHeight() - 0)) {
			this.setHeight(newHeight > 6 ? newHeight : 6);
			this.setWidth(newWidth > 6 ? newWidth : 6);
			this.setX(newWidth > 6 ? this.getX() + deltaX : this.getX());
		}
		
	}

	public void resizeNE( final double deltaX, final double deltaY ) {

		final double newWidth = this.getWidth() + deltaX;
		final double newHeight = this.getHeight() - deltaY;

		if (!(newWidth + this.getX() + deltaX > image.getWidth() - 0
				|| this.getY() + deltaY < 0)) {
			this.setY(newHeight > 6 ? this.getY() + deltaY : this.getY());
			this.setWidth(newWidth > 6 ? newWidth : 6);
			this.setHeight(newHeight > 6 ? newHeight : 5);
		}
		
	}

	public void resizeE( final double deltaX, final double deltaY ) {

		final double newWidth = this.getWidth() + deltaX;

		if (!(newWidth + this.getX() + deltaX > image.getWidth() - 0)) {
			this.setWidth(newWidth > 6 ? newWidth : 6);
		}
		
	}

	public void resizeW( final double deltaX, final double deltaY ) {

		final double newWidth = this.getWidth() - deltaX;

		if (!(this.getX() + deltaX < 0)) {
			this.setX(newWidth > 6 ? this.getX() + deltaX : this.getX());
			this.setWidth(newWidth > 6 ? newWidth : 6);
		}
		
	}

	public void resizeS( final double deltaX, final double deltaY ) {

		final double newHeight = this.getHeight() + deltaY;

		if (!(newHeight + this.getY() + deltaY > image.getHeight() - 0)) {
			this.setHeight(newHeight > 6 ? newHeight : 6);
		}
		
	}

	public void resizeN( final double deltaX, final double deltaY ) {

		final double newHeight = this.getHeight() - deltaY;

		if (!(this.getY() + deltaY < 0)) {
			this.setY(newHeight > 6 ? this.getY() + deltaY : this.getY());
			this.setHeight(newHeight > 6 ? newHeight : 6);
		}
		
	}

	public void drag( final double deltaX, final double deltaY ) {

		if (!(this.getX() + deltaX + this.getWidth() > image.getWidth() - 0
				|| this.getX() + deltaX < 0
				|| this.getY() + deltaY + this.getHeight() > image.getHeight() - 0
				|| this.getY() + deltaY < 0)) {
			this.setX(this.getX() + deltaX);
			this.setY(this.getY() + deltaY);
		}
	}

}
