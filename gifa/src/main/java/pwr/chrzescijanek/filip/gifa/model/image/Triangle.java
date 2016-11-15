package pwr.chrzescijanek.filip.gifa.model.image;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

/**
 * Represents a triangle.
 */
public class Triangle extends Polygon {

	/**
	 * Triangle stroke style.
	 */
	protected static final String STROKE_STYLE = "-fx-stroke-type: inside;\n" +
	                                             "    -fx-stroke-width: 2;\n" +
	                                             "    -fx-stroke-dash-array: 12 2 4 2;\n" +
	                                             "    -fx-stroke-dash-offset: 6;\n" +
	                                             "    -fx-stroke-line-cap: butt;";

	/**
	 * Vertices coordinates.
	 */
	public final DoubleProperty[] pointsProperty;

	private final double xBound;

	private final double yBound;

	/**
	 * Constructs a new Triangle with given X and Y bounds.
	 *
	 * @param xBound X bound
	 * @param yBound Y bound
	 */
	public Triangle(final double xBound, final double yBound) {
		super(0, 0, 0, 0, 0, 0);
		this.xBound = xBound;
		this.yBound = yBound;
		this.pointsProperty = new DoubleProperty[6];
		initializePointsWithZeros();
		initializeListenersAndStyle();
	}

	/**
	 * Constructs a new Triangle with given vertices and X and Y bounds.
	 *
	 * @param p1     first vertex
	 * @param p2     second vertex
	 * @param p3     third vertex
	 * @param xBound X bound
	 * @param yBound Y bound
	 */
	public Triangle(final Point p1, final Point p2, final Point p3, final double xBound, final double yBound) {
		super(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
		this.xBound = xBound;
		this.yBound = yBound;
		this.pointsProperty = new DoubleProperty[6];
		initializePoints();
		initializeListenersAndStyle();
	}

	/**
	 * @return matrix of triangle vertices
	 */
	public MatOfPoint2f getMatOfVertices() {
		return new MatOfPoint2f(
				new Point(pointsProperty[0].getValue(), pointsProperty[1].getValue()),
				new Point(pointsProperty[2].getValue(), pointsProperty[3].getValue()),
				new Point(pointsProperty[4].getValue(), pointsProperty[5].getValue())
		);
	}

	private void initializePointsWithZeros() {
		for (int i = 0; i < pointsProperty.length; i++)
			pointsProperty[i] = new SimpleDoubleProperty(0.0);
	}

	private void initializePoints() {
		final ObservableList<Double> points = getPoints();
		for (int i = 0; i < pointsProperty.length; i++)
			pointsProperty[i] = new SimpleDoubleProperty(points.get(i));
	}

	private void initializeListenersAndStyle() {
		registerPointsListeners();
		registerScaleListeners();
		setStyle();
	}

	private void registerPointsListeners() {
		for (int i = 0; i < pointsProperty.length; i++) {
			final int index = i;
			pointsProperty[index].addListener((observable, oldValue, newValue) -> {
				final double value = newValue.doubleValue();
				getPoints().set(index, value);
				if (index % 2 == 0) recalculateTranslateX();
				else recalculateTranslateY();
			});
		}
	}

	private void registerScaleListeners() {
		scaleXProperty().addListener((observable, oldValue, newValue) -> {
			setScaleY(newValue.doubleValue());
			recalculateTranslates();
		});
	}

	private void recalculateTranslates() {
		recalculateTranslateX();
		recalculateTranslateY();
	}

	private void recalculateTranslateX() {
		setTranslateX((xBound * 0.5 * (getScaleX() - 1.0)) -
		              (xBound * 0.5 - getTriangleMiddleX()) * (getScaleX() - 1.0));
	}

	private void recalculateTranslateY() {
		setTranslateY((yBound * 0.5 * (getScaleY() - 1.0)) -
		              (yBound * 0.5 - getTriangleMiddleY()) * (getScaleY() - 1.0));
	}

	private double getTriangleMiddleX() {
		final double firstPointX = getPoints().get(0);
		final double secondPointX = getPoints().get(2);
		final double thirdPointX = getPoints().get(4);
		final double minX = getMin(firstPointX, secondPointX, thirdPointX);
		final double maxX = getMax(firstPointX, secondPointX, thirdPointX);
		return (maxX - minX) / 2.0 + minX;
	}

	private double getTriangleMiddleY() {
		final double firstPointY = getPoints().get(1);
		final double secondPointY = getPoints().get(3);
		final double thirdPointY = getPoints().get(5);
		final double minY = getMin(firstPointY, secondPointY, thirdPointY);
		final double maxY = getMax(firstPointY, secondPointY, thirdPointY);
		return (maxY - minY) / 2.0 + minY;
	}

	private double getMin(final double first, final double second, final double third) {
		return Math.min(first, Math.min(second, third));
	}

	private double getMax(final double first, final double second, final double third) {
		return Math.max(first, Math.max(second, third));
	}

	private void setStyle() {
		setFill(Color.color(1.0, 1.0, 1.0, 0.35));
		setStroke(Color.color(1.0, 1.0, 1.0, 0.6));
		setStyle(STROKE_STYLE);
	}

}
