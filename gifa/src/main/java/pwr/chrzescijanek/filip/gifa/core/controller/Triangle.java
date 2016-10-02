package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Polygon;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

public class Triangle extends Polygon {

	public final DoubleProperty[] points;

	public Triangle() {
		super();
		this.points = new DoubleProperty[6];
		for (int i = 0; i < points.length; i++)
			points[i] = new SimpleDoubleProperty();
		registerListeners();
	}

	public Triangle( final Point p1, final Point p2, final Point p3) {
		super(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
		this.points = new DoubleProperty[6];
		for (int i = 0; i < points.length; i++)
			points[i] = new SimpleDoubleProperty();
		registerListeners();
	}

	private void registerListeners() {
		for ( int i = 0; i < points.length; i++) {
			final int index = i;
			points[i].addListener(( observable, oldValue, newValue ) -> {
				getPoints().set(index, newValue.doubleValue());
			});
		}
	}

	public void copy( Triangle other) {
		for (int i = 0; i < points.length; i++)
			this.points[i].setValue(other.points[i].getValue());
	}

	public void moveFirstPointTo(final double x, final double y) {
		points[0].setValue(x);
		points[1].setValue(y);
	}

	public void moveSecondPointTo(final double x, final double y) {
		points[2].setValue(x);
		points[3].setValue(y);
	}

	public void moveThirdPointTo(final double x, final double y) {
		points[4].setValue(x);
		points[5].setValue(y);
	}

	public void moveTriangleBy(final double x, final double y) {
		for (int i = 0; i < points.length; i += 2) {
			points[i].setValue(points[i].getValue() + x);
			points[i + 1].setValue(points[i + 1].getValue() + y);
		}
	}

	public MatOfPoint2f getMatOfPoints() {
		return new MatOfPoint2f(
				new Point(points[0].getValue(), points[1].getValue()),
				new Point(points[2].getValue(), points[3].getValue()),
				new Point(points[4].getValue(), points[5].getValue())
		);
	}

	public int getIndexOfNearestPointInRadius(final double x, final double y, final double radius) {
		int index = -1;
		double minDistance = radius;
		for (int i = 0; i < points.length; i += 2) {
			final double distance = getDistanceBetween(x, y, points[i].getValue(), points[i + 1].getValue());
			if ( distance < minDistance ) {
				index = i / 2;
				minDistance = distance;
			}
		}
		return index;
	}

	private double getDistanceBetween(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

}
