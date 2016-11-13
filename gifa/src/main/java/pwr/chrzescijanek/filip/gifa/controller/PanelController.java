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

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    public BorderPane getRoot() {
        return root;
    }

    public HBox getPanelHBox() {
        return panelHBox;
    }

    public Label getPanelInfo() {
        return panelInfo;
    }

    public HBox getPanelControls() {
        return panelControls;
    }

    public Label getPanelColumnsLabel() {
        return panelColumnsLabel;
    }

    public TextField getPanelColumnsTextField() {
        return panelColumnsTextField;
    }

    public ScrollPane getPanelGridScrollPane() {
        return panelGridScrollPane;
    }

    public GridPane getPanelGrid() {
        return panelGrid;
    }


    //fields
    private List<? extends PanelView> imageViews = null;

    //setters

    public void setImageViews(final List<? extends PanelView> imageViews) {
        this.imageViews = imageViews;
        addBoundsChangedListeners();
    }

    private void addBoundsChangedListeners() {
        for (ImageView iv : this.imageViews) {
            bindSize(iv, panelGridScrollPane);
        }
    }

    public void setInfo(String info) {
        panelInfo.setText(info);
    }


    @Inject
    public PanelController(final SharedState state) {
        super(state);
    }

    public void placeImages() {
        calculateColumnsAndRows(panelColumnsTextField, imageViews);
        placeNodes(imageViews, panelGrid);
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
                placeImages();
        });
    }

    private void setVisibilityBindings() {
        panelHBox.visibleProperty().bind(panelInfo.textProperty().isNotNull());
    }

}
