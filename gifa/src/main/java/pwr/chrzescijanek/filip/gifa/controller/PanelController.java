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
import pwr.chrzescijanek.filip.gifa.view.panel.PanelView;
import pwr.chrzescijanek.filip.gifa.view.panel.PanelViewFactory;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Panel controller class.
 */
public class PanelController extends BaseController implements Initializable {

	/**
	 * Panel views factory.
	 */
	protected final PanelViewFactory panelViewFactory;

	/**
	 * Panel views list.
	 */
	protected List<? extends PanelView> panelViews = null;

	@FXML
	private URL location;

	@FXML
	private Label panelColumnsLabel;

	@FXML
	private TextField panelColumnsTextField;

	@FXML
	private HBox panelControls;

	@FXML
	private GridPane panelGrid;

	@FXML
	private ScrollPane panelGridScrollPane;

	@FXML
	private HBox panelHBox;

	@FXML
	private Label panelInfo;

	@FXML
	private ResourceBundle resources;

	@FXML
	private BorderPane root;

	/**
	 * Constructs new PanelController with given shared state.
	 *
	 * @param state            shared state
	 * @param panelViewFactory panel views factory
	 */
	@Inject
	public PanelController(final SharedState state, final PanelViewFactory panelViewFactory) {
		super(state);
		this.panelViewFactory = panelViewFactory;
	}

	/**
	 * @return location
	 */
	public URL getLocation() { return location; }

	/**
	 * @return max. columns label
	 */
	public Label getPanelColumnsLabel() {
		return panelColumnsLabel;
	}

	/**
	 * @return max. columns text field
	 */
	public TextField getPanelColumnsTextField() {
		return panelColumnsTextField;
	}

	/**
	 * @return panel controls horizontal box
	 */
	public HBox getPanelControls() {
		return panelControls;
	}

	/**
	 * @return panel's grid pane
	 */
	public GridPane getPanelGrid() {
		return panelGrid;
	}

	/**
	 * @return panel's scroll pane containing grid pane
	 */
	public ScrollPane getPanelGridScrollPane() {
		return panelGridScrollPane;
	}

	/**
	 * @return panel label horizontal box
	 */
	public HBox getPanelHBox() {
		return panelHBox;
	}

	/**
	 * @return panel info label
	 */
	public Label getPanelInfo() {
		return panelInfo;
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
	 * Creates and sets panel views of vertex with given index.
	 *
	 * @param vertexIndex vertex index
	 */
	public void setVertexPanelViews(final int vertexIndex) {
		this.panelViews = state.imagesToAlign
				.values().stream().map(img -> panelViewFactory
						.createVertexPanelView(img.writableImage.get(), img.vertices[vertexIndex]))
				.collect(Collectors.toList());
		addBoundsChangedListeners();
	}

	private void addBoundsChangedListeners() {
		for (final PanelView view : this.panelViews) {
			bindSize(view, panelGridScrollPane);
		}
	}

	/**
	 * Creates and sets panel views of sample with given index.
	 *
	 * @param sampleIndex sample index
	 */
	public void setSamplePanelViews(final int sampleIndex) {
		this.panelViews = state.samplesImages
				.values().stream().map(img -> panelViewFactory
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
		panelInfo.setText(info);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		//FXML fields load assertions
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'gifa-panel.fxml'.";
		assert panelHBox != null : "fx:id=\"panelHBox\" was not injected: check your FXML file 'gifa-panel.fxml'.";
		assert panelInfo != null : "fx:id=\"panelInfo\" was not injected: check your FXML file 'gifa-panel.fxml'.";
		assert panelControls != null :
				"fx:id=\"panelControls\" was not injected: check your FXML file 'gifa-panel.fxml'.";
		assert panelColumnsLabel != null :
				"fx:id=\"panelColumnsLabel\" was not injected: check your FXML file 'gifa-panel.fxml'.";
		assert panelColumnsTextField != null :
				"fx:id=\"panelColumnsTextField\" was not injected: check your FXML file 'gifa-panel.fxml'.";
		assert panelGridScrollPane != null :
				"fx:id=\"panelGridScrollPane\" was not injected: check your FXML file 'gifa-panel.fxml'.";
		assert panelGrid != null : "fx:id=\"panelGrid\" was not injected: check your FXML file 'gifa-panel.fxml'.";

		this.location = location;
		this.resources = resources;
		bindTextFieldsValidators();
		setVisibilityBindings();
		injectStylesheets(root);
	}

	private void bindTextFieldsValidators() {
		panelColumnsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d+"))
				panelColumnsTextField.setText(oldValue);
			else
				refresh();
		});
	}

	private void setVisibilityBindings() {
		panelHBox.visibleProperty().bind(panelInfo.textProperty().isNotNull());
	}

	/**
	 * Refreshes panel.
	 */
	public void refresh() {
		calculateColumnsAndRows(panelColumnsTextField, panelViews);
		placeNodes(panelViews, panelGrid);
	}

}
