package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.collections.ObservableList;
import javafx.scene.shape.Polygon;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

public class Triangle extends Polygon {

	private final ObservableList< Double > points = this.getPoints();

	public Triangle() {super();}

	public Triangle( final Point p1, final Point p2, final Point p3) {
		super(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
	}

	public void copy( Triangle other) {
		this.points.clear();
		this.points.addAll(other.points);
	}

	public void moveFirstPointTo(final double x, final double y) {
		points.set(0, x);
		points.set(1, y);
	}

	public void moveSecondPointTo(final double x, final double y) {
		points.set(2, x);
		points.set(3, y);
	}

	public void moveThirdPointTo(final double x, final double y) {
		points.set(4, x);
		points.set(5, y);
	}

	public void moveTriangleBy(final double x, final double y) {
		for (int i = 0; i < points.size(); i += 2) {
			points.set(i, points.get(i) + x);
			points.set(i + 1, points.get(i + 1) + y);
		}
	}

	public MatOfPoint2f getMatOfPoints() {
		return new MatOfPoint2f(
				new Point(points.get(0), points.get(1)),
				new Point(points.get(2), points.get(3)),
				new Point(points.get(4), points.get(5))
		);
	}

	public int getIndexOfNearestPointInRadius(final double x, final double y, final double radius) {
		int index = -1;
		double minDistance = radius;
		for (int i = 0; i < points.size(); i += 2) {
			final double distance = getDistanceBetween(x, y, points.get(i), points.get(i + 1));
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
