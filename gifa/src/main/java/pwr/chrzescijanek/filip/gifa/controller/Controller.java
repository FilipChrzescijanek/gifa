package pwr.chrzescijanek.filip.gifa.controller;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.charts.Legend;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import pwr.chrzescijanek.filip.gifa.Main;
import pwr.chrzescijanek.filip.gifa.core.generator.DataGenerator;
import pwr.chrzescijanek.filip.gifa.core.generator.DataGeneratorFactory;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;
import pwr.chrzescijanek.filip.gifa.core.util.Result;
import pwr.chrzescijanek.filip.gifa.model.BaseSample;
import pwr.chrzescijanek.filip.gifa.model.ImageDataFactory;
import pwr.chrzescijanek.filip.gifa.model.Sample;
import pwr.chrzescijanek.filip.gifa.model.SampleFactory;
import pwr.chrzescijanek.filip.gifa.model.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.TransformImageData;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.opencv.imgproc.Imgproc.INTER_CUBIC;
import static org.opencv.imgproc.Imgproc.INTER_LINEAR;
import static org.opencv.imgproc.Imgproc.INTER_NEAREST;
import static org.opencv.imgproc.Imgproc.ellipse;

public class Controller implements Initializable {

	//static strings
	public static final String THEME_PREFERENCE_KEY = "gifa.theme";
	public static final String THEME_DARK = "/css/theme-dark.css";
	public static final String THEME_LIGHT = "/css/theme-light.css";
	public static final String GRAPH_SELECTION_STYLE = "-fx-border-color: yellow; -fx-border-width: 3px;";

	private DataGeneratorFactory generatorFactory;
	private SampleFactory sampleFactory;
	private ImageDataFactory imageDataFactory;
	private List< List< Pair< String, ImageView > > > samples = new ArrayList<>();

	@Inject
	public Controller( DataGeneratorFactory generatorFactory, final SampleFactory sampleFactory,
					   final ImageDataFactory imageDataFactory ) {
		this.generatorFactory = generatorFactory;
		this.sampleFactory = sampleFactory;
		this.imageDataFactory = imageDataFactory;
	}

	//FXML fields
	@FXML
	private AnchorPane samplesImageViewAnchor;

	@FXML
	private AnchorPane transformImageViewAnchor;

	@FXML
	private BorderPane chartsMainPane;

	@FXML
	private BorderPane featuresBorderPane;

	@FXML
	private BorderPane samplesBorderPane;

	@FXML
	private BorderPane transformBorderPane;

	@FXML
	private Button chartsDeleteButton;

	@FXML
	private Button chartsMergeButton;

	@FXML
	private Button chartsRefreshButton;

	@FXML
	private Button chartsShiftButton;

	@FXML
	private Button clearImagesButton;

	@FXML
	private Button clearSamplesButton;

	@FXML
	private Button deleteImageButton;

	@FXML
	private Button deleteSampleButton;

	@FXML
	private Button deselectAllButton;

	@FXML
	private Button horizontalFlipButton;

	@FXML
	private Button loadImagesButton;

	@FXML
	private Button resultsButton;

	@FXML
	private Button rotateLeftButton;

	@FXML
	private Button rotateRightButton;

	@FXML
	private Button selectAllButton;

	@FXML
	private Button transformImagesButton;

	@FXML
	private Button verticalFlipButton;

	@FXML
	private ColorPicker sampleBorderColor;

	@FXML
	private ColorPicker sampleFillColor;

	@FXML
	private ColorPicker sampleStrokeColor;

	@FXML
	private ColorPicker triangleFillColor;

	@FXML
	private ColorPicker triangleStrokeColor;

	@FXML
	private ColorPicker vertexBorderColor;

	@FXML
	private ColorPicker vertexFillColor;

	@FXML
	private ColorPicker vertexStrokeColor;

	@FXML
	private ComboBox< Integer > chartsSampleComboBox;

	@FXML
	private ComboBox< String > samplesScaleCombo;

	@FXML
	private ComboBox< String > transformScaleCombo;

	@FXML
	private GridPane allChartsGrid;

	@FXML
	private GridPane chartsBySampleGrid;

	@FXML
	private GridPane chartsControls;

	@FXML
	private GridPane imagesBySampleGrid;

	@FXML
	private GridPane root;

	@FXML
	private GridPane sampleColorControls;

	@FXML
	private GridPane samplesBottomGrid;

	@FXML
	private GridPane samplesImageListGrid;

	@FXML
	private GridPane samplesMainPane;

	@FXML
	private GridPane samplesToolsGridPane;

	@FXML
	private GridPane transformBottomGrid;

	@FXML
	private GridPane transformColorControls;

	@FXML
	private GridPane transformImageListGrid;

	@FXML
	private GridPane transformMainPane;

	@FXML
	private GridPane transformToolsGridPane;

	@FXML
	private GridPane vertexColorControls;

	@FXML
	private Group samplesImageViewGroup;

	@FXML
	private Group transformImageViewGroup;

	@FXML
	private HBox chartsGraphsHBox;

	@FXML
	private HBox chartsGraphsToolbar;

	@FXML
	private HBox samplesTopHBox;

	@FXML
	private HBox selectionButtonsHBox;

	@FXML
	private HBox transformImageListToolbar;

	@FXML
	private HBox transformTopHBox;

	@FXML
	private ImageView samplesImageView;

	@FXML
	private ImageView transformImageView;

	@FXML
	private Label chartsColumnsLabel;

	@FXML
	private Label chartsGraphsInfo;

	@FXML
	private Label chartsSampleLabel;

	@FXML
	private Label sampleBorderLabel;

	@FXML
	private Label sampleFillLabel;

	@FXML
	private Label sampleStrokeLabel;

	@FXML
	private Label samplesImageSizeLabel;

	@FXML
	private Label samplesInfo;

	@FXML
	private Label samplesMousePositionLabel;

	@FXML
	private Label transformImageListInfo;

	@FXML
	private Label transformImageSizeLabel;

	@FXML
	private Label transformInfo;

	@FXML
	private Label transformMousePositionLabel;

	@FXML
	private Label triangleFillLabel;

	@FXML
	private Label triangleStrokeLabel;

	@FXML
	private Label vertexBorderLabel;

	@FXML
	private Label vertexFillLabel;

	@FXML
	private Label vertexStrokeLabel;

	@FXML
	private ListView< String > samplesImageList;

	@FXML
	private ListView< String > transformImageList;

	@FXML
	private Menu chartsMenu;

	@FXML
	private Menu editMenu;

	@FXML
	private Menu fileMenu;

	@FXML
	private Menu helpMenu;

	@FXML
	private Menu navMenu;

	@FXML
	private Menu optionsMenu;

	@FXML
	private Menu optionsMenuTheme;

	@FXML
	private Menu runMenu;

	@FXML
	private Menu samplesMenu;

	@FXML
	private Menu transformMenu;

	@FXML
	private MenuBar menuBar;

	@FXML
	private MenuItem chartsMenuExtractChart;

	@FXML
	private MenuItem chartsMenuMergeCharts;

	@FXML
	private MenuItem chartsMenuRemoveCharts;

	@FXML
	private MenuItem chartsMenuRestoreCharts;

	@FXML
	private MenuItem editMenuZoomIn;

	@FXML
	private MenuItem editMenuZoomOut;

	@FXML
	private MenuItem fileMenuExit;

	@FXML
	private MenuItem fileMenuExportToCsv;

	@FXML
	private MenuItem fileMenuExportToPng;

	@FXML
	private MenuItem helpMenuAbout;

	@FXML
	private MenuItem helpMenuHelp;

	@FXML
	private MenuItem navMenuAllCharts;

	@FXML
	private MenuItem navMenuCharts;

	@FXML
	private MenuItem navMenuChartsBySample;

	@FXML
	private MenuItem navMenuImagesBySample;

	@FXML
	private MenuItem navMenuSamples;

	@FXML
	private MenuItem navMenuTransform;

	@FXML
	private MenuItem runMenuResultsButton;

	@FXML
	private MenuItem runMenuTransformButton;

	@FXML
	private MenuItem samplesMenuClearSamples;

	@FXML
	private MenuItem samplesMenuCreateMode;

	@FXML
	private MenuItem samplesMenuDeleteSample;

	@FXML
	private MenuItem samplesMenuDeselectAllFeatures;

	@FXML
	private MenuItem samplesMenuRotateMode;

	@FXML
	private MenuItem samplesMenuSelectAllFeatures;

	@FXML
	private MenuItem samplesMenuSelectMode;

	@FXML
	private MenuItem transformMenuClear;

	@FXML
	private MenuItem transformMenuDeleteImage;

	@FXML
	private MenuItem transformMenuHorizontalFlip;

	@FXML
	private MenuItem transformMenuLoadImages;

	@FXML
	private MenuItem transformMenuMenuRotateLeft;

	@FXML
	private MenuItem transformMenuMenuRotateRight;

	@FXML
	private MenuItem transformMenuVerticalFlip;

	@FXML
	private RadioButton chartsBySampleRadioButton;

	@FXML
	private RadioButton createRadioButton;

	@FXML
	private RadioButton cubicRadioButton;

	@FXML
	private RadioButton imagesBySampleRadioButton;

	@FXML
	private RadioButton linearRadioButton;

	@FXML
	private RadioButton nearestRadioButton;

	@FXML
	private RadioButton rotateRadioButton;

	@FXML
	private RadioButton selectRadioButton;

	@FXML
	private RadioMenuItem optionsMenuThemeDark;

	@FXML
	private RadioMenuItem optionsMenuThemeLight;

	@FXML
	private ResourceBundle resources;

	@FXML
	private ScrollPane allChartsGridScrollPane;

	@FXML
	private ScrollPane chartsBySampleGridScrollPane;

	@FXML
	private ScrollPane featuresScrollPane;

	@FXML
	private ScrollPane imagesBySampleGridScrollPane;

	@FXML
	private ScrollPane samplesScrollPane;

	@FXML
	private ScrollPane transformScrollPane;

	@FXML
	private Tab allChartsTab;

	@FXML
	private Tab bySampleTab;

	@FXML
	private Tab chartsTab;

	@FXML
	private Tab featuresTab;

	@FXML
	private Tab samplesTab;

	@FXML
	private Tab toolboxTab;

	@FXML
	private Tab transformTab;

	@FXML
	private TabPane chartsTabPane;

	@FXML
	private TabPane mainTabPane;

	@FXML
	private TabPane rightVBoxTabPane;

	@FXML
	private TextField chartsColumnsTextField;

	@FXML
	private TitledPane interpolationTitledPane;

	@FXML
	private TitledPane sampleTitledPane;

	@FXML
	private TitledPane samplesModeTitledPane;

	@FXML
	private TitledPane samplesToolsTitledPane;

	@FXML
	private TitledPane toolsTitledPane;

	@FXML
	private TitledPane triangleTitledPane;

	@FXML
	private TitledPane vertexTitledPane;

	@FXML
	private ToggleGroup drawMethod;

	@FXML
	private ToggleGroup interpolation;

	@FXML
	private ToggleGroup result;

	@FXML
	private ToggleGroup theme;

	@FXML
	private URL location;

	@FXML
	private VBox featuresVBox;

	@FXML
	private VBox interpolationVBox;

	@FXML
	private VBox rightVBox;

	@FXML
	private VBox samplesLeftVBox;

	@FXML
	private VBox samplesModeVBox;

	@FXML
	private VBox transformLeftVBox;

	//FXML getters
	public AnchorPane getSamplesImageViewAnchor() {
		return samplesImageViewAnchor;
	}

	public AnchorPane getTransformImageViewAnchor() {
		return transformImageViewAnchor;
	}

	public BorderPane getChartsMainPane() {
		return chartsMainPane;
	}

	public BorderPane getFeaturesBorderPane() {
		return featuresBorderPane;
	}

	public BorderPane getSamplesBorderPane() {
		return samplesBorderPane;
	}

	public BorderPane getTransformBorderPane() {
		return transformBorderPane;
	}

	public Button getChartsDeleteButton() {
		return chartsDeleteButton;
	}

	public Button getChartsMergeButton() {
		return chartsMergeButton;
	}

	public Button getChartsRefreshButton() {
		return chartsRefreshButton;
	}

	public Button getChartsShiftButton() {
		return chartsShiftButton;
	}

	public Button getClearImagesButton() {
		return clearImagesButton;
	}

