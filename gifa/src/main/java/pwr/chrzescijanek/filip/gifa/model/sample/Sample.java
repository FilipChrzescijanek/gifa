package pwr.chrzescijanek.filip.gifa.model.sample;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import pwr.chrzescijanek.filip.gifa.model.image.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.panel.PanelViewFactory;
import pwr.chrzescijanek.filip.gifa.model.panel.SamplePanelView;
import pwr.chrzescijanek.filip.gifa.util.SharedState;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static pwr.chrzescijanek.filip.gifa.model.sample.BasicSample.SampleSelection.NIL;
import static pwr.chrzescijanek.filip.gifa.util.StageUtils.getNewStage;

/**
 * Represents user-defined sample.
 */
public class Sample extends BasicSample {

	private final DoubleProperty semimajor = new SimpleDoubleProperty();

	private final DoubleProperty semiminor = new SimpleDoubleProperty();

	private double constantAngle = 0;

	/**
	 * Constructs a new Sample on given image, with given position, size, shared state,
	 * panel views factory and bounds.
	 *
	 * @param imageData        image
	 * @param x                X position
	 * @param y                Y position
	 * @param radiusX          X radius
	 * @param radiusY          Y radius
	 * @param state            shared state
	 * @param panelViewFactory panel views factory
	 * @param xBound           X bound
	 * @param yBound           Y bound
	 */
	protected Sample(final SamplesImageData imageData, final double x, final double y, final double radiusX,
	                 final double radiusY, final SharedState state, final PanelViewFactory panelViewFactory,
	                 final double xBound, final double yBound) {
		super(imageData, x, y, radiusX, radiusY, state, panelViewFactory, xBound, yBound);
		bindAxes();
		overwriteListeners();
	}

	private void bindAxes() {
		bindSemimajor();
		bindSemiminor();
	}

	private void overwriteListeners() {
		sampleArea.setOnMousePressed(this::setStart);
		setOnMousePressed(this::setStart);
		sampleArea.setOnMouseDragged(this::resize);
		setOnMouseDragged(this::resize);
		onMouseScrolled();
		setOnRotate();
	}

	private void bindSemimajor() {
		semimajor.bind(new DoubleBinding() {
			{
				super.bind(radiusXProperty(), radiusYProperty());
			}

			@Override
			protected double computeValue() {
				return max(getRadiusX(), getRadiusY());
			}
		});
	}

	private void bindSemiminor() {
		semiminor.bind(new DoubleBinding() {
			{
				super.bind(radiusXProperty(), radiusYProperty());
			}

			@Override
			protected double computeValue() {
				return min(getRadiusX(), getRadiusY());
			}
		});
	}

	private void onMouseScrolled() {
		setOnScroll(event -> {
			if (isSelected() && state.rotate.get())
				rotate(event);
		});
	}

