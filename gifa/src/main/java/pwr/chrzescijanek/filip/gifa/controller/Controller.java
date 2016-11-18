package pwr.chrzescijanek.filip.gifa.controller;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.charts.Legend;
import com.sun.javafx.charts.Legend.LegendItem;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgcodecs.Imgcodecs;
import pwr.chrzescijanek.filip.gifa.core.generator.DataGenerator;
import pwr.chrzescijanek.filip.gifa.core.generator.DataGeneratorFactory;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;
import pwr.chrzescijanek.filip.gifa.core.util.Result;
import pwr.chrzescijanek.filip.gifa.model.image.ImageData;
import pwr.chrzescijanek.filip.gifa.model.image.ImageDataFactory;
import pwr.chrzescijanek.filip.gifa.model.image.ImageToAlignData;
import pwr.chrzescijanek.filip.gifa.model.image.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.sample.BasicSample;
import pwr.chrzescijanek.filip.gifa.model.sample.BasicSampleFactory;
import pwr.chrzescijanek.filip.gifa.model.sample.Sample;
import pwr.chrzescijanek.filip.gifa.model.sample.Vertex;
import pwr.chrzescijanek.filip.gifa.util.SharedState;
import pwr.chrzescijanek.filip.gifa.util.StageUtils;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.opencv.imgproc.Imgproc.INTER_CUBIC;
import static org.opencv.imgproc.Imgproc.INTER_LINEAR;
import static org.opencv.imgproc.Imgproc.INTER_NEAREST;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.createImage;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.getImagesCopy;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.getCSVFile;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.getDirectory;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.getHBoxWithLabelAndProgressIndicator;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.getHelpView;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.getImageFiles;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.getWebColor;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.prepareImage;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.startTask;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.writeImage;

/**
 * Application controller class.
 */
public class Controller extends BaseController implements Initializable {
	
	private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
	
	private static final String CHART_DEFAULT_STYLE = "-fx-border-color: null;";
	
	private static final String CHART_SELECTED_STYLE = "-fx-border-color: yellow; -fx-border-width: 3px;";
	
	/**
	 * Data generators factory.
	 */
	protected final DataGeneratorFactory dataGeneratorFactory;
	
	/**
	 * Samples factory.
	 */
	protected final BasicSampleFactory basicSampleFactory;
	
	/**
	 * Image data factory.
	 */
	protected final ImageDataFactory imageDataFactory;
	
	private final ObjectProperty<List<Result>> results = new SimpleObjectProperty<>(null);
	
	private final List<List<XYChart.Series<String, Number>>> series = new ArrayList<>();
	
	private final List<List<BarChart<String, Number>>> charts = new ArrayList<>();
	
	private final List<LineChart<String, Number>> summaryCharts = new ArrayList<>();
	
	private final List<Map<String, Color>> seriesColors = new ArrayList<>();
	
	private final Map<String, Color> summarySeriesColors = new HashMap<>();
	
	private final List<List<Pair<String, ImageView>>> samples = new ArrayList<>();
	
	private final Map<Object, String> columns = new HashMap<>();
	
	@FXML
	private AnchorPane samplesImageViewAnchor;
	
	@FXML
	private AnchorPane alignImageViewAnchor;
	
	@FXML
	private BorderPane chartsMainPane;
	
	@FXML
	private BorderPane featuresBorderPane;
	
	@FXML
	private BorderPane samplesBorderPane;
	
	@FXML
	private BorderPane alignBorderPane;
	
	@FXML
	private Button chartsRemoveButton;
	
	@FXML
	private Button chartsMergeButton;
	
	@FXML
	private Button chartsRefreshButton;
	
	@FXML
	private Button chartsExtractButton;
	
	@FXML
	private Button clearImagesButton;
	
	@FXML
	private Button clearSamplesButton;
	
	@FXML
	private Button removeImageButton;
	
	@FXML
	private Button removeSampleButton;
	
	@FXML
	private Button deselectAllButton;
	
	@FXML
	private Button horizontalFlipButton;
	
	@FXML
	private Button loadImagesButton;
	
	@FXML
	private Button calculateResultsButton;
	
	@FXML
	private Button rotateLeftButton;
	
	@FXML
	private Button rotateRightButton;
	
	@FXML
	private Button selectAllButton;
	
	@FXML
	private Button alignButton;
	
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
	private ComboBox<Integer> chartsSampleComboBox;
	
	@FXML
	private ComboBox<String> samplesScaleCombo;
	
	@FXML
	private ComboBox<String> alignScaleCombo;
	
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
	private GridPane alignBottomGrid;
	
	@FXML
	private GridPane alignColorControls;
	
	@FXML
	private GridPane alignImageListGrid;
	
	@FXML
	private GridPane alignMainPane;
	
	@FXML
	private GridPane alignToolsGridPane;
	
	@FXML
	private GridPane vertexColorControls;
	
	@FXML
	private Group samplesImageViewGroup;
	
	@FXML
	private Group alignImageViewGroup;
	
	@FXML
	private HBox chartsGraphsHBox;
	
	@FXML
	private HBox chartsGraphsToolbar;
	
	@FXML
	private HBox samplesTopHBox;
	
	@FXML
	private HBox selectionButtonsHBox;
	
	@FXML
	private HBox alignImageListToolbar;
	
	@FXML
	private HBox alignTopHBox;
	
	@FXML
	private ImageView samplesImageView;
	
	@FXML
	private ImageView alignImageView;
	
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
	private Label alignImageListInfo;
	
	@FXML
	private Label alignImageSizeLabel;
	
	@FXML
	private Label alignInfo;
	
	@FXML
	private Label alignMousePositionLabel;
	
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
	private ListView<String> samplesImageList;
	
	@FXML
	private ListView<String> alignImageList;
	
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
	private Menu alignMenu;
	
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
	private MenuItem navMenuAlign;
	
	@FXML
	private MenuItem runMenuCalculateResults;
	
	@FXML
	private MenuItem runMenuAlign;
	
	@FXML
	private MenuItem samplesMenuClearSamples;
	
	@FXML
	private MenuItem samplesMenuCreateMode;
	
	@FXML
	private MenuItem samplesMenuRemoveSample;
	
	@FXML
	private MenuItem samplesMenuDeselectAllFeatures;
	
	@FXML
	private MenuItem samplesMenuRotateMode;
	
	@FXML
	private MenuItem samplesMenuSelectAllFeatures;
	
	@FXML
	private MenuItem samplesMenuSelectMode;
	
	@FXML
	private MenuItem alignMenuClearImages;
	
	@FXML
	private MenuItem alignMenuRemoveImage;
	
	@FXML
	private MenuItem alignMenuHorizontalFlip;
	
	@FXML
	private MenuItem alignMenuLoadImages;
	
	@FXML
	private MenuItem alignMenuMenuRotateLeft;
	
	@FXML
	private MenuItem alignMenuMenuRotateRight;
	
	@FXML
	private MenuItem alignMenuVerticalFlip;
	
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
	private ScrollPane alignScrollPane;
	
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
	private Tab imageListTab;
	
	@FXML
	private Tab alignTab;
	
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
	private ToggleGroup bySampleToggle;
	
	@FXML
	private ToggleGroup themeToggleGroup;

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
	private VBox alignLeftVBox;
	
	/**
	 * Constructs new Controller with given shared state, data generators factory, samples factory
	 * and image data factory.
	 *
	 * @param state                shared state
	 * @param dataGeneratorFactory data generators factory
	 * @param basicSampleFactory   samples factory
	 * @param imageDataFactory     image data factory
	 */
	@Inject
	public Controller(final SharedState state, final DataGeneratorFactory dataGeneratorFactory,
	                  final BasicSampleFactory basicSampleFactory, final ImageDataFactory imageDataFactory) {
		super(state);
		this.dataGeneratorFactory = dataGeneratorFactory;
		this.basicSampleFactory = basicSampleFactory;
		this.imageDataFactory = imageDataFactory;
	}
	
	/**
	 * @return samples view image view anchor
	 */
	public AnchorPane getSamplesImageViewAnchor() {
		return samplesImageViewAnchor;
	}
	
	/**
	 * @return align view image view anchor
	 */
	public AnchorPane getAlignImageViewAnchor() {
		return alignImageViewAnchor;
	}
	
	/**
	 * @return charts view border pane
	 */
	public BorderPane getChartsMainPane() {
		return chartsMainPane;
	}
	
	/**
	 * @return features tab border pane
	 */
	public BorderPane getFeaturesBorderPane() {
		return featuresBorderPane;
	}
	
	/**
	 * @return samples view border pane
	 */
	public BorderPane getSamplesBorderPane() {
		return samplesBorderPane;
	}
	
	/**
	 * @return align view border pane
	 */
	public BorderPane getAlignBorderPane() {
		return alignBorderPane;
	}
	
	/**
	 * @return remove charts button
	 */
	public Button getChartsRemoveButton() {
		return chartsRemoveButton;
	}
	
	/**
	 * @return merge charts button
	 */
	public Button getChartsMergeButton() {
		return chartsMergeButton;
	}
	
	/**
	 * @return refresh charts button
	 */
	public Button getChartsRefreshButton() {
		return chartsRefreshButton;
	}
	
	/**
	 * @return charts extract button
	 */
	public Button getChartsExtractButton() {
		return chartsExtractButton;
	}
	
	/**
	 * @return clear images button
	 */
	public Button getClearImagesButton() {
		return clearImagesButton;
	}
	
	/**
	 * @return clear samples button
	 */
	public Button getClearSamplesButton() {
		return clearSamplesButton;
	}
	
	/**
	 * @return remove image button
	 */
	public Button getRemoveImageButton() {
		return removeImageButton;
	}
	
	/**
	 * @return remove sample button
	 */
	public Button getRemoveSampleButton() {
		return removeSampleButton;
	}
	
	/**
	 * @return deselect all button
	 */
	public Button getDeselectAllButton() {
		return deselectAllButton;
	}
	
	/**
	 * @return horizontal flip button
	 */
	public Button getHorizontalFlipButton() {
		return horizontalFlipButton;
	}
	
	/**
	 * @return load images button
	 */
	public Button getLoadImagesButton() {
		return loadImagesButton;
	}
	
	/**
	 * @return calculate results button
	 */
	public Button getCalculateResultsButton() {
		return calculateResultsButton;
	}
	
	/**
	 * @return rotate left button
	 */
	public Button getRotateLeftButton() {
		return rotateLeftButton;
	}
	
	/**
	 * @return rotate right button
	 */
	public Button getRotateRightButton() {
		return rotateRightButton;
	}
	
	/**
	 * @return select all button
	 */
	public Button getSelectAllButton() {
		return selectAllButton;
	}
	
	/**
	 * @return align button
	 */
	public Button getAlignButton() {
		return alignButton;
	}
	
	/**
	 * @return vertical flip button
	 */
	public Button getVerticalFlipButton() {
		return verticalFlipButton;
	}
	
	/**
	 * @return sample border color picker
	 */
	public ColorPicker getSampleBorderColor() {
		return sampleBorderColor;
	}
	
	/**
	 * @return sample fill color picker
	 */
	public ColorPicker getSampleFillColor() {
		return sampleFillColor;
	}
	
	/**
	 * @return sample stroke color picker
	 */
	public ColorPicker getSampleStrokeColor() {
		return sampleStrokeColor;
	}
	
	/**
	 * @return triangle fill color picker
	 */
	public ColorPicker getTriangleFillColor() {
		return triangleFillColor;
	}
	
	/**
	 * @return triangle stroke color picker
	 */
	public ColorPicker getTriangleStrokeColor() {
		return triangleStrokeColor;
	}
	
	/**
	 * @return vertex border color picker
	 */
	public ColorPicker getVertexBorderColor() {
		return vertexBorderColor;
	}
	
	/**
	 * @return vertex fill color picker
	 */
	public ColorPicker getVertexFillColor() {
		return vertexFillColor;
	}
	
	/**
	 * @return vertex stroke color picker
	 */
	public ColorPicker getVertexStrokeColor() {
		return vertexStrokeColor;
	}
	
	/**
	 * @return charts view sample combo box
	 */
	public ComboBox<Integer> getChartsSampleComboBox() {
		return chartsSampleComboBox;
	}
	
	/**
	 * @return samples view scale combo box
	 */
	public ComboBox<String> getSamplesScaleCombo() {
		return samplesScaleCombo;
	}
	
	/**
	 * @return align view scale combo box
	 */
	public ComboBox<String> getAlignScaleCombo() {
		return alignScaleCombo;
	}
	
	/**
	 * @return all charts view grid pane
	 */
	public GridPane getAllChartsGrid() {
		return allChartsGrid;
	}
	
	/**
	 * @return charts by sample view grid pane
	 */
	public GridPane getChartsBySampleGrid() {
		return chartsBySampleGrid;
	}
	
	/**
	 * @return charts view controls grid pane
	 */
	public GridPane getChartsControls() {
		return chartsControls;
	}
	
	/**
	 * @return images by sample view grid pane
	 */
	public GridPane getImagesBySampleGrid() {
		return imagesBySampleGrid;
	}
	
	/**
	 * @return root grid pane
	 */
	public GridPane getRoot() {
		return root;
	}
	
	/**
	 * @return samples view color controls grid pane
	 */
	public GridPane getSampleColorControls() {
		return sampleColorControls;
	}
	
	/**
	 * @return samples view bottom grid pane
	 */
	public GridPane getSamplesBottomGrid() {
		return samplesBottomGrid;
	}
	
	/**
	 * @return samples view image list grid pane
	 */
	public GridPane getSamplesImageListGrid() {
		return samplesImageListGrid;
	}
	
	/**
	 * @return samples view grid pane
	 */
	public GridPane getSamplesMainPane() {
		return samplesMainPane;
	}
	
	/**
	 * @return samples view tools grid pane
	 */
	public GridPane getSamplesToolsGridPane() {
		return samplesToolsGridPane;
	}
	
	/**
	 * @return align view bottom grid pane
	 */
	public GridPane getAlignBottomGrid() {
		return alignBottomGrid;
	}
	
	/**
	 * @return align view color controls grid pane
	 */
	public GridPane getAlignColorControls() {
		return alignColorControls;
	}
	
	/**
	 * @return align view image list grid pane
	 */
	public GridPane getAlignImageListGrid() {
		return alignImageListGrid;
	}
	
	/**
	 * @return align view grid pane
	 */
	public GridPane getAlignMainPane() {
		return alignMainPane;
	}
	
	/**
	 * @return align view tools grid pane
	 */
	public GridPane getAlignToolsGridPane() {
		return alignToolsGridPane;
	}
	
