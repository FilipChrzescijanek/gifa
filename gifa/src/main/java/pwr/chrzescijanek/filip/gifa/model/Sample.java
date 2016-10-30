package pwr.chrzescijanek.filip.gifa.model;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pwr.chrzescijanek.filip.gifa.controller.SharedState;

import java.util.List;
import java.util.stream.Collectors;

public class Sample extends BaseSample {

	Sample( final SamplesImageData imageData, final double x, final double y, final double width, final double height,
			final SharedState state, final PanelViewFactory panelViewFactory, final double xBound, final double yBound ) {
		super(imageData, x, y, width, height, state, panelViewFactory, xBound, yBound);
		overwriteListeners();
	}

	private void overwriteListeners() {
		sampleArea.setOnMousePressed(this::setStart);
		setOnMousePressed(this::setStart);
		sampleArea.setOnMouseDragged(this::resize);
		setOnMouseDragged(this::resize);
	}

	private void setStart( final MouseEvent event ) {
		if ( event.isControlDown() ) {
			setStartPosition(event);
		} else {
			final int index = getIndexOf();
			state.samplesImages.values()
					.forEach(img -> img.samples.get(index).setStartPosition(event));
		}
	}

	private void resize( final MouseEvent event ) {
		if ( event.isControlDown() ) {
			resizeAndPlace(event);
		} else {
			final int index = getIndexOf();
			state.samplesImages.values()
					.forEach(img -> img.samples.get(index).resizeAndPlace(event));
		}
	}

	@Override
	int getIndexOf() {
		return ((SamplesImageData) imageData).samples.indexOf(this);
	}

	@Override
	void handleSingleClick( final MouseEvent event ) {
		if ( event.isAltDown() ) state.zoom = true;
		state.selectedSample.set(this);
		((SamplesImageData) imageData).samples.forEach(s -> s.setVisible(false));
		setVisible(true);
	}

	@Override
	void handleDoubleClick( final MouseEvent event ) {
		if ( event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() > 1 ) {
			String viewPath = "/static/gifa-panel.fxml";
			String info = "Drag to move the sample.";
			final int index = getIndexOf();
			String title = "Sample #" + (index + 1);
			final List< SamplePanelView > samplePanelViews = state.samplesImages.values().stream()
					.map(img -> panelViewFactory.createSamplePanelView(img.image, img.samples.get(index)))
					.collect(Collectors.toList());
			final Stage newStage = getNewStage(viewPath, info, title, samplePanelViews);
			addCloseListeners(newStage);
		}
	}

	private void addCloseListeners( final Stage newStage ) {
		((SamplesImageData) imageData).samples.addListener((ListChangeListener< ? super BaseSample >) c -> {
			while ( c.next() )
				if ( c.wasRemoved() && c.getRemoved().contains(this) )
					newStage.close();
		});
		state.samplesImages.addListener(
				(MapChangeListener< ? super String, ? super SamplesImageData >) c -> newStage.close()
		);
	}
}
