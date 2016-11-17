package pwr.chrzescijanek.filip.gifa.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import pwr.chrzescijanek.filip.gifa.util.SharedState;
import pwr.chrzescijanek.filip.gifa.view.compare.CompareView;
import pwr.chrzescijanek.filip.gifa.view.compare.CompareViewFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Compare view controller class.
 */
public class CompareViewController extends BaseController implements Initializable {

	/**
	 * Compare views factory.
	 */
	protected final CompareViewFactory compareViewFactory;

	/**
	 * Compare views list.
	 */
	protected List<? extends CompareView> compareViews = null;

	@FXML
	private URL location;

	@FXML
	private Label compareViewColumnsLabel;

	@FXML
	private TextField compareViewColumnsTextField;

	@FXML
	private HBox compareViewControls;

	@FXML
	private GridPane compareViewGrid;

	@FXML
	private ScrollPane compareViewGridScrollPane;

	@FXML
	private HBox compareViewHBox;

	@FXML
	private Label compareViewInfo;

	@FXML
	private ResourceBundle resources;

	@FXML
	private BorderPane root;

	/**
	 * Constructs new CompareViewController with given shared state and compare views factory.
	 *
	 * @param state              shared state
	 * @param compareViewFactory compare views factory
	 */
	@Inject
	public CompareViewController(final SharedState state, final CompareViewFactory compareViewFactory) {
		super(state);
		this.compareViewFactory = compareViewFactory;
	}

	/**
	 * @return location
	 */
	public URL getLocation() { return location; }

	/**
	 * @return max. columns label
	 */
	public Label getCompareViewColumnsLabel() {
		return compareViewColumnsLabel;
	}

	/**
	 * @return max. columns text field
	 */
	public TextField getCompareViewColumnsTextField() {
		return compareViewColumnsTextField;
	}

	/**
	 * @return compare view's controls horizontal box
	 */
	public HBox getCompareViewControls() {
		return compareViewControls;
	}

	/**
	 * @return compare view's grid pane
	 */
	public GridPane getCompareViewGrid() {
		return compareViewGrid;
	}

	/**
	 * @return compare view's scroll pane containing grid pane
	 */
	public ScrollPane getCompareViewGridScrollPane() {
		return compareViewGridScrollPane;
	}

	/**
	 * @return compare view's label horizontal box
	 */
	public HBox getCompareViewHBox() {
		return compareViewHBox;
	}

	/**
	 * @return compare view info label
	 */
	public Label getCompareViewInfo() {
		return compareViewInfo;
	}

	/**
	 * @return resources
	 */
	public ResourceBundle getResources() { return resources; }

	/**
	 * @return root view
	 */
	public BorderPane getRoot() {
		return root;
	}

	/**
	 * Creates and sets compare views of vertex with given index.
	 *
	 * @param vertexIndex vertex index
	 */
	public void setVertexCompareViews(final int vertexIndex) {
		this.compareViews = state.imagesToAlign
				.values().stream().map(img -> compareViewFactory
						.createVertexPanelView(img.writableImage.get(), img.vertices[vertexIndex]))
				.collect(Collectors.toList());
		addBoundsChangedListeners();
	}

	private void addBoundsChangedListeners() {
		for (final CompareView view : this.compareViews) {
			bindSize(view, compareViewGridScrollPane);
		}
	}

	/**
	 * Creates and sets compare views of sample with given index.
	 *
	 * @param sampleIndex sample index
	 */
	public void setSampleCompareViews(final int sampleIndex) {
		this.compareViews = state.samplesImages
				.values().stream().map(img -> compareViewFactory
						.createSamplePanelView(img.image.get(), img.samples.get(sampleIndex)))
				.collect(Collectors.toList());
		addBoundsChangedListeners();
	}

	/**
	 * Sets info label.
	 *
	 * @param info label content
	 */
	public void setInfo(final String info) {
		compareViewInfo.setText(info);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		//FXML fields load assertions
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'gifa-compare.fxml'.";
		assert compareViewHBox != null : "fx:id=\"compareViewHBox\" was not injected: check your FXML file "
		                                 + "'gifa-compare.fxml'.";
		assert compareViewInfo != null : "fx:id=\"compareViewInfo\" was not injected: check your FXML file "
		                                 + "'gifa-compare.fxml'.";
		assert compareViewControls != null :
				"fx:id=\"compareViewControls\" was not injected: check your FXML file 'gifa-compare.fxml'.";
		assert compareViewColumnsLabel != null :
				"fx:id=\"compareViewColumnsLabel\" was not injected: check your FXML file 'gifa-compare.fxml'.";
		assert compareViewColumnsTextField != null :
				"fx:id=\"compareViewColumnsTextField\" was not injected: check your FXML file 'gifa-compare.fxml'.";
		assert compareViewGridScrollPane != null :
				"fx:id=\"compareViewGridScrollPane\" was not injected: check your FXML file 'gifa-compare.fxml'.";
		assert compareViewGrid != null : "fx:id=\"compareViewGrid\" was not injected: check your FXML file "
		                                 + "'gifa-compare.fxml'.";

		this.location = location;
		this.resources = resources;
		bindTextFieldsValidators();
		setVisibilityBindings();
		injectStylesheets(root);
	}

	private void bindTextFieldsValidators() {
		compareViewColumnsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d+"))
				compareViewColumnsTextField.setText(oldValue);
			else
				refresh();
		});
	}

	private void setVisibilityBindings() {
		compareViewHBox.visibleProperty().bind(compareViewInfo.textProperty().isNotNull());
	}

	/**
	 * Refreshes compare view.
	 */
	public void refresh() {
		calculateColumnsAndRows(compareViewColumnsTextField, compareViews);
		placeNodes(compareViews, compareViewGrid);
	}

}