	/**
	 * @return align view vertex color controls grid pane
	 */
	public GridPane getVertexColorControls() {
		return vertexColorControls;
	}
	
	/**
	 * @return samples view image view group
	 */
	public Group getSamplesImageViewGroup() {
		return samplesImageViewGroup;
	}
	
	/**
	 * @return align view image view group
	 */
	public Group getAlignImageViewGroup() {
		return alignImageViewGroup;
	}
	
	/**
	 * @return charts view graphs horizontal box
	 */
	public HBox getChartsGraphsHBox() {
		return chartsGraphsHBox;
	}
	
	/**
	 * @return charts view graphs toolbar horizontal box
	 */
	public HBox getChartsGraphsToolbar() {
		return chartsGraphsToolbar;
	}
	
	/**
	 * @return samples view top horizontal box
	 */
	public HBox getSamplesTopHBox() {
		return samplesTopHBox;
	}
	
	/**
	 * @return selection buttons horizontal box
	 */
	public HBox getSelectionButtonsHBox() {
		return selectionButtonsHBox;
	}
	
	/**
	 * @return align view image list toolbar horizontal box
	 */
	public HBox getAlignImageListToolbar() {
		return alignImageListToolbar;
	}
	
	/**
	 * @return align view top horizontal box
	 */
	public HBox getAlignTopHBox() {
		return alignTopHBox;
	}
	
	/**
	 * @return samples view image view
	 */
	public ImageView getSamplesImageView() {
		return samplesImageView;
	}
	
	/**
	 * @return align view image view
	 */
	public ImageView getAlignImageView() {
		return alignImageView;
	}
	
	/**
	 * @return charts view max. column label
	 */
	public Label getChartsColumnsLabel() {
		return chartsColumnsLabel;
	}
	
	/**
	 * @return charts view graphs info label
	 */
	public Label getChartsGraphsInfo() {
		return chartsGraphsInfo;
	}
	
	/**
	 * @return charts view sample label
	 */
	public Label getChartsSampleLabel() {
		return chartsSampleLabel;
	}
	
	/**
	 * @return sample border color label
	 */
	public Label getSampleBorderLabel() {
		return sampleBorderLabel;
	}
	
	/**
	 * @return sample fill color label
	 */
	public Label getSampleFillLabel() {
		return sampleFillLabel;
	}
	
	/**
	 * @return sample stroke color label
	 */
	public Label getSampleStrokeLabel() {
		return sampleStrokeLabel;
	}
	
	/**
	 * @return samples view image size label
	 */
	public Label getSamplesImageSizeLabel() {
		return samplesImageSizeLabel;
	}
	
	/**
	 * @return samples view info label
	 */
	public Label getSamplesInfo() {
		return samplesInfo;
	}
	
	/**
	 * @return samples view mouse position label
	 */
	public Label getSamplesMousePositionLabel() {
		return samplesMousePositionLabel;
	}
	
	/**
	 * @return align view image list info label
	 */
	public Label getAlignImageListInfo() {
		return alignImageListInfo;
	}
	
	/**
	 * @return align view image size label
	 */
	public Label getAlignImageSizeLabel() {
		return alignImageSizeLabel;
	}
	
	/**
	 * @return align view info label
	 */
	public Label getAlignInfo() {
		return alignInfo;
	}
	
	/**
	 * @return align view mouse position label
	 */
	public Label getAlignMousePositionLabel() {
		return alignMousePositionLabel;
	}
	
	/**
	 * @return triangle fill label
	 */
	public Label getTriangleFillLabel() {
		return triangleFillLabel;
	}
	
	/**
	 * @return triangle stroke label
	 */
	public Label getTriangleStrokeLabel() {
		return triangleStrokeLabel;
	}
	
	/**
	 * @return vertex border label
	 */
	public Label getVertexBorderLabel() {
		return vertexBorderLabel;
	}
	
	/**
	 * @return vertex fill label
	 */
	public Label getVertexFillLabel() {
		return vertexFillLabel;
	}
	
	/**
	 * @return vertex stroke label
	 */
	public Label getVertexStrokeLabel() {
		return vertexStrokeLabel;
	}
	
	/**
	 * @return samples view image list
	 */
	public ListView<String> getSamplesImageList() {
		return samplesImageList;
	}
	
	/**
	 * @return align view image list
	 */
	public ListView<String> getAlignImageList() {
		return alignImageList;
	}
	
	/**
	 * @return charts menu
	 */
	public Menu getChartsMenu() {
		return chartsMenu;
	}
	
	/**
	 * @return edit menu
	 */
	public Menu getEditMenu() {
		return editMenu;
	}
	
	/**
	 * @return file menu
	 */
	public Menu getFileMenu() {
		return fileMenu;
	}
	
	/**
	 * @return help menu
	 */
	public Menu getHelpMenu() {
		return helpMenu;
	}
	
	/**
	 * @return navigation menu
	 */
	public Menu getNavMenu() {
		return navMenu;
	}
	
	/**
	 * @return options menu
	 */
	public Menu getOptionsMenu() {
		return optionsMenu;
	}
	
	/**
	 * @return options theme menu
	 */
	public Menu getOptionsMenuTheme() {
		return optionsMenuTheme;
	}
	
	/**
	 * @return run menu
	 */
	public Menu getRunMenu() {
		return runMenu;
	}
	
	/**
	 * @return samples menu
	 */
	public Menu getSamplesMenu() {
		return samplesMenu;
	}
	
	/**
	 * @return align menu
	 */
	public Menu getAlignMenu() {
		return alignMenu;
	}
	
	/**
	 * @return menu bar
	 */
	public MenuBar getMenuBar() {
		return menuBar;
	}
	
	/**
	 * @return charts menu extract chart menu item
	 */
	public MenuItem getChartsMenuExtractChart() {
		return chartsMenuExtractChart;
	}
	
	/**
	 * @return charts menu merge charts menu item
	 */
	public MenuItem getChartsMenuMergeCharts() {
		return chartsMenuMergeCharts;
	}
	
	/**
	 * @return charts menu remove charts menu item
	 */
	public MenuItem getChartsMenuRemoveCharts() {
		return chartsMenuRemoveCharts;
	}
	
	/**
	 * @return charts menu restore charts menu item
	 */
	public MenuItem getChartsMenuRestoreCharts() {
		return chartsMenuRestoreCharts;
	}
	
	/**
	 * @return edit menu zoom in menu item
	 */
	public MenuItem getEditMenuZoomIn() {
		return editMenuZoomIn;
	}
	
	/**
	 * @return edit menu zoom out menu item
	 */
	public MenuItem getEditMenuZoomOut() {
		return editMenuZoomOut;
	}
	
	/**
	 * @return file menu exit menu item
	 */
	public MenuItem getFileMenuExit() {
		return fileMenuExit;
	}
	
	/**
	 * @return file menu export to CSV menu item
	 */
	public MenuItem getFileMenuExportToCsv() {
		return fileMenuExportToCsv;
	}
	
	/**
	 * @return file menu export to PNG menu item
	 */
	public MenuItem getFileMenuExportToPng() {
		return fileMenuExportToPng;
	}
	
	/**
	 * @return help menu about menu item
	 */
	public MenuItem getHelpMenuAbout() {
		return helpMenuAbout;
	}
	
	/**
	 * @return help menu help menu item
	 */
	public MenuItem getHelpMenuHelp() {
		return helpMenuHelp;
	}
	
	/**
	 * @return navigation menu all charts menu item
	 */
	public MenuItem getNavMenuAllCharts() {
		return navMenuAllCharts;
	}
	
	/**
	 * @return navigation menu charts menu item
	 */
	public MenuItem getNavMenuCharts() {
		return navMenuCharts;
	}
	
	/**
	 * @return navigation menu charts by sample menu item
	 */
	public MenuItem getNavMenuChartsBySample() {
		return navMenuChartsBySample;
	}
	
	/**
	 * @return navigation menu images by sample menu item
	 */
	public MenuItem getNavMenuImagesBySample() {
		return navMenuImagesBySample;
	}
	
	/**
	 * @return navigation menu samples menu item
	 */
	public MenuItem getNavMenuSamples() {
		return navMenuSamples;
	}
	
	/**
	 * @return navigation menu align menu item
	 */
	public MenuItem getNavMenuAlign() {
		return navMenuAlign;
	}
	
	/**
	 * @return run menu calculate results menu item
	 */
	public MenuItem getRunMenuCalculateResults() {
		return runMenuCalculateResults;
	}
	
	/**
	 * @return run menu align menu item
	 */
	public MenuItem getRunMenuAlign() {
		return runMenuAlign;
	}
	
	/**
	 * @return samples menu clear samples menu item
	 */
	public MenuItem getSamplesMenuClearSamples() {
		return samplesMenuClearSamples;
	}
	
	/**
	 * @return samples menu create mode menu item
	 */
	public MenuItem getSamplesMenuCreateMode() {
		return samplesMenuCreateMode;
	}
	
	/**
	 * @return samples menu remove sample menu item
	 */
	public MenuItem getSamplesMenuRemoveSample() {
		return samplesMenuRemoveSample;
	}
	
	/**
	 * @return samples menu deselect all features menu item
	 */
	public MenuItem getSamplesMenuDeselectAllFeatures() {
		return samplesMenuDeselectAllFeatures;
	}
	
	/**
	 * @return samples menu rotate mode menu item
	 */
	public MenuItem getSamplesMenuRotateMode() {
		return samplesMenuRotateMode;
	}
	
	/**
	 * @return samples menu select all features menu item
	 */
	public MenuItem getSamplesMenuSelectAllFeatures() {
		return samplesMenuSelectAllFeatures;
	}
	
	/**
	 * @return samples menu select mode menu item
	 */
	public MenuItem getSamplesMenuSelectMode() {
		return samplesMenuSelectMode;
	}
	
	/**
	 * @return align menu clear images menu item
	 */
	public MenuItem getAlignMenuClearImages() {
		return alignMenuClearImages;
	}
	
	/**
	 * @return align menu remove image menu item
	 */
	public MenuItem getAlignMenuRemoveImage() {
		return alignMenuRemoveImage;
	}
	
	/**
	 * @return align menu horizontal flip menu item
	 */
	public MenuItem getAlignMenuHorizontalFlip() {
		return alignMenuHorizontalFlip;
	}
	
	/**
	 * @return align menu load images menu item
	 */
	public MenuItem getAlignMenuLoadImages() {
		return alignMenuLoadImages;
	}
	
	/**
	 * @return align menu rotate left menu item
	 */
	public MenuItem getAlignMenuMenuRotateLeft() {
		return alignMenuMenuRotateLeft;
	}
	
	/**
	 * @return align menu rotate right menu item
	 */
	public MenuItem getAlignMenuMenuRotateRight() {
		return alignMenuMenuRotateRight;
	}
	
	/**
	 * @return align menu vertical flip menu item
	 */
	public MenuItem getAlignMenuVerticalFlip() {
		return alignMenuVerticalFlip;
	}
	
	/**
	 * @return charts by sample radio button
	 */
	public RadioButton getChartsBySampleRadioButton() {
		return chartsBySampleRadioButton;
	}
	
	/**
	 * @return create radio button
	 */
	public RadioButton getCreateRadioButton() {
		return createRadioButton;
	}
	
	/**
	 * @return cubic interpolation radio button
	 */
	public RadioButton getCubicRadioButton() {
		return cubicRadioButton;
	}
	
	/**
	 * @return images by sample radio button
	 */
	public RadioButton getImagesBySampleRadioButton() {
		return imagesBySampleRadioButton;
	}
	
	/**
	 * @return linear interpolation radio button
	 */
	public RadioButton getLinearRadioButton() {
		return linearRadioButton;
	}
	
	/**
	 * @return nearest neighbour interpolation radio button
	 */
	public RadioButton getNearestRadioButton() {
		return nearestRadioButton;
	}
	
	/**
	 * @return rotate radio button
	 */
	public RadioButton getRotateRadioButton() {
		return rotateRadioButton;
	}
	
	/**
	 * @return select radio button
	 */
	public RadioButton getSelectRadioButton() {
		return selectRadioButton;
	}
	
	/**
	 * @return options menu dark theme radio menu item
	 */
	public RadioMenuItem getOptionsMenuThemeDark() {
		return optionsMenuThemeDark;
	}
	
	/**
	 * @return options menu light theme radio menu item
	 */
	public RadioMenuItem getOptionsMenuThemeLight() {
		return optionsMenuThemeLight;
	}
	
	/**
	 * @return resources
	 */
	public ResourceBundle getResources() {
		return resources;
	}
	
	/**
	 * @return all charts view's scroll pane containing grid pane
	 */
	public ScrollPane getAllChartsGridScrollPane() {
		return allChartsGridScrollPane;
	}
	
	/**
	 * @return charts by sample view's scroll pane containing grid pane
	 */
	public ScrollPane getChartsBySampleGridScrollPane() {
		return chartsBySampleGridScrollPane;
	}
	
	/**
	 * @return features scroll pane
	 */
	public ScrollPane getFeaturesScrollPane() {
		return featuresScrollPane;
	}
	
	/**
	 * @return images by sample view's scroll pane containing grid pane
	 */
	public ScrollPane getImagesBySampleGridScrollPane() {
		return imagesBySampleGridScrollPane;
	}
	
	/**
	 * @return samples view scroll pane
	 */
	public ScrollPane getSamplesScrollPane() {
		return samplesScrollPane;
	}
	
	/**
	 * @return align view scroll pane
	 */
	public ScrollPane getAlignScrollPane() {
		return alignScrollPane;
	}
	
	/**
	 * @return all charts tab
	 */
	public Tab getAllChartsTab() {
		return allChartsTab;
	}
	
	/**
	 * @return by sample tab
	 */
	public Tab getBySampleTab() {
		return bySampleTab;
	}
	
	/**
	 * @return charts tab
	 */
	public Tab getChartsTab() {
		return chartsTab;
	}
	
	/**
	 * @return features tab
	 */
	public Tab getFeaturesTab() {
		return featuresTab;
	}
	
	/**
	 * @return samples tab
	 */
	public Tab getSamplesTab() {
		return samplesTab;
	}
	
	/**
	 * @return image list tab
	 */
	public Tab getImageListTab() {
		return imageListTab;
	}
	
	/**
	 * @return align tab
	 */
	public Tab getAlignTab() {
		return alignTab;
	}
	
	/**
	 * @return charts tab pane
	 */
	public TabPane getChartsTabPane() {
		return chartsTabPane;
	}
	
	/**
	 * @return main tab pane
	 */
	public TabPane getMainTabPane() {
		return mainTabPane;
	}
	
	/**
	 * @return samples view right tab pane
	 */
	public TabPane getRightVBoxTabPane() {
		return rightVBoxTabPane;
	}
	
