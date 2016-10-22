package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.shape.Polygon;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

public class Triangle extends Polygon {

	public final DoubleProperty[] pointsProperty;

	public Triangle() {
		super(0,0,0,0,0,0);
		this.pointsProperty = new DoubleProperty[6];
		for ( int i = 0; i < pointsProperty.length; i++)
			pointsProperty[i] = new SimpleDoubleProperty();
		registerListeners();
	}

	public Triangle( final Point p1, final Point p2, final Point p3) {
		super(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
		this.pointsProperty = new DoubleProperty[6];
		final ObservableList< Double > points = getPoints();
		for ( int i = 0; i < pointsProperty.length; i++)
			pointsProperty[i] = new SimpleDoubleProperty(points.get(i));
		registerListeners();
	}

	private void registerListeners() {
		for ( int i = 0; i < pointsProperty.length; i++) {
			final int index = i;
			pointsProperty[index].addListener(( observable, oldValue, newValue ) -> {
				final double value = newValue.doubleValue();
				getPoints().set(index, value);
			});
		}
	}

	public void recalculateTranslates(double width, double height) {
		setTranslateX(( width * 0.5 * ( getScaleX() - 1.0 ) ) -
				( width * 0.5 - getTriangleMiddleX() ) * ( getScaleX() - 1.0 ));
		setTranslateY(( height * 0.5 * ( getScaleY() - 1.0 ) ) -
				( height * 0.5 - getTriangleMiddleY() ) * ( getScaleY() - 1.0 ));
	}

	private double getTriangleMiddleX() {
		double firstPointX = getPoints().get(0);
		double secondPointX = getPoints().get(2);
		double thirdPointX = getPoints().get(4);
		double minX = getMin(firstPointX, secondPointX, thirdPointX);
		double maxX = getMax(firstPointX, secondPointX, thirdPointX);
		return ( maxX - minX ) / 2.0 + minX;
	}

	private double getTriangleMiddleY() {
		double firstPointY = getPoints().get(1);
		double secondPointY = getPoints().get(3);
		double thirdPointY = getPoints().get(5);
		double minY = getMin(firstPointY, secondPointY, thirdPointY);
		double maxY = getMax(firstPointY, secondPointY, thirdPointY);
		return ( maxY - minY ) / 2.0 + minY;
	}

	private double getMin( final double firstPointX, final double secondPointX, final double thirdPointX ) {
		return Math.min(firstPointX, Math.min(secondPointX, thirdPointX));
	}

	private double getMax( final double firstPointY, final double secondPointY, final double thirdPointY ) {
		return Math.max(firstPointY, Math.max(secondPointY, thirdPointY));
	}

	public void reset() {
		for ( int i = 0; i < pointsProperty.length; i++)
			this.pointsProperty[i].setValue(0);
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
