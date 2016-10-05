package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.scene.shape.Rectangle;

public class RectangleOfInterest extends Rectangle {

	public RectangleOfInterest() {super();}

	public RectangleOfInterest(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	public void copy( RectangleOfInterest other) {
		this.setX(0.0);
		this.setY(0.0);
		this.setWidth(0.0);
		this.setHeight(0.0);
		this.setX(other.getX());
		this.setY(other.getY());
		this.setWidth(other.getWidth());
		this.setHeight(other.getHeight());
	}

}
