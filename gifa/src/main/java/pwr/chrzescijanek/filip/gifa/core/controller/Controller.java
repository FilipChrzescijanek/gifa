package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgcodecs.Imgcodecs;
import pwr.chrzescijanek.filip.gifa.Main;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;
import pwr.chrzescijanek.filip.gifa.generator.DataGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	private ListView< String > imagesList;

	@FXML
	private Button loadImagesButton;

	@FXML
	private BorderPane centerBorderPane;

	@FXML
	private ScrollPane imageScrollPane;

	@FXML
	private AnchorPane imageViewAnchor;

	@FXML
	private AnchorPane resultsImageViewAnchor;

	@FXML
	private Group imageViewGroup;

	@FXML
	private ImageView imageView;

	@FXML
	private GridPane bottomGrid;

	@FXML
	private Label imageSizeLabel;

	@FXML
	private ComboBox< String > scaleCombo;

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
	private HBox methodHBox;

	@FXML
	private RadioButton triangleRadioButton;

	@FXML
	private ToggleGroup method;

	@FXML
	private RadioButton rectangleRadioButton;

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
	private ColorPicker triangleFillColor;

	@FXML
	private ColorPicker triangleStrokeColor;

	@FXML
	private Label triangleFillLabel;

	@FXML
	private Label triangleStrokeLabel;

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
	private TextField rectangleHeightTextField;

	@FXML
	private ColorPicker rectangleFillColor;

	@FXML
	private ColorPicker rectangleStrokeColor;

	@FXML
	private Label rectangleFillLabel;

	@FXML
	private Label rectangleStrokeLabel;

	@FXML
	private Button verticalFlipButton;

	@FXML
	private Button horizontalFlipButton;

	@FXML
	private ToolBar toolbar;

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
	private ImageView resultsImageView;

	@FXML
	private Group resultsImageViewGroup;

	@FXML
	private ScrollPane resultsGridScrollPane;

	@FXML
	private ScrollPane resultsImageScrollPane;

	@FXML
	private GridPane resultsGrid;

	@FXML
	private HBox resultsControls;

	@FXML
	private RadioButton resultImageRadioButton;

	@FXML
	private ToggleGroup result;

	@FXML
	private RadioButton graphsRadioButton;

	@FXML
	private Label resultsColumnsLabel;

	@FXML
	private TextField resultsColumnsTextField;

	@FXML
	private ComboBox< String > resultsScaleCombo;

	@FXML
	private Label resultsMousePositionLabel;

	@FXML
	private Triangle triangle;

	@FXML
	private RectangleOfInterest rectangle;

	@FXML
	void about( ActionEvent event ) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Global image features analyzer\n\nCopyright © 2016 Filip Chrześcijanek", ButtonType.OK);
		alert.setTitle("About");
		alert.setHeaderText("gifa®");
		alert.setGraphic(new ImageView(new Image(getClass().getResourceAsStream( "/icon-big.png" ) )));
		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream( "/icon-small.png" )));
		alert.show();
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
		placeCharts();
	}

	@FXML
	void deselectAllFunctions( ActionEvent event ) {
		for ( Node chb : featuresVBox.getChildren() ) {
			( (CheckBox) chb ).setSelected(false);
		}
		DataGenerator.INSTANCE.clearChosenFunctions();
	}

	@FXML
	void exit( ActionEvent event ) {
		root.getScene().getWindow().hide();
	}

	@FXML
	void exportToCsv( ActionEvent event ) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export results to CSV file");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma-separated values", "*.csv"));
		File csvFile = fileChooser.showSaveDialog(root.getScene().getWindow());
		if ( csvFile != null ) {
			try ( BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile)) ) {
				bw.write(createCsvContents());
			} catch ( IOException e ) {
				e.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR, "Save failed! Check your write permissions.", ButtonType.OK);
				alert.showAndWait();
			}
		}
	}

	private String createCsvContents() {
		Map< String, double[] > results = State.INSTANCE.result.getValue().results;
		String csvContents = "Image";
		for ( String s : results.keySet() )
			csvContents += ",\"" + s + "\"";
		final ObservableList< String > images = imagesList.getItems();
		for ( int i = 0; i < images.size(); i++ ) {
			csvContents += "\r\n\"" + images.get(i) + "\"";
			for ( String s : results.keySet() )
				csvContents += ",\"" + results.get(s)[i] + "\"";
		}
		return csvContents;
	}

	@FXML
	void firstPointXChanged( ActionEvent event ) {

	}

	@FXML
	void firstPointYChanged( ActionEvent event ) {

	}

	@FXML
	void flipHorizontal( ActionEvent event ) {
		String imageName = imagesList.getSelectionModel().getSelectedItem();
		ImageData img = State.INSTANCE.images.get(imageName);
		Core.flip(img.imageData, img.imageData, 1);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(img.imageData), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		final ImageData newImageData = new ImageData(fxImage, img.imageData);
		newImageData.triangle.copy(triangle);
		newImageData.rectangle.copy(rectangle);
		State.INSTANCE.images.put(imageName, newImageData);
		imageView.setImage(fxImage);
	}

	@FXML
	void flipVertical( ActionEvent event ) {
		String imageName = imagesList.getSelectionModel().getSelectedItem();
		ImageData img = State.INSTANCE.images.get(imageName);
		Core.flip(img.imageData, img.imageData, 0);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(img.imageData), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		final ImageData newImageData = new ImageData(fxImage, img.imageData);
		newImageData.triangle.copy(triangle);
		newImageData.rectangle.copy(rectangle);
		State.INSTANCE.images.put(imageName, newImageData);
		imageView.setImage(fxImage);
	}

	@FXML
	void getResults( ActionEvent event ) {
		imagesList.getSelectionModel().clearSelection();
		doSample();
		mainTabPane.getSelectionModel().select(resultsTab);
	}

	private void doSample() {
		final Mat[] images = new Mat[3];
		images[0] = State.INSTANCE.images.get("tri.jpg").imageData;
		images[1] = State.INSTANCE.images.get("tri-aff.jpg").imageData;
		images[2] = State.INSTANCE.images.get("tri-new.jpg").imageData;
		final MatOfPoint2f[] points = new MatOfPoint2f[3];
		points[0] = State.INSTANCE.images.get("tri.jpg").triangle.getMatOfPoints();
		points[1] = State.INSTANCE.images.get("tri-aff.jpg").triangle.getMatOfPoints();
		points[2] = State.INSTANCE.images.get("tri-new.jpg").triangle.getMatOfPoints();
		State.INSTANCE.result.setValue(DataGenerator.INSTANCE.generateData(ImageUtils.getImagesCopy(images), points));
		resultsImageView.setImage(State.INSTANCE.result.getValue().resultImage);
		State.INSTANCE.charts.setValue(generateCharts());
		placeCharts();
	}

	private void placeCharts() {
		List< BarChart< String, Number > > charts = State.INSTANCE.charts.get();
		if ( charts != null ) {
			resultsGrid.getChildren().clear();
			resultsGrid.getColumnConstraints().clear();
			resultsGrid.getRowConstraints().clear();
			final int noOfColumns = Integer.parseInt(resultsColumnsTextField.getText());
			for ( int i = 0; i < noOfColumns; i++ ) {
				final ColumnConstraints columnConstraints = new ColumnConstraints();
				columnConstraints.setPercentWidth(100.0 / noOfColumns);
				resultsGrid.getColumnConstraints().add(columnConstraints);
			}
			final int noOfRows = charts.size() / noOfColumns + 1;
			for ( int i = 0; i < noOfRows; i++ ) {
				List< BarChart< String, Number > > chartsInRow = new ArrayList<>();
				int n = 0;
				while ( i * noOfColumns + n < charts.size() && n < noOfColumns ) {
					chartsInRow.add(charts.get(i * noOfColumns + n));
					n++;
				}
				resultsGrid.addRow(i, chartsInRow.toArray(new BarChart[0]));
			}
		}
	}

	private List< BarChart< String, Number > > generateCharts() {
		Map< String, double[] > results = State.INSTANCE.result.getValue().results;
		List< BarChart< String, Number > > charts = new ArrayList<>();
		for ( Map.Entry< String, double[] > e : results.entrySet() ) {
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			final BarChart< String, Number > bc = new BarChart<>(xAxis, yAxis);
			bc.setTitle(e.getKey());
			yAxis.setLabel("Value");

			XYChart.Series series1 = new XYChart.Series();
			for ( int i = 0; i < e.getValue().length; i++ ) {
				series1.getData().add(new XYChart.Data(imagesList.getItems().get(i), e.getValue()[i]));
			}

			bc.getData().addAll(series1);
			bc.setLegendVisible(false);
			charts.add(bc);
		}
		return charts;
	}

	@FXML
	void loadImages( ActionEvent event ) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load images");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp"));
		List< File > selectedFiles = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
		if ( selectedFiles != null ) {
			for ( File f : selectedFiles ) {
				String filePath = f.getAbsolutePath();
				String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
				Mat image = Imgcodecs.imread(filePath, Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
				Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(image), image.width(), image.height(),
						image.channels(), PixelFormat.getByteRgbInstance());
				State.INSTANCE.images.put(fileName, new ImageData(fxImage, image));
				imagesList.getItems().add(fileName);
			}
			loadSample();
			imagesList.getSelectionModel().selectFirst();
		}
	}

	private void loadSample() {
		ImageData data1 = State.INSTANCE.images.get("tri.jpg");
		data1.triangle.moveFirstPointTo(58, 10);
		data1.triangle.moveSecondPointTo(13, 97);
		data1.triangle.moveThirdPointTo(103, 97);
		ImageData data2 = State.INSTANCE.images.get("tri-aff.jpg");
		data2.triangle.moveFirstPointTo(70, 10);
		data2.triangle.moveSecondPointTo(25, 97);
		data2.triangle.moveThirdPointTo(115, 97);
		ImageData data3 = State.INSTANCE.images.get("tri-new.jpg");
		data3.triangle.moveFirstPointTo(127, 57);
		data3.triangle.moveSecondPointTo(40, 12);
		data3.triangle.moveThirdPointTo(40, 102);
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
	void rectangleStrokeColorChanged( ActionEvent event ) {

	}

	@FXML
	void rectangleFillColorChanged( ActionEvent event ) {

	}

	@FXML
	void rotateLeft( ActionEvent event ) {
		String imageName = imagesList.getSelectionModel().getSelectedItem();
		ImageData img = State.INSTANCE.images.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 0);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(img.imageData), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		final ImageData newImageData = new ImageData(fxImage, img.imageData);
		newImageData.triangle.copy(triangle);
		newImageData.rectangle.copy(rectangle);
		State.INSTANCE.images.put(imageName, newImageData);
		imageView.setImage(fxImage);

	}

	@FXML
	void rotateRight( ActionEvent event ) {
		String imageName = imagesList.getSelectionModel().getSelectedItem();
		ImageData img = State.INSTANCE.images.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 1);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(img.imageData), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		final ImageData newImageData = new ImageData(fxImage, img.imageData);
		newImageData.triangle.copy(triangle);
		newImageData.rectangle.copy(rectangle);
		State.INSTANCE.images.put(imageName, newImageData);
		imageView.setImage(fxImage);
	}

	@FXML
	void secondPointXChanged( ActionEvent event ) {

	}

	@FXML
	void secondPointYChanged( ActionEvent event ) {

	}

	@FXML
	void selectAllFunctions( ActionEvent event ) {
		for ( Node chb : featuresVBox.getChildren() ) {
			( (CheckBox) chb ).setSelected(true);
		}
		DataGenerator.INSTANCE.chooseAllAvailableFunctions();
	}

	@FXML
	void showGraphs( ActionEvent event ) {

	}

	@FXML
	void showRectangle( ActionEvent event ) {

	}

	@FXML
	void showResultImage( ActionEvent event ) {

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

	@FXML
	void triangleFillColorChanged( ActionEvent event ) {

	}

	@FXML
	void triangleStrokeColorChanged( ActionEvent event ) {

	}

	@FXML
	void initialize() {
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert fileMenu != null : "fx:id=\"fileMenu\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert fileMenuLoadImages != null : "fx:id=\"fileMenuLoadImages\" was not injected: check your FXML file 'gifa-gui.fxml'.";
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
		assert imagesTab != null : "fx:id=\"imagesTab\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imagesMainPane != null : "fx:id=\"imagesMainPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imagesListGrid != null : "fx:id=\"imagesListGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imagesList != null : "fx:id=\"imagesList\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert loadImagesButton != null : "fx:id=\"loadImagesButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert centerBorderPane != null : "fx:id=\"centerBorderPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imageScrollPane != null : "fx:id=\"imageScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imageViewAnchor != null : "fx:id=\"imageViewAnchor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert bottomGrid != null : "fx:id=\"bottomGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert scaleCombo != null : "fx:id=\"scaleCombo\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert mousePositionLabel != null : "fx:id=\"mousePositionLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rightVBox != null : "fx:id=\"rightVBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rightVBoxTabPane != null : "fx:id=\"rightVBoxTabPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert toolboxTab != null : "fx:id=\"toolboxTab\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert toolboxVBox != null : "fx:id=\"toolboxVBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert methodHBox != null : "fx:id=\"methodHBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert triangleRadioButton != null : "fx:id=\"triangleRadioButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert method != null : "fx:id=\"method\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleRadioButton != null : "fx:id=\"rectangleRadioButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert toolboxStackPane != null : "fx:id=\"toolboxStackPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert triangleGroupGrid != null : "fx:id=\"triangleGroupGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert triangleGroupLabel != null : "fx:id=\"triangleGroupLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert triangleFillColor != null : "fx:id=\"triangleFillColor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert firstPointXLabel != null : "fx:id=\"firstPointXLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert secondPointXLabel != null : "fx:id=\"secondPointXLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert thirdPointXLabel != null : "fx:id=\"thirdPointXLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert firstPointYLabel != null : "fx:id=\"firstPointYLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert secondPointYLabel != null : "fx:id=\"secondPointYLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert thirdPointYLabel != null : "fx:id=\"thirdPointYLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert firstPointXTextField != null : "fx:id=\"firstPointXTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert firstPointYTextField != null : "fx:id=\"firstPointYTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert secondPointXTextField != null : "fx:id=\"secondPointXTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert secondPointYTextField != null : "fx:id=\"secondPointYTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert thirdPointXTextField != null : "fx:id=\"thirdPointXTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert thirdPointYTextField != null : "fx:id=\"thirdPointYTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert triangleStrokeColor != null : "fx:id=\"triangleStrokeColor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert triangleStrokeLabel != null : "fx:id=\"triangleStrokeLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert triangleFillLabel != null : "fx:id=\"triangleFillLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleGroupGrid != null : "fx:id=\"rectangleGroupGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleGroupLabel != null : "fx:id=\"rectangleGroupLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleXLabel != null : "fx:id=\"rectangleXLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleFillColor != null : "fx:id=\"rectangleFillColor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleWidthLabel != null : "fx:id=\"rectangleWidthLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleYLabel != null : "fx:id=\"rectangleYLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleHeightLabel != null : "fx:id=\"rectangleHeightLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleXTextField != null : "fx:id=\"rectangleXTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleYTextField != null : "fx:id=\"rectangleYTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleWidthTextField != null : "fx:id=\"rectangleWidthTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleHeightTextField != null : "fx:id=\"rectangleHeightTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleStrokeColor != null : "fx:id=\"rectangleStrokeColor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleFillLabel != null : "fx:id=\"rectangleFillLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangleStrokeLabel != null : "fx:id=\"rectangleStrokeLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert toolbar != null : "fx:id=\"toolbar\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert verticalFlipButton != null : "fx:id=\"verticalFlipButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert horizontalFlipButton != null : "fx:id=\"horizontalFlipButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rotateLeftButton != null : "fx:id=\"rotateLeftButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rotateRightButton != null : "fx:id=\"rotateRightButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert featuresTab != null : "fx:id=\"featuresTab\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert featuresBorderPane != null : "fx:id=\"featuresBorderPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert featuresScrollPane != null : "fx:id=\"featuresScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert featuresVBox != null : "fx:id=\"featuresVBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert selectionButtonsHBox != null : "fx:id=\"selectionButtonsHBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert selectAllButton != null : "fx:id=\"selectAllButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert deselectAllButton != null : "fx:id=\"deselectAllButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsButton != null : "fx:id=\"resultsButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsTab != null : "fx:id=\"resultsTab\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsMainPane != null : "fx:id=\"resultsMainPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsControls != null : "fx:id=\"resultsControls\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultImageRadioButton != null : "fx:id=\"resultImageRadioButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert result != null : "fx:id=\"result\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert graphsRadioButton != null : "fx:id=\"graphsRadioButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsColumnsLabel != null : "fx:id=\"resultsColumnsLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsColumnsTextField != null : "fx:id=\"resultsColumnsTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsGridScrollPane != null : "fx:id=\"resultsGridScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsImageScrollPane != null : "fx:id=\"resultsImageScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsImageView != null : "fx:id=\"resultsImageView\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsGrid != null : "fx:id=\"resultsGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert triangle != null : "fx:id=\"triangle\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert rectangle != null : "fx:id=\"rectangle\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsImageViewAnchor != null : "fx:id=\"resultsImageViewAnchor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imageViewGroup != null : "fx:id=\"imageViewGroup\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsImageViewGroup != null : "fx:id=\"resultsImageViewGroup\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsScaleCombo != null : "fx:id=\"resultsScaleCombo\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsMousePositionLabel != null : "fx:id=\"resultsMousePositionLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imageSizeLabel != null : "fx:id=\"imageSizeLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";

		scaleCombo.itemsProperty().get().addAll(
				"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%", "250%", "500%", "1000%"
		);
		scaleCombo.setValue("100%");
		resultsScaleCombo.itemsProperty().get().addAll(
				"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%", "250%", "500%", "1000%"
		);
		resultsScaleCombo.setValue("100%");
		createCheckBoxes();
		setVisibilityBindings();
		setEnablementBindings();
		setSelectionListeners();
		rectangleFillColor.setValue(Color.DODGERBLUE);
		rectangleStrokeColor.setValue(Color.RED);
		triangleFillColor.setValue(Color.DODGERBLUE);
		triangleStrokeColor.setValue(Color.RED);
		rectangle.fillProperty().bind(rectangleFillColor.valueProperty());
		triangle.fillProperty().bind(triangleFillColor.valueProperty());
		rectangle.strokeProperty().bind(rectangleStrokeColor.valueProperty());
		triangle.strokeProperty().bind(triangleStrokeColor.valueProperty());
		addNumberTextFieldListener(firstPointXTextField);
		addNumberTextFieldListener(firstPointYTextField);
		addNumberTextFieldListener(secondPointXTextField);
		addNumberTextFieldListener(secondPointYTextField);
		addNumberTextFieldListener(thirdPointXTextField);
		addNumberTextFieldListener(thirdPointYTextField);
		addNumberTextFieldListener(rectangleXTextField);
		addNumberTextFieldListener(rectangleYTextField);
		addNumberTextFieldListener(rectangleWidthTextField);
		addNumberTextFieldListener(rectangleHeightTextField);
		addNumberTextFieldListener(resultsColumnsTextField);
		addXTextFieldListener(firstPointXTextField);
		addXTextFieldListener(secondPointXTextField);
		addXTextFieldListener(thirdPointXTextField);
		addYTextFieldListener(firstPointYTextField);
		addYTextFieldListener(secondPointYTextField);
		addYTextFieldListener(thirdPointYTextField);
		addRectangleXTextFieldListener(rectangleXTextField);
		addRectangleYTextFieldListener(rectangleYTextField);
		addWidthTextFieldListener(rectangleWidthTextField);
		addHeightTextFieldListener(rectangleHeightTextField);
		bindTrianglePoint(firstPointXTextField, 0);
		bindTrianglePoint(firstPointYTextField, 1);
		bindTrianglePoint(secondPointXTextField, 2);
		bindTrianglePoint(secondPointYTextField, 3);
		bindTrianglePoint(thirdPointXTextField, 4);
		bindTrianglePoint(thirdPointYTextField, 5);
		rectangleXTextField.textProperty().bindBidirectional(rectangle.xProperty(), new NumberStringConverter());
		rectangleYTextField.textProperty().bindBidirectional(rectangle.yProperty(), new NumberStringConverter());
		rectangleWidthTextField.textProperty().bindBidirectional(rectangle.widthProperty(), new NumberStringConverter());
		rectangleHeightTextField.textProperty().bindBidirectional(rectangle.heightProperty(), new NumberStringConverter());
		setImageViewControls(imageView, imageScrollPane, imageViewGroup, scaleCombo, mousePositionLabel);
		setImageViewControls(resultsImageView, resultsImageScrollPane, resultsImageViewGroup, resultsScaleCombo, resultsMousePositionLabel);
		resultsColumnsTextField.textProperty().addListener(( observable, oldValue, newValue ) -> placeCharts());
		horizontalFlipButton.setTooltip(new Tooltip("Flip horizontally"));
		verticalFlipButton.setTooltip(new Tooltip("Flip vertically"));
		rotateLeftButton.setTooltip(new Tooltip("Rotate left by 90 degrees"));
		rotateRightButton.setTooltip(new Tooltip("Rotate right by 90 degrees"));
		imageView.scaleYProperty().addListener(( observable, oldValue, newValue ) -> {
			final double scale = newValue.doubleValue();
			triangle.setScaleX(scale);
			triangle.setScaleY(scale);
			rectangle.setScaleX(scale);
			rectangle.setScaleY(scale);
			recalculateTranslates(scale);
		});
	}

	private void recalculateTranslates( final double scale ) {
		triangle.setTranslateX(( imageView.getImage().getWidth() * 0.5 * ( scale - 1.0 ) ) -
				( imageView.getImage().getWidth() * 0.5 - getTriangleMiddleX() ) * ( scale - 1.0 ));
		triangle.setTranslateY(( imageView.getImage().getHeight() * 0.5 * ( scale - 1.0 ) ) -
				( imageView.getImage().getHeight() * 0.5 - getTriangleMiddleY() ) * ( scale - 1.0 ));
		rectangle.setTranslateX(( imageView.getImage().getWidth() * 0.5 * ( scale - 1.0 ) ) -
				( imageView.getImage().getWidth() * 0.5 - ( rectangle.getX() + rectangle.getWidth() * 0.5 ) ) * ( scale - 1.0 ));
		rectangle.setTranslateY(( imageView.getImage().getHeight() * 0.5 * ( scale - 1.0 ) ) -
				( imageView.getImage().getHeight() * 0.5 - ( rectangle.getY() + rectangle.getHeight() * 0.5 ) ) * ( scale - 1.0 ));
	}

	private void setImageViewControls( ImageView imageView, ScrollPane imageScrollPane, Group imageViewGroup, ComboBox< String > scaleCombo, Label
			mousePositionLabel ) {
		imageViewGroup.setOnMouseMoved(event -> mousePositionLabel.setText((int) ( ( event.getX() / imageView.getScaleX() ) ) + " : " + (int) ( ( event.getY()
				/ imageView.getScaleY() ) )));
		imageViewGroup.setOnMouseExited(event -> mousePositionLabel.setText("- : -"));
		imageViewGroup.setOnScroll(event -> {
			if ( event.isControlDown() ) {
				double deltaY = event.getDeltaY();
				if ( deltaY > 0 ) {
					imageView.setScaleX(imageView.getScaleX() * 1.05);
				} else {
					imageView.setScaleX(imageView.getScaleX() * 0.95);
				}
			}
		});
		imageScrollPane.setOnScroll(event -> {
			if ( event.isControlDown() ) {
				double deltaY = event.getDeltaY();
				if ( deltaY > 0 ) {
					imageView.setScaleX(imageView.getScaleX() * 1.05);
				} else {
					imageView.setScaleX(imageView.getScaleX() * 0.95);
				}
			}
		});
		imageView.scaleXProperty().addListener(( observable, oldValue, newValue ) -> {
			String asString = String.format("%.0f%%", newValue.doubleValue() * 100);
			if ( !scaleCombo.getValue().equals(asString) )
				scaleCombo.setValue(asString);
		});
		scaleCombo.valueProperty().addListener(( observable, oldValue, newValue ) -> {
			if ( !newValue.matches("\\d+%") )
				scaleCombo.setValue(oldValue);
			else {
				double scale = Double.parseDouble(newValue.substring(0, newValue.length() - 1)) / 100.0;
				imageView.setScaleX(scale);
				imageView.setScaleY(scale);
				imageView.setTranslateX(imageView.getImage().getWidth() * 0.5 * ( scale - 1.0 ));
				imageView.setTranslateY(imageView.getImage().getHeight() * 0.5 * ( scale - 1.0 ));
			}
		});
	}

	private void bindTrianglePoint( TextField textField, final int index ) {
		Bindings.bindBidirectional(textField.textProperty(), triangle.pointsProperty[index], new NumberStringConverter());
	}

	private void addNumberTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			if ( !newValue.matches("\\d+(\\.\\d+)?") ) textField.setText(oldValue);
		});
	}

	private double getTriangleMiddleX() {
		double firstPointX = Double.parseDouble(firstPointXTextField.getText());
		double secondPointX = Double.parseDouble(secondPointXTextField.getText());
		double thirdPointX = Double.parseDouble(thirdPointXTextField.getText());
		double minX = Math.min(firstPointX, Math.min(secondPointX, thirdPointX));
		double maxX = Math.max(firstPointX, Math.max(secondPointX, thirdPointX));
		return ( maxX - minX ) / 2.0 + minX;
	}

	private double getTriangleMiddleY() {
		double firstPointY = Double.parseDouble(firstPointYTextField.getText());
		double secondPointY = Double.parseDouble(secondPointYTextField.getText());
		double thirdPointY = Double.parseDouble(thirdPointYTextField.getText());
		double minY = Math.min(firstPointY, Math.min(secondPointY, thirdPointY));
		double maxY = Math.max(firstPointY, Math.max(secondPointY, thirdPointY));
		return ( maxY - minY ) / 2.0 + minY;
	}

	private void addXTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			try {
				final Image image = imageView.getImage();
				if ( image != null && Double.parseDouble(newValue) > image.getWidth() )
					textField.setText(oldValue);
				else if ( image != null )
					triangle.setTranslateX(( imageView.getImage().getWidth() * 0.5 * ( triangle.getScaleX() - 1.0 ) ) -
							( imageView.getImage().getWidth() * 0.5 - getTriangleMiddleX() ) * ( triangle.getScaleX() - 1.0 ));
			} catch ( NumberFormatException e ) {}
		});
	}

	private void addYTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			try {
				final Image image = imageView.getImage();
				if ( image != null && Double.parseDouble(newValue) > image.getHeight() )
					textField.setText(oldValue);
				else if ( image != null )
					triangle.setTranslateY(( imageView.getImage().getHeight() * 0.5 * ( triangle.getScaleY() - 1.0 ) ) -
							( imageView.getImage().getHeight() * 0.5 - getTriangleMiddleY() ) * ( triangle.getScaleY() - 1.0 ));
			} catch ( NumberFormatException e ) {}
		});
	}

	private void addRectangleXTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			try {
				final Image image = imageView.getImage();
				if ( image != null && Double.parseDouble(newValue) + rectangle.getWidth() > image.getWidth() )
					textField.setText(oldValue);
				else if ( image != null )
					rectangle.setTranslateX(( imageView.getImage().getWidth() * 0.5 * ( rectangle.getScaleX() - 1.0 ) ) -
							( imageView.getImage().getWidth() * 0.5 - ( Double.parseDouble(newValue) + rectangle.getWidth() * 0.5 ) ) * ( rectangle.getScaleX
									() - 1.0 ));
			} catch ( NumberFormatException e ) {}
		});
	}

	private void addRectangleYTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			try {
				final Image image = imageView.getImage();
				if ( image != null && Double.parseDouble(newValue) + rectangle.getHeight() > image.getHeight() ) textField.setText(oldValue);
				else if ( image != null )
					rectangle.setTranslateY(( imageView.getImage().getHeight() * 0.5 * ( rectangle.getScaleY() - 1.0 ) ) -
							( imageView.getImage().getHeight() * 0.5 - ( Double.parseDouble(newValue) + rectangle.getHeight() * 0.5 ) ) * ( rectangle
									.getScaleY() - 1.0 ));
			} catch ( NumberFormatException e ) {}
		});
	}

	private void addWidthTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			try {
				final Image image = imageView.getImage();
				if ( image != null && Double.parseDouble(newValue) + rectangle.getX() > image.getWidth() ) textField.setText(oldValue);
				else if ( image != null )
					rectangle.setTranslateX(( imageView.getImage().getWidth() * 0.5 * ( rectangle.getScaleX() - 1.0 ) ) -
							( imageView.getImage().getWidth() * 0.5 - ( rectangle.getX() + Double.parseDouble(newValue) * 0.5 ) ) * ( rectangle.getScaleX() -
									1.0 ));
			} catch ( NumberFormatException e ) {}
		});
	}

	private void addHeightTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			try {
				final Image image = imageView.getImage();
				if ( image != null && Double.parseDouble(newValue) + rectangle.getY() > image.getHeight() ) textField.setText(oldValue);
				else if ( image != null )
					rectangle.setTranslateY(( imageView.getImage().getHeight() * 0.5 * ( rectangle.getScaleY() - 1.0 ) ) -
							( imageView.getImage().getHeight() * 0.5 - ( rectangle.getY() + Double.parseDouble(newValue) * 0.5 ) ) * ( rectangle.getScaleY() -
									1.0 ));
			} catch ( NumberFormatException e ) {}
		});
	}

	private void createCheckBoxes() {
		for ( String s : DataGenerator.INSTANCE.getAllAvailableFunctions() ) {
			final CheckBox checkBox = new CheckBox(s);
			featuresVBox.getChildren().add(checkBox);
			checkBox.selectedProperty().addListener(( observable, oldValue, newValue ) -> {
				if ( newValue ) DataGenerator.INSTANCE.chooseFunction(s);
				else DataGenerator.INSTANCE.deselectFunction(s);
			});
			checkBox.setSelected(true);
		}
	}

	private void setVisibilityBindings() {
		SimpleListProperty imageListProperty = new SimpleListProperty();
		imageListProperty.bind(imagesList.itemsProperty());
		imageViewAnchor.visibleProperty().bind(imageView.imageProperty().isNotNull());
		scaleCombo.visibleProperty().bind(imageView.imageProperty().isNotNull());
		mousePositionLabel.visibleProperty().bind(imageView.imageProperty().isNotNull());
		rightVBox.visibleProperty().bind(imageView.imageProperty().isNotNull());
		triangle.visibleProperty().bind(triangleRadioButton.selectedProperty());
		triangleGroupGrid.visibleProperty().bind(triangleRadioButton.selectedProperty());
		rectangle.visibleProperty().bind(rectangleRadioButton.selectedProperty());
		rectangleGroupGrid.visibleProperty().bind(rectangleRadioButton.selectedProperty());
		resultsImageScrollPane.visibleProperty().bind(resultImageRadioButton.selectedProperty());
		resultsScaleCombo.visibleProperty().bind(resultImageRadioButton.selectedProperty());
		resultsMousePositionLabel.visibleProperty().bind(resultImageRadioButton.selectedProperty());
		resultsGridScrollPane.visibleProperty().bind(graphsRadioButton.selectedProperty());
		resultsColumnsLabel.visibleProperty().bind(graphsRadioButton.selectedProperty());
		resultsColumnsTextField.visibleProperty().bind(graphsRadioButton.selectedProperty());
	}

	private void setEnablementBindings() {
		fileMenuExportToCsv.disableProperty().bind(State.INSTANCE.result.isNull());
		IntegerProperty featuresSize = new SimpleIntegerProperty(featuresVBox.getChildren().size());
		BooleanBinding noFeaturesAvailable = Bindings.equal(0, featuresSize);
		BooleanBinding noFeaturesChosen = Bindings.createBooleanBinding(
				() -> featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
						.map(CheckBox.class::cast).noneMatch(CheckBox::isSelected),
				featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
						.map(CheckBox.class::cast).map(CheckBox::selectedProperty).toArray(Observable[]::new)
		);
		resultsButton.disableProperty().bind(Bindings.or(noFeaturesAvailable, noFeaturesChosen));
		resultsTab.disableProperty().bind(State.INSTANCE.result.isNull());
	}

	private void setSelectionListeners() {
		imagesList.getSelectionModel().selectedItemProperty().addListener(
				( observable, oldValue, newValue ) -> {
					if ( oldValue != null && !oldValue.isEmpty() ) {
						ImageData img = State.INSTANCE.images.get(oldValue);
						img.triangle.copy(triangle);
						img.rectangle.copy(rectangle);
					}
					if ( newValue != null ) {
						ImageData img = State.INSTANCE.images.get(newValue);
						imageView.setImage(img.image);
						triangle.copy(img.triangle);
						rectangle.copy(img.rectangle);
						imageView.setTranslateX(imageView.getImage().getWidth() * 0.5 * ( imageView.getScaleX() - 1.0 ));
						imageView.setTranslateY(imageView.getImage().getHeight() * 0.5 * ( imageView.getScaleY() - 1.0 ));
						recalculateTranslates(imageView.getScaleX());
						imageSizeLabel.setText((int) img.image.getWidth() + "x" + (int) img.image.getHeight() + " px");
					} else {
						imageView.setImage(null);
						imageSizeLabel.setText("");
					}
				}
		);
	}

}