	public Button getClearSamplesButton() {
		return clearSamplesButton;
	}

	public Button getDeleteImageButton() {
		return deleteImageButton;
	}

	public Button getDeleteSampleButton() {
		return deleteSampleButton;
	}

	public Button getDeselectAllButton() {
		return deselectAllButton;
	}

	public Button getHorizontalFlipButton() {
		return horizontalFlipButton;
	}

	public Button getLoadImagesButton() {
		return loadImagesButton;
	}

	public Button getResultsButton() {
		return resultsButton;
	}

	public Button getRotateLeftButton() {
		return rotateLeftButton;
	}

	public Button getRotateRightButton() {
		return rotateRightButton;
	}

	public Button getSelectAllButton() {
		return selectAllButton;
	}

	public Button getTransformImagesButton() {
		return transformImagesButton;
	}

	public Button getVerticalFlipButton() {
		return verticalFlipButton;
	}

	public ColorPicker getSampleBorderColor() {
		return sampleBorderColor;
	}

	public ColorPicker getSampleFillColor() {
		return sampleFillColor;
	}

	public ColorPicker getSampleStrokeColor() {
		return sampleStrokeColor;
	}

	public ColorPicker getTriangleFillColor() {
		return triangleFillColor;
	}

	public ColorPicker getTriangleStrokeColor() {
		return triangleStrokeColor;
	}

	public ColorPicker getVertexBorderColor() {
		return vertexBorderColor;
	}

	public ColorPicker getVertexFillColor() {
		return vertexFillColor;
	}

	public ColorPicker getVertexStrokeColor() {
		return vertexStrokeColor;
	}

	public ComboBox<Integer> getChartsSampleComboBox() {
		return chartsSampleComboBox;
	}

	public ComboBox<String> getSamplesScaleCombo() {
		return samplesScaleCombo;
	}

	public ComboBox<String> getTransformScaleCombo() {
		return transformScaleCombo;
	}

	public GridPane getAllChartsGrid() {
		return allChartsGrid;
	}

	public GridPane getChartsBySampleGrid() {
		return chartsBySampleGrid;
	}

	public GridPane getChartsControls() {
		return chartsControls;
	}

	public GridPane getImagesBySampleGrid() {
		return imagesBySampleGrid;
	}

	public GridPane getRoot() {
		return root;
	}

	public GridPane getSampleColorControls() {
		return sampleColorControls;
	}

	public GridPane getSamplesBottomGrid() {
		return samplesBottomGrid;
	}

	public GridPane getSamplesImageListGrid() {
		return samplesImageListGrid;
	}

	public GridPane getSamplesMainPane() {
		return samplesMainPane;
	}

	public GridPane getSamplesToolsGridPane() {
		return samplesToolsGridPane;
	}

	public GridPane getTransformBottomGrid() {
		return transformBottomGrid;
	}

	public GridPane getTransformColorControls() {
		return transformColorControls;
	}

	public GridPane getTransformImageListGrid() {
		return transformImageListGrid;
	}

	public GridPane getTransformMainPane() {
		return transformMainPane;
	}

	public GridPane getTransformToolsGridPane() {
		return transformToolsGridPane;
	}

	public GridPane getVertexColorControls() {
		return vertexColorControls;
	}

	public Group getSamplesImageViewGroup() {
		return samplesImageViewGroup;
	}

	public Group getTransformImageViewGroup() {
		return transformImageViewGroup;
	}

	public HBox getChartsGraphsHBox() {
		return chartsGraphsHBox;
	}

	public HBox getChartsGraphsToolbar() {
		return chartsGraphsToolbar;
	}

	public HBox getSamplesTopHBox() {
		return samplesTopHBox;
	}

	public HBox getSelectionButtonsHBox() {
		return selectionButtonsHBox;
	}

	public HBox getTransformImageListToolbar() {
		return transformImageListToolbar;
	}

	public HBox getTransformTopHBox() {
		return transformTopHBox;
	}

	public ImageView getSamplesImageView() {
		return samplesImageView;
	}

	public ImageView getTransformImageView() {
		return transformImageView;
	}

	public Label getChartsColumnsLabel() {
		return chartsColumnsLabel;
	}

	public Label getChartsGraphsInfo() {
		return chartsGraphsInfo;
	}

	public Label getChartsSampleLabel() {
		return chartsSampleLabel;
	}

	public Label getSampleBorderLabel() {
		return sampleBorderLabel;
	}

	public Label getSampleFillLabel() {
		return sampleFillLabel;
	}

	public Label getSampleStrokeLabel() {
		return sampleStrokeLabel;
	}

	public Label getSamplesImageSizeLabel() {
		return samplesImageSizeLabel;
	}

	public Label getSamplesInfo() {
		return samplesInfo;
	}

	public Label getSamplesMousePositionLabel() {
		return samplesMousePositionLabel;
	}

	public Label getTransformImageListInfo() {
		return transformImageListInfo;
	}

	public Label getTransformImageSizeLabel() {
		return transformImageSizeLabel;
	}

	public Label getTransformInfo() {
		return transformInfo;
	}

	public Label getTransformMousePositionLabel() {
		return transformMousePositionLabel;
	}

	public Label getTriangleFillLabel() {
		return triangleFillLabel;
	}

	public Label getTriangleStrokeLabel() {
		return triangleStrokeLabel;
	}

	public Label getVertexBorderLabel() {
		return vertexBorderLabel;
	}

	public Label getVertexFillLabel() {
		return vertexFillLabel;
	}

	public Label getVertexStrokeLabel() {
		return vertexStrokeLabel;
	}

	public ListView<String> getSamplesImageList() {
		return samplesImageList;
	}

	public ListView<String> getTransformImageList() {
		return transformImageList;
	}

	public Menu getChartsMenu() {
		return chartsMenu;
	}

	public Menu getEditMenu() {
		return editMenu;
	}

	public Menu getFileMenu() {
		return fileMenu;
	}

	public Menu getHelpMenu() {
		return helpMenu;
	}

	public Menu getNavMenu() {
		return navMenu;
	}

	public Menu getOptionsMenu() {
		return optionsMenu;
	}

	public Menu getOptionsMenuTheme() {
		return optionsMenuTheme;
	}

	public Menu getRunMenu() {
		return runMenu;
	}

	public Menu getSamplesMenu() {
		return samplesMenu;
	}

	public Menu getTransformMenu() {
		return transformMenu;
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}

	public MenuItem getChartsMenuExtractChart() {
		return chartsMenuExtractChart;
	}

	public MenuItem getChartsMenuMergeCharts() {
		return chartsMenuMergeCharts;
	}

	public MenuItem getChartsMenuRemoveCharts() {
		return chartsMenuRemoveCharts;
	}

	public MenuItem getChartsMenuRestoreCharts() {
		return chartsMenuRestoreCharts;
	}

	public MenuItem getEditMenuZoomIn() {
		return editMenuZoomIn;
	}

	public MenuItem getEditMenuZoomOut() {
		return editMenuZoomOut;
	}

	public MenuItem getFileMenuExit() {
		return fileMenuExit;
	}

	public MenuItem getFileMenuExportToCsv() {
		return fileMenuExportToCsv;
	}

	public MenuItem getFileMenuExportToPng() {
		return fileMenuExportToPng;
	}

	public MenuItem getHelpMenuAbout() {
		return helpMenuAbout;
	}

	public MenuItem getHelpMenuHelp() {
		return helpMenuHelp;
	}

	public MenuItem getNavMenuAllCharts() {
		return navMenuAllCharts;
	}

	public MenuItem getNavMenuCharts() {
		return navMenuCharts;
	}

	public MenuItem getNavMenuChartsBySample() {
		return navMenuChartsBySample;
	}

	public MenuItem getNavMenuImagesBySample() {
		return navMenuImagesBySample;
	}

	public MenuItem getNavMenuSamples() {
		return navMenuSamples;
	}

	public MenuItem getNavMenuTransform() {
		return navMenuTransform;
	}

	public MenuItem getRunMenuResultsButton() {
		return runMenuResultsButton;
	}

	public MenuItem getRunMenuTransformButton() {
		return runMenuTransformButton;
	}

	public MenuItem getSamplesMenuClearSamples() {
		return samplesMenuClearSamples;
	}

	public MenuItem getSamplesMenuCreateMode() {
		return samplesMenuCreateMode;
	}

	public MenuItem getSamplesMenuDeleteSample() {
		return samplesMenuDeleteSample;
	}

	public MenuItem getSamplesMenuDeselectAllFeatures() {
		return samplesMenuDeselectAllFeatures;
	}

	public MenuItem getSamplesMenuRotateMode() {
		return samplesMenuRotateMode;
	}

	public MenuItem getSamplesMenuSelectAllFeatures() {
		return samplesMenuSelectAllFeatures;
	}

	public MenuItem getSamplesMenuSelectMode() {
		return samplesMenuSelectMode;
	}

	public MenuItem getTransformMenuClear() {
		return transformMenuClear;
	}

	public MenuItem getTransformMenuDeleteImage() {
		return transformMenuDeleteImage;
	}

	public MenuItem getTransformMenuHorizontalFlip() {
		return transformMenuHorizontalFlip;
	}

	public MenuItem getTransformMenuLoadImages() {
		return transformMenuLoadImages;
	}

	public MenuItem getTransformMenuMenuRotateLeft() {
		return transformMenuMenuRotateLeft;
	}

	public MenuItem getTransformMenuMenuRotateRight() {
		return transformMenuMenuRotateRight;
	}

	public MenuItem getTransformMenuVerticalFlip() {
		return transformMenuVerticalFlip;
	}

	public RadioButton getChartsBySampleRadioButton() {
		return chartsBySampleRadioButton;
	}

	public RadioButton getCreateRadioButton() {
		return createRadioButton;
	}

	public RadioButton getCubicRadioButton() {
		return cubicRadioButton;
	}

	public RadioButton getImagesBySampleRadioButton() {
		return imagesBySampleRadioButton;
	}

	public RadioButton getLinearRadioButton() {
		return linearRadioButton;
	}

	public RadioButton getNearestRadioButton() {
		return nearestRadioButton;
	}

	public RadioButton getRotateRadioButton() {
		return rotateRadioButton;
	}

	public RadioButton getSelectRadioButton() {
		return selectRadioButton;
	}

	public RadioMenuItem getOptionsMenuThemeDark() {
		return optionsMenuThemeDark;
	}

	public RadioMenuItem getOptionsMenuThemeLight() {
		return optionsMenuThemeLight;
	}

	public ResourceBundle getResources() {
		return resources;
	}

	public ScrollPane getAllChartsGridScrollPane() {
		return allChartsGridScrollPane;
	}

	public ScrollPane getChartsBySampleGridScrollPane() {
		return chartsBySampleGridScrollPane;
	}

	public ScrollPane getFeaturesScrollPane() {
		return featuresScrollPane;
	}

	public ScrollPane getImagesBySampleGridScrollPane() {
		return imagesBySampleGridScrollPane;
	}

	public ScrollPane getSamplesScrollPane() {
		return samplesScrollPane;
	}

	public ScrollPane getTransformScrollPane() {
		return transformScrollPane;
	}

	public Tab getAllChartsTab() {
		return allChartsTab;
	}

	public Tab getBySampleTab() {
		return bySampleTab;
	}

	public Tab getChartsTab() {
		return chartsTab;
	}

	public Tab getFeaturesTab() {
		return featuresTab;
	}

	public Tab getSamplesTab() {
		return samplesTab;
	}

	public Tab getToolboxTab() {
		return toolboxTab;
	}

	public Tab getTransformTab() {
		return transformTab;
	}

	public TabPane getChartsTabPane() {
		return chartsTabPane;
	}

	public TabPane getMainTabPane() {
		return mainTabPane;
	}

	public TabPane getRightVBoxTabPane() {
		return rightVBoxTabPane;
	}

	public TextField getChartsColumnsTextField() {
		return chartsColumnsTextField;
	}

	public TitledPane getInterpolationTitledPane() {
		return interpolationTitledPane;
	}

	public TitledPane getSampleTitledPane() {
		return sampleTitledPane;
	}

	public TitledPane getSamplesModeTitledPane() {
		return samplesModeTitledPane;
	}

	public TitledPane getSamplesToolsTitledPane() {
		return samplesToolsTitledPane;
	}

	public TitledPane getToolsTitledPane() {
		return toolsTitledPane;
	}