	/**
	 * @return max. columns text field
	 */
	public TextField getChartsColumnsTextField() {
		return chartsColumnsTextField;
	}
	
	/**
	 * @return interpolation group titled pane
	 */
	public TitledPane getInterpolationTitledPane() {
		return interpolationTitledPane;
	}
	
	/**
	 * @return sample group titled pane
	 */
	public TitledPane getSampleTitledPane() {
		return sampleTitledPane;
	}
	
	/**
	 * @return samples view mode group titled pane
	 */
	public TitledPane getSamplesModeTitledPane() {
		return samplesModeTitledPane;
	}
	
	/**
	 * @return samples view tools group titled pane
	 */
	public TitledPane getSamplesToolsTitledPane() {
		return samplesToolsTitledPane;
	}
	
	/**
	 * @return align view tools group titled pane
	 */
	public TitledPane getToolsTitledPane() {
		return toolsTitledPane;
	}
	
	/**
	 * @return triangle group titled pane
	 */
	public TitledPane getTriangleTitledPane() {
		return triangleTitledPane;
	}
	
	/**
	 * @return vertex group titled pane
	 */
	public TitledPane getVertexTitledPane() {
		return vertexTitledPane;
	}
	
	/**
	 * @return draw method toggle group
	 */
	public ToggleGroup getDrawMethod() {
		return drawMethod;
	}
	
	/**
	 * @return interpolation toggle group
	 */
	public ToggleGroup getInterpolation() {
		return interpolation;
	}
	
	/**
	 * @return by sample toggle group
	 */
	public ToggleGroup getBySampleToggle() {
		return bySampleToggle;
	}
	
	/**
	 * @return theme toggle group
	 */
	public ToggleGroup getThemeToggleGroup() {
		return themeToggleGroup;
	}

	/**
	 * @return location
	 */
	public URL getLocation() {
		return location;
	}
	
	/**
	 * @return features vertical box
	 */
	public VBox getFeaturesVBox() {
		return featuresVBox;
	}
	
	/**
	 * @return interpolation vertical box
	 */
	public VBox getInterpolationVBox() {
		return interpolationVBox;
	}
	
	/**
	 * @return samples view right vertical box
	 */
	public VBox getRightVBox() {
		return rightVBox;
	}
	
	/**
	 * @return samples view left vertical box
	 */
	public VBox getSamplesLeftVBox() {
		return samplesLeftVBox;
	}
	
	/**
	 * @return samples view mode vertical box
	 */
	public VBox getSamplesModeVBox() {
		return samplesModeVBox;
	}
	
	/**
	 * @return align view left vertical box
	 */
	public VBox getAlignLeftVBox() {
		return alignLeftVBox;
	}
	
	@FXML
	void about() {
		final Alert alert = StageUtils.getAboutDialog();
		final DialogPane dialogPane = alert.getDialogPane();
		injectStylesheets(dialogPane);
		alert.show();
	}
	
	@FXML
	void applyDarkTheme() {
		setDarkTheme();
	}
	
	@FXML
	void applyLightTheme() {
		setLightTheme();
	}
	
	@FXML
	void refresh() {
		final Integer index = chartsSampleComboBox.getValue() - 1;
		createCharts(index);
		placeCharts();
		validateChartsControlsDisableProperties();
	}
	
