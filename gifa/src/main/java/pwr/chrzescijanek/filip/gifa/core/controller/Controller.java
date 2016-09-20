package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import pwr.chrzescijanek.filip.gifa.Main;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Controller {

	public static final String THEME_PREFERENCE_KEY = "gifa.theme";
	public static final String THEME_DARK = "/theme-dark.css";
	public static final String THEME_LIGHT = "/theme-light.css";

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private VBox root;

	@FXML
	private MenuBar menuBar;

	@FXML
	private Menu fileMenu;

	@FXML
	private MenuItem fileMenuLoadImages;

	@FXML
	private MenuItem fileMenuExportToCsv;

	@FXML
	private MenuItem fileMenuExit;

	@FXML
	private Menu optionsMenu;

	@FXML
	private Menu optionsMenuTheme;

	@FXML
	private RadioMenuItem optionsMenuThemeLight;

	@FXML
	private ToggleGroup theme;

	@FXML
	private RadioMenuItem optionsMenuThemeDark;

	@FXML
	private Menu helpMenu;

	@FXML
	private MenuItem helpMenuAbout;

	@FXML
	private TabPane mainTabPane;

	@FXML
	private Tab imagesTab;

	@FXML
	private GridPane imagesMainPane;

	@FXML
	private GridPane imagesListGrid;

	@FXML
	private ListView< ? > imagesList;

	@FXML
	private Button loadImagesButton;

	@FXML
	private BorderPane centerBorderPane;

	@FXML
	private ScrollPane imageScrollPane;

	@FXML
	private AnchorPane imageViewAnchor;

	@FXML
	private ImageView imageView;

	@FXML
	private HBox centerVBoxHBox;

	@FXML
	private Label scaleLabel;

	@FXML
	private Label mousePositionLabel;

	@FXML
	private VBox rightVBox;

	@FXML
	private TabPane rightVBoxTabPane;

	@FXML
	private Tab toolboxTab;

	@FXML
	private VBox toolboxVBox;

	@FXML
	private StackPane toolboxStackPane;

	@FXML
	private GridPane triangleGroupGrid;

	@FXML
	private Label triangleGroupLabel;

	@FXML
	private Label firstPointXLabel;

	@FXML
	private Label secondPointXLabel;

	@FXML
	private Label thirdPointXLabel;

	@FXML
	private Label firstPointYLabel;

	@FXML
	private Label secondPointYLabel;

	@FXML
	private Label thirdPointYLabel;

	@FXML
	private TextField firstPointXTextField;

	@FXML
	private TextField firstPointYTextField;

	@FXML
	private TextField secondPointXTextField;

	@FXML
	private TextField secondPointYTextField;

	@FXML
	private TextField thirdPointXTextField;

	@FXML
	private TextField thirdPointYTextField;

	@FXML
	private GridPane rectangleGroupGrid;

	@FXML
	private Label rectangleGroupLabel;

	@FXML
	private Label rectangleXLabel;

	@FXML
	private Label rectangleWidthLabel;

	@FXML
	private Label rectangleYLabel;

	@FXML
	private Label rectangleHeightLabel;

	@FXML
	private TextField rectangleXTextField;

	@FXML
	private TextField rectangleYTextField;

	@FXML
	private TextField rectangleWidthTextField;

	@FXML
	private TextField rectangleHeightTexfField;

	@FXML
	private HBox methodHBox;

	@FXML
	private RadioButton triangleRadioButton;

	@FXML
	private ToggleGroup method;

	@FXML
	private RadioButton rectangleRadioButton;

	@FXML
	private Label flipLabel;

	@FXML
	private ToolBar flipToolbar;

	@FXML
	private Button verticalFlipButton;

	@FXML
	private Button horizontalFlipButton;

	@FXML
	private Label rotateLabel;

	@FXML
	private ToolBar rotateToolbar;

	@FXML
	private Button rotateLeftButton;

	@FXML
	private Button rotateRightButton;

	@FXML
	private Tab featuresTab;

	@FXML
	private BorderPane featuresBorderPane;

	@FXML
	private ScrollPane featuresScrollPane;

	@FXML
	private VBox featuresVBox;

	@FXML
	private HBox selectionButtonsHBox;

	@FXML
	private Button selectAllButton;

	@FXML
	private Button deselectAllButton;

	@FXML
	private Button resultsButton;

	@FXML
	private Tab resultsTab;

	@FXML
	private BorderPane resultsMainPane;

	@FXML
	private ScrollPane resultsScrollPane;

	@FXML
	private GridPane resultsGrid;

	@FXML
	private HBox resultsControls;

	@FXML
	private Label resultsColumnsLabel;

	@FXML
	private TextField resultsColumnsTextField;

	@FXML
	void about( ActionEvent event ) {

	}

	@FXML
	void applyDarkTheme( ActionEvent event ) {
		applyTheme(THEME_DARK);
	}

	@FXML
	void applyLightTheme( ActionEvent event ) {
		applyTheme(THEME_LIGHT);
	}

	public ToggleGroup getThemeToggleGroup() {
		return theme;
	}

	public Toggle getDarkThemeToggle() {
		return optionsMenuThemeDark;
	}

	public Toggle getLightThemeToggle() {
		return optionsMenuThemeLight;
	}

	private void applyTheme( final String theme ) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		prefs.put(THEME_PREFERENCE_KEY, theme);
		ObservableList< String > stylesheets = root.getScene().getStylesheets();
		stylesheets.clear();
		stylesheets.add(theme);
	}

	@FXML
	void columnsChanged( ActionEvent event ) {

	}

	@FXML
	void deselectAllFunctions( ActionEvent event ) {

	}

	@FXML
	void exit( ActionEvent event ) {
		root.getScene().getWindow().hide();
	}

	@FXML
	void exportToCsv( ActionEvent event ) {

	}

	@FXML
	void firstPointXChanged( ActionEvent event ) {

	}

	@FXML
	void firstPointYChanged( ActionEvent event ) {

	}

	@FXML
	void flipHorizontal( ActionEvent event ) {

	}

	@FXML
	void flipVertical( ActionEvent event ) {

	}

	@FXML
	void getResults( ActionEvent event ) {
		mainTabPane.getSelectionModel().select(resultsTab);
	}

	@FXML
	void loadImages( ActionEvent event ) {

	}

	@FXML
	void rectangleHeightChanged( ActionEvent event ) {

	}

	@FXML
	void rectangleWidthChanged( ActionEvent event ) {

	}

	@FXML
	void rectangleXChanged( ActionEvent event ) {

	}

	@FXML
	void rectangleYChanged( ActionEvent event ) {

	}

	@FXML
	void rotateLeft( ActionEvent event ) {

	}

	@FXML
	void rotateRight( ActionEvent event ) {

	}

	@FXML
	void secondPointXChanged( ActionEvent event ) {

	}

	@FXML
	void secondPointYChanged( ActionEvent event ) {

	}

	@FXML
	void selectAllFunctions( ActionEvent event ) {

	}

	@FXML
	void showRectangle( ActionEvent event ) {

	}

	@FXML
	void showTriangle( ActionEvent event ) {

	}

	@FXML
	void thirdPointXChanged( ActionEvent event ) {

	}

	@FXML
	void thirdPointYChanged( ActionEvent event ) {

	}

	public void setImage( final Image img ) {
		imageView.setImage(img);
	}

	@FXML
	void initialize() {
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'sample.fxml'.";
		assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'sample.fxml'.";
		assert fileMenu != null : "fx:id=\"fileMenu\" was not injected: check your FXML file 'sample.fxml'.";
		assert fileMenuLoadImages != null : "fx:id=\"fileMenuLoadImages\" was not injected: check your FXML file 'sample.fxml'.";
		assert fileMenuExportToCsv != null : "fx:id=\"fileMenuExportToCsv\" was not injected: check your FXML file 'sample.fxml'.";
		assert fileMenuExit != null : "fx:id=\"fileMenuExit\" was not injected: check your FXML file 'sample.fxml'.";
		assert optionsMenu != null : "fx:id=\"optionsMenu\" was not injected: check your FXML file 'sample.fxml'.";
		assert optionsMenuTheme != null : "fx:id=\"optionsMenuTheme\" was not injected: check your FXML file 'sample.fxml'.";
		assert optionsMenuThemeLight != null : "fx:id=\"optionsMenuThemeLight\" was not injected: check your FXML file 'sample.fxml'.";
		assert theme != null : "fx:id=\"theme\" was not injected: check your FXML file 'sample.fxml'.";
		assert optionsMenuThemeDark != null : "fx:id=\"optionsMenuThemeDark\" was not injected: check your FXML file 'sample.fxml'.";
		assert helpMenu != null : "fx:id=\"helpMenu\" was not injected: check your FXML file 'sample.fxml'.";
		assert helpMenuAbout != null : "fx:id=\"helpMenuAbout\" was not injected: check your FXML file 'sample.fxml'.";
		assert mainTabPane != null : "fx:id=\"mainTabPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert imagesTab != null : "fx:id=\"imagesTab\" was not injected: check your FXML file 'sample.fxml'.";
		assert imagesMainPane != null : "fx:id=\"imagesMainPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert imagesListGrid != null : "fx:id=\"imagesListGrid\" was not injected: check your FXML file 'sample.fxml'.";
		assert imagesList != null : "fx:id=\"imagesList\" was not injected: check your FXML file 'sample.fxml'.";
		assert loadImagesButton != null : "fx:id=\"loadImagesButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert centerBorderPane != null : "fx:id=\"centerBorderPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert imageScrollPane != null : "fx:id=\"imageScrollPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert imageViewAnchor != null : "fx:id=\"imageViewAnchor\" was not injected: check your FXML file 'sample.fxml'.";
		assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'sample.fxml'.";
		assert centerVBoxHBox != null : "fx:id=\"centerVBoxHBox\" was not injected: check your FXML file 'sample.fxml'.";
		assert scaleLabel != null : "fx:id=\"scaleLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert mousePositionLabel != null : "fx:id=\"mousePositionLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert rightVBox != null : "fx:id=\"rightVBox\" was not injected: check your FXML file 'sample.fxml'.";
		assert rightVBoxTabPane != null : "fx:id=\"rightVBoxTabPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert toolboxTab != null : "fx:id=\"toolboxTab\" was not injected: check your FXML file 'sample.fxml'.";
		assert toolboxVBox != null : "fx:id=\"toolboxVBox\" was not injected: check your FXML file 'sample.fxml'.";
		assert toolboxStackPane != null : "fx:id=\"toolboxStackPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert triangleGroupGrid != null : "fx:id=\"triangleGroupGrid\" was not injected: check your FXML file 'sample.fxml'.";
		assert triangleGroupLabel != null : "fx:id=\"triangleGroupLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert firstPointXLabel != null : "fx:id=\"firstPointXLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert secondPointXLabel != null : "fx:id=\"secondPointXLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert thirdPointXLabel != null : "fx:id=\"thirdPointXLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert firstPointYLabel != null : "fx:id=\"firstPointYLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert secondPointYLabel != null : "fx:id=\"secondPointYLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert thirdPointYLabel != null : "fx:id=\"thirdPointYLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert firstPointXTextField != null : "fx:id=\"firstPointXTextField\" was not injected: check your FXML file 'sample.fxml'.";
		assert firstPointYTextField != null : "fx:id=\"firstPointYTextField\" was not injected: check your FXML file 'sample.fxml'.";
		assert secondPointXTextField != null : "fx:id=\"secondPointXTextField\" was not injected: check your FXML file 'sample.fxml'.";
		assert secondPointYTextField != null : "fx:id=\"secondPointYTextField\" was not injected: check your FXML file 'sample.fxml'.";
		assert thirdPointXTextField != null : "fx:id=\"thirdPointXTextField\" was not injected: check your FXML file 'sample.fxml'.";
		assert thirdPointYTextField != null : "fx:id=\"thirdPointYTextField\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleGroupGrid != null : "fx:id=\"rectangleGroupGrid\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleGroupLabel != null : "fx:id=\"rectangleGroupLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleXLabel != null : "fx:id=\"rectangleXLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleWidthLabel != null : "fx:id=\"rectangleWidthLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleYLabel != null : "fx:id=\"rectangleYLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleHeightLabel != null : "fx:id=\"rectangleHeightLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleXTextField != null : "fx:id=\"rectangleXTextField\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleYTextField != null : "fx:id=\"rectangleYTextField\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleWidthTextField != null : "fx:id=\"rectangleWidthTextField\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleHeightTexfField != null : "fx:id=\"rectangleHeightTexfField\" was not injected: check your FXML file 'sample.fxml'.";
		assert methodHBox != null : "fx:id=\"methodHBox\" was not injected: check your FXML file 'sample.fxml'.";
		assert triangleRadioButton != null : "fx:id=\"triangleRadioButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert method != null : "fx:id=\"method\" was not injected: check your FXML file 'sample.fxml'.";
		assert rectangleRadioButton != null : "fx:id=\"rectangleRadioButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert flipLabel != null : "fx:id=\"flipLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert flipToolbar != null : "fx:id=\"flipToolbar\" was not injected: check your FXML file 'sample.fxml'.";
		assert verticalFlipButton != null : "fx:id=\"verticalFlipButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert horizontalFlipButton != null : "fx:id=\"horizontalFlipButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert rotateLabel != null : "fx:id=\"rotateLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert rotateToolbar != null : "fx:id=\"rotateToolbar\" was not injected: check your FXML file 'sample.fxml'.";
		assert rotateLeftButton != null : "fx:id=\"rotateLeftButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert rotateRightButton != null : "fx:id=\"rotateRightButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert featuresTab != null : "fx:id=\"featuresTab\" was not injected: check your FXML file 'sample.fxml'.";
		assert featuresBorderPane != null : "fx:id=\"featuresBorderPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert featuresScrollPane != null : "fx:id=\"featuresScrollPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert featuresVBox != null : "fx:id=\"featuresVBox\" was not injected: check your FXML file 'sample.fxml'.";
		assert selectionButtonsHBox != null : "fx:id=\"selectionButtonsHBox\" was not injected: check your FXML file 'sample.fxml'.";
		assert selectAllButton != null : "fx:id=\"selectAllButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert deselectAllButton != null : "fx:id=\"deselectAllButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert resultsButton != null : "fx:id=\"resultsButton\" was not injected: check your FXML file 'sample.fxml'.";
		assert resultsTab != null : "fx:id=\"resultsTab\" was not injected: check your FXML file 'sample.fxml'.";
		assert resultsMainPane != null : "fx:id=\"resultsMainPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert resultsScrollPane != null : "fx:id=\"resultsScrollPane\" was not injected: check your FXML file 'sample.fxml'.";
		assert resultsGrid != null : "fx:id=\"resultsGrid\" was not injected: check your FXML file 'sample.fxml'.";
		assert resultsControls != null : "fx:id=\"resultsControls\" was not injected: check your FXML file 'sample.fxml'.";
		assert resultsColumnsLabel != null : "fx:id=\"resultsColumnsLabel\" was not injected: check your FXML file 'sample.fxml'.";
		assert resultsColumnsTextField != null : "fx:id=\"resultsColumnsTextField\" was not injected: check your FXML file 'sample.fxml'.";

	}
}