	public TitledPane getTriangleTitledPane() {
		return triangleTitledPane;
	}

	public TitledPane getVertexTitledPane() {
		return vertexTitledPane;
	}

	public ToggleGroup getDrawMethod() {
		return drawMethod;
	}

	public ToggleGroup getInterpolation() {
		return interpolation;
	}

	public ToggleGroup getResult() {
		return result;
	}

	public ToggleGroup getTheme() {
		return theme;
	}

	public URL getLocation() {
		return location;
	}

	public VBox getFeaturesVBox() {
		return featuresVBox;
	}

	public VBox getInterpolationVBox() {
		return interpolationVBox;
	}

	public VBox getRightVBox() {
		return rightVBox;
	}

	public VBox getSamplesLeftVBox() {
		return samplesLeftVBox;
	}

	public VBox getSamplesModeVBox() {
		return samplesModeVBox;
	}

	public VBox getTransformLeftVBox() {
		return transformLeftVBox;
	}


	//others
	@FXML
	void about() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Global image features analyzer\n\nCopyright © 2016 Filip Chrześcijanek", ButtonType.OK);
		DialogPane dialogPane = alert.getDialogPane();
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String theme = prefs.get(Controller.THEME_PREFERENCE_KEY, Controller.THEME_LIGHT);
		if ( theme.equals(Controller.THEME_LIGHT) ) {
			dialogPane.getStylesheets().add(Controller.THEME_LIGHT);
		} else {
			dialogPane.getStylesheets().add(Controller.THEME_DARK);
		}
		alert.setTitle("About");
		alert.setHeaderText("gifa®");
		alert.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/icon-big.png"))));
		( (Stage) alert.getDialogPane().getScene().getWindow() ).getIcons().add(new Image(getClass().getResourceAsStream("/images/icon-small.png")));
		alert.show();
	}

	@FXML
	void applyDarkTheme() {
		applyTheme(THEME_DARK);
	}

	@FXML
	void applyLightTheme() {
		applyTheme(THEME_LIGHT);
	}

	private void applyTheme( final String theme ) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		prefs.put(THEME_PREFERENCE_KEY, theme);
		ObservableList< String > stylesheets = root.getStylesheets();
		stylesheets.clear();
		stylesheets.add(theme);
	}

	@FXML
	void refresh() {
		final Integer index = chartsSampleComboBox.getValue() - 1;
		createCharts(index);
		placeCharts();
		validateChartsControlsDisableProperties();
	}

	private void createCharts( final int index ) {
		List< Series > series = SharedState.INSTANCE.series.get().get(index);
		List< BarChart< String, Number > > charts = SharedState.INSTANCE.charts.get().get(index);
		if ( charts != null ) charts.clear();
		for ( Series serie : series ) {
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			final BarChart< String, Number > bc = new BarChart<>(xAxis, yAxis);
			yAxis.setLabel("Value");
			bc.getData().addAll(serie);
			bc.setOnMouseClicked(event -> {
				if ( bc.getStyle().equals(GRAPH_SELECTION_STYLE) )
					bc.setStyle("-fx-border-color: null;");
				else
					bc.setStyle(GRAPH_SELECTION_STYLE);

				validateChartsControlsDisableProperties();
			});
			charts.add(bc);
		}
	}

	@FXML
	void deselectAllFunctions() {
		if ( !featuresTab.isSelected() ) rightVBoxTabPane.getSelectionModel().select(featuresTab);
		for ( Node chb : featuresVBox.getChildren() ) {
			( (CheckBox) chb ).setSelected(false);
		}
		generatorFactory.clearChosenFunctions();
	}

	@FXML
	void exit() {
		root.getScene().getWindow().hide();
	}

	@FXML
	void exportToCsv() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export results to CSV file");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma-separated values", "*.csv"));
		File csvFile = fileChooser.showSaveDialog(root.getScene().getWindow());
		if ( csvFile != null ) {
			try ( BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile)) ) {
				bw.write(createCsvContents());
			} catch ( IOException e ) {
				e.printStackTrace();
				showAlert("Save failed! Check your write permissions.");
			}
		}
	}

	private String createCsvContents() {
		final Set< String > collect = SharedState.INSTANCE.results.get().stream()
				.flatMap(r -> r.getResults().keySet().stream()).collect(Collectors.toSet());
		TreeSet< String > functions = new TreeSet<>(collect);
		String csvContents = "Sample,Image";
		for ( String s : functions )
			csvContents += ",\"" + s + "\"";
		int no = 0;
		for ( Result r : SharedState.INSTANCE.results.get() ) {
			no++;
			Map< String, UnmodifiableArrayList< Double > > results = r.getResults();
			final List< String > images = r.getImageNames();
			for ( int i = 0; i < images.size(); i++ ) {
				csvContents += "\r\n" + no + ",\"" + images.get(i) + "\"";
				for ( String s : functions ) {
					final double[] doubles = results.get(s).stream().mapToDouble(Double::doubleValue).toArray();
					if ( doubles == null )
						csvContents += ",";
					else
						csvContents += ",\"" + doubles[i] + "\"";
				}
			}
		}
		return csvContents;
	}

	@FXML
	void flipHorizontal() {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = SharedState.INSTANCE.transformImages.get(imageName);
		Core.flip(img.imageData, img.imageData, 1);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		//		cvtColor(imageCopy, imageCopy, COLOR_BGR2RGB);
		//		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
		//				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		Image fxImage = ImageUtils.createImage(imageCopy);
		refreshTransformImage(img, fxImage);
	}

	private void refreshTransformImage( final TransformImageData img, final Image fxImage ) {
		img.image = fxImage;
		img.writableImage.set(new WritableImage(fxImage.getPixelReader(), (int) fxImage.getWidth(), (int) fxImage.getHeight()));
		transformImageView.setImage(fxImage);
		img.vertexSamples[0].setSample(fxImage.getWidth() * 1 / 2, fxImage.getHeight() * 3 / 10, fxImage.getWidth() / 10, fxImage.getHeight() / 10, fxImage
				.getWidth(), fxImage.getHeight());
		img.vertexSamples[1].setSample(fxImage.getWidth() * 3 / 10, fxImage.getHeight() * 7 / 10, fxImage.getWidth() / 10, fxImage.getHeight() / 10, fxImage
				.getWidth(), fxImage.getHeight());
		img.vertexSamples[2].setSample(fxImage.getWidth() * 7 / 10, fxImage.getHeight() * 7 / 10, fxImage.getWidth() / 10, fxImage.getHeight() / 10, fxImage
				.getWidth(), fxImage.getHeight());
		transformImageView.setTranslateX(transformImageView.getImage().getWidth() * 0.5 * ( transformImageView.getScaleX() - 1.0 ));
		transformImageView.setTranslateY(transformImageView.getImage().getHeight() * 0.5 * ( transformImageView.getScaleY() - 1.0 ));
		transformImageSizeLabel.setText((int) fxImage.getWidth() + "x" + (int) fxImage.getHeight() + " px");
	}

	@FXML
	void flipVertical() {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = SharedState.INSTANCE.transformImages.get(imageName);
		Core.flip(img.imageData, img.imageData, 0);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		//		cvtColor(imageCopy, imageCopy, COLOR_BGR2RGB);
		//		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
		//				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		Image fxImage = ImageUtils.createImage(imageCopy);
		refreshTransformImage(img, fxImage);
	}

	private void createHistoryCharts() {
		List< LineChart< String, Number > > charts = new ArrayList<>();
		List< Result > history = SharedState.INSTANCE.results.get();
		final Set< String > collect = history.stream().flatMap(r -> r.getResults().keySet().stream()).collect(Collectors.toSet());
		Set< String > functions = new TreeSet<>(collect);
		for ( String s : functions ) {
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			final LineChart< String, Number > lc = new LineChart<>(xAxis, yAxis);
			lc.setTitle(s);
			yAxis.setLabel("Value");
			xAxis.setLabel("Sample");
			List< Double[] > results = history.stream().map(r -> r.getResults().get(s).toArray(new Double[] {})).collect(Collectors.toList());
			List< Series > series = new ArrayList<>();
			for ( int i = 0; i < results.size(); i++ ) {
				double[] r = Arrays.stream(results.get(i)).mapToDouble(Double::doubleValue).toArray();
				if ( r != null ) {
					List< String > names = history.stream().findFirst().map(result -> result.getImageNames()).orElse(
							IntStream.range(1, r.length + 1).mapToObj(n -> "Series " + n).collect(Collectors.toList())
					);
					for ( int j = 0; j < r.length; j++ ) {
						if ( series.size() == j ) {
							Series series1 = new Series();
							series1.setName(names.get(j));
							series.add(series1);
						}
						Series current = series.get(j);
						current.getData().add(new XYChart.Data("" + ( i + 1 ), r[j]));
					}
				}
			}
			series.forEach(lc.getData()::add);
			charts.add(lc);
		}

		for ( LineChart lc : charts ) {
			for ( int i = 0; i < lc.getData().size(); i++ ) {
				final int index = i;
				Series s = (Series) lc.getData().get(i);
				Legend.LegendItem item = lc.getChildrenUnmodifiable().stream()
						.filter(x -> x instanceof Legend).findAny()
						.map(l -> ( (Legend) l ).getItems())
						.map(it -> it.stream()
								.filter(r -> r.getText().equals(s.getName())).findAny().orElse(null)).orElse(null);
				if ( item != null ) {
					final ColorPicker e = new ColorPicker();
					e.setMaxWidth(12);
					e.valueProperty().addListener(( observable, oldValue, newValue ) -> {
						String web = String.format("#%02X%02X%02X%02X",
								(int) ( newValue.getRed() * 255 ),
								(int) ( newValue.getGreen() * 255 ),
								(int) ( newValue.getBlue() * 255 ),
								(int) ( newValue.getOpacity() * 255 ));
						s.getNode().setStyle("-fx-stroke: " + web + ";");
						( (ObservableList< XYChart.Data >) s.getData() ).forEach(n -> n.getNode().setStyle("-fx-background-color: " + web + ", white;"));
						SharedState.INSTANCE.resultsSeriesColors.put(lc.getTitle() + "/" + s.getName(), newValue);
					});
					final List< Color > defaultColors = Arrays.asList(Color.web("#f3622d"), Color.web("#fba71b"), Color.web("#57b757"), Color.web("#41a9c9"),
							Color.web("#4258c9"), Color.web("#9a42c8"), Color.web("#c84164"), Color.web("#888888"));
					e.setValue(SharedState.INSTANCE.resultsSeriesColors.get(lc.getTitle() + "/" + s.getName()) == null ? defaultColors.get(index %
							defaultColors
									.size()) : SharedState.INSTANCE.resultsSeriesColors.get(lc.getTitle() + "/" + s.getName()));
					item.setSymbol(e);
				}
			}
		}
		SharedState.INSTANCE.samplesCharts.get().clear();
		SharedState.INSTANCE.samplesCharts.get().addAll(charts);
		placeHistoryCharts();
	}

	private void placeHistoryCharts() {
		List< LineChart< String, Number > > charts = SharedState.INSTANCE.samplesCharts.get();
		allChartsGrid.getChildren().clear();
		allChartsGrid.getColumnConstraints().clear();
		allChartsGrid.getRowConstraints().clear();
		final int noOfColumns = Math.min(Integer.parseInt(chartsColumnsTextField.getText()), charts.size());
		if ( noOfColumns > 0 ) {
			for ( int i = 0; i < noOfColumns; i++ ) {
				final ColumnConstraints columnConstraints = new ColumnConstraints();
				columnConstraints.setPercentWidth(100.0 / noOfColumns);
				allChartsGrid.getColumnConstraints().add(columnConstraints);
			}
			final int noOfRows = charts.size() / noOfColumns + 1;
			for ( int i = 0; i < noOfRows; i++ ) {
				List< LineChart< String, Number > > chartsInRow = new ArrayList<>();
				int n = 0;
				while ( i * noOfColumns + n < charts.size() && n < noOfColumns ) {
					chartsInRow.add(charts.get(i * noOfColumns + n));
					n++;
				}
				allChartsGrid.addRow(i, chartsInRow.toArray(new LineChart[0]));
			}
		}
	}

	private void placeCharts() {
		final Integer index = chartsSampleComboBox.getValue() - 1;
		List< BarChart< String, Number > > charts = SharedState.INSTANCE.charts.get().get(index);
		if ( charts != null ) {
			chartsBySampleGrid.getChildren().clear();
			chartsBySampleGrid.getColumnConstraints().clear();
			chartsBySampleGrid.getRowConstraints().clear();
			final int noOfColumns = Math.min(Integer.parseInt(chartsColumnsTextField.getText()), charts.size());
			if ( noOfColumns > 0 ) {
				for ( int i = 0; i < noOfColumns; i++ ) {
					final ColumnConstraints columnConstraints = new ColumnConstraints();
					columnConstraints.setPercentWidth(100.0 / noOfColumns);
					chartsBySampleGrid.getColumnConstraints().add(columnConstraints);
				}
				final int noOfRows = charts.size() / noOfColumns + ( charts.size() % noOfColumns == 0 ? 0 : 1 );
				for ( int i = 0; i < noOfRows; i++ ) {
					List< BarChart< String, Number > > chartsInRow = new ArrayList<>();
					int n = 0;
					while ( i * noOfColumns + n < charts.size() && n < noOfColumns ) {
						chartsInRow.add(charts.get(i * noOfColumns + n));
						n++;
					}
					chartsBySampleGrid.addRow(i, chartsInRow.toArray(new BarChart[0]));
				}
			}
		}
		colorSeries(index);
	}

	private void colorSeries( int idx ) {
		final List< BarChart< String, Number > > charts = SharedState.INSTANCE.charts.get().get(idx);
		final List< Series > series = SharedState.INSTANCE.series.get().get(idx);

		for ( int i = 0; i < series.size(); i++ ) {
			final int index = i;
			Series s = series.get(i);
			Legend.LegendItem item = charts.stream()
					.map(c -> c.getChildrenUnmodifiable())
					.map(n -> n.stream()
							.filter(x -> x instanceof Legend).collect(Collectors.toList()))
					.filter(l -> !l.isEmpty())
					.map(l -> ( (Legend) l.get(0) ).getItems())
					.map(it -> it.stream()
							.filter(r -> r.getText().equals(s.getName())).collect(Collectors.toList()))
					.filter(l -> !l.isEmpty())
					.map(w -> w.get(0)).findAny().orElse(null);
			if ( item != null ) {
				final ColorPicker e = new ColorPicker();
				e.setMaxWidth(15);
				e.valueProperty().addListener(( observable, oldValue, newValue ) -> {
					String web = String.format("#%02X%02X%02X%02X",
							(int) ( newValue.getRed() * 255 ),
							(int) ( newValue.getGreen() * 255 ),
							(int) ( newValue.getBlue() * 255 ),
							(int) ( newValue.getOpacity() * 255 ));
					charts.stream()
							.map(c -> c.getData().stream().filter(d -> d.getName().equals(s.getName())).collect(Collectors.toList()))
							.filter(l -> !l.isEmpty())
							.map(l -> l.get(0).getData())
							.forEach(d -> d.forEach(n -> n.getNode().setStyle("-fx-bar-fill: " + web + ";")));
					SharedState.INSTANCE.seriesColors.get(idx).put(s.getName(), newValue);
				});
				final List< Color > defaultColors = Arrays.asList(Color.web("#f3622d"), Color.web("#fba71b"), Color.web("#57b757"), Color.web("#41a9c9"),
						Color.web("#4258c9"), Color.web("#9a42c8"), Color.web("#c84164"), Color.web("#888888"));
				e.setValue(SharedState.INSTANCE.seriesColors.get(idx).get(s.getName()) == null ? defaultColors.get(index % defaultColors.size()) : SharedState
						.INSTANCE
						.seriesColors.get(idx).get(s.getName()));
				item.setSymbol(e);
			}
		}
	}

	private List< Series > generateSeries( final Map< String, UnmodifiableArrayList< Double > > results ) {
		List< Series > series = new ArrayList<>();
		for ( Map.Entry< String, UnmodifiableArrayList< Double > > e : results.entrySet() ) {
			Series series1 = new Series();
			series1.setName(e.getKey());
			for ( int i = 0; i < e.getValue().size(); i++ ) {
				series1.getData().add(new XYChart.Data(samplesImageList.getItems().get(i), e.getValue().get(i)));
			}
			series.add(series1);
		}
		return series;
	}

	@FXML
	void selectAllFunctions() {
		if ( !featuresTab.isSelected() ) rightVBoxTabPane.getSelectionModel().select(featuresTab);
		for ( Node chb : featuresVBox.getChildren() ) {
			( (CheckBox) chb ).setSelected(true);
		}
		generatorFactory.chooseAllAvailableFunctions();
	}

	private void addNumberTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			if ( !newValue.matches("\\d+") )
				textField.setText(oldValue);
		});
	}

	private void createCheckBoxes() {
		for ( String s : generatorFactory.getAvailableFunctionsNames() ) {
			final CheckBox checkBox = new CheckBox(s);
			featuresVBox.getChildren().add(checkBox);
			checkBox.selectedProperty().addListener(( observable, oldValue, newValue ) -> {
				if ( newValue ) generatorFactory.chooseFunction(s);
				else generatorFactory.deselectFunction(s);
			});
			checkBox.setSelected(true);
		}
	}

	@FXML
	void merge() {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart< String, Number > bc = new BarChart<>(xAxis, yAxis);
		yAxis.setLabel("Value");
		chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData
				()).forEach(series -> {
			bc.getData().addAll(series);
		});
		bc.setOnMouseClicked(e -> {
			if ( bc.getStyle().equals(GRAPH_SELECTION_STYLE) )
				bc.setStyle("-fx-border-color: null;");
			else
				bc.setStyle(GRAPH_SELECTION_STYLE);

			validateChartsControlsDisableProperties();
		});
		Optional< Integer > index = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> SharedState
				.INSTANCE
				.charts.get().get(chartsSampleComboBox.getValue() - 1).indexOf(n)).min(Integer::compareTo);
		SharedState.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).removeAll(chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle
				().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		SharedState.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).add(index.orElse(0), bc);
		placeCharts();
		validateChartsControlsDisableProperties();
	}

	private void validateChartsControlsDisableProperties() {
		final List< Node > nodes = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
		final Integer integer = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
		chartsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
		chartsDeleteButton.setDisable(nodes.isEmpty());
		chartsMergeButton.setDisable(nodes.size() < 2);
		chartsMenuMergeCharts.setDisable(nodes.size() < 2);
		chartsMenuExtractChart.setDisable(nodes.size() != 1 || integer <= 1);
		chartsMenuRemoveCharts.setDisable(nodes.isEmpty());
	}

	@FXML
	void shift() {
		final CategoryAxis xAxis1 = new CategoryAxis();
		final NumberAxis yAxis1 = new NumberAxis();
		final CategoryAxis xAxis2 = new CategoryAxis();
		final NumberAxis yAxis2 = new NumberAxis();
		final BarChart< String, Number > bc1 = new BarChart<>(xAxis1, yAxis1);
		final BarChart< String, Number > bc2 = new BarChart<>(xAxis2, yAxis2);
		yAxis1.setLabel("Value");
		yAxis2.setLabel("Value");
		BarChart< String, Number > chart = (BarChart< String, Number >) chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()).get(0); //.map(n -> ((BarChart<String, Number>) n).getData()).forEach(series -> {
		final Integer index = chartsSampleComboBox.getValue() - 1;
		Series s = SharedState.INSTANCE.series.get().get(index).stream().filter(n -> n.getName().equals(chart.getData().get(chart.getData().size() - 1)
				.getName())).collect
				(Collectors.toList()).get(0);
		List< String > names = chart.getData().stream().filter(n -> !n.getName().equals(chart.getData().get(chart.getData().size() - 1).getName())).map(n -> n
				.getName()).collect(Collectors.toList());
		bc1.getData().add(s);
		bc2.getData().addAll(SharedState.INSTANCE.series.get().get(index).stream().filter(n -> names.contains(n.getName())).toArray(Series[]::new));
		//								bc1.setTitle(bc1.getData().get(0).getName());
		if ( bc2.getData().size() == 1 ) {
			//									bc2.setTitle(bc2.getData().get(0).getName());
			//									bc2.setLegendVisible(false);
		}
		bc1.setOnMouseClicked(e -> {
			if ( bc1.getStyle().equals(GRAPH_SELECTION_STYLE) )
				bc1.setStyle("-fx-border-color: null;");
			else
				bc1.setStyle(GRAPH_SELECTION_STYLE);

			validateChartsControlsDisableProperties();
		});
		bc2.setOnMouseClicked(e -> {
			if ( bc2.getStyle().equals(GRAPH_SELECTION_STYLE) )
				bc2.setStyle("-fx-border-color: null;");
			else
				bc2.setStyle(GRAPH_SELECTION_STYLE);

			validateChartsControlsDisableProperties();

		});
		//								bc1.setLegendVisible(false);
		Optional< Integer > idx = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> SharedState
				.INSTANCE
				.charts.get().get(chartsSampleComboBox.getValue() - 1).indexOf(n)).min(Integer::compareTo);
		//								SharedState.INSTANCE.chartsBySample.get().removeAll(chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle()
		// .equals(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		int i = idx.orElse(SharedState.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).size() - 1);
		SharedState.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).remove(chart);
		SharedState.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).add(i, bc1);
		SharedState.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).add(i, bc2);
		placeCharts();


		validateChartsControlsDisableProperties();
	}

	@FXML
	void delete() {
		SharedState.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).removeAll(chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle
				().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		placeCharts();
		validateChartsControlsDisableProperties();
	}

	@FXML
	void showAllCharts() { }

	public void transformTab() {
		if ( !transformTab.isSelected() ) mainTabPane.getSelectionModel().select(transformTab);
	}

	public void samplesTab() {
		if ( !samplesTab.isSelected() ) mainTabPane.getSelectionModel().select(samplesTab);
	}

	public void chartsTab() {
		if ( !chartsTab.isSelected() ) mainTabPane.getSelectionModel().select(chartsTab);
	}

	public void chartsBySample() {
		if ( !chartsTab.isSelected() ) mainTabPane.getSelectionModel().select(chartsTab);
		if ( !bySampleTab.isSelected() ) chartsTabPane.getSelectionModel().select(bySampleTab);
		chartsBySampleRadioButton.setSelected(true);
	}

	public void allCharts() {
		if ( !chartsTab.isSelected() ) mainTabPane.getSelectionModel().select(chartsTab);
		if ( !allChartsTab.isSelected() ) chartsTabPane.getSelectionModel().select(allChartsTab);
	}

	public void zoomIn() {
		if ( transformTab.isSelected() )
			updateScrollbars(transformImageView, transformScrollPane, 1);
		else if ( samplesTab.isSelected() )
			updateScrollbars(samplesImageView, samplesScrollPane, 1);
	}

	public void zoomOut() {
		if ( transformTab.isSelected() )
			updateScrollbars(transformImageView, transformScrollPane, -1);
		else if ( samplesTab.isSelected() )
			updateScrollbars(samplesImageView, samplesScrollPane, -1);
	}

	@FXML
	void calculateResults() {
		final Stage myDialog = showPopup("Calculating results");
		Task task = createCalculateResultsTask(myDialog);
		startRunnable(task);
	}

	private Task createCalculateResultsTask( final Stage myDialog ) {
		return new Task< Void >() {
			@Override
			protected Void call() throws Exception {
				calculateResults(myDialog);
				return null;
			}
		};
	}

	private void calculateResults( final Stage myDialog ) {
		try {
			SamplesImageData img = SharedState.INSTANCE.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem());
			SharedState.INSTANCE.results.set(new ArrayList<>());
			SharedState.INSTANCE.series.get().clear();
			SharedState.INSTANCE.charts.get().clear();
			SharedState.INSTANCE.samplesCharts.get().clear();
			SharedState.INSTANCE.seriesColors.clear();
			SharedState.INSTANCE.resultsSeriesColors.clear();
			samples.clear();
			if ( img != null ) {
				for ( int j = 0; j < img.samples.size(); j++ ) {
					int i = 0;
					samples.add(new ArrayList<>());
					BaseSample r = img.samples.get(j);
					final Mat[] images = new Mat[SharedState.INSTANCE.samplesImages.size()];
					for ( String key : samplesImageList.getItems() ) {
						final SamplesImageData samplesImageData = SharedState.INSTANCE.samplesImages.get(key);
						final int x = (int) r.sampleArea.getX();
						final int y = (int) r.sampleArea.getY();
						int width = (int) r.sampleArea.getWidth();
						int height = (int) r.sampleArea.getHeight();
						images[i] = samplesImageData.imageData
								.submat(new Rect(x, y, width, height)).clone();
						Mat zeros = Mat.zeros(images[i].rows(), images[i].cols(), images[i].type());
						ellipse(zeros, new Point(r.getCenterX() - x, r.getCenterY() - y), new Size(r.getRadiusX(), r.getRadiusY()), r.getRotate(), 0.0, 360.0,
								new Scalar(255,
										255, 255, 255), Core.FILLED);
						Core.bitwise_and(images[i], zeros, images[i]);
						final ImageView view = new ImageView(ImageUtils.createImage(images[i]));
						view.setPreserveRatio(true);
						samples.get(j).add(new Pair<>(key, view));
						i++;
					}
					final DataGenerator generator = generatorFactory.createGenerator();
					final Result result = generator.generateData(ImageUtils.getImagesCopy(images), samplesImageList.getItems());
					final Map< String, Mat[] > preprocessedImages = generator.getPreprocessedImages();
					for ( Map.Entry< String, Mat[] > e : preprocessedImages.entrySet() ) {
						final ObservableList< String > names = samplesImageList.getItems();
						for ( int k = 0; k < names.size(); k++ ) {
							String key = names.get(k);
							Mat m = e.getValue()[k];
							final ImageView view = new ImageView(ImageUtils.createImage(m));
							view.setPreserveRatio(true);
							samples.get(j).add(new Pair<>(key + "_" + e.getKey().toLowerCase().replaceAll("\\s+", "_"), view));
						}
					}
					SharedState.INSTANCE.results.get().add(result);
					SharedState.INSTANCE.series.get().add(generateSeries(result.getResults()));
					SharedState.INSTANCE.charts.get().add(new ArrayList<>());
					SharedState.INSTANCE.seriesColors.add(new HashMap<>());
					createCharts(j);
				}
				Platform.runLater(() -> {
					chartsSampleComboBox.setItems(FXCollections.observableArrayList(IntStream.range(1, SharedState.INSTANCE.results.get().size() + 1).boxed()
									.collect(Collectors.toList())));
					chartsSampleComboBox.setValue(1);
					createHistoryCharts();
					validateChartsControlsDisableProperties();
					allCharts();
					myDialog.close();
				});
			}
		} catch ( CvException e ) {
			Platform.runLater(() -> myDialog.close());
			showAlert("Generating results failed! If you are using custom function, check them for errors.");
		}
	}

	private void placeImages() {
		final Integer index = chartsSampleComboBox.getValue() - 1;
		List< Pair< String, ImageView > > samples = this.samples.get(index);
		imagesBySampleGrid.getChildren().clear();
		imagesBySampleGrid.getColumnConstraints().clear();
		imagesBySampleGrid.getRowConstraints().clear();
		Bounds bounds = imagesBySampleGridScrollPane.getLayoutBounds();
		final int noOfColumns = Math.min(Integer.parseInt(chartsColumnsTextField.getText()), samples.size());
		if ( noOfColumns > 0 ) {
			for ( int i = 0; i < noOfColumns; i++ ) {
				final ColumnConstraints columnConstraints = new ColumnConstraints();
				columnConstraints.setPercentWidth(100.0 / noOfColumns);
				columnConstraints.setHalignment(HPos.CENTER);
				imagesBySampleGrid.getColumnConstraints().add(columnConstraints);
			}
			final int noOfRows = samples.size() / noOfColumns + ( samples.size() % noOfColumns == 0 ? 0 : 1 );
			for ( int i = 0; i < noOfRows; i++ ) {
				final RowConstraints rowConstraints = new RowConstraints();
				rowConstraints.setPercentHeight(100.0 / noOfRows);
				rowConstraints.setValignment(VPos.CENTER);
				imagesBySampleGrid.getRowConstraints().add(rowConstraints);
			}
			double fitWidth = Math.max(( bounds.getWidth() - 50.0 ) / noOfColumns, 150.0);
			double fitHeight = Math.max(( bounds.getHeight() - 50.0 ) / noOfRows, 150.0);
			for ( int i = 0; i < noOfRows; i++ ) {
				List< VBox > nodesInRow = new ArrayList<>();
				int n = 0;
				while ( i * noOfColumns + n < samples.size() && n < noOfColumns ) {
					VBox vbox = new VBox();
					vbox.setAlignment(Pos.CENTER);
					vbox.setSpacing(10.0);
					final Pair< String, ImageView > pair = samples.get(i * noOfColumns + n);
					pair.getValue().setFitWidth(fitWidth);
					pair.getValue().setFitHeight(fitHeight);
					vbox.getChildren().add(pair.getValue());
					vbox.getChildren().add(new Label(pair.getKey()));
					nodesInRow.add(vbox);
					n++;
				}
				imagesBySampleGrid.addRow(i, nodesInRow.toArray(new Node[0]));
			}
		}
	}

	private void showAlert( final String contentText ) {
		Alert alert = new Alert(Alert.AlertType.ERROR,
				contentText, ButtonType.OK);
		DialogPane dialogPane = alert.getDialogPane();
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String theme = prefs.get(Controller.THEME_PREFERENCE_KEY, Controller.THEME_LIGHT);
		if ( theme.equals(Controller.THEME_LIGHT) ) {
			dialogPane.getStylesheets().add(Controller.THEME_LIGHT);
		} else {
			dialogPane.getStylesheets().add(Controller.THEME_DARK);
		}
		alert.showAndWait();
	}

	@FXML
	void loadImages() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load images");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.tif"));
		List< File > selectedFiles = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
		if ( selectedFiles != null ) {
			final Stage myDialog = showPopup("Loading images");
			Task task = createLoadImagesTask(myDialog, selectedFiles);
			startRunnable(task);
		}
	}

	private Task createLoadImagesTask( final Stage myDialog, final List< File > selectedFiles ) {
		return new Task< Void >() {
			@Override
			protected Void call() throws Exception {
				loadImages(myDialog, selectedFiles);
				return null;
			}
		};
	}

	private void loadImages( final Stage myDialog, final List< File > selectedFiles ) {
		for ( File f : selectedFiles ) {
			String filePath;
			try {
				filePath = f.getCanonicalPath();
				String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
				Mat image;
				try {
					image = Imgcodecs.imread(filePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
					if ( image.dataAddr() == 0 )
						throw new CvException("Imgcodecs.imread failed to load image!");
				} catch ( CvException e ) {
					e.printStackTrace();
					Platform.runLater(() -> myDialog.close());
					showAlert("Loading failed!\nImages might be corrupted or you do not have sufficient read permissions.");
					break;
				}
				if ( image != null ) {
					final Mat imageCopy = ImageUtils.getImageCopy(image);
					Image fxImage = ImageUtils.createImage(imageCopy);
					if (SharedState.INSTANCE.transformImages.containsKey(fileName)) {

					}
					SharedState.INSTANCE.transformImages.put(fileName, imageDataFactory.createTransformImageData(fxImage, image));
					Platform.runLater(() -> {
						transformImageList.getItems().add(fileName);
					});
				}
			} catch ( IOException e ) {
				Platform.runLater(() -> myDialog.close());
				showAlert("Loading failed!\nImages might be corrupted or you do not have sufficient read permissions.");
				e.printStackTrace();
				break;
			}
		}
		Platform.runLater(() -> {
			transformScrollPane.setHvalue(0.5);
			transformScrollPane.setVvalue(0.5);
			transformImageList.getSelectionModel().selectLast();
			myDialog.close();
		});
	}

	@FXML
	void rotateLeft() {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = SharedState.INSTANCE.transformImages.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 0);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Image fxImage = ImageUtils.createImage(imageCopy);
		refreshTransformImage(img, fxImage);
	}

	@FXML
	void rotateRight() {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = SharedState.INSTANCE.transformImages.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 1);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Image fxImage = ImageUtils.createImage(imageCopy);
		refreshTransformImage(img, fxImage);
	}

	@FXML
	void deleteImage() {
		String key = transformImageList.getSelectionModel().getSelectedItem();
		if ( key != null ) {
			transformImageList.getSelectionModel().clearSelection();
			final int index = transformImageList.getItems().indexOf(key);
			transformImageList.getItems().remove(key);
			SharedState.INSTANCE.imageViews.forEach(
					l -> l.remove(index)
			);
			SharedState.INSTANCE.transformImages.remove(key);
		}
	}

	@FXML
	void clearImages() {
		transformImageList.getSelectionModel().clearSelection();
		transformImageList.getItems().clear();
		SharedState.INSTANCE.transformImages.clear();
		SharedState.INSTANCE.imageViews.forEach(List< ImageView >::clear);
		SharedState.INSTANCE.imageViews.clear();
	}

	@Override
	public void initialize( final URL location, final ResourceBundle resources ) {

		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String s = prefs.get(Controller.THEME_PREFERENCE_KEY, Controller.THEME_LIGHT);
		if ( s.equals(Controller.THEME_LIGHT) ) {
			theme.selectToggle(optionsMenuThemeLight);
			root.getStylesheets().add(Controller.THEME_LIGHT);
		} else {
			theme.selectToggle(optionsMenuThemeDark);
			root.getStylesheets().add(Controller.THEME_DARK);
		}
		//assertions
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert fileMenu != null : "fx:id=\"fileMenu\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformMenuLoadImages != null : "fx:id=\"transformMenuLoadImages\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert fileMenuExportToCsv != null : "fx:id=\"fileMenuExportToCsv\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert fileMenuExit != null : "fx:id=\"fileMenuExit\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert optionsMenu != null : "fx:id=\"optionsMenu\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert optionsMenuTheme != null : "fx:id=\"optionsMenuTheme\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert optionsMenuThemeLight != null : "fx:id=\"optionsMenuThemeLight\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert theme != null : "fx:id=\"theme\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert optionsMenuThemeDark != null : "fx:id=\"optionsMenuThemeDark\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert helpMenu != null : "fx:id=\"helpMenu\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert helpMenuAbout != null : "fx:id=\"helpMenuAbout\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert mainTabPane != null : "fx:id=\"mainTabPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformTab != null : "fx:id=\"transformTab\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformMainPane != null : "fx:id=\"transformMainPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformImageListGrid != null : "fx:id=\"transformImageListGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformImageList != null : "fx:id=\"transformImageList\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert loadImagesButton != null : "fx:id=\"loadImagesButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformBorderPane != null : "fx:id=\"transformBorderPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformScrollPane != null : "fx:id=\"transformScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformImageViewAnchor != null : "fx:id=\"transformImageViewAnchor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformImageView != null : "fx:id=\"transformImageView\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformBottomGrid != null : "fx:id=\"transformBottomGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformScaleCombo != null : "fx:id=\"transformScaleCombo\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformMousePositionLabel != null : "fx:id=\"transformMousePositionLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rightVBox != null : "fx:id=\"rightVBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rightVBoxTabPane != null : "fx:id=\"rightVBoxTabPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert toolboxTab != null : "fx:id=\"toolboxTab\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformLeftVBox != null : "fx:id=\"transformLeftVBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert vertexFillColor != null : "fx:id=\"vertexFillColor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert vertexStrokeColor != null : "fx:id=\"vertexStrokeColor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert vertexFillLabel != null : "fx:id=\"vertexFillLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert vertexStrokeLabel != null : "fx:id=\"vertexStrokeLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformToolsGridPane != null : "fx:id=\"transformToolsGridPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert verticalFlipButton != null : "fx:id=\"verticalFlipButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert horizontalFlipButton != null : "fx:id=\"horizontalFlipButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rotateLeftButton != null : "fx:id=\"rotateLeftButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rotateRightButton != null : "fx:id=\"rotateRightButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert featuresTab != null : "fx:id=\"chartsTab\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert featuresBorderPane != null : "fx:id=\"featuresBorderPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert featuresScrollPane != null : "fx:id=\"featuresScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert featuresVBox != null : "fx:id=\"featuresVBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert selectionButtonsHBox != null : "fx:id=\"selectionButtonsHBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert selectAllButton != null : "fx:id=\"selectAllButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert deselectAllButton != null : "fx:id=\"deselectAllButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsButton != null : "fx:id=\"resultsButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsTab != null : "fx:id=\"chartsTab\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsMainPane != null : "fx:id=\"chartsMainPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsControls != null : "fx:id=\"chartsControls\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert result != null : "fx:id=\"result\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsBySampleRadioButton != null : "fx:id=\"chartsBySampleRadioButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsColumnsLabel != null : "fx:id=\"chartsColumnsLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsColumnsTextField != null : "fx:id=\"chartsColumnsTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsBySampleGridScrollPane != null : "fx:id=\"chartsBySampleGridScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsBySampleGrid != null : "fx:id=\"chartsBySampleGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformImageViewGroup != null : "fx:id=\"transformImageViewGroup\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformImageSizeLabel != null : "fx:id=\"transformImageSizeLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert deleteImageButton != null : "fx:id=\"deleteImageButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert clearImagesButton != null : "fx:id=\"clearImagesButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsRefreshButton != null : "fx:id=\"chartsRefreshButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imagesBySampleRadioButton != null : "fx:id=\"imagesBySampleRadioButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsMergeButton != null : "fx:id=\"chartsMergeButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsShiftButton != null : "fx:id=\"chartsShiftButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsDeleteButton != null : "fx:id=\"chartsDeleteButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsGraphsInfo != null : "fx:id=\"chartsGraphsInfo\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformImageListInfo != null : "fx:id=\"transformImageListInfo\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformImageListToolbar != null : "fx:id=\"transformImageListToolbar\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsGraphsToolbar != null : "fx:id=\"chartsGraphsToolbar\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert chartsGraphsHBox != null : "fx:id=\"chartsGraphsHBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert allChartsGrid != null : "fx:id=\"allChartsGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert allChartsGridScrollPane != null : "fx:id=\"allChartsGridScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";

		//others
		transformScaleCombo.itemsProperty().get().addAll(
				"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%", "250%", "500%", "1000%"
		);
		transformScaleCombo.setValue("100%");
		samplesScaleCombo.itemsProperty().get().addAll(
				"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%", "250%", "500%", "1000%"
		);
		samplesScaleCombo.setValue("100%");

		chartsShiftButton.setDisable(true);
		chartsDeleteButton.setDisable(true);
		chartsMergeButton.setDisable(true);
		chartsMenuMergeCharts.setDisable(true);
		chartsMenuExtractChart.setDisable(true);
		chartsMenuRemoveCharts.setDisable(true);
		chartsSampleComboBox.getSelectionModel().selectedItemProperty().addListener(( observable, oldValue, newValue ) -> {
			if ( newValue != null ) {
				placeCharts();
				placeImages();
				validateChartsControlsDisableProperties();
			}
		});
		chartsColumnsTextField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			if (newValue != null && !newValue.isEmpty()) {
				placeCharts();
				placeImages();
				placeHistoryCharts();
			}
		});
		setTooltips();
		createCheckBoxes();
		setVisibilityBindings();
		setEnablementBindings();
		setSelectionListeners();
		triangleFillColor.valueProperty().addListener((observable, oldValue, newValue ) ->
				SharedState.INSTANCE.transformImages.get(transformImageList.getSelectionModel().getSelectedItem()).triangle.setFill(newValue));
		triangleStrokeColor.valueProperty().addListener((observable, oldValue, newValue ) ->
				SharedState.INSTANCE.transformImages.get(transformImageList.getSelectionModel().getSelectedItem()).triangle.setStroke(newValue));
		vertexFillColor.valueProperty().addListener((observable, oldValue, newValue ) ->
				SharedState.INSTANCE.selectedRectangle.get().setFill(newValue));
		vertexStrokeColor.valueProperty().addListener((observable, oldValue, newValue ) ->
				SharedState.INSTANCE.selectedRectangle.get().setStroke(newValue));
		vertexBorderColor.valueProperty().addListener((observable, oldValue, newValue ) ->
				SharedState.INSTANCE.selectedRectangle.get().sampleArea.setStroke(newValue));
		sampleFillColor.valueProperty().addListener((observable, oldValue, newValue ) ->
				SharedState.INSTANCE.selectedSample.get().setFill(newValue));
		sampleStrokeColor.valueProperty().addListener((observable, oldValue, newValue ) ->
				SharedState.INSTANCE.selectedSample.get().setStroke(newValue));
		sampleBorderColor.valueProperty().addListener((observable, oldValue, newValue ) ->
				SharedState.INSTANCE.selectedSample.get().sampleArea.setStroke(newValue));
		SharedState.INSTANCE.selectedRectangle.addListener(( observable, oldValue, newValue ) -> {
			if ( newValue != null ) {
				vertexFillColor.setValue((Color) newValue.getFill());
				vertexStrokeColor.setValue((Color) newValue.getStroke());
				vertexBorderColor.setValue((Color) newValue.sampleArea.getStroke());
				for ( Map.Entry< String, TransformImageData > e : SharedState.INSTANCE.transformImages.entrySet() ) {
					if ( Arrays.asList(e.getValue().vertexSamples).contains(newValue) ) {
						transformTab();
						transformImageList.getSelectionModel().select(e.getKey());
						break;
					}
				}

				if ( SharedState.INSTANCE.zoom ) {
					double newX = Math.max(0, newValue.sampleArea.getX() - 50);
					double newY = Math.max(0, newValue.sampleArea.getY() - 50);
					double newWidth = newValue.sampleArea.getWidth() + 100;
					double newHeight = newValue.sampleArea.getHeight() + 100;
					final double scale = transformScrollPane.getWidth() / transformScrollPane.getHeight() > newWidth / newHeight ?
							transformScrollPane.getHeight() / newHeight
							: transformScrollPane.getWidth() / newWidth;
					transformImageView.setScaleX(scale);
					double newHDenominator = calculateDenominator(
							transformImageView.getScaleX(), transformImageView.getImage().getWidth(), transformScrollPane.getWidth());
					double newVDenominator = calculateDenominator(
							transformImageView.getScaleX(), transformImageView.getImage().getHeight(), transformScrollPane.getHeight());
					transformScrollPane.setHvalue(
							( Math.max(0, ( newX + newWidth / 2 ) * transformImageView.getScaleX() - ( transformScrollPane.getWidth() / 2 )) / (
									transformScrollPane.getWidth() / 2 ) )
									/ newHDenominator);
					transformScrollPane.setVvalue(
							( Math.max(0, ( newY + newHeight / 2 ) * transformImageView.getScaleX() - ( transformScrollPane.getHeight() / 2 )) / (
									transformScrollPane.getHeight() / 2 ) )
									/ newVDenominator);
				}
				SharedState.INSTANCE.zoom = false;
			}
		});
		SharedState.INSTANCE.selectedSample.addListener(( observable, oldValue, newValue ) -> {
			if ( createRadioButton.isSelected() ) {
				SharedState.INSTANCE.selectedSample.set(null);
			} else {
				if ( newValue != null ) {
					sampleFillColor.setValue((Color) newValue.getFill());
					sampleStrokeColor.setValue((Color) newValue.getStroke());
					sampleBorderColor.setValue((Color) newValue.sampleArea.getStroke());
					for ( Map.Entry< String, SamplesImageData > e : SharedState.INSTANCE.samplesImages.entrySet() ) {
						if ( e.getValue().samples.contains(newValue) ) {
							samplesTab();
							samplesImageList.getSelectionModel().select(e.getKey());
							break;
						}
					}
					if ( SharedState.INSTANCE.zoom ) {
						double newX = Math.max(0, newValue.sampleArea.getX() - 50);
						double newY = Math.max(0, newValue.sampleArea.getY() - 50);
						double newWidth = newValue.sampleArea.getWidth() + 100;
						double newHeight = newValue.sampleArea.getHeight() + 100;
						final double scale = samplesScrollPane.getWidth() / samplesScrollPane.getHeight() > newWidth / newHeight ?
								samplesScrollPane.getHeight() / newHeight
								: samplesScrollPane.getWidth() / newWidth;
						samplesImageView.setScaleX(scale);
						double newHDenominator = calculateDenominator(
								samplesImageView.getScaleX(), samplesImageView.getImage().getWidth(), samplesScrollPane.getWidth());
						double newVDenominator = calculateDenominator(
								samplesImageView.getScaleX(), samplesImageView.getImage().getHeight(), samplesScrollPane.getHeight());
						samplesScrollPane.setHvalue(
								( Math.max(0, ( newX + newWidth / 2 ) * samplesImageView.getScaleX() - ( samplesScrollPane.getWidth() / 2 )) / (
										transformScrollPane.getWidth() / 2 ) )
										/ newHDenominator);
						samplesScrollPane.setVvalue(
								( Math.max(0, ( newY + newHeight / 2 ) * samplesImageView.getScaleX() - ( samplesScrollPane.getHeight() / 2 )) / (
										transformScrollPane.getHeight() / 2 ) )
										/ newVDenominator);
					}
					SharedState.INSTANCE.zoom = false;
				}
			}

		});
		addNumberTextFieldListener(chartsColumnsTextField);
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		nf.setGroupingUsed(false);
		setImageViewControls(transformImageView, transformScrollPane, transformImageViewGroup, transformScaleCombo, transformMousePositionLabel);
		setImageViewControls(samplesImageView, samplesScrollPane, samplesImageViewGroup, samplesScaleCombo, samplesMousePositionLabel);

		transformImageView.scaleXProperty().addListener(( observable, oldValue, newValue ) -> {
			final double scale = newValue.doubleValue();
			TransformImageData img = SharedState.INSTANCE.transformImages.get(transformImageList.getSelectionModel().getSelectedItem());
			if (img != null) img.scale.set(scale);
		});

		samplesImageView.scaleXProperty().addListener(( observable, oldValue, newValue ) -> {
			final double scale = newValue.doubleValue();
			SamplesImageData img = SharedState.INSTANCE.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem());
			if (img != null) img.scale.set(scale);
		});
		mainTabPane.setOnMouseReleased(event -> {
			transformImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
			samplesImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
		});

		transformImageViewGroup.setOnMouseMoved(event -> {
			transformMousePositionLabel.setText((int) ( event.getX() / transformImageView.getScaleX() ) + " : " + (int) ( event.getY() / transformImageView
					.getScaleY() ));

		});
		transformImageViewGroup.setOnMouseClicked(event -> {
			if ( SharedState.INSTANCE.selectedRectangle.isNotNull().get() ) {
				final Rectangle rectangle = SharedState.INSTANCE.selectedRectangle.get().sampleArea;
				double dX = event.getX() / transformImageView.getScaleX() - rectangle.getX();
				double dY = event.getY() / transformImageView.getScaleY() - rectangle.getY();
				if ( dX < 0 || dY < 0 || dX > rectangle.getWidth() || dY > rectangle.getHeight() )
					SharedState.INSTANCE.selectedRectangle.set(null);
			}
		});
		samplesImageViewGroup.setOnMouseClicked(event -> {
			if ( createRadioButton.isSelected() ) {
				final SamplesImageData imageData = SharedState.INSTANCE.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem());
				Image image = imageData.image;
				final double x = event.getX() / samplesImageView.getScaleX();
				final double y = event.getY() / samplesImageView.getScaleY();
				final double centerX = Math.max(x, 0);
				final double centerY = Math.max(y, 0);
				final double size = image.getWidth() > image.getHeight() ? image.getHeight() / 15 : image.getWidth() / 15;
				final double radiusX = Math.min(centerX, Math.min(size, image.getWidth() - centerX));
				final double radiusY = Math.min(centerY, Math.min(size, image.getHeight() - centerY));
				for ( SamplesImageData img : SharedState.INSTANCE.samplesImages.values() )
					sampleFactory.createNewSample(img, centerX, centerY, radiusX, radiusY);
				final List< Sample > rectangles = imageData.samples;
				final Sample sample = rectangles.get(rectangles.size() - 1);
				samplesImageViewAnchor.getChildren().add(sample.sampleArea);
				samplesImageViewAnchor.getChildren().add(sample);
			} else {
				if ( SharedState.INSTANCE.selectedSample.isNotNull().get() ) {
					final Rectangle rectangle = SharedState.INSTANCE.selectedSample.get().sampleArea;
					double dX = event.getX() / samplesImageView.getScaleX() - rectangle.getX();
					double dY = event.getY() / samplesImageView.getScaleY() - rectangle.getY();
					if ( dX < 0 || dY < 0 || dX > rectangle.getWidth() || dY > rectangle.getHeight() )
						SharedState.INSTANCE.selectedSample.set(null);
				}
			}
		});

		samplesImageViewGroup.setOnMouseMoved(event -> {
			samplesMousePositionLabel.setText((int) ( event.getX() / samplesImageView.getScaleX() ) + " : " + (int) ( event.getY() / samplesImageView
					.getScaleY() ));
		});

		rotateRadioButton.selectedProperty().addListener(( observable, oldValue, newValue ) -> {
			SharedState.INSTANCE.rotate.set(newValue);
		});
	}

	private void setTooltips() {
		horizontalFlipButton.setTooltip(new Tooltip("Flip horizontally"));
		verticalFlipButton.setTooltip(new Tooltip("Flip vertically"));
		rotateLeftButton.setTooltip(new Tooltip("Rotate left by 90°"));
		rotateRightButton.setTooltip(new Tooltip("Rotate right by 90°"));
		loadImagesButton.setTooltip(new Tooltip("Load images"));
		deleteImageButton.setTooltip(new Tooltip("Remove image"));
		clearImagesButton.setTooltip(new Tooltip("Clear image list"));
		deleteSampleButton.setTooltip(new Tooltip("Remove sample"));
		clearSamplesButton.setTooltip(new Tooltip("Clear samples"));
		chartsRefreshButton.setTooltip(new Tooltip("Restore charts"));
		chartsMergeButton.setTooltip(new Tooltip("Merge chartse"));
		chartsShiftButton.setTooltip(new Tooltip("Extract chart"));
		chartsDeleteButton.setTooltip(new Tooltip("Remove charts"));
	}

	private void setImageViewControls( ImageView imageView, ScrollPane imageScrollPane, Group imageViewGroup, ComboBox< String > scaleCombo, Label
			mousePositionLabel ) {
		imageViewGroup.setOnMouseMoved(event -> mousePositionLabel.setText((int) ( event.getX() / imageView.getScaleX() ) + " : " + (int) ( event.getY() /
				imageView.getScaleY() )));
		imageViewGroup.setOnMouseExited(event -> mousePositionLabel.setText("- : -"));
		imageViewGroup.setOnScroll(event -> {
			if ( event.isControlDown() && imageView.getImage() != null) {
				double deltaY = event.getDeltaY();
				updateScrollbars(imageView, imageScrollPane, deltaY);
			}
		});
		imageScrollPane.addEventFilter(ScrollEvent.ANY, event -> {
			if ( event.isControlDown() && imageView.getImage() != null) {
				double deltaY = event.getDeltaY();
				updateScrollbars(imageView, imageScrollPane, deltaY);
				event.consume();
			}
		});
		imageView.scaleXProperty().addListener(( observable, oldValue, newValue ) -> {
			final double oldScale = oldValue.doubleValue();
			final double hValue = imageScrollPane.getHvalue();
			final double vValue = imageScrollPane.getVvalue();
			final double scale = newValue.doubleValue();
			imageView.setScaleY(scale);
			imageView.setTranslateX(imageView.getImage().getWidth() * 0.5 * ( scale - 1.0 ));
			imageView.setTranslateY(imageView.getImage().getHeight() * 0.5 * ( scale - 1.0 ));
			if ( Math.round(oldScale * 100) != Math.round(scale * 100) ) {
				validateScrollbars(imageView, imageScrollPane, scale, oldScale, hValue, vValue);
			}
			String asString = String.format("%.0f%%", newValue.doubleValue() * 100);
			if ( !scaleCombo.getValue().equals(asString) )
				scaleCombo.setValue(asString);
		});
		scaleCombo.valueProperty().addListener(( observable, oldValue, newValue ) -> {
			if ( !newValue.matches("\\d+%") )
				scaleCombo.setValue(oldValue);
			else
				imageView.setScaleX(Double.parseDouble(newValue.substring(0, newValue.length() - 1)) / 100.0);
		});
	}

	private void validateScrollbars( final ImageView imageView, final ScrollPane imageScrollPane, final double scale, final double oldScale,
									 final double hValue, final double vValue ) {
		if ( ( scale * imageView.getImage().getWidth() > imageScrollPane.getWidth() ) ) {
			double oldHDenominator = calculateDenominator(oldScale, imageView.getImage().getWidth(), imageScrollPane.getWidth());
			double newHDenominator = calculateDenominator(scale, imageView.getImage().getWidth(), imageScrollPane.getWidth());
			imageScrollPane.setHvalue(calculateValue(scale, oldScale, hValue, oldHDenominator, newHDenominator));
		}
		if ( ( scale * imageView.getImage().getHeight() > imageScrollPane.getHeight() ) ) {
			double oldVDenominator = calculateDenominator(oldScale, imageView.getImage().getHeight(), imageScrollPane.getHeight());
			double newVDenominator = calculateDenominator(scale, imageView.getImage().getHeight(), imageScrollPane.getHeight());
			imageScrollPane.setVvalue(calculateValue(scale, oldScale, vValue, oldVDenominator, newVDenominator));
		}
	}

	private double calculateValue( final double scale, final double oldScale, final double value, final double oldDenominator, final double newDenominator ) {
		return ( ( scale - 1 ) + ( value * oldDenominator - ( oldScale - 1 ) ) / oldScale * scale ) / newDenominator;
	}

	private double calculateDenominator( final double scale, final double imageSize, final double paneSize ) {
		return ( scale * imageSize - paneSize ) * 2 / paneSize;
	}

	private void updateScrollbars( final ImageView imageView, final ScrollPane imageScrollPane, final double deltaY ) {
		final double oldScale = imageView.getScaleX();
		final double hValue = imageScrollPane.getHvalue();
		final double vValue = imageScrollPane.getVvalue();
		if ( deltaY > 0 ) {
			imageView.setScaleX(imageView.getScaleX() * 1.05);
		} else {
			imageView.setScaleX(imageView.getScaleX() / 1.05);
		}
		final double scale = imageView.getScaleX();
		validateScrollbars(imageView, imageScrollPane, scale, oldScale, hValue, vValue);
	}

	private void setVisibilityBindings() {
		transformMenu.visibleProperty().bind(transformTab.selectedProperty());
		samplesMenu.visibleProperty().bind(samplesTab.selectedProperty());
		chartsMenu.visibleProperty().bind(chartsTab.selectedProperty());
		transformInfo.visibleProperty().bind(transformImageView.imageProperty().isNotNull());
		transformImageViewGroup.visibleProperty().bind(transformImageView.imageProperty().isNotNull());
		transformBottomGrid.visibleProperty().bind(transformImageView.imageProperty().isNotNull());
		transformLeftVBox.visibleProperty().bind(transformImageView.imageProperty().isNotNull());
		samplesLeftVBox.visibleProperty().bind(samplesImageView.imageProperty().isNotNull());
		samplesImageViewGroup.visibleProperty().bind(samplesImageView.imageProperty().isNotNull());
		samplesBottomGrid.visibleProperty().bind(samplesImageView.imageProperty().isNotNull());
		rightVBoxTabPane.visibleProperty().bind(samplesImageView.imageProperty().isNotNull());
		chartsBySampleRadioButton.visibleProperty().bind(bySampleTab.selectedProperty());
		imagesBySampleRadioButton.visibleProperty().bind(bySampleTab.selectedProperty());
		chartsBySampleGridScrollPane.visibleProperty().bind(chartsBySampleRadioButton.selectedProperty());
		imagesBySampleGridScrollPane.visibleProperty().bind(imagesBySampleRadioButton.selectedProperty());
		chartsSampleComboBox.visibleProperty().bind(bySampleTab.selectedProperty());
		chartsSampleLabel.visibleProperty().bind(bySampleTab.selectedProperty());
		chartsGraphsToolbar.visibleProperty().bind(Bindings.and(bySampleTab.selectedProperty(), chartsBySampleRadioButton.selectedProperty()));
		chartsGraphsHBox.visibleProperty().bind(Bindings.and(bySampleTab.selectedProperty(), chartsBySampleRadioButton.selectedProperty()));
		transformImageListInfo.visibleProperty().bind(Bindings.isEmpty(transformImageList.getItems()));
		sampleTitledPane.visibleProperty().bind(Bindings.isNotNull(SharedState.INSTANCE.selectedSample));
		vertexTitledPane.visibleProperty().bind(Bindings.isNotNull(SharedState.INSTANCE.selectedRectangle));
	}

	private void setEnablementBindings() {
		deleteImageButton.disableProperty().bind(transformImageList.getSelectionModel().selectedItemProperty().isNull());
		clearImagesButton.disableProperty().bind(Bindings.isEmpty(transformImageList.getItems()));
		IntegerProperty featuresSize = new SimpleIntegerProperty(featuresVBox.getChildren().size());
		BooleanBinding noFeaturesAvailable = Bindings.equal(0, featuresSize);
		BooleanBinding noFeaturesChosen = Bindings.createBooleanBinding(
				() -> featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
						.map(CheckBox.class::cast).noneMatch(CheckBox::isSelected),
				featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
						.map(CheckBox.class::cast).map(CheckBox::selectedProperty).toArray(Observable[]::new)
		);
		BooleanBinding noSamplesAdded = Bindings.createBooleanBinding(
				() -> !samplesImageViewAnchor.getChildren().stream().filter(Sample.class::isInstance)
						.findAny().isPresent(),
				samplesImageViewAnchor.getChildren()
		);
		deleteSampleButton.disableProperty().bind(SharedState.INSTANCE.selectedSample.isNull());
		clearSamplesButton.disableProperty().bind(noSamplesAdded);
		resultsButton.disableProperty().bind(Bindings.or(noSamplesAdded,
				Bindings.or(Bindings.isEmpty(samplesImageList.getItems()), Bindings.or(noFeaturesAvailable, noFeaturesChosen))));
		runMenuResultsButton.disableProperty().bind(Bindings.or(noSamplesAdded, Bindings.or(samplesTab.selectedProperty().not(),
				Bindings.or(Bindings.isEmpty(samplesImageList.getItems()), Bindings.or(noFeaturesAvailable, noFeaturesChosen)))));
		runMenuTransformButton.disableProperty().bind(Bindings.or(transformTab.selectedProperty().not(),
				Bindings.isEmpty(transformImageList.getItems())));
		transformImagesButton.disableProperty().bind(Bindings.isEmpty(transformImageList.getItems()));
		samplesTab.disableProperty().bind(Bindings.isEmpty(samplesImageList.getItems()));
		chartsTab.disableProperty().bind(SharedState.INSTANCE.results.isNull());

		fileMenuExportToCsv.disableProperty().bind(SharedState.INSTANCE.results.isNull());
		fileMenuExportToPng.disableProperty().bind(SharedState.INSTANCE.results.isNull());

		editMenuZoomIn.disableProperty().bind(Bindings.or(chartsTab.selectedProperty(), Bindings.or(Bindings.and(transformImageView.imageProperty().isNull(),
				transformTab.selectedProperty()), Bindings
				.and(samplesImageView.imageProperty().isNull(), samplesTab.selectedProperty()))));
		editMenuZoomOut.disableProperty().bind(Bindings.or(chartsTab.selectedProperty(), Bindings.or(Bindings.and(transformImageView.imageProperty().isNull(),
				transformTab.selectedProperty()),
				Bindings.and(samplesImageView.imageProperty().isNull(), samplesTab.selectedProperty()))));

		transformMenuVerticalFlip.disableProperty().bind(Bindings.or(transformImageView.imageProperty().isNull(), transformMenu.visibleProperty().not()));
		transformMenuHorizontalFlip.disableProperty().bind(Bindings.or(transformImageView.imageProperty().isNull(), transformMenu.visibleProperty().not()));
		transformMenuMenuRotateLeft.disableProperty().bind(Bindings.or(transformImageView.imageProperty().isNull(), transformMenu.visibleProperty().not()));
		transformMenuMenuRotateRight.disableProperty().bind(Bindings.or(transformImageView.imageProperty().isNull(), transformMenu.visibleProperty().not()));
		transformMenuLoadImages.disableProperty().bind(transformMenu.visibleProperty().not());
		transformMenuDeleteImage.disableProperty().bind(Bindings.or(transformImageList.getSelectionModel().selectedItemProperty().isNull(), transformMenu
				.visibleProperty().not()));
		transformMenuClear.disableProperty().bind(Bindings.or(Bindings.isEmpty(transformImageList.getItems()), transformMenu.visibleProperty().not()));

		samplesMenuCreateMode.disableProperty().bind(Bindings.or(samplesMenu.visibleProperty().not(), createRadioButton.selectedProperty()));
		samplesMenuSelectMode.disableProperty().bind(Bindings.or(samplesMenu.visibleProperty().not(), selectRadioButton.selectedProperty()));
		samplesMenuRotateMode.disableProperty().bind(Bindings.or(samplesMenu.visibleProperty().not(), rotateRadioButton.selectedProperty()));
		samplesMenuDeleteSample.disableProperty().bind(Bindings.or(SharedState.INSTANCE.selectedSample.isNull(), samplesMenu.visibleProperty().not()));
		samplesMenuClearSamples.disableProperty().bind(Bindings.or(samplesMenu.visibleProperty().not(), noSamplesAdded));
		samplesMenuSelectAllFeatures.disableProperty().bind(samplesMenu.visibleProperty().not());
		samplesMenuDeselectAllFeatures.disableProperty().bind(samplesMenu.visibleProperty().not());

		chartsMenuRestoreCharts.disableProperty().bind(Bindings.or(Bindings.or(chartsBySampleRadioButton.selectedProperty().not(), bySampleTab
				.selectedProperty().not()), chartsMenu.visibleProperty().not()));

		navMenuTransform.disableProperty().bind(transformTab.selectedProperty());
		navMenuSamples.disableProperty().bind(Bindings.or(Bindings.isEmpty(SharedState.INSTANCE.samplesImages), samplesTab.selectedProperty()));
		navMenuCharts.disableProperty().bind(Bindings.or(SharedState.INSTANCE.results.isNull(), chartsTab.selectedProperty()));
		navMenuAllCharts.disableProperty().bind(Bindings.and(chartsTab.selectedProperty(), allChartsTab
				.selectedProperty()));
		navMenuChartsBySample.disableProperty().bind(Bindings.and(chartsTab.selectedProperty(), Bindings.and
				(bySampleTab.selectedProperty(), chartsBySampleRadioButton.selectedProperty())));
		navMenuImagesBySample.disableProperty().bind( Bindings.and(chartsTab.selectedProperty(), Bindings.and
				(bySampleTab.selectedProperty(), imagesBySampleRadioButton.selectedProperty())));
	}

	private void setSelectionListeners() {
		transformImageList.getSelectionModel().selectedItemProperty().addListener(
				( observable, oldValue, newValue ) -> {
					if ( oldValue != null && !oldValue.isEmpty() ) {
						TransformImageData img = SharedState.INSTANCE.transformImages.get(oldValue);
						img.hScrollPos = transformScrollPane.getHvalue();
						img.vScrollPos = transformScrollPane.getVvalue();
						transformImageViewAnchor.getChildren().remove(img.triangle);
						transformImageViewAnchor.getChildren().removeAll(img.vertexSamples);
						transformImageViewAnchor.getChildren().removeAll(
								Arrays.stream(img.vertexSamples).map(r -> r.sampleArea).toArray(Rectangle[]::new)
						);
						SharedState.INSTANCE.selectedRectangle.set(null);
					}
					if ( newValue != null ) {
						TransformImageData img = SharedState.INSTANCE.transformImages.get(newValue);
						transformImageView.setImage(img.image);
						transformImageViewAnchor.getChildren().add(img.triangle);
						transformImageViewAnchor.getChildren().addAll(
								Arrays.stream(img.vertexSamples).map(r -> r.sampleArea).toArray(Rectangle[]::new)
						);
						transformImageViewAnchor.getChildren().addAll(img.vertexSamples);
						transformImageView.setScaleX(img.scale.get());
						transformScrollPane.setHvalue(img.hScrollPos);
						transformScrollPane.setVvalue(img.vScrollPos);
						transformImageSizeLabel.setText((int) img.image.getWidth() + "x" + (int) img.image.getHeight() + " px");
						triangleFillColor.setValue((Color) img.triangle.getFill());
						triangleStrokeColor.setValue((Color) img.triangle.getStroke());
						SharedState.INSTANCE.selectedRectangle.set(null);
					} else {
						transformImageView.setScaleX(1.0);
						transformImageView.setImage(null);
						transformImageSizeLabel.setText("");
						SharedState.INSTANCE.selectedRectangle.set(null);
					}
				}
		);
		samplesImageList.getSelectionModel().selectedItemProperty().addListener(
				( observable, oldValue, newValue ) -> {
					if ( oldValue != null && !oldValue.isEmpty() ) {
						SamplesImageData img = SharedState.INSTANCE.samplesImages.get(oldValue);
						img.hScrollPos = samplesScrollPane.getHvalue();
						img.vScrollPos = samplesScrollPane.getVvalue();
						samplesImageViewAnchor.getChildren().removeAll(img.samples);
						samplesImageViewAnchor.getChildren().removeAll(
								img.samples.stream().map(r -> r.sampleArea).toArray(Rectangle[]::new)
						);
						SharedState.INSTANCE.selectedSample.set(null);
					}
					if ( newValue != null ) {
						SamplesImageData img = SharedState.INSTANCE.samplesImages.get(newValue);
						samplesImageView.setImage(img.image);
						samplesImageViewAnchor.getChildren().addAll(
								img.samples.stream().map(r -> r.sampleArea).toArray(Rectangle[]::new)
						);
						samplesImageViewAnchor.getChildren().addAll(img.samples);
						samplesImageView.setScaleX(img.scale.get());
						samplesScrollPane.setHvalue(img.hScrollPos);
						samplesScrollPane.setVvalue(img.vScrollPos);
						samplesImageSizeLabel.setText((int) img.image.getWidth() + "x" + (int) img.image.getHeight() + " px");
						SharedState.INSTANCE.selectedSample.set(null);
					} else {
						samplesImageView.setImage(null);
						samplesImageSizeLabel.setText("");
						SharedState.INSTANCE.selectedSample.set(null);
					}
				}
		);
	}

	public void transform() {
		final Stage myDialog = showPopup("Transforming images");
		Task task = createTransformTask(myDialog);
		startRunnable(task);
	}

	private Task createTransformTask( final Stage myDialog ) {
		return new Task< Void >() {
			@Override
			protected Void call() throws Exception {
				transform(myDialog);
				return null;
			}
		};
	}

	private void startRunnable( final Task task ) {
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}

	private Stage showPopup( String info ) {
		final Stage myDialog = new Stage();
		myDialog.initOwner(root.getScene().getWindow());
		myDialog.initStyle(StageStyle.UNDECORATED);
		myDialog.initModality(Modality.APPLICATION_MODAL);
		final Label label = new Label(info);
		label.setAlignment(Pos.CENTER);
		final HBox box = new HBox(label, new ProgressIndicator(-1.0));
		box.setSpacing(30.0);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(25));
		Scene myDialogScene = new Scene(box);
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String theme = prefs.get(Controller.THEME_PREFERENCE_KEY, Controller.THEME_LIGHT);
		if ( theme.equals(Controller.THEME_LIGHT) ) {
			myDialogScene.getStylesheets().add(Controller.THEME_LIGHT);
		} else {
			myDialogScene.getStylesheets().add(Controller.THEME_DARK);
		}
		myDialog.setScene(myDialogScene);
		myDialog.show();
		return myDialog;
	}

	private void transform( final Stage myDialog ) {
		samplesImageList.getSelectionModel().clearSelection();
		samplesImageList.getItems().clear();
		SharedState.INSTANCE.samplesImages.clear();
		samplesImageViewAnchor.getChildren().removeIf(n -> !( n instanceof ImageView ));
		final Mat[] images = new Mat[SharedState.INSTANCE.transformImages.size()];
		int interpolation = cubicRadioButton.isSelected() ?
				INTER_CUBIC : linearRadioButton.isSelected() ?
				INTER_LINEAR : INTER_NEAREST;
		try {
			final MatOfPoint2f[] points = new MatOfPoint2f[SharedState.INSTANCE.transformImages.size()];
			int i = 0;
			for ( String key : transformImageList.getItems() ) {
				images[i] = SharedState.INSTANCE.transformImages.get(key).imageData;
				points[i] = SharedState.INSTANCE.transformImages.get(key).triangle.getMatOfPoints();
				i++;
			}
			final SamplesImageData[] result = transform(ImageUtils.getImagesCopy(images), points, interpolation);
			samplesImageList.getItems().addAll(transformImageList.getItems());
			for ( int j = 0; j < result.length; j++ )
				SharedState.INSTANCE.samplesImages.put(samplesImageList.getItems().get(j), result[j]);
			Platform.runLater(() -> {
				mainTabPane.getSelectionModel().select(samplesTab);
				samplesImageList.getSelectionModel().selectFirst();
				samplesScrollPane.setHvalue(0.5);
				samplesScrollPane.setVvalue(0.5);
				myDialog.close();
			});
		} catch ( CvException e ) {
			Platform.runLater(() -> myDialog.close());
			showAlert("Transforming transformImages failed! Please check selected points.");
		}
	}

	private SamplesImageData[] transform( final Mat[] images, final MatOfPoint2f[] points, int interpolation ) {
		if ( images.length != points.length )
			throw new IllegalArgumentException("Images count does not match passed vertices count!");
		ImageUtils.performAffineTransformations(images, points, interpolation);
		return Arrays.stream(images)
				.map(i ->
						imageDataFactory.createSamplesImageData(
								//								ImageUtils.createImage(
								//										ImageUtils.getImageData(i), i.width(), i.height(), i.channels(), PixelFormat
								// .getByteBgraPreInstance()
								//								), i
								ImageUtils.createImage(i), i
						)
				).toArray(SamplesImageData[]::new);
	}

	public void deleteSample() {
		BaseSample r = SharedState.INSTANCE.selectedSample.get();
		if ( r != null ) {
			int index = -1;
			for ( SamplesImageData img : SharedState.INSTANCE.samplesImages.values() ) {
				index = img.samples.indexOf(r);
				if ( index >= 0 ) break;
			}
			for ( SamplesImageData img : SharedState.INSTANCE.samplesImages.values() ) {
				img.samples.remove(index);
			}
			SharedState.INSTANCE.selectedSample.set(null);
			samplesImageViewAnchor.getChildren().remove(r.sampleArea);
			samplesImageViewAnchor.getChildren().remove(r);
		}
	}

	public void clearSamples() {
		for ( SamplesImageData img : SharedState.INSTANCE.samplesImages.values() ) {
			img.samples.clear();
		}
		SharedState.INSTANCE.selectedSample.set(null);
		samplesImageViewAnchor.getChildren().removeIf(Sample.class::isInstance);
		samplesImageViewAnchor.getChildren().removeIf(Rectangle.class::isInstance);
	}


	public void setCreateMode() {
		if ( !createRadioButton.isSelected() ) createRadioButton.setSelected(true);
	}

	public void setSelectMode() {
		if ( !selectRadioButton.isSelected() ) selectRadioButton.setSelected(true);
	}

	public void setRotateMode() {
		if ( !rotateRadioButton.isSelected() ) rotateRadioButton.setSelected(true);
	}

	public void exportToPng() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose directory");
		File selectedDirectory = chooser.showDialog(root.getScene().getWindow());
		if ( selectedDirectory != null ) {
			if ( selectedDirectory.canWrite() ) {
				for ( int i = 0; i < samples.size(); i++ ) {
					List< Pair< String, ImageView > > currentSamples = samples.get(i);
					for ( int j = 0; j < currentSamples.size(); j++ ) {
						try {
							BufferedImage bimg = SwingFXUtils.fromFXImage(currentSamples.get(j).getValue().getImage(), null);
							ImageIO.write(bimg, "png",
									new File(selectedDirectory.getCanonicalPath()
											+ File.separator + "sample#" + ( i + 1 ) + "_" + currentSamples.get(j).getKey() + ".png"));
						} catch ( IOException e ) {
							e.printStackTrace();
							showAlert("Save failed! Check your write permissions.");
						}
					}
				}
			} else {
				showAlert("Save failed! Check your write permissions.");
			}
		}
	}

	public void imagesBySample() {
		if ( !chartsTab.isSelected() ) mainTabPane.getSelectionModel().select(chartsTab);
		if ( !bySampleTab.isSelected() ) chartsTabPane.getSelectionModel().select(bySampleTab);
		imagesBySampleRadioButton.setSelected(true);
	}

	public void help() {

	}
}