	private void setOnRotate() {
		state.rotate.addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				sampleSelection = NIL;
				if (getRadiusX() < getRadiusY()) constantAngle = 90;
				rotateBindings();
				recalculateTranslates();
			}
			else {
				constantAngle = 0;
				setRotate(0);
				defaultBindings();
				recalculateTranslates();
			}
		});
	}

	/**
	 * Handles sample rotation based on given scroll event.
	 *
	 * @param event scroll event
	 */
	public void rotate(final ScrollEvent event) {
		if (event.isControlDown()) {
			updateRotation(event);
			event.consume();
		}
		else {
			final int index = getIndexOf();
			state.samplesImages.values()
			                   .forEach(img -> img.samples.get(index).updateRotation(event));
			event.consume();
		}
	}

	/**
	 * Sets rotate mode property bindings.
	 */
	protected void rotateBindings() {
		bindX();
		bindY();
		sampleArea.widthProperty().bind((this.centerXProperty().subtract(sampleArea.xProperty())).multiply(2.0));
		sampleArea.heightProperty().bind((this.centerYProperty().subtract(sampleArea.yProperty())).multiply(2.0));
	}

	/**
	 * Updates sample rotation angle based on given scroll event.
	 *
	 * @param event scroll event
	 */
	public void updateRotation(final ScrollEvent event) {
		final double deltaY = event.getDeltaY();
		final double rotate = getRotate();
		if (deltaY > 0) {
			double nextValue = (rotate - 1) % 180;
			if (Math.abs(nextValue) < 0.00001) nextValue = 0;
			checkIfCanRotate(nextValue);
		}
		else {
			double nextValue = (rotate + 1) % 180;
			if (Math.abs(nextValue) < 0.00001) nextValue = 0;
			checkIfCanRotate(nextValue);
		}
	}

	private void bindX() {
		sampleArea.xProperty().bind(new DoubleBinding() {
			{
				super.bind(semimajor, semiminor, centerXProperty(), centerYProperty(), rotateProperty());
			}

			@Override
			protected double computeValue() {
				final double phi = (constantAngle + getRotate()) / 180.0 * PI;
				return calculateX(phi);
			}
		});
	}

	private void bindY() {
		sampleArea.yProperty().bind(new DoubleBinding() {
			{
				super.bind(semimajor, semiminor, centerXProperty(), centerYProperty(), rotateProperty());
			}

			@Override
			protected double computeValue() {
				final double phi = (constantAngle + getRotate()) / 180.0 * PI;
				return calculateY(phi);
			}
		});
	}

	private void checkIfCanRotate(final double nextValue) {
		final double phi = (constantAngle + nextValue) / 180.0 * PI;
		final double nextX = calculateX(phi);
		final double nextY = calculateY(phi);
		final double nextWidth = (getCenterX() - nextX) * 2;
		final double nextHeight = (getCenterY() - nextY) * 2;
		if (nextX >= 0 && nextY >= 0 && nextX + nextWidth <= imageData.image.get().getWidth()
		    && nextY + nextHeight <= imageData.image.get().getHeight()) {
			setRotate(nextValue);
			this.autosize();
		}
	}

	private double calculateX(final double phi) {
		final double h = getCenterX();
		final double b = semiminor.get();
		final double a = semimajor.get();
		final double tangent = -b * tan(phi) / a;
		final double t = atan(tangent) - (phi < -PI / 2 || phi > PI / 2 ? 0 : PI);
		return h + a * cos(t) * cos(phi) - b * sin(t) * sin(phi);
	}

	private double calculateY(final double phi) {
		final double k = getCenterY();
		final double b = semiminor.get();
		final double a = semimajor.get();
		final double tangent = b * (1 / tan(phi)) / a;
		final double t = atan(tangent) - (phi < 0.0 || phi > PI ? 0 : PI);
		return k + b * sin(t) * cos(phi) + a * cos(t) * sin(phi);
	}

	@Override
	protected void bindProperties() {
		scaleXProperty().bind(imageData.scale);
		sampleArea.visibleProperty().bind(Bindings.equal(state.selectedSample, this));
	}

	@Override
	protected void handleSingleClick(final MouseEvent event) {
		state.selectedSample.set(this);
		if (event.isAltDown()) state.zoomSample.set(true);
	}

	@Override
	protected void handleDoubleClick(final MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() > 1) {
			final String viewPath = "/static/gifa-panel.fxml";
			final String info = "Drag to move the sample.";
			final int index = getIndexOf();
			final String title = "Sample #" + (index + 1);
			final List<SamplePanelView> samplePanelViews =
					state.samplesImages.values().stream()
					                   .map(img -> panelViewFactory
							                   .createSamplePanelView(img.image.get(), img.samples.get(index)))
					                   .collect(Collectors.toList());
			final Stage newStage = getNewStage(viewPath, info, title, samplePanelViews);
			addCloseListeners(newStage);
		}
	}

	@Override
	public int getIndexOf() {
		return ((SamplesImageData) imageData).samples.indexOf(this);
	}

	private void addCloseListeners(final Stage newStage) {
		((SamplesImageData) imageData).samples.addListener((ListChangeListener<? super BasicSample>) c -> {
			while (c.next())
				if (c.wasRemoved() && c.getRemoved().contains(this))
					newStage.close();
		});
		state.samplesImages.addListener(
				(MapChangeListener<? super String, ? super SamplesImageData>) c -> Platform.runLater(newStage::close)
		                               );
	}

	private void setStart(final MouseEvent event) {
		if (event.isControlDown()) {
			setStartPosition(event);
		}
		else {
			final int index = getIndexOf();
			state.samplesImages.values()
			                   .forEach(img -> img.samples.get(index).setStartPosition(event));
		}
	}

	private void resize(final MouseEvent event) {
		if (event.isControlDown()) {
			resizeAndPlace(event);
		}
		else {
			final int index = getIndexOf();
			final double deltaX = updateStartX(event);
			final double deltaY = updateStartY(event);
			state.samplesImages.values()
			                   .forEach(img -> img.samples.get(index).resizeAndPlace(deltaX, deltaY));
		}
	}

}
