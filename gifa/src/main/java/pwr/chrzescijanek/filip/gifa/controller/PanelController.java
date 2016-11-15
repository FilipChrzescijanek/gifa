package pwr.chrzescijanek.filip.gifa.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import pwr.chrzescijanek.filip.gifa.model.panel.PanelView;
import pwr.chrzescijanek.filip.gifa.util.SharedState;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Panel controller class.
 */
public class PanelController extends BaseController implements Initializable {

    //FXML fields

    @FXML
    private BorderPane root;

    @FXML
    private GridPane panelGrid;

    @FXML
    private HBox panelControls;

    @FXML
    private HBox panelHBox;

    @FXML
    private Label panelColumnsLabel;

    @FXML
    private Label panelInfo;

    @FXML
    private ResourceBundle resources;

    @FXML
    private ScrollPane panelGridScrollPane;

    @FXML
    private TextField panelColumnsTextField;

    @FXML
    private URL location;


    //FXML fields getters

    /**
     * @return root view
     */
    public BorderPane getRoot() {
        return root;
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
     * @return panel controls horizontal box
     */
    public HBox getPanelControls() {
        return panelControls;
    }

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
     * @return panel's scroll pane containing grid pane
     */
    public ScrollPane getPanelGridScrollPane() {
        return panelGridScrollPane;
    }

    /**
     * @return panel's grid pane
     */
    public GridPane getPanelGrid() {
        return panelGrid;
    }


    //fields

    /**
     * Panel views list.
     */
    protected List<? extends PanelView> panelViews = null;

    //setters

    /**
     * Sets panel views.
     * @param panelViews panel views
     */
    public void setPanelViews(final List<? extends PanelView> panelViews) {
        this.panelViews = panelViews;
        addBoundsChangedListeners();
    }

    private void addBoundsChangedListeners() {
        for (ImageView iv : this.panelViews) {
            bindSize(iv, panelGridScrollPane);
        }
    }

    /**
     * Sets info label.
     * @param info label content
     */
    public void setInfo(String info) {
        panelInfo.setText(info);
    }

    /**
     * Constructs new PanelController with given shared state.
     * @param state shared state
     */
    @Inject
    public PanelController(final SharedState state) {
        super(state);
    }

    /**
     * Refreshes panel.
     */
    public void refresh() {
        calculateColumnsAndRows(panelColumnsTextField, panelViews);
        placeNodes(panelViews, panelGrid);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //FXML fields load assertions
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'gifa-panel.fxml'.";
        assert panelHBox != null : "fx:id=\"panelHBox\" was not injected: check your FXML file 'gifa-panel.fxml'.";
        assert panelInfo != null : "fx:id=\"panelInfo\" was not injected: check your FXML file 'gifa-panel.fxml'.";
        assert panelControls != null : "fx:id=\"panelControls\" was not injected: check your FXML file 'gifa-panel.fxml'.";
        assert panelColumnsLabel != null : "fx:id=\"panelColumnsLabel\" was not injected: check your FXML file 'gifa-panel.fxml'.";
        assert panelColumnsTextField != null : "fx:id=\"panelColumnsTextField\" was not injected: check your FXML file 'gifa-panel.fxml'.";
        assert panelGridScrollPane != null : "fx:id=\"panelGridScrollPane\" was not injected: check your FXML file 'gifa-panel.fxml'.";
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

}
