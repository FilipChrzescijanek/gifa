package pwr.chrzescijanek.filip.gifa.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.shape.Polygon;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

public class Triangle extends Polygon {

	public final DoubleProperty[] pointsProperty;
	private double xBound;
	private double yBound;

	public Triangle(final double xBound, final double yBound) {
		super(0,0,0,0,0,0);
		this.xBound = xBound;
		this.yBound = yBound;
		this.pointsProperty = new DoubleProperty[6];
		initializePointsWithZeros();
		registerPointsListeners();
		registerScaleListeners();
	}

	private void initializePointsWithZeros() {
		for ( int i = 0; i < pointsProperty.length; i++)
			pointsProperty[i] = new SimpleDoubleProperty(0.0);
	}

	public Triangle( final Point p1, final Point p2, final Point p3, final double xBound, final double yBound) {
		super(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
		this.xBound = xBound;
		this.yBound = yBound;
		this.pointsProperty = new DoubleProperty[6];
		initializePoints();
		registerPointsListeners();
		registerScaleListeners();
	}

	private void initializePoints() {
		final ObservableList< Double > points = getPoints();
		for ( int i = 0; i < pointsProperty.length; i++)
			pointsProperty[i] = new SimpleDoubleProperty(points.get(i));
	}

	private void registerPointsListeners() {
		for ( int i = 0; i < pointsProperty.length; i++) {
			final int index = i;
			pointsProperty[index].addListener(( observable, oldValue, newValue ) -> {
				final double value = newValue.doubleValue();
				getPoints().set(index, value);
				if (index % 2 == 0) recalculateTranslateX();
				else recalculateTranslateY();
			});
		}
	}

	private void registerScaleListeners() {
		scaleXProperty().addListener(( observable, oldValue, newValue ) -> {
			setScaleY(newValue.doubleValue());
			recalculateTranslates();
		});
	}

	private void recalculateTranslates() {
		recalculateTranslateX();
		recalculateTranslateY();
	}

	private void recalculateTranslateX() {
		setTranslateX(( xBound * 0.5 * ( getScaleX() - 1.0 ) ) -
				( xBound * 0.5 - getTriangleMiddleX() ) * ( getScaleX() - 1.0 ));
	}

	private void recalculateTranslateY() {
		setTranslateY(( yBound * 0.5 * ( getScaleY() - 1.0 ) ) -
				( yBound * 0.5 - getTriangleMiddleY() ) * ( getScaleY() - 1.0 ));
	}

	public double getTriangleMiddleX() {
		double firstPointX = getPoints().get(0);
		double secondPointX = getPoints().get(2);
		double thirdPointX = getPoints().get(4);
		double minX = getMin(firstPointX, secondPointX, thirdPointX);
		double maxX = getMax(firstPointX, secondPointX, thirdPointX);
		return ( maxX - minX ) / 2.0 + minX;
	}

	public double getTriangleMiddleY() {
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

	public MatOfPoint2f getMatOfPoints() {
		return new MatOfPoint2f(
				new Point(pointsProperty[0].getValue(), pointsProperty[1].getValue()),
				new Point(pointsProperty[2].getValue(), pointsProperty[3].getValue()),
				new Point(pointsProperty[4].getValue(), pointsProperty[5].getValue())
		);
	}
}
