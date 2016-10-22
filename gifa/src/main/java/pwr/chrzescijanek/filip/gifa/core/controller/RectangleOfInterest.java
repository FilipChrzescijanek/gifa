package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.scene.Cursor;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class RectangleOfInterest extends Rectangle {

	public RectangleOfInterest(double x, double y, double width, double height) {
		super(x, y, width, height);

		setOnMouseMoved(event -> {
			if (State.INSTANCE.selectedRectangle.get() == this || State.INSTANCE.selectedSample.get() == this) {
				double dX = event.getX() - this.getX();
				double dY = event.getY() - this.getY();
				if ( dX > 7.0 / this.getScaleX() && dY > 7.0 / this.getScaleY() && this.getWidth() - dX > 7.0 / this.getScaleX()
						&& this.getHeight() - dY > 7.0 / this.getScaleY() &&
						State.INSTANCE.getRectangleSelection() == State.RectangleSelection.NONE ) {
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.DRAG);
					this.getScene().setCursor(Cursor.MOVE);
				} else if ( !( dX > 7.0 / this.getScaleX() && dY > 7.0 / this.getScaleY()
						&& this.getWidth() - dX > 7.0 / this.getScaleX()
						&& this.getHeight() - dY > 7.0 / this.getScaleY() ) &&
						State.INSTANCE.getRectangleSelection() == State.RectangleSelection.DRAG ) {
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
					this.getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});
	}

	public void copy( RectangleOfInterest other) {
		this.setX(other.getX());
		this.setY(other.getY());
		this.setWidth(other.getWidth());
		this.setHeight(other.getHeight());
	}

	public void reset() {
		this.setX(0.0);
		this.setY(0.0);
		this.setWidth(0.0);
		this.setHeight(0.0);
	}
}