	private void createCharts(final int index) {
		final List<Series<String, Number>> series = this.series.get(index);
		final List<BarChart<String, Number>> charts = this.charts.get(index);
		if (charts != null) charts.clear();
		for (final Series s : series) {
			final BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), createValueAxis());
			chart.getData().addAll(s);
			registerOnChartClicked(chart);
			charts.add(chart);
		}
	}
	
	private NumberAxis createValueAxis() {
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Value");
		return yAxis;
	}
	
	private void registerOnChartClicked(final BarChart<String, Number> chart) {
		chart.setOnMouseClicked(event -> {
			if (chart.getStyle().equals(CHART_SELECTED_STYLE))
				chart.setStyle(CHART_DEFAULT_STYLE);
			else
				chart.setStyle(CHART_SELECTED_STYLE);
			
			validateChartsControlsDisableProperties();
		});
	}
	
	@FXML
	void deselectAllFunctions() {
		if (!featuresTab.isSelected()) rightVBoxTabPane.getSelectionModel().select(featuresTab);
		for (final Node chb : featuresVBox.getChildren()) {
			((CheckBox) chb).setSelected(false);
		}
		dataGeneratorFactory.clearChosenFunctions();
	}
	
	@FXML
	void exit() {
		root.getScene().getWindow().hide();
	}
	
	@FXML
	void exportToCsv() {
		final File csvFile = getCSVFile(root.getScene().getWindow());
		if (csvFile != null) {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
				bw.write(createCsvContents());
			} catch (final IOException e) {
				handleException(e, "Save failed! Check your write permissions.");
			}
		}
	}
	
	private String createCsvContents() {
		final Set<String> names = results.get().stream()
		                                 .flatMap(r -> r.getScores().keySet().stream()).collect(Collectors.toSet());
		final TreeSet<String> functions = new TreeSet<>(names);
		String csvContents = createHeader(functions);
		csvContents += appendSampleScores(functions, results.get());
		return csvContents;
	}
	
	private void handleException(final Exception e, final String alert) {
		LOGGER.log(Level.SEVERE, e.toString(), e);
		Platform.runLater(() -> showAlert(alert));
	}
	
	private String createHeader(final TreeSet<String> functions) {
		String csvContents = "Sample,Image";
		for (final String s : functions)
			csvContents += ",\"" + s + "\"";
		return csvContents;
	}
	
	private String appendSampleScores(final TreeSet<String> functions, final List<Result> results) {
		String csvContents = "";
		for (int i = 0; i < results.size(); i++)
			csvContents += appendSampleScores(functions, results.get(i), i + 1);
		return csvContents;
	}
	
	private void showAlert(final String content) {
		final Alert alert = StageUtils.getErrorAlert(content);
		final DialogPane dialogPane = alert.getDialogPane();
		injectStylesheets(dialogPane);
		alert.showAndWait();
	}
	
	private String appendSampleScores(final TreeSet<String> functions, final Result result, final int sample) {
		final List<List<String>> imageNames = result.getImageNames().stream().distinct().collect(Collectors.toList());
		String csvContents = "";
		for (final List<String> names : imageNames) {
			for (int i = 0; i < names.size(); i++) {
				csvContents += "\r\n" + sample + ",\"" + names.get(i) + "\"";
				int index = 0;
				for (final String s : functions) {
					if (!result.getImageNames().get(index).contains(names.get(i)))
						csvContents += ",";
					else {
						final double[] doubles = result.getScores().get(s).stream().mapToDouble(Double::doubleValue)
						                               .toArray();
						csvContents += ",\"" + doubles[i] + "\"";
					}
					index++;
				}
			}
		}
		return csvContents;
	}
	
	@FXML
	void flipHorizontal() {
		final String imageName = alignImageList.getSelectionModel().getSelectedItem();
		final ImageToAlignData img = state.imagesToAlign.get(imageName);
		Core.flip(img.imageData, img.imageData, 1);
		final Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		final Image fxImage = createImage(imageCopy);
		refreshImage(img, fxImage);
	}
	
	private void refreshImage(final ImageToAlignData img, final Image fxImage) {
		img.image.set(fxImage);
		img.writableImage.set(new WritableImage(fxImage.getPixelReader(), (int) fxImage.getWidth(), (int) fxImage
				.getHeight()));
		alignImageView.setImage(fxImage);
		img.recalculateVertices();
		setImageViewTranslates(alignImageView);
		alignImageSizeLabel.setText((int) fxImage.getWidth() + "x" + (int) fxImage.getHeight() + " px");
	}
	
	private void setImageViewTranslates(final ImageView view) {
		view.setTranslateX(view.getImage().getWidth() * 0.5 * (view.getScaleX() - 1.0));
		view.setTranslateY(view.getImage().getHeight() * 0.5 * (view.getScaleY() - 1.0));
	}
	
	@FXML
	void flipVertical() {
		final String imageName = alignImageList.getSelectionModel().getSelectedItem();
		final ImageToAlignData img = state.imagesToAlign.get(imageName);
		Core.flip(img.imageData, img.imageData, 0);
		final Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		final Image fxImage = createImage(imageCopy);
		refreshImage(img, fxImage);
	}
	
	@FXML
	void rotateLeft() {
		final String imageName = alignImageList.getSelectionModel().getSelectedItem();
		final ImageToAlignData img = state.imagesToAlign.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 0);
		final Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		final Image fxImage = createImage(imageCopy);
		refreshImage(img, fxImage);
	}
	
	@FXML
	void rotateRight() {
		final String imageName = alignImageList.getSelectionModel().getSelectedItem();
		final ImageToAlignData img = state.imagesToAlign.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 1);
		final Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		final Image fxImage = createImage(imageCopy);
		refreshImage(img, fxImage);
	}
	
	private void createSummaryCharts() {
		final List<LineChart<String, Number>> charts = populateCharts();
		bindColors(charts);
		saveCharts(charts);
		placeSummaryCharts();
	}
	
	private List<LineChart<String, Number>> populateCharts() {
		final List<LineChart<String, Number>> charts = new ArrayList<>();
		final List<Result> summary = results.get();
		final Set<String> collectedNames = summary.stream().flatMap(r -> r.getScores().keySet().stream())
		                                          .collect(Collectors.toSet());
		final Set<String> functions = new TreeSet<>(collectedNames);
		for (final String function : functions)
			populateCharts(charts, summary, function);
		return charts;
	}
	
	private void populateCharts(final List<LineChart<String, Number>> charts, final List<Result> summary,
	                            final String function) {
		final LineChart<String, Number> chart = new LineChart<>(createCategoryAxis(), createValueAxis());
		chart.setTitle(function);
		final List<Series<String, Number>> series = createSeries(charts.size(), summary, function);
		series.forEach(chart.getData()::add);
		charts.add(chart);
	}
	
	private CategoryAxis createCategoryAxis() {
		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Sample");
		return xAxis;
	}
	
	private List<Series<String, Number>> createSeries(final int chartIndex, final List<Result> summary,
	                                                  final String function) {
		final List<Double[]> results = summary.stream()
		                                      .map(r -> r.getScores().get(function)
		                                                 .toArray(new Double[0]))
		                                      .collect(Collectors.toList());
		final List<Series<String, Number>> series = populateSeries(chartIndex, summary, results);
		return series;
	}
	
	private List<Series<String, Number>> populateSeries(final int chartIndex, final List<Result> summary,
	                                                    final List<Double[]> results) {
		final List<Series<String, Number>> series = new ArrayList<>();
		for (int i = 0; i < results.size(); i++) {
			final double[] scores = Arrays.stream(results.get(i)).mapToDouble(Double::doubleValue).toArray();
			final List<List<String>> names = summary.get(i).getImageNames();
			populateSeries(chartIndex, series, scores, names, i);
		}
		return series;
	}
	
	private void populateSeries(final int chartIndex, final List<Series<String, Number>> series, final double[] scores,
	                            final List<List<String>> names, final int index) {
		for (int i = 0; i < scores.length; i++) {
			if (series.size() == i) {
				final Series<String, Number> s = new Series<>();
				s.setName(Optional.ofNullable(names.get(chartIndex).get(i)).orElse("Series " + (i + 1)));
				series.add(s);
			}
			final Series<String, Number> current = series.get(i);
			current.getData().add(new XYChart.Data<>("" + (index + 1), scores[i]));
		}
	}
	
	private void bindColors(final List<LineChart<String, Number>> charts) {
		final List<Color> defaultColors = Arrays.asList(
				Color.web("#f3622d"), Color.web("#fba71b"), Color.web("#57b757"), Color.web("#41a9c9"),
				Color.web("#4258c9"), Color.web("#9a42c8"), Color.web("#c84164"), Color.web("#888888"));
		for (final LineChart<String, Number> chart : charts) {
			for (int i = 0; i < chart.getData().size(); i++) {
				final Series<String, Number> series = chart.getData().get(i);
				final LegendItem item = getLegendItem(chart, series);
				if (item != null)
					item.setSymbol(initializeColorPicker(defaultColors, chart, series, i));
			}
		}
	}
	
	private LegendItem getLegendItem(final LineChart<? extends String, ? extends Number> chart,
	                                 final Series<? extends String, ? extends Number> series) {
		return chart.getChildrenUnmodifiable().stream()
		            .filter(x -> x instanceof Legend).findAny()
		            .map(l -> ((Legend) l).getItems())
		            .map(it -> it.stream()
		                         .filter(r -> r.getText().equals(series.getName())).findAny().orElse(null))
		            .orElse(null);
	}
	
	private ColorPicker initializeColorPicker(final List<Color> defaultColors, final LineChart<String, Number> chart,
	                                          final Series<String, Number> series, final int index) {
		final ColorPicker picker = new ColorPicker();
		picker.setMaxWidth(12);
		picker.valueProperty().addListener((observable, oldValue, newValue) -> {
			final String web = getWebColor(newValue);
			updateColors(chart, series, newValue, web);
		});
		picker.setValue(summarySeriesColors.get(chart.getTitle() + "/" + series.getName()) == null ?
				                defaultColors.get(index % defaultColors.size())
				                : summarySeriesColors.get(chart.getTitle() + "/" + series.getName()));
		return picker;
	}
	
	private void updateColors(final LineChart<String, Number> chart, final Series<String, Number> s, final Color
			newValue, final String web) {
		s.getNode().setStyle("-fx-stroke: " + web + ";");
		s.getData().forEach(n -> n.getNode().setStyle("-fx-background-color: " + web + ", white;"));
		summarySeriesColors.put(chart.getTitle() + "/" + s.getName(), newValue);
	}
	
	private void saveCharts(final List<LineChart<String, Number>> charts) {
		summaryCharts.clear();
		summaryCharts.addAll(charts);
	}
	
	private void placeSummaryCharts() {
		calculateColumnsAndRows(chartsColumnsTextField, summaryCharts);
		placeNodes(summaryCharts, allChartsGrid);
	}
	
	private List<Series<String, Number>> generateSeries(final Result results) {
		final List<List<String>> imageNames = results.getImageNames();
		final List<Series<String, Number>> series = new ArrayList<>();
		int index = 0;
		for (final Entry<String, UnmodifiableArrayList<Double>> e : results.getScores().entrySet()) {
			final Series<String, Number> s = new Series<>();
			s.setName(e.getKey());
			for (int i = 0; i < e.getValue().size(); i++) {
				s.getData().add(new XYChart.Data<>(imageNames.get(index).get(i), e.getValue().get(i)));
			}
			series.add(s);
			index++;
		}
		return series;
	}
	
	@FXML
	void selectAllFunctions() {
		if (!featuresTab.isSelected()) rightVBoxTabPane.getSelectionModel().select(featuresTab);
		for (final Node chb : featuresVBox.getChildren()) {
			((CheckBox) chb).setSelected(true);
		}
		dataGeneratorFactory.chooseAllAvailableFunctions();
	}
	
	@FXML
	void merge() {
		final BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), createValueAxis());
		chartsBySampleGrid.getChildren().stream().filter(node -> node.getStyle().equals(CHART_SELECTED_STYLE))
		                  .map(node -> ((BarChart) node).getData()).forEach(series -> chart.getData().addAll(series));
		registerOnChartClicked(chart);
		final Integer index = findMergeIndex();
		charts.get(chartsSampleComboBox.getValue() - 1).removeAll(getSelectedCharts());
		charts.get(chartsSampleComboBox.getValue() - 1).add(index, chart);
		placeCharts();
		validateChartsControlsDisableProperties();
	}
	
	private Integer findMergeIndex() {
		return chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals(CHART_SELECTED_STYLE))
		                         .map(n -> charts.get(chartsSampleComboBox.getValue() - 1).indexOf(n))
		                         .min(Integer::compareTo).orElse(0);
	}
	
	@FXML
	void extract() {
		final BarChart<String, Number> chart = (BarChart<String, Number>) getSelectedCharts().get(0);
		final Integer index = chartsSampleComboBox.getValue() - 1;
		populateNewCharts(chart, index);
	}
	
	private void populateNewCharts(final BarChart<String, Number> chart, final Integer index) {
		final BarChart<String, Number> firstChart = new BarChart<>(new CategoryAxis(), createValueAxis());
		final BarChart<String, Number> secondChart = new BarChart<>(new CategoryAxis(), createValueAxis());
		final Series s = findLastSeries(chart, index);
		final Series[] other = getAllOtherSeries(chart, index, s);
		firstChart.getData().add(s);
		secondChart.getData().addAll(other);
		registerOnChartClicked(firstChart);
		registerOnChartClicked(secondChart);
		placeShiftedCharts(firstChart, secondChart, chart);
	}
	
	private Series findLastSeries(final BarChart<String, Number> chart, final Integer index) {
		return series.get(index).stream()
		             .filter(n -> n.getName().equals(chart.getData().get(chart.getData().size() - 1).getName()))
		             .findAny().orElse(null);
	}
	
	private Series[] getAllOtherSeries(final BarChart<String, Number> chart, final Integer index, final Series s) {
		final List<String> names = chart.getData().stream()
		                                .filter(n -> !n.getName().equals(s.getName()))
		                                .map(Series::getName).collect(Collectors.toList());
		return series.get(index).stream().filter(n -> names.contains(n.getName())).toArray(Series[]::new);
	}
	
	private void placeShiftedCharts(final BarChart<String, Number> firstChart, final BarChart<String, Number>
			secondChart,
	                                final BarChart<String, Number> chart) {
		final Integer index = chartsBySampleGrid.getChildren().stream()
		                                        .filter(n -> n.getStyle().equals(CHART_SELECTED_STYLE))
		                                        .map(n -> charts.get(chartsSampleComboBox.getValue() - 1)
		                                                        .indexOf(n)).min(Integer::compareTo)
		                                        .orElse(charts.get(chartsSampleComboBox.getValue() - 1).size() - 1);
		charts.get(chartsSampleComboBox.getValue() - 1).remove(chart);
		charts.get(chartsSampleComboBox.getValue() - 1).add(index, firstChart);
		charts.get(chartsSampleComboBox.getValue() - 1).add(index, secondChart);
		placeCharts();
		validateChartsControlsDisableProperties();
	}
	
	@FXML
	void delete() {
		charts.get(chartsSampleComboBox.getValue() - 1).removeAll(getSelectedCharts());
		placeCharts();
		validateChartsControlsDisableProperties();
	}
	
	private List<Node> getSelectedCharts() {
		return chartsBySampleGrid.getChildren().stream()
		                         .filter(n -> n.getStyle().equals(CHART_SELECTED_STYLE))
		                         .collect(Collectors.toList());
	}
	
	private void placeCharts() {
		final Integer index = chartsSampleComboBox.getValue() - 1;
		final List<BarChart<String, Number>> charts = this.charts.get(index);
		calculateColumnsAndRows(chartsColumnsTextField, charts);
		placeNodes(charts, chartsBySampleGrid);
		colorSeries(index);
	}
	
	private void validateChartsControlsDisableProperties() {
		final List<Node> nodes = getSelectedCharts();
		final Integer seriesSize = checkSizes();
		final boolean sameSeries = checkNames(nodes);
		chartsExtractButton.setDisable(nodes.size() != 1 || seriesSize <= 1);
		chartsRemoveButton.setDisable(nodes.isEmpty());
		chartsMergeButton.setDisable(nodes.size() < 2 || !sameSeries);
		chartsMenuMergeCharts.setDisable(nodes.size() < 2);
		chartsMenuExtractChart.setDisable(nodes.size() != 1 || seriesSize <= 1);
		chartsMenuRemoveCharts.setDisable(nodes.isEmpty());
	}

	private void colorSeries(final int index) {
		final List<BarChart<String, Number>> charts = this.charts.get(index);
		final List<Series<String, Number>> series = this.series.get(index);
		final List<Color> defaultColors = Arrays.asList(Color.web("#f3622d"), Color.web("#fba71b"), Color.web
				                                                ("#57b757"), Color.web("#41a9c9"),
		                                                Color.web("#4258c9"), Color.web("#9a42c8"), Color.web
						("#c84164"), Color.web("#888888"));

		for (int i = 0; i < series.size(); i++) {
			final Series<String, Number> s = series.get(i);
			final LegendItem item = getLegendItem(charts, s);
			if (item != null)
				item.setSymbol(instantiateColorPicker(defaultColors, index, charts, i, s));
		}
	}

	private Integer checkSizes() {
		return chartsBySampleGrid
				.getChildren()
				.stream()
				.filter(n -> n.getStyle().equals(CHART_SELECTED_STYLE))
				.map(n -> ((BarChart) n).getData().size())
				.reduce(Integer::sum).orElse(0);
	}
	
	private boolean checkNames(final List<Node> nodes) {
		final List<List<String>> seriesNames = new ArrayList<>();
		for (int i = 0; i < nodes.size(); i++) {
			final List<Series<String, Number>> series = ((BarChart<String, Number>) nodes.get(i)).getData();
			final List<String> names = new ArrayList<>();
			for (int j = 0; j < series.size(); j++) {
				names.add(series.get(j).getData().get(j).getXValue());
			}
			seriesNames.add(names);
		}
		return seriesNames.stream().allMatch(list -> list.equals(seriesNames.get(0)));
	}
	
	private LegendItem getLegendItem(final List<BarChart<String, Number>> charts, final Series<? extends String, ?
			extends Number>
			series) {
		return charts.stream()
		             .map(Parent::getChildrenUnmodifiable)
		             .map(nodes -> nodes.stream()
		                                .filter(node -> node instanceof Legend).findAny().orElse(null))
		             .filter(Objects::nonNull)
		             .map(legend -> ((Legend) legend).getItems())
		             .map(items -> items.stream()
		                                .filter(item -> item.getText().equals(series.getName())).findAny()
		                                .orElse(null))
		             .filter(Objects::nonNull)
		             .findAny().orElse(null);
	}
	
	private ColorPicker instantiateColorPicker(final List<Color> defaultColors, final int chartIndex, final
	List<BarChart<String,
			Number>> charts, final int seriesIndex, final Series<? extends String, ? extends Number> series) {
		final ColorPicker picker = new ColorPicker();
		picker.setMaxWidth(12);
		picker.valueProperty().addListener((observable, oldValue, newValue) -> {
			final String web = getWebColor(newValue);
			updateColors(chartIndex, charts, series, newValue, web);
		});
		picker.setValue(seriesColors.get(chartIndex).get(series.getName()) == null ?
				                defaultColors.get(seriesIndex % defaultColors.size())
				                : seriesColors.get(chartIndex).get(series.getName()));
		return picker;
	}
	
	private void updateColors(final int chartIndex, final List<BarChart<String, Number>> charts, final Series<?
			extends String, ?
			extends Number> series, final Color newValue, final String web) {
		charts.stream()
		      .map(chart -> chart.getData().stream().filter(s -> s.getName().equals(series.getName()))
		                         .findAny().orElse(null))
		      .filter(Objects::nonNull)
		      .map(Series::getData)
		      .forEach(data -> data.forEach(bar -> bar.getNode().setStyle("-fx-bar-fill: " + web + ";")));
		seriesColors.get(chartIndex).put(series.getName(), newValue);
	}
	
	@FXML
	void alignTab() {
		if (!alignTab.isSelected()) mainTabPane.getSelectionModel().select(alignTab);
	}
	
	@FXML
	void samplesTab() {
		if (!samplesTab.isSelected()) mainTabPane.getSelectionModel().select(samplesTab);
	}
	
	@FXML
	void chartsTab() {
		if (!chartsTab.isSelected()) mainTabPane.getSelectionModel().select(chartsTab);
	}
	
	@FXML
	void chartsBySample() {
		if (!chartsTab.isSelected()) mainTabPane.getSelectionModel().select(chartsTab);
		if (!bySampleTab.isSelected()) chartsTabPane.getSelectionModel().select(bySampleTab);
		chartsBySampleRadioButton.setSelected(true);
	}
	
	@FXML
	void imagesBySample() {
		if (!chartsTab.isSelected()) mainTabPane.getSelectionModel().select(chartsTab);
		if (!bySampleTab.isSelected()) chartsTabPane.getSelectionModel().select(bySampleTab);
		imagesBySampleRadioButton.setSelected(true);
	}
	
	@FXML
	void allCharts() {
		if (!chartsTab.isSelected()) mainTabPane.getSelectionModel().select(chartsTab);
		if (!allChartsTab.isSelected()) chartsTabPane.getSelectionModel().select(allChartsTab);
	}
	
	@FXML
	void zoomIn() {
		if (alignTab.isSelected())
			updateScrollbars(alignImageView, alignScrollPane, 1);
		else if (samplesTab.isSelected())
			updateScrollbars(samplesImageView, samplesScrollPane, 1);
	}
	
	private void updateScrollbars(final ImageView imageView, final ScrollPane imageScrollPane, final double deltaY) {
		final double oldScale = imageView.getScaleX();
		final double hValue = imageScrollPane.getHvalue();
		final double vValue = imageScrollPane.getVvalue();
		if (deltaY > 0) {
			imageView.setScaleX(imageView.getScaleX() * 1.05);
		}
		else {
			imageView.setScaleX(imageView.getScaleX() / 1.05);
		}
		final double scale = imageView.getScaleX();
		validateScrollbars(imageView, imageScrollPane, scale, oldScale, hValue, vValue);
	}
	
	private void validateScrollbars(final ImageView imageView, final ScrollPane imageScrollPane, final double scale,
	                                final double oldScale, final double hValue, final double vValue) {
		validateHorizontalScrollbar(imageView, imageScrollPane, scale, oldScale, hValue);
		validateVerticalScrollbar(imageView, imageScrollPane, scale, oldScale, vValue);
	}
	
	private void validateHorizontalScrollbar(final ImageView imageView, final ScrollPane imageScrollPane,
	                                         final double scale, final double oldScale, final double hValue) {
		if ((scale * imageView.getImage().getWidth() > imageScrollPane.getWidth())) {
			final double oldHDenominator = calculateDenominator(oldScale, imageView.getImage().getWidth(),
			                                                    imageScrollPane.getWidth());
			final double newHDenominator = calculateDenominator(scale, imageView.getImage().getWidth(),
			                                                    imageScrollPane.getWidth());
			imageScrollPane.setHvalue(calculateValue(scale, oldScale, hValue, oldHDenominator, newHDenominator));
		}
	}
	
	private void validateVerticalScrollbar(final ImageView imageView, final ScrollPane imageScrollPane, final double
			scale, final double
			                                       oldScale, final double vValue) {
		if ((scale * imageView.getImage().getHeight() > imageScrollPane.getHeight())) {
			final double oldVDenominator = calculateDenominator(oldScale, imageView.getImage().getHeight(),
			                                                    imageScrollPane.getHeight());
			final double newVDenominator = calculateDenominator(scale, imageView.getImage().getHeight(),
			                                                    imageScrollPane.getHeight());
			imageScrollPane.setVvalue(calculateValue(scale, oldScale, vValue, oldVDenominator, newVDenominator));
		}
	}
	
	private double calculateDenominator(final double scale, final double imageSize, final double paneSize) {
		return (scale * imageSize - paneSize) * 2 / paneSize;
	}
	
	private double calculateValue(final double scale, final double oldScale, final double value, final double
			oldDenominator, final double newDenominator) {
		return ((scale - 1) + (value * oldDenominator - (oldScale - 1)) / oldScale * scale) / newDenominator;
	}
	
	@FXML
	void zoomOut() {
		if (alignTab.isSelected())
			updateScrollbars(alignImageView, alignScrollPane, -1);
		else if (samplesTab.isSelected())
			updateScrollbars(samplesImageView, samplesScrollPane, -1);
	}
	
	@FXML
	void calculateResults() {
		final Stage dialog = showPopup("Calculating results");
		final Task<? extends Void> task = createCalculateResultsTask(dialog);
		startTask(task);
	}
	
	private Task<? extends Void> createCalculateResultsTask(final Stage dialog) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				calculateResults(dialog);
				return null;
			}
		};
	}
	
	private void calculateResults(final Stage dialog) {
		try {
			final SamplesImageData img = state.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem
					());
			resetFields();
			if (img != null) {
				calculateResults(img);
				Platform.runLater(() -> showCharts(dialog));
			}
		} catch (final CvException e) {
			handleException(dialog, e,
			                "Generating results failed! If you are using custom functions, check them for errors.");
		}
	}
	
	private void resetFields() {
		results.set(new ArrayList<>());
		series.clear();
		charts.clear();
		summaryCharts.clear();
		seriesColors.clear();
		summarySeriesColors.clear();
		samples.clear();
	}
	
	private void calculateResults(final SamplesImageData img) {
		for (int i = 0; i < img.samples.size(); i++) {
			samples.add(new ArrayList<>());
			final Mat[] images = prepareImages(i);
			final DataGenerator generator = dataGeneratorFactory.createGenerator();
			final Result result = generator
					.generateData(getImagesCopy(images), samplesImageList.getItems());
			addSamplesFromPreprocessedImages(i, generator);
			saveResults(i, result);
		}
	}
	
	private Mat[] prepareImages(final int i) {
		int index = 0;
		final Mat[] images = new Mat[state.samplesImages.size()];
		for (final String key : samplesImageList.getItems()) {
			final SamplesImageData imageData = state.samplesImages.get(key);
			prepareImage(imageData.samples.get(i), index, images, imageData);
			createView(i, images[index], key);
			index++;
		}
		return images;
	}
	
	private void createView(final int index, final Mat image, final String key) {
		final ImageView view = new ImageView(createImage(image));
		view.setPreserveRatio(true);
		bindSize(view, imagesBySampleGridScrollPane, 2);
		samples.get(index).add(new Pair<>(key, view));
	}
	
	private void addSamplesFromPreprocessedImages(final int index, final DataGenerator generator) {
		final Map<String, Pair<String[], Mat[]>> preprocessedImages = generator.getPreprocessedImages();
		for (final Entry<String, Pair<String[], Mat[]>> entry : preprocessedImages.entrySet())
			createViews(index, entry);
	}
	
	private void createViews(final int sampleIndex, final Entry<String, Pair<String[], Mat[]>> entry) {
		final String[] names = entry.getValue().getKey();
		final Mat[] images = entry.getValue().getValue();
		for (int i = 0; i < names.length; i++) {
			createView(sampleIndex, images[i], names[i].replaceAll("\\s+", "_") + "_" + entry.getKey().toLowerCase()
			                                                                                 .replaceAll("\\s+", "_"));
		}
	}
	
	private void saveResults(final int i, final Result result) {
		results.get().add(result);
		series.add(generateSeries(result));
		charts.add(new ArrayList<>());
		seriesColors.add(new HashMap<>());
		createCharts(i);
	}
	
	private void showCharts(final Stage dialog) {
		chartsSampleComboBox.setItems(FXCollections.observableArrayList(
				IntStream.range(1, results.get().size() + 1).boxed().collect(Collectors.toList())));
		chartsSampleComboBox.setValue(1);
		allCharts();
		createSummaryCharts();
		validateChartsControlsDisableProperties();
		dialog.close();
	}
	
	@FXML
	void loadImages() {
		final List<File> selectedFiles = getImageFiles(root.getScene().getWindow());
		if (selectedFiles != null) {
			final Stage dialog = showPopup("Loading images");
			final Task<? extends Void> task = createLoadImagesTask(dialog, selectedFiles);
			startTask(task);
		}
	}
	
	private Stage showPopup(final String info) {
		final Stage dialog = StageUtils.initDialog(root.getScene().getWindow());
		final HBox box = getHBoxWithLabelAndProgressIndicator(info);
		final Scene scene = new Scene(box);
		injectStylesheets(box);
		dialog.setScene(scene);
		dialog.show();
		return dialog;
	}
	
	private Task<? extends Void> createLoadImagesTask(final Stage dialog, final List<File> selectedFiles) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				loadImages(dialog, selectedFiles);
				return null;
			}
		};
	}
	
	private void loadImages(final Stage dialog, final List<File> selectedFiles) {
		for (final File f : selectedFiles) {
			final String filePath;
			try {
				filePath = f.getCanonicalPath();
				final Mat image = getImage(filePath);
				final Image fxImage = getFXImage(image);
				addNewImage(filePath, image, fxImage);
			} catch (IOException | CvException e) {
				handleException(dialog, e,
				                "Loading failed!\nImages might be corrupted, paths may contain non-ASCII symbols or "
				                + "you do not have sufficient read permissions.");
				break;
			}
		}
		showImage(dialog);
	}
	
	private Mat getImage(final String filePath) {
		final Mat image = Imgcodecs.imread(filePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
		if (image.dataAddr() == 0)
			throw new CvException("Failed to load image! Check if file path contains only ASCII symbols");
		return image;
	}
	
	private Image getFXImage(final Mat image) {
		final Mat imageCopy = ImageUtils.getImageCopy(image);
		return createImage(imageCopy);
	}
	
	private void addNewImage(final String filePath, final Mat image, final Image fxImage) {
		final String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
		if (state.imagesToAlign.containsKey(fileName)) {
			deleteImageWithName(fileName);
		}
		Platform.runLater(() -> {
			state.imagesToAlign.put(fileName, imageDataFactory.createImageToAlignData(fxImage, image));
			alignImageList.getItems().add(fileName);
		});
	}
	
	private void handleException(final Stage dialog, final Exception e, final String alert) {
		Platform.runLater(dialog::close);
		LOGGER.log(Level.SEVERE, e.toString(), e);
		Platform.runLater(() -> showAlert(alert));
	}
	
	private void showImage(final Stage dialog) {
		Platform.runLater(() -> {
			alignScrollPane.setHvalue(0.5);
			alignScrollPane.setVvalue(0.5);
			alignImageList.getSelectionModel().selectLast();
			dialog.close();
		});
	}
	
	private void deleteImageWithName(final String key) {
		Platform.runLater(() -> {
			alignImageList.getSelectionModel().clearSelection();
			alignImageList.getItems().remove(key);
			state.imagesToAlign.remove(key);
		});
	}
	
	@FXML
	void deleteImage() {
		final String key = alignImageList.getSelectionModel().getSelectedItem();
		if (key != null) {
			deleteImageWithName(key);
		}
	}
	
	@FXML
	void clearImages() {
		Platform.runLater(() -> {
			alignImageList.getSelectionModel().clearSelection();
			alignImageList.getItems().clear();
			state.imagesToAlign.clear();
		});
	}
	
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		//FXML fields load assertions
		assert samplesImageViewAnchor != null
				: "fx:id=\"samplesImageViewAnchor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignImageViewAnchor != null
				: "fx:id=\"alignImageViewAnchor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsMainPane != null
				: "fx:id=\"chartsMainPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert featuresBorderPane != null
				: "fx:id=\"featuresBorderPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesBorderPane != null
				: "fx:id=\"samplesBorderPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignBorderPane != null
				: "fx:id=\"alignBorderPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsRemoveButton != null
				: "fx:id=\"chartsRemoveButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsMergeButton != null
				: "fx:id=\"chartsMergeButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsRefreshButton != null
				: "fx:id=\"chartsRefreshButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsExtractButton != null
				: "fx:id=\"chartsExtractButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert clearImagesButton != null
				: "fx:id=\"clearImagesButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert clearSamplesButton != null
				: "fx:id=\"clearSamplesButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert removeImageButton != null
				: "fx:id=\"removeImageButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert removeSampleButton != null
				: "fx:id=\"removeSampleButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert deselectAllButton != null
				: "fx:id=\"deselectAllButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert horizontalFlipButton != null
				: "fx:id=\"horizontalFlipButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert loadImagesButton != null
				: "fx:id=\"loadImagesButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert calculateResultsButton != null
				: "fx:id=\"calculateResultsButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert rotateLeftButton != null
				: "fx:id=\"rotateLeftButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert rotateRightButton != null
				: "fx:id=\"rotateRightButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert selectAllButton != null
				: "fx:id=\"selectAllButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignButton != null
				: "fx:id=\"alignButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert verticalFlipButton != null
				: "fx:id=\"verticalFlipButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert sampleBorderColor != null
				: "fx:id=\"sampleBorderColor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert sampleFillColor != null
				: "fx:id=\"sampleFillColor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert sampleStrokeColor != null
				: "fx:id=\"sampleStrokeColor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert triangleFillColor != null
				: "fx:id=\"triangleFillColor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert triangleStrokeColor != null
				: "fx:id=\"triangleStrokeColor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert vertexBorderColor != null
				: "fx:id=\"vertexBorderColor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert vertexFillColor != null
				: "fx:id=\"vertexFillColor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert vertexStrokeColor != null
				: "fx:id=\"vertexStrokeColor\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsSampleComboBox != null
				: "fx:id=\"chartsSampleComboBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesScaleCombo != null
				: "fx:id=\"samplesScaleCombo\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignScaleCombo != null
				: "fx:id=\"alignScaleCombo\" was not injected: check your FXML file 'gifa.fxml'.";
		assert allChartsGrid != null
				: "fx:id=\"allChartsGrid\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsBySampleGrid != null
				: "fx:id=\"chartsBySampleGrid\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsControls != null
				: "fx:id=\"chartsControls\" was not injected: check your FXML file 'gifa.fxml'.";
		assert imagesBySampleGrid != null
				: "fx:id=\"imagesBySampleGrid\" was not injected: check your FXML file 'gifa.fxml'.";
		assert root != null
				: "fx:id=\"root\" was not injected: check your FXML file 'gifa.fxml'.";
		assert sampleColorControls != null
				: "fx:id=\"sampleColorControls\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesBottomGrid != null
				: "fx:id=\"samplesBottomGrid\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesImageListGrid != null
				: "fx:id=\"samplesImageListGrid\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMainPane != null
				: "fx:id=\"samplesMainPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesToolsGridPane != null
				: "fx:id=\"samplesToolsGridPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignBottomGrid != null
				: "fx:id=\"alignBottomGrid\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignColorControls != null
				: "fx:id=\"alignColorControls\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignImageListGrid != null
				: "fx:id=\"alignImageListGrid\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMainPane != null
				: "fx:id=\"alignMainPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignToolsGridPane != null
				: "fx:id=\"alignToolsGridPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert vertexColorControls != null
				: "fx:id=\"vertexColorControls\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesImageViewGroup != null
				: "fx:id=\"samplesImageViewGroup\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignImageViewGroup != null
				: "fx:id=\"alignImageViewGroup\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsGraphsHBox != null
				: "fx:id=\"chartsGraphsHBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsGraphsToolbar != null
				: "fx:id=\"chartsGraphsToolbar\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesTopHBox != null
				: "fx:id=\"samplesTopHBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert selectionButtonsHBox != null
				: "fx:id=\"selectionButtonsHBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignImageListToolbar != null
				: "fx:id=\"alignImageListToolbar\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignTopHBox != null
				: "fx:id=\"alignTopHBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesImageView != null
				: "fx:id=\"samplesImageView\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignImageView != null
				: "fx:id=\"alignImageView\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsColumnsLabel != null
				: "fx:id=\"chartsColumnsLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsGraphsInfo != null
				: "fx:id=\"chartsGraphsInfo\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsSampleLabel != null
				: "fx:id=\"chartsSampleLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert sampleBorderLabel != null
				: "fx:id=\"sampleBorderLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert sampleFillLabel != null
				: "fx:id=\"sampleFillLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert sampleStrokeLabel != null
				: "fx:id=\"sampleStrokeLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesImageSizeLabel != null
				: "fx:id=\"samplesImageSizeLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesInfo != null
				: "fx:id=\"samplesInfo\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMousePositionLabel != null
				: "fx:id=\"samplesMousePositionLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignImageListInfo != null
				: "fx:id=\"alignImageListInfo\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignImageSizeLabel != null
				: "fx:id=\"alignImageSizeLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignInfo != null
				: "fx:id=\"alignInfo\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMousePositionLabel != null
				: "fx:id=\"alignMousePositionLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert triangleFillLabel != null
				: "fx:id=\"triangleFillLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert triangleStrokeLabel != null
				: "fx:id=\"triangleStrokeLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert vertexBorderLabel != null
				: "fx:id=\"vertexBorderLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert vertexFillLabel != null
				: "fx:id=\"vertexFillLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert vertexStrokeLabel != null
				: "fx:id=\"vertexStrokeLabel\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesImageList != null
				: "fx:id=\"samplesImageList\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignImageList != null
				: "fx:id=\"alignImageList\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsMenu != null
				: "fx:id=\"chartsMenu\" was not injected: check your FXML file 'gifa.fxml'.";
		assert editMenu != null
				: "fx:id=\"editMenu\" was not injected: check your FXML file 'gifa.fxml'.";
		assert fileMenu != null
				: "fx:id=\"fileMenu\" was not injected: check your FXML file 'gifa.fxml'.";
		assert helpMenu != null
				: "fx:id=\"helpMenu\" was not injected: check your FXML file 'gifa.fxml'.";
		assert navMenu != null
				: "fx:id=\"navMenu\" was not injected: check your FXML file 'gifa.fxml'.";
		assert optionsMenu != null
				: "fx:id=\"optionsMenu\" was not injected: check your FXML file 'gifa.fxml'.";
		assert optionsMenuTheme != null
				: "fx:id=\"optionsMenuTheme\" was not injected: check your FXML file 'gifa.fxml'.";
		assert runMenu != null
				: "fx:id=\"runMenu\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMenu != null
				: "fx:id=\"samplesMenu\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMenu != null
				: "fx:id=\"alignMenu\" was not injected: check your FXML file 'gifa.fxml'.";
		assert menuBar != null
				: "fx:id=\"menuBar\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsMenuExtractChart != null
				: "fx:id=\"chartsMenuExtractChart\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsMenuMergeCharts != null
				: "fx:id=\"chartsMenuMergeCharts\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsMenuRemoveCharts != null
				: "fx:id=\"chartsMenuRemoveCharts\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsMenuRestoreCharts != null
				: "fx:id=\"chartsMenuRestoreCharts\" was not injected: check your FXML file 'gifa.fxml'.";
		assert editMenuZoomIn != null
				: "fx:id=\"editMenuZoomIn\" was not injected: check your FXML file 'gifa.fxml'.";
		assert editMenuZoomOut != null
				: "fx:id=\"editMenuZoomOut\" was not injected: check your FXML file 'gifa.fxml'.";
		assert fileMenuExit != null
				: "fx:id=\"fileMenuExit\" was not injected: check your FXML file 'gifa.fxml'.";
		assert fileMenuExportToCsv != null
				: "fx:id=\"fileMenuExportToCsv\" was not injected: check your FXML file 'gifa.fxml'.";
		assert fileMenuExportToPng != null
				: "fx:id=\"fileMenuExportToPng\" was not injected: check your FXML file 'gifa.fxml'.";
		assert helpMenuAbout != null
				: "fx:id=\"helpMenuAbout\" was not injected: check your FXML file 'gifa.fxml'.";
		assert helpMenuHelp != null
				: "fx:id=\"helpMenuHelp\" was not injected: check your FXML file 'gifa.fxml'.";
		assert navMenuAllCharts != null
				: "fx:id=\"navMenuAllCharts\" was not injected: check your FXML file 'gifa.fxml'.";
		assert navMenuCharts != null
				: "fx:id=\"navMenuCharts\" was not injected: check your FXML file 'gifa.fxml'.";
		assert navMenuChartsBySample != null
				: "fx:id=\"navMenuChartsBySample\" was not injected: check your FXML file 'gifa.fxml'.";
		assert navMenuImagesBySample != null
				: "fx:id=\"navMenuImagesBySample\" was not injected: check your FXML file 'gifa.fxml'.";
		assert navMenuSamples != null
				: "fx:id=\"navMenuSamples\" was not injected: check your FXML file 'gifa.fxml'.";
		assert navMenuAlign != null
				: "fx:id=\"navMenuAlign\" was not injected: check your FXML file 'gifa.fxml'.";
		assert runMenuCalculateResults != null
				: "fx:id=\"runMenuCalculateResults\" was not injected: check your FXML file 'gifa.fxml'.";
		assert runMenuAlign != null
				: "fx:id=\"runMenuAlign\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMenuClearSamples != null
				: "fx:id=\"samplesMenuClearSamples\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMenuCreateMode != null
				: "fx:id=\"samplesMenuCreateMode\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMenuRemoveSample != null
				: "fx:id=\"samplesMenuRemoveSample\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMenuDeselectAllFeatures != null
				: "fx:id=\"samplesMenuDeselectAllFeatures\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMenuRotateMode != null
				: "fx:id=\"samplesMenuRotateMode\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMenuSelectAllFeatures != null
				: "fx:id=\"samplesMenuSelectAllFeatures\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesMenuSelectMode != null
				: "fx:id=\"samplesMenuSelectMode\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMenuClearImages != null
				: "fx:id=\"alignMenuClearImages\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMenuRemoveImage != null
				: "fx:id=\"alignMenuRemoveImage\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMenuHorizontalFlip != null
				: "fx:id=\"alignMenuHorizontalFlip\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMenuLoadImages != null
				: "fx:id=\"alignMenuLoadImages\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMenuMenuRotateLeft != null
				: "fx:id=\"alignMenuMenuRotateLeft\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMenuMenuRotateRight != null
				: "fx:id=\"alignMenuMenuRotateRight\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignMenuVerticalFlip != null
				: "fx:id=\"alignMenuVerticalFlip\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsBySampleRadioButton != null
				: "fx:id=\"chartsBySampleRadioButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert createRadioButton != null
				: "fx:id=\"createRadioButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert cubicRadioButton != null
				: "fx:id=\"cubicRadioButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert imagesBySampleRadioButton != null
				: "fx:id=\"imagesBySampleRadioButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert linearRadioButton != null
				: "fx:id=\"linearRadioButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert nearestRadioButton != null
				: "fx:id=\"nearestRadioButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert rotateRadioButton != null
				: "fx:id=\"rotateRadioButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert selectRadioButton != null
				: "fx:id=\"selectRadioButton\" was not injected: check your FXML file 'gifa.fxml'.";
		assert optionsMenuThemeDark != null
				: "fx:id=\"optionsMenuThemeDark\" was not injected: check your FXML file 'gifa.fxml'.";
		assert optionsMenuThemeLight != null
				: "fx:id=\"optionsMenuThemeLight\" was not injected: check your FXML file 'gifa.fxml'.";
		assert allChartsGridScrollPane != null
				: "fx:id=\"allChartsGridScrollPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsBySampleGridScrollPane != null
				: "fx:id=\"chartsBySampleGridScrollPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert featuresScrollPane != null
				: "fx:id=\"featuresScrollPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert imagesBySampleGridScrollPane != null
				: "fx:id=\"imagesBySampleGridScrollPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesScrollPane != null
				: "fx:id=\"samplesScrollPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignScrollPane != null
				: "fx:id=\"alignScrollPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert allChartsTab != null
				: "fx:id=\"allChartsTab\" was not injected: check your FXML file 'gifa.fxml'.";
		assert bySampleTab != null
				: "fx:id=\"bySampleTab\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsTab != null
				: "fx:id=\"chartsTab\" was not injected: check your FXML file 'gifa.fxml'.";
		assert featuresTab != null
				: "fx:id=\"featuresTab\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesTab != null
				: "fx:id=\"samplesTab\" was not injected: check your FXML file 'gifa.fxml'.";
		assert imageListTab != null
				: "fx:id=\"imageListTab\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignTab != null
				: "fx:id=\"alignTab\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsTabPane != null
				: "fx:id=\"chartsTabPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert mainTabPane != null
				: "fx:id=\"mainTabPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert rightVBoxTabPane != null
				: "fx:id=\"rightVBoxTabPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert chartsColumnsTextField != null
				: "fx:id=\"chartsColumnsTextField\" was not injected: check your FXML file 'gifa.fxml'.";
		assert interpolationTitledPane != null
				: "fx:id=\"interpolationTitledPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert sampleTitledPane != null
				: "fx:id=\"sampleTitledPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesModeTitledPane != null
				: "fx:id=\"samplesModeTitledPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesToolsTitledPane != null
				: "fx:id=\"samplesToolsTitledPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert toolsTitledPane != null
				: "fx:id=\"toolsTitledPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert triangleTitledPane != null
				: "fx:id=\"triangleTitledPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert vertexTitledPane != null
				: "fx:id=\"vertexTitledPane\" was not injected: check your FXML file 'gifa.fxml'.";
		assert drawMethod != null
				: "fx:id=\"drawMethod\" was not injected: check your FXML file 'gifa.fxml'.";
		assert interpolation != null
				: "fx:id=\"interpolation\" was not injected: check your FXML file 'gifa.fxml'.";
		assert bySampleToggle != null
				: "fx:id=\"bySampleToggle\" was not injected: check your FXML file 'gifa.fxml'.";
		assert themeToggleGroup != null
				: "fx:id=\"themeToggleGroup\" was not injected: check your FXML file 'gifa.fxml'.";
		assert featuresVBox != null
				: "fx:id=\"featuresVBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert interpolationVBox != null
				: "fx:id=\"interpolationVBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert rightVBox != null
				: "fx:id=\"rightVBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesLeftVBox != null
				: "fx:id=\"samplesLeftVBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert samplesModeVBox != null
				: "fx:id=\"samplesModeVBox\" was not injected: check your FXML file 'gifa.fxml'.";
		assert alignLeftVBox != null
				: "fx:id=\"alignLeftVBox\" was not injected: check your FXML file 'gifa.fxml'.";
		
		initializeComponents(location, resources);
		setBindings();
		addListeners();
	}
	
	private void addListeners() {
		setSelectionListeners();
		addVertexZoomListener();
		addVertexSelectionListener();
		addSampleZoomListener();
		addSampleSelectionListener();
		addColumnsTextFieldListener();
		addScaleListeners();
		addOnMouseReleasedListeners();
		addOnMouseClickedListeners();
		addRotateListeners();
		addChartsListeners();
		setImageViewControls(alignImageView, alignScrollPane, alignImageViewGroup, alignScaleCombo,
		                     alignMousePositionLabel);
		setImageViewControls(samplesImageView, samplesScrollPane, samplesImageViewGroup, samplesScaleCombo,
		                     samplesMousePositionLabel);
	}
	
	private void setBindings() {
		setVisibilityBindings();
		setEnablementBindings();
	}
	
	private void initializeComponents(final URL location, final ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		initializeStyle();
		initializeComboBoxes();
		initializeColorPickers();
		createCheckBoxes();
		disableChartsControls();
		setTooltips();
	}
	
	private void addRotateListeners() {
		rotateRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
			state.rotate.set(newValue);
		});
	}
	
	private void addChartsListeners() {
		onChartsTabChanged();
		onBySampleToggleChanged();
	}
	
	private void onChartsTabChanged() {
		chartsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue.equals(allChartsTab)) {
				columns.put(allChartsTab, chartsColumnsTextField.getText());
				chartsColumnsTextField.setText(columns.getOrDefault(bySampleToggle.getSelectedToggle(), "3"));
				refreshSample(bySampleToggle.getSelectedToggle());
			}
			else {
				columns.put(bySampleToggle.getSelectedToggle(), chartsColumnsTextField.getText());
				chartsColumnsTextField.setText(columns.getOrDefault(allChartsTab, "3"));
			}
		});
	}
	
	private void onBySampleToggleChanged() {
		bySampleToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				columns.put(oldValue, chartsColumnsTextField.getText());
			}
			chartsColumnsTextField.setText(columns.getOrDefault(newValue, "3"));
			refreshSample(newValue);
		});
	}
	
	private void refreshSample(final Toggle newValue) {
		if (newValue.equals(chartsBySampleRadioButton))
			placeCharts();
		else if (newValue.equals(imagesBySampleRadioButton))
			placeImages();
	}
	
	private void addOnMouseClickedListeners() {
		setOnAlignImageMouseClicked();
		setOnSamplesImageMouseClicked();
	}
	
	private void setOnAlignImageMouseClicked() {
		alignImageViewGroup.setOnMouseClicked(event -> {
			if (state.selectedVertex.isNotNull().get())
				uncheck(event, state.selectedVertex, alignImageView);
		});
	}
	
	private void setOnSamplesImageMouseClicked() {
		samplesImageViewGroup.setOnMouseClicked(event -> {
			if (createRadioButton.isSelected()) {
				state.selectedSample.set(null);
				final SamplesImageData imageData = createNewSamples(event);
				showSamples(imageData);
			}
			else if (state.selectedSample.isNotNull().get())
				uncheck(event, state.selectedSample, samplesImageView);
		});
	}
	
	private void uncheck(final MouseEvent event, final ObjectProperty<? extends BasicSample> property, final ImageView
			samplesImageView) {
		final Rectangle rectangle = property.get().sampleArea;
		final double dX = event.getX() / samplesImageView.getScaleX() - rectangle.getX();
		final double dY = event.getY() / samplesImageView.getScaleY() - rectangle.getY();
		if (dX < 0 || dY < 0 || dX > rectangle.getWidth() || dY > rectangle.getHeight())
			property.set(null);
	}
	
	private SamplesImageData createNewSamples(final MouseEvent event) {
		final SamplesImageData imageData = state.samplesImages.get(samplesImageList.getSelectionModel()
		                                                                           .getSelectedItem());
		final Image image = imageData.image.get();
		final double x = event.getX() / samplesImageView.getScaleX();
		final double y = event.getY() / samplesImageView.getScaleY();
		final double centerX = Math.max(x, 0);
		final double centerY = Math.max(y, 0);
		final double size = image.getWidth() > image.getHeight() ? image.getHeight() / 15 : image.getWidth() / 15;
		final double radiusX = Math.min(centerX, Math.min(size, image.getWidth() - centerX));
		final double radiusY = Math.min(centerY, Math.min(size, image.getHeight() - centerY));
		for (final SamplesImageData img : state.samplesImages.values())
			basicSampleFactory.createNewSample(img, centerX, centerY, radiusX, radiusY);
		return imageData;
	}
	
	private void showSamples(final SamplesImageData imageData) {
		final List<Sample> samples = imageData.samples;
		final Sample sample = samples.get(samples.size() - 1);
		samplesImageViewAnchor.getChildren().add(sample.sampleArea);
		samplesImageViewAnchor.getChildren().add(sample);
	}
	
	private void addOnMouseReleasedListeners() {
		mainTabPane.setOnMouseReleased(event -> {
			alignImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
			samplesImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
		});
	}
	
	private void addScaleListeners() {
		alignImageView.scaleXProperty().addListener((observable, oldValue, newValue) -> {
			final double scale = newValue.doubleValue();
			final ImageToAlignData img = state.imagesToAlign.get(alignImageList.getSelectionModel().getSelectedItem());
			if (img != null) img.scale.set(scale);
		});
		
		samplesImageView.scaleXProperty().addListener((observable, oldValue, newValue) -> {
			final double scale = newValue.doubleValue();
			final SamplesImageData img =
					state.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem());
			if (img != null) img.scale.set(scale);
		});
	}
	
	private void addSampleSelectionListener() {
		state.selectedSample.addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				sampleFillColor.setValue((Color) newValue.getFill());
				sampleStrokeColor.setValue((Color) newValue.getStroke());
				sampleBorderColor.setValue((Color) newValue.sampleArea.getStroke());
				for (final Entry<String, SamplesImageData> e : state.samplesImages.entrySet()) {
					if (e.getValue().samples.contains(newValue)) {
						samplesTab();
						samplesImageList.getSelectionModel().select(e.getKey());
						break;
					}
				}
			}
		});
	}
	
	private void addVertexSelectionListener() {
		state.selectedVertex.addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				vertexFillColor.setValue((Color) newValue.getFill());
				vertexStrokeColor.setValue((Color) newValue.getStroke());
				vertexBorderColor.setValue((Color) newValue.sampleArea.getStroke());
				for (final Entry<String, ImageToAlignData> e : state.imagesToAlign.entrySet()) {
					if (Arrays.asList(e.getValue().vertices).contains(newValue)) {
						alignTab();
						alignImageList.getSelectionModel().select(e.getKey());
						break;
					}
				}
			}
		});
	}
	
	private void addSampleZoomListener() {
		state.zoomSample.addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				final Sample sample = state.selectedSample.get();
				calculateZoom(sample, samplesScrollPane, samplesImageView);
				state.zoomSample.set(false);
			}
		});
	}
	
	private void addVertexZoomListener() {
		state.zoomVertex.addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				final Vertex vertex = state.selectedVertex.get();
				calculateZoom(vertex, alignScrollPane, alignImageView);
				state.zoomVertex.set(false);
			}
		});
	}
	
	private void calculateZoom(final BasicSample sample, final ScrollPane pane, final ImageView view) {
		final double newX = Math.max(0, sample.sampleArea.getX() - 50);
		final double newY = Math.max(0, sample.sampleArea.getY() - 50);
		final double newWidth = sample.sampleArea.getWidth() + 100;
		final double newHeight = sample.sampleArea.getHeight() + 100;
		final double scale = pane.getWidth() / pane.getHeight() > newWidth / newHeight ?
				pane.getHeight() / newHeight
				: pane.getWidth() / newWidth;
		view.setScaleX(scale);
		final double newHDenominator = calculateDenominator(
				view.getScaleX(), view.getImage().getWidth(), pane.getWidth());
		final double newVDenominator = calculateDenominator(
				view.getScaleX(), view.getImage().getHeight(), pane.getHeight());
		pane.setHvalue((Math.max(0, (newX + newWidth / 2) * view.getScaleX() - (pane.getWidth() / 2))
		                / (pane.getWidth() / 2)) / newHDenominator);
		pane.setVvalue((Math.max(0, (newY + newHeight / 2) * view.getScaleX() - (pane.getHeight() / 2))
		                / (pane.getHeight() / 2)) / newVDenominator);
	}
	
	private void initializeColorPickers() {
		initializeTriangleColorPickers();
		initializeVertexColorPickers();
		initializeSampleColorPickers();
	}
	
	private void initializeTriangleColorPickers() {
		triangleFillColor.valueProperty()
		                 .addListener((observable, oldValue, newValue) ->
				                              state.imagesToAlign
						                              .get(alignImageList.getSelectionModel().getSelectedItem())
						                              .triangle.setFill(newValue));
		triangleStrokeColor.valueProperty()
		                   .addListener((observable, oldValue, newValue) ->
				                                state.imagesToAlign
						                                .get(alignImageList.getSelectionModel().getSelectedItem())
						                                .triangle.setStroke(newValue));
	}
	
	private void initializeVertexColorPickers() {
		vertexFillColor.valueProperty()
		               .addListener((observable, oldValue, newValue) -> state.selectedVertex.get().setFill(newValue));
		vertexStrokeColor.valueProperty()
		                 .addListener((observable, oldValue, newValue) ->
				                              state.selectedVertex.get().setStroke(newValue));
		vertexBorderColor.valueProperty()
		                 .addListener((observable, oldValue, newValue) ->
				                              state.selectedVertex.get().sampleArea.setStroke(newValue));
	}
	
	private void initializeSampleColorPickers() {
		sampleFillColor.valueProperty()
		               .addListener((observable, oldValue, newValue) -> state.selectedSample.get().setFill(newValue));
		sampleStrokeColor.valueProperty()
		                 .addListener((observable, oldValue, newValue) ->
				                              state.selectedSample.get().setStroke(newValue));
		sampleBorderColor.valueProperty()
		                 .addListener((observable, oldValue, newValue) ->
				                              state.selectedSample.get().sampleArea.setStroke(newValue));
	}
	
	private void disableChartsControls() {
		chartsExtractButton.setDisable(true);
		chartsRemoveButton.setDisable(true);
		chartsMergeButton.setDisable(true);
		chartsMenuMergeCharts.setDisable(true);
		chartsMenuExtractChart.setDisable(true);
		chartsMenuRemoveCharts.setDisable(true);
	}
	
	private void initializeComboBoxes() {
		initializeScaleComboBoxes();
		chartsSampleComboBox.getSelectionModel().selectedItemProperty()
		                    .addListener((observable, oldValue, newValue) -> {
			                    if (newValue != null) {
				                    placeCharts();
				                    placeImages();
				                    validateChartsControlsDisableProperties();
			                    }
		                    });
	}
	
	private void initializeScaleComboBoxes() {
		alignScaleCombo.itemsProperty().get().addAll(
				"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%", "250%", "500%", "1000%"
		);
		alignScaleCombo.setValue("100%");
		samplesScaleCombo.itemsProperty().get().addAll(
				"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%", "250%", "500%", "1000%"
		);
		samplesScaleCombo.setValue("100%");
	}
	
	private void initializeStyle() {
		injectStylesheets(root);
		if (isLightThemeSelected()) {
			themeToggleGroup.selectToggle(optionsMenuThemeLight);
		}
		else {
			themeToggleGroup.selectToggle(optionsMenuThemeDark);
		}
	}
	
	private void placeImages() {
		final Integer index = chartsSampleComboBox.getValue() - 1;
		final List<Pair<String, ImageView>> samples = this.samples.get(index);
		final List<VBox> images = createSampleVBoxes(samples);
		calculateColumnsAndRows(chartsColumnsTextField, images);
		placeNodes(images, imagesBySampleGrid);
	}
	
	private List<VBox> createSampleVBoxes(final List<Pair<String, ImageView>> samples) {
		return samples.stream().map(pair -> {
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER);
			vbox.setSpacing(10.0);
			vbox.getChildren().add(pair.getValue());
			vbox.getChildren().add(new Label(pair.getKey()));
			return vbox;
		}).collect(Collectors.toList());
	}
	
	private void addColumnsTextFieldListener() {
		chartsColumnsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[1-9]\\d*"))
				chartsColumnsTextField.setText(oldValue);
			else {
				if (bySampleTab.isSelected() && chartsBySampleRadioButton.isSelected())
					placeCharts();
				else if (bySampleTab.isSelected() && imagesBySampleRadioButton.isSelected())
					placeImages();
				else if (allChartsTab.isSelected())
					placeSummaryCharts();
			}
		});
	}
	
	private void createCheckBoxes() {
		for (final String function : dataGeneratorFactory.getAvailableFunctionsNames()) {
			final CheckBox checkBox = new CheckBox(function);
			featuresVBox.getChildren().add(checkBox);
			checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue)
					dataGeneratorFactory.chooseFunction(function);
				else
					dataGeneratorFactory.deselectFunction(function);
			});
			checkBox.setSelected(true);
		}
	}
	
	private void setTooltips() {
		setImagesControlsTooltips();
		setAlignControlsTooltips();
		setSamplesControlsTooltips();
		setChartsControlsTooltips();
	}
	
	private void setImagesControlsTooltips() {
		loadImagesButton.setTooltip(new Tooltip("Load images"));
		removeImageButton.setTooltip(new Tooltip("Remove image"));
		clearImagesButton.setTooltip(new Tooltip("Clear image list"));
	}
	
	private void setAlignControlsTooltips() {
		horizontalFlipButton.setTooltip(new Tooltip("Flip horizontally"));
		verticalFlipButton.setTooltip(new Tooltip("Flip vertically"));
		rotateLeftButton.setTooltip(new Tooltip("Rotate left by 90°"));
		rotateRightButton.setTooltip(new Tooltip("Rotate right by 90°"));
	}
	
	private void setSamplesControlsTooltips() {
		removeSampleButton.setTooltip(new Tooltip("Remove sample"));
		clearSamplesButton.setTooltip(new Tooltip("Clear samples"));
	}
	
	private void setChartsControlsTooltips() {
		chartsRefreshButton.setTooltip(new Tooltip("Restore charts"));
		chartsMergeButton.setTooltip(new Tooltip("Merge charts"));
		chartsExtractButton.setTooltip(new Tooltip("Extract chart"));
		chartsRemoveButton.setTooltip(new Tooltip("Remove charts"));
	}
	
	private void setImageViewControls(final ImageView imageView, final ScrollPane imageScrollPane,
	                                  final Group imageViewGroup, final ComboBox<String> scaleCombo,
	                                  final Label mousePositionLabel) {
		setImageViewGroupListeners(imageView, imageScrollPane, imageViewGroup, mousePositionLabel);
		setImageScrollPaneEventFilter(imageView, imageScrollPane);
		setImageViewScaleListener(imageView, imageScrollPane, scaleCombo);
		setComboBoxListener(imageView, scaleCombo);
	}
	
	private void setImageViewGroupListeners(final ImageView imageView, final ScrollPane imageScrollPane,
	                                        final Group imageViewGroup, final Label mousePositionLabel) {
		imageViewGroup.setOnMouseMoved(event -> mousePositionLabel.setText(
				(int) (event.getX() / imageView.getScaleX()) + " : " + (int) (event.getY() / imageView.getScaleY())));
		imageViewGroup.setOnMouseExited(event -> mousePositionLabel.setText("- : -"));
		imageViewGroup.setOnScroll(event -> {
			if (event.isControlDown() && imageView.getImage() != null) {
				final double deltaY = event.getDeltaY();
				updateScrollbars(imageView, imageScrollPane, deltaY);
			}
		});
	}
	
	private void setImageScrollPaneEventFilter(final ImageView imageView, final ScrollPane imageScrollPane) {
		imageScrollPane.addEventFilter(ScrollEvent.ANY, event -> {
			if (event.isControlDown() && imageView.getImage() != null) {
				final double deltaY = event.getDeltaY();
				updateScrollbars(imageView, imageScrollPane, deltaY);
				event.consume();
			}
		});
	}
	
	private void setImageViewScaleListener(final ImageView imageView, final ScrollPane imageScrollPane,
	                                       final ComboBox<String> scaleCombo) {
		imageView.scaleXProperty().addListener((observable, oldValue, newValue) -> {
			final double oldScale = oldValue.doubleValue();
			final double hValue = imageScrollPane.getHvalue();
			final double vValue = imageScrollPane.getVvalue();
			final double scale = newValue.doubleValue();
			imageView.setScaleY(scale);
			setImageViewTranslates(imageView);
			updateScrollbars(imageView, imageScrollPane, oldScale, hValue, vValue, scale);
			updateComboBox(scaleCombo, newValue);
		});
	}
	
	private void updateScrollbars(final ImageView imageView, final ScrollPane imageScrollPane,
	                              final double oldScale, final double hValue, final double vValue, final double
			                              scale) {
		if (Math.round(oldScale * 100) != Math.round(scale * 100)) {
			validateScrollbars(imageView, imageScrollPane, scale, oldScale, hValue, vValue);
		}
	}
	
	private void updateComboBox(final ComboBox<String> scaleCombo, final Number newValue) {
		final String asString = String.format("%.0f%%", newValue.doubleValue() * 100);
		if (!scaleCombo.getValue().equals(asString))
			scaleCombo.setValue(asString);
	}
	
	private void setComboBoxListener(final ImageView imageView, final ComboBox<String> scaleCombo) {
		scaleCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[1-9]\\d*%"))
				scaleCombo.setValue(oldValue);
			else
				imageView.setScaleX(Double.parseDouble(newValue.substring(0, newValue.length() - 1)) / 100.0);
		});
	}
	
	private void setVisibilityBindings() {
		menuVisibilityBindings();
		onAlignImageIsPresent();
		onSamplesImageIsPresent();
		setChartsControlsVisibilityBindings();
		alignImageListInfo.visibleProperty().bind(Bindings.isEmpty(alignImageList.getItems()));
		onSampleSelected();
	}
	
	private void menuVisibilityBindings() {
		alignMenu.visibleProperty().bind(alignTab.selectedProperty());
		samplesMenu.visibleProperty().bind(samplesTab.selectedProperty());
		chartsMenu.visibleProperty().bind(chartsTab.selectedProperty());
	}
	
	private void onAlignImageIsPresent() {
		final BooleanBinding alignImageIsPresent = alignImageView.imageProperty().isNotNull();
		alignInfo.visibleProperty().bind(alignImageIsPresent);
		alignLeftVBox.visibleProperty().bind(alignImageIsPresent);
		alignImageViewGroup.visibleProperty().bind(alignImageIsPresent);
		alignBottomGrid.visibleProperty().bind(alignImageIsPresent);
	}
	
	private void onSamplesImageIsPresent() {
		final BooleanBinding samplesImageIsPresent = samplesImageView.imageProperty().isNotNull();
		samplesLeftVBox.visibleProperty().bind(samplesImageIsPresent);
		samplesImageViewGroup.visibleProperty().bind(samplesImageIsPresent);
		samplesBottomGrid.visibleProperty().bind(samplesImageIsPresent);
		rightVBoxTabPane.visibleProperty().bind(samplesImageIsPresent);
	}
	
	private void setChartsControlsVisibilityBindings() {
		final ReadOnlyBooleanProperty bySampleTabSelected = bySampleTab.selectedProperty();
		onBySampleTabSelected(bySampleTabSelected);
		final BooleanProperty chartsBySampleSelected = chartsBySampleRadioButton.selectedProperty();
		chartsGraphsToolbar.visibleProperty().bind(Bindings.and(bySampleTabSelected, chartsBySampleSelected));
		chartsGraphsHBox.visibleProperty().bind(Bindings.and(bySampleTabSelected, chartsBySampleSelected));
		chartsBySampleGridScrollPane.visibleProperty().bind(chartsBySampleSelected);
		imagesBySampleGridScrollPane.visibleProperty().bind(imagesBySampleRadioButton.selectedProperty());
	}
	
	private void onBySampleTabSelected(final ReadOnlyBooleanProperty bySampleTabSelected) {
		chartsBySampleRadioButton.visibleProperty().bind(bySampleTabSelected);
		imagesBySampleRadioButton.visibleProperty().bind(bySampleTabSelected);
		chartsSampleComboBox.visibleProperty().bind(bySampleTabSelected);
		chartsSampleLabel.visibleProperty().bind(bySampleTabSelected);
	}
	
	private void onSampleSelected() {
		sampleTitledPane.visibleProperty().bind(Bindings.isNotNull(state.selectedSample));
		vertexTitledPane.visibleProperty().bind(Bindings.isNotNull(state.selectedVertex));
	}
	
	private void setEnablementBindings() {
		setImageControlsEnablementBindings();
		setOnResultsPresentEnablementBindings();
		setSamplesControlsEnablementBindings();
		setSamplesMenuEnablementBindings();
		
		final ReadOnlyBooleanProperty alignTabSelected = alignTab.selectedProperty();
		final ReadOnlyBooleanProperty samplesTabSelected = samplesTab.selectedProperty();
		final ReadOnlyBooleanProperty chartsTabSelected = chartsTab.selectedProperty();
		final ReadOnlyBooleanProperty bySampleTabSelected = bySampleTab.selectedProperty();
		final BooleanProperty chartsBySampleSelected = chartsBySampleRadioButton.selectedProperty();
		
		final BooleanBinding emptyAlignImages = Bindings.isEmpty(alignImageList.getItems());
		final BooleanBinding nullAlignImage = alignImageView.imageProperty().isNull();
		final BooleanBinding nullSamplesImage = samplesImageView.imageProperty().isNull();
		final BooleanBinding alignMenuNotVisible = alignMenu.visibleProperty().not();
		final BooleanBinding disableZoom = Bindings.or(
				chartsTabSelected, Bindings.or(Bindings.and(nullAlignImage, alignTabSelected),
				                               Bindings.and(nullSamplesImage, samplesTabSelected)));
		final BooleanBinding disableAlignControls = Bindings.or(nullAlignImage, alignMenuNotVisible);
		final BooleanBinding noImages = Bindings.or(emptyAlignImages, alignMenuNotVisible);
		final BooleanBinding bySampleTabAndChartsTabSelected = Bindings.and(chartsTabSelected, bySampleTabSelected);
		
		alignButton.disableProperty().bind(emptyAlignImages);
		
		chartsMenuRestoreCharts.disableProperty().bind(
				Bindings.or(Bindings.or(chartsBySampleSelected.not(), bySampleTabSelected.not()),
				            chartsMenu.visibleProperty().not()));
		
		alignMenuClearImages.disableProperty().bind(noImages);
		alignMenuVerticalFlip.disableProperty().bind(disableAlignControls);
		alignMenuHorizontalFlip.disableProperty().bind(disableAlignControls);
		alignMenuMenuRotateLeft.disableProperty().bind(disableAlignControls);
		alignMenuMenuRotateRight.disableProperty().bind(disableAlignControls);
		alignMenuLoadImages.disableProperty().bind(alignMenuNotVisible);
		alignMenuRemoveImage.disableProperty()
		                    .bind(Bindings.or(alignImageList.getSelectionModel().selectedItemProperty().isNull(),
		                                      alignMenuNotVisible));
		
		editMenuZoomIn.disableProperty().bind(disableZoom);
		editMenuZoomOut.disableProperty().bind(disableZoom);
		
		navMenuAlign.disableProperty().bind(alignTabSelected);
		navMenuSamples.disableProperty().bind(Bindings.or(Bindings.isEmpty(state.samplesImages), samplesTabSelected));
		navMenuCharts.disableProperty().bind(Bindings.or(results.isNull(), chartsTabSelected));
		navMenuAllCharts.disableProperty().bind(Bindings.or(navMenuCharts.disableProperty(), allChartsTab
				.selectedProperty()));
		navMenuChartsBySample.disableProperty().bind(Bindings.or(navMenuCharts.disableProperty(), Bindings.and(
				bySampleTabAndChartsTabSelected, chartsBySampleSelected)));
		navMenuImagesBySample.disableProperty().bind(Bindings.or(navMenuCharts.disableProperty(), Bindings.and
				(bySampleTabAndChartsTabSelected, imagesBySampleRadioButton.selectedProperty())));
		
		runMenuAlign.disableProperty().bind(noImages);
	}
	
	private void setImageControlsEnablementBindings() {
		removeImageButton.disableProperty().bind(alignImageList.getSelectionModel().selectedItemProperty().isNull());
		clearImagesButton.disableProperty().bind(Bindings.isEmpty(alignImageList.getItems()));
	}
	
	private void setOnResultsPresentEnablementBindings() {
		final BooleanBinding nullResults = results.isNull();
		fileMenuExportToCsv.disableProperty().bind(nullResults);
		fileMenuExportToPng.disableProperty().bind(nullResults);
		chartsTab.disableProperty().bind(nullResults);
	}
	
	private void setSamplesControlsEnablementBindings() {
		final IntegerProperty featuresSize = new SimpleIntegerProperty(featuresVBox.getChildren().size());
		final BooleanBinding noFeaturesAvailable = Bindings.equal(0, featuresSize);
		final BooleanBinding noFeaturesChosen = getNoFeaturesChosenBinding();
		final BooleanBinding noSamplesAdded = getNoSamplesAddedBinding();
		final BooleanBinding noSampleSelected = state.selectedSample.isNull();
		final BooleanBinding samplesMenuNotVisible = samplesMenu.visibleProperty().not();
		final BooleanBinding emptyList = Bindings.isEmpty(samplesImageList.getItems());
		final BooleanBinding cannotCalculateResults = Bindings.or(noSamplesAdded,
		                                                          Bindings.or(emptyList, Bindings.or
				                                                          (noFeaturesAvailable, noFeaturesChosen)));
		
		samplesTab.disableProperty().bind(emptyList);
		removeSampleButton.disableProperty().bind(noSampleSelected);
		clearSamplesButton.disableProperty().bind(noSamplesAdded);
		samplesMenuRemoveSample.disableProperty().bind(Bindings.or(noSampleSelected, samplesMenuNotVisible));
		samplesMenuClearSamples.disableProperty().bind(Bindings.or(samplesMenuNotVisible, noSamplesAdded));
		calculateResultsButton.disableProperty().bind(cannotCalculateResults);
		runMenuCalculateResults.disableProperty().bind(Bindings.or(samplesMenuNotVisible, cannotCalculateResults));
	}
	
	private BooleanBinding getNoFeaturesChosenBinding() {
		return Bindings.createBooleanBinding(
				() -> featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
				                  .map(CheckBox.class::cast).noneMatch(CheckBox::isSelected),
				featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
				            .map(CheckBox.class::cast).map(CheckBox::selectedProperty).toArray(Observable[]::new)
		);
	}
	
	private BooleanBinding getNoSamplesAddedBinding() {
		return Bindings.createBooleanBinding(
				() -> !samplesImageViewAnchor.getChildren().stream().filter(Sample.class::isInstance)
				                             .findAny().isPresent(),
				samplesImageViewAnchor.getChildren()
		);
	}
	
	private void setSamplesMenuEnablementBindings() {
		final BooleanBinding samplesMenuNotVisible = samplesMenu.visibleProperty().not();
		samplesMenuCreateMode.disableProperty().bind(Bindings.or(samplesMenuNotVisible,
		                                                         createRadioButton.selectedProperty()));
		samplesMenuSelectMode.disableProperty().bind(Bindings.or(samplesMenuNotVisible,
		                                                         selectRadioButton.selectedProperty()));
		samplesMenuRotateMode.disableProperty().bind(Bindings.or(samplesMenuNotVisible,
		                                                         rotateRadioButton.selectedProperty()));
		samplesMenuSelectAllFeatures.disableProperty().bind(samplesMenuNotVisible);
		samplesMenuDeselectAllFeatures.disableProperty().bind(samplesMenuNotVisible);
	}
	
	private void setSelectionListeners() {
		addAlignImageListListener();
		addSampleImageListListener();
	}
	
	private void addAlignImageListListener() {
		alignImageList.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> {
					checkOldAlignImage(oldValue);
					checkNewAlignImage(newValue);
				});
	}
	
	private void checkOldAlignImage(final String oldValue) {
		if (oldValue != null && !oldValue.isEmpty()) {
			final ImageToAlignData img = state.imagesToAlign.get(oldValue);
			hideImage(alignScrollPane, img);
			removeChildren(img);
		}
	}
	
	private void removeChildren(final ImageToAlignData img) {
		if (Arrays.asList(img.vertices).contains(state.selectedVertex.get()))
			state.selectedVertex.set(null);
		alignImageViewAnchor.getChildren().removeAll(img.vertices);
		alignImageViewAnchor.getChildren().removeAll(
				Arrays.stream(img.vertices).map(r -> r.sampleArea).toArray(Rectangle[]::new));
		alignImageViewAnchor.getChildren().remove(img.triangle);
	}
	
	private void checkNewAlignImage(final String newValue) {
		if (newValue != null) {
			final ImageToAlignData img = state.imagesToAlign.get(newValue);
			setImage(alignScrollPane, alignImageView, alignImageSizeLabel, img);
			addChildren(img);
			setColors(img);
		}
		else
			hideAll(state.selectedVertex, alignImageView, alignImageSizeLabel);
	}
	
	private void addChildren(final ImageToAlignData img) {
		alignImageViewAnchor.getChildren().add(img.triangle);
		alignImageViewAnchor.getChildren().addAll(
				Arrays.stream(img.vertices).map(r -> r.sampleArea).toArray(Rectangle[]::new)
		);
		alignImageViewAnchor.getChildren().addAll(img.vertices);
	}
	
	private void setColors(final ImageToAlignData img) {
		triangleFillColor.setValue((Color) img.triangle.getFill());
		triangleStrokeColor.setValue((Color) img.triangle.getStroke());
	}
	
	private void addSampleImageListListener() {
		samplesImageList.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> {
					checkOldSampleImage(oldValue);
					checkNewSampleImage(newValue);
				});
	}
	
	private void checkOldSampleImage(final String oldValue) {
		if (oldValue != null && !oldValue.isEmpty()) {
			final SamplesImageData img = state.samplesImages.get(oldValue);
			hideImage(samplesScrollPane, img);
			removeChildren(img);
		}
	}
	
	private void removeChildren(final SamplesImageData img) {
		if (img.samples.contains(state.selectedSample.get()))
			state.selectedSample.set(null);
		samplesImageViewAnchor.getChildren().removeAll(img.samples);
		samplesImageViewAnchor.getChildren().removeAll(
				img.samples.stream().map(r -> r.sampleArea).toArray(Rectangle[]::new));
	}
	
	private void checkNewSampleImage(final String newValue) {
		if (newValue != null) {
			final SamplesImageData img = state.samplesImages.get(newValue);
			setImage(samplesScrollPane, samplesImageView, samplesImageSizeLabel, img);
			addChildren(img);
		}
		else
			hideAll(state.selectedSample, samplesImageView, samplesImageSizeLabel);
	}
	
	private void addChildren(final SamplesImageData img) {
		samplesImageViewAnchor.getChildren().addAll(
				img.samples.stream().map(r -> r.sampleArea).toArray(Rectangle[]::new));
		samplesImageViewAnchor.getChildren().addAll(img.samples);
	}
	
	private void hideImage(final ScrollPane pane, final ImageData img) {
		img.hScrollPos.set(pane.getHvalue());
		img.vScrollPos.set(pane.getVvalue());
	}
	
	private void setImage(final ScrollPane pane, final ImageView view, final Label label, final ImageData img) {
		view.setImage(img.image.get());
		view.setScaleX(img.scale.get());
		pane.setHvalue(img.hScrollPos.get());
		pane.setVvalue(img.vScrollPos.get());
		label.setText((int) img.image.get().getWidth() + "x" + (int) img.image.get().getHeight() + " px");
	}
	
	private void hideAll(final ObjectProperty<? extends BasicSample> property, final ImageView view, final Label
			label) {
		view.setScaleX(1.0);
		view.setImage(null);
		label.setText("");
		property.set(null);
	}
	
	@FXML
	void align() {
		final Stage dialog = showPopup("Aligning images");
		final Task<Void> task = createAlignTask(dialog);
		startTask(task);
	}
	
	private Task<Void> createAlignTask(final Stage dialog) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				align(dialog);
				return null;
			}
		};
	}
	
	private void align(final Stage dialog) {
		Platform.runLater(this::resetSamples);
		final Mat[] images = new Mat[state.imagesToAlign.size()];
		final MatOfPoint2f[] points = new MatOfPoint2f[state.imagesToAlign.size()];
		int i = 0;
		for (final String key : alignImageList.getItems()) {
			images[i] = state.imagesToAlign.get(key).imageData;
			points[i] = state.imagesToAlign.get(key).triangle.getMatOfVertices();
			i++;
		}
		final int interpolation = getInterpolationType();
		try {
			align(dialog, images, points, interpolation);
		} catch (final CvException e) {
			handleException(dialog, e, "Aligning images failed! Please check selected points.");
		}
	}
	
	private int getInterpolationType() {
		return cubicRadioButton.isSelected() ?
				INTER_CUBIC : linearRadioButton.isSelected() ?
				INTER_LINEAR : INTER_NEAREST;
	}
	
	private void align(final Stage dialog, final Mat[] images, final MatOfPoint2f[] points, final int interpolation) {
		final SamplesImageData[] result = align(getImagesCopy(images), points, interpolation);
		Platform.runLater(() -> addSampleImages(dialog, result));
	}
	
	private SamplesImageData[] align(final Mat[] images, final MatOfPoint2f[] points, final int interpolation) {
		assert images.length == points.length : "Images count does not match passed vertices count!";
		ImageUtils.performAffineTransformations(images, points, interpolation);
		return Arrays.stream(images)
		             .map(i -> imageDataFactory.createSamplesImageData(createImage(i), i))
		             .toArray(SamplesImageData[]::new);
	}
	
	private void addSampleImages(final Stage dialog, final SamplesImageData[] result) {
		samplesImageList.getItems().addAll(alignImageList.getItems());
		for (int j = 0; j < result.length; j++)
			state.samplesImages.put(samplesImageList.getItems().get(j), result[j]);
		showSampleImage();
		dialog.close();
	}
	
	private void showSampleImage() {
		mainTabPane.getSelectionModel().select(samplesTab);
		samplesImageList.getSelectionModel().selectFirst();
		samplesScrollPane.setHvalue(0.5);
		samplesScrollPane.setVvalue(0.5);
	}
	
	private void resetSamples() {
		samplesImageList.getSelectionModel().clearSelection();
		samplesImageList.getItems().clear();
		state.samplesImages.clear();
		samplesImageViewAnchor.getChildren().removeIf(n -> !(n instanceof ImageView));
	}
	
	@FXML
	void deleteSample() {
		final Sample sample = state.selectedSample.get();
		if (sample != null) {
			Platform.runLater(() -> {
				final int index = sample.getIndexOf();
				deleteSample(sample, index);
			});
		}
	}
	
	private void deleteSample(final Sample sample, final int index) {
		for (final SamplesImageData img : state.samplesImages.values()) {
			img.samples.remove(index);
		}
		state.selectedSample.set(null);
		samplesImageViewAnchor.getChildren().remove(sample.sampleArea);
		samplesImageViewAnchor.getChildren().remove(sample);
	}
	
	@FXML
	void clearSamples() {
		Platform.runLater(() -> {
			for (final SamplesImageData img : state.samplesImages.values()) {
				img.samples.clear();
			}
			state.selectedSample.set(null);
			samplesImageViewAnchor.getChildren().removeIf(Sample.class::isInstance);
			samplesImageViewAnchor.getChildren().removeIf(Rectangle.class::isInstance);
		});
	}
	
	@FXML
	void setCreateMode() {
		if (!createRadioButton.isSelected()) createRadioButton.setSelected(true);
	}
	
	@FXML
	void setSelectMode() {
		if (!selectRadioButton.isSelected()) selectRadioButton.setSelected(true);
	}
	
	@FXML
	void setRotateMode() {
		if (!rotateRadioButton.isSelected()) rotateRadioButton.setSelected(true);
	}
	
	@FXML
	void exportToPng() {
		final File selectedDirectory = getDirectory(root.getScene().getWindow());
		if (selectedDirectory != null) {
			if (selectedDirectory.canWrite()) {
				final Stage dialog = showPopup("Saving images");
				final Task<Void> task = createWriteImagesTask(selectedDirectory, dialog);
				startTask(task);
			}
			else
				Platform.runLater(() -> showAlert("Save failed! Check your write permissions."));
		}
	}
	
	private Task<Void> createWriteImagesTask(final File selectedDirectory, final Stage dialog) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				writeImages(selectedDirectory, dialog);
				return null;
			}
		};
	}
	
	private void writeImages(final File selectedDirectory, final Stage dialog) {
		for (int i = 0; i < samples.size(); i++) {
			final List<Pair<String, ImageView>> currentSamples = samples.get(i);
			for (int j = 0; j < currentSamples.size(); j++) {
				try {
					writeImage(selectedDirectory, currentSamples, i, j);
				} catch (final IOException e) {
					handleException(dialog, e, "Save failed! Check your write permissions.");
				}
			}
			Platform.runLater(dialog::close);
		}
	}
	
	@FXML
	void help() {
		final Stage stage = new Stage();
		final WebView helpView = getHelpView();
		StageUtils.prepareHelpStage(stage, helpView);
		injectStylesheets(helpView);
		stage.show();
	}
	
}
