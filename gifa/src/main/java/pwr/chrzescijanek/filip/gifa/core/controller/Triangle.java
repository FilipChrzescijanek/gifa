package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Polygon;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

public class Triangle extends Polygon {

	public final DoubleProperty[] pointsProperty;
	public double xBound = Double.POSITIVE_INFINITY;
	public double yBound = Double.POSITIVE_INFINITY;

	public Triangle() {
		super();
		this.pointsProperty = new DoubleProperty[6];
		for ( int i = 0; i < pointsProperty.length; i++)
			pointsProperty[i] = new SimpleDoubleProperty();
		registerListeners();
	}

	public Triangle( final Point p1, final Point p2, final Point p3) {
		super(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
		this.pointsProperty = new DoubleProperty[6];
		for ( int i = 0; i < pointsProperty.length; i++)
			pointsProperty[i] = new SimpleDoubleProperty();
		registerListeners();
	}

	private void registerListeners() {
		for ( int i = 0; i < pointsProperty.length; i += 2) {
			final int index = i;
			pointsProperty[index].addListener(( observable, oldValue, newValue ) -> {
				final double value = newValue.doubleValue();
//			s	if ( value >= 0 && value < xBound)
					getPoints().set(index, value);
//				else
//					pointsProperty[index].setValue(oldValue);
			});
			pointsProperty[index + 1].addListener(( observable, oldValue, newValue ) -> {
				final double value = newValue.doubleValue();
//				if (value >= 0 && value < yBound)
					getPoints().set(index + 1, value);
//				else
//					pointsProperty[index + 1].setValue(oldValue);
			});
		}
	}

	public void copy( Triangle other) {
		for ( int i = 0; i < pointsProperty.length; i++)
			this.pointsProperty[i].setValue(other.pointsProperty[i].getValue());
	}

	public void moveFirstPointTo(final double x, final double y) {
		pointsProperty[0].setValue(x);
		pointsProperty[1].setValue(y);
	}

	public void moveSecondPointTo(final double x, final double y) {
		pointsProperty[2].setValue(x);
		pointsProperty[3].setValue(y);
	}

	public void moveThirdPointTo(final double x, final double y) {
		pointsProperty[4].setValue(x);
		pointsProperty[5].setValue(y);
	}

	public void moveTriangleBy(final double x, final double y) {
		for ( int i = 0; i < pointsProperty.length; i += 2) {
			pointsProperty[i].setValue(pointsProperty[i].getValue() + x);
			pointsProperty[i + 1].setValue(pointsProperty[i + 1].getValue() + y);
		}
	}

	public MatOfPoint2f getMatOfPoints() {
		return new MatOfPoint2f(
				new Point(pointsProperty[0].getValue(), pointsProperty[1].getValue()),
				new Point(pointsProperty[2].getValue(), pointsProperty[3].getValue()),
				new Point(pointsProperty[4].getValue(), pointsProperty[5].getValue())
		);
	}

	public int getIndexOfNearestPointInRadius(final double x, final double y, final double radius) {
		int index = -1;
		double minDistance = radius;
		for ( int i = 0; i < pointsProperty.length; i += 2) {
			final double distance = getDistanceBetween(x, y, pointsProperty[i].getValue(), pointsProperty[i + 1].getValue());
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
