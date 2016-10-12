package pwr.chrzescijanek.filip.gifa.core.controller;

import com.sun.javafx.charts.Legend;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgcodecs.Imgcodecs;
import pwr.chrzescijanek.filip.gifa.Main;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;
import pwr.chrzescijanek.filip.gifa.core.util.Result;
import pwr.chrzescijanek.filip.gifa.generator.DataGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class Controller {

	public static final String THEME_PREFERENCE_KEY = "gifa.theme";
	public static final String THEME_DARK = "/theme-dark.css";
	public static final String THEME_LIGHT = "/theme-light.css";
	public static final String GRAPH_SELECTION_STYLE = "-fx-border-color: yellow; -fx-border-width: 3px;";
	@FXML private ScrollPane resultsHistoryGridScrollPane;
	@FXML private GridPane resultsHistoryGrid;
	@FXML private HBox resultsGraphsHBox;
	@FXML private HBox resultsGraphsToolbar;
	@FXML private Label resultsGraphsInfo;
	@FXML private Label imagesListInfo;
	@FXML private HBox imagesListToolbar;
	@FXML private RadioButton historyRadioButton;
	@FXML private Button resultsMergeButton;
	@FXML private Button resultsShiftButton;
	@FXML private Button resultsDeleteButton;

	@FXML private Button resultsRefreshButton;
	@FXML private Button deleteImageButton;
	@FXML private Button clearImagesButton;

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
	private HBox toolbar;

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
	private GridPane resultsControls;

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
	void refresh( ActionEvent actionEvent ) {
		List< Series > series = State.INSTANCE.series.get();
		List< BarChart< String, Number > > charts = State.INSTANCE.charts.get();
		if (charts != null) charts.clear();
		for (Series serie : series) {
					final CategoryAxis xAxis = new CategoryAxis();
					final NumberAxis yAxis = new NumberAxis();
					final BarChart< String, Number > bc = new BarChart<>(xAxis, yAxis);
//					bc.setTitle(serie.getName());
					yAxis.setLabel("Value");
					bc.getData().addAll(serie);
//					bc.setLegendVisible(false);
//					for (Node node : bc.lookupAll(".default-color0.chart-bar")) {
//						node.setStyle("-fx-bar-fill: red;");
//					}
					bc.setOnMouseClicked(event -> {
						if (bc.getStyle().equals(GRAPH_SELECTION_STYLE))
							bc.setStyle("-fx-border-color: null;");
						else
							bc.setStyle(GRAPH_SELECTION_STYLE);

						final List< Node > nodes = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
								(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
						final Integer integer = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
								(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
						resultsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
						resultsDeleteButton.setDisable(nodes.isEmpty());
						resultsMergeButton.setDisable(nodes.size() < 2);
					});
					charts.add(bc);
				}
		placeCharts();

		final List< Node > nodes = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
		final Integer integer = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
		resultsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
		resultsDeleteButton.setDisable(nodes.isEmpty());
		resultsMergeButton.setDisable(nodes.size() < 2);
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
		State.INSTANCE.setNoSelection();
		imageViewGroup.getScene().setCursor(Cursor.DEFAULT);
		imagesList.getSelectionModel().clearSelection();
		doSample();
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
		try {
		if (triangleRadioButton.isSelected()) {
			State.INSTANCE.result.setValue(DataGenerator.INSTANCE.generateData(ImageUtils.getImagesCopy(images), points, false));
			State.INSTANCE.history.get().add(State.INSTANCE.result.get());
		} else if (rectangleRadioButton.isSelected()) {
			RectangleOfInterest rectangle = State.INSTANCE.images.get("tri.jpg").rectangle;
			images[0] = images[0].submat((int) rectangle.getY(), (int) rectangle.getY() + (int) rectangle.getHeight() - 1, (int) rectangle.getX(), (int) rectangle.getX() + (int) rectangle.getWidth() - 1);
			rectangle = State.INSTANCE.images.get("tri-aff.jpg").rectangle;
			images[1] = images[1].submat((int) rectangle.getY(), (int) rectangle.getY() + (int) rectangle.getHeight() - 1, (int) rectangle.getX(), (int) rectangle.getX() + (int) rectangle.getWidth() - 1);
			rectangle = State.INSTANCE.images.get("tri-new.jpg").rectangle;
			images[2] = images[2].submat((int) rectangle.getY(), (int) rectangle.getY() + (int) rectangle.getHeight() - 1, (int) rectangle.getX(), (int) rectangle.getX() + (int) rectangle.getWidth() - 1);

			State.INSTANCE.result.setValue(DataGenerator.INSTANCE.generateData(ImageUtils.getImagesCopy(images), points, true));
			State.INSTANCE.history.get().add(State.INSTANCE.result.get());
		}
			resultsImageView.setImage(State.INSTANCE.result.getValue().resultImage);
			State.INSTANCE.series.setValue(generateSeries());
			createHistoryCharts();
			refresh(null);
			mainTabPane.getSelectionModel().select(resultsTab);
			graphsRadioButton.setSelected(true);
		}
		catch (CvException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Generating results failed! Probably we couldn't find proper image transformation. " +
					"Please check your rectangle selections.", ButtonType.OK);
			alert.showAndWait();
		}
	}

	private void createHistoryCharts() {
		List< LineChart< String, Number > > charts = new ArrayList<>();
		List<Result > history = State.INSTANCE.history.get();
		Set<String> functions = new TreeSet<>(history.stream().flatMap(r -> r.results.keySet().stream()).collect(Collectors.toSet()));
		for (String s : functions) {
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			final LineChart< String, Number > lc = new LineChart<>(xAxis, yAxis);
			lc.setTitle(s);
			yAxis.setLabel("Value");
			List< double[] > results = history.stream().map(r -> r.results.get(s)).collect(Collectors.toList());
			List< Series > series = new ArrayList<>();
			for ( int i = 0; i < results.size(); i++ ) {
				double[] r = results.get(i);
				for ( int j = 0; j < r.length; j++ ) {
					if ( series.size() == j ) {
						Series series1 = new Series();
						series1.setName("Series " + ( j + 1 ));
						series.add(series1);
					}
					Series current = series.get(j);
					current.getData().add(new XYChart.Data("" + ( i + 1 ), r[j]));
				}
			}
			series.forEach(lc.getData()::add);
			charts.add(lc);
		}

		for (LineChart lc : charts) {
			for ( int i = 0; i < lc.getData().size(); i++ ) {
				final int index = i;
				Series s = (Series) lc.getData().get(i);
				Legend.LegendItem item = lc.getChildrenUnmodifiable().stream()
						.filter(x -> x instanceof Legend).findAny()
						.map(l -> ((Legend) l).getItems())
						.map(it -> it.stream()
								.filter(r -> r.getText().equals(s.getName())).findAny().orElse(null)).orElse(null);
				if ( item != null ) {
					final ColorPicker e = new ColorPicker();
					e.setMaxWidth(15);
					e.valueProperty().addListener(( observable, oldValue, newValue ) -> {
						String web = String.format("#%02X%02X%02X%02X",
								(int) ( newValue.getRed() * 255 ),
								(int) ( newValue.getGreen() * 255 ),
								(int) ( newValue.getBlue() * 255 ),
								(int) ( newValue.getOpacity() * 255 ));
						final ObservableList<XYChart.Series> data = lc.getData();
						s.getNode().setStyle("-fx-stroke: " + web + ";");
						((ObservableList<XYChart.Data>) s.getData()).forEach(n -> n.getNode().setStyle("-fx-background-color: " + web + ", white;"));
						State.INSTANCE.historySeriesColors.put(lc.getTitle() + "/" + s.getName(), newValue);
					});
					final List< Color > defaultColors = Arrays.asList(Color.web("#f3622d"), Color.web("#fba71b"), Color.web("#57b757"), Color.web("#41a9c9"), Color.web("#4258c9"), Color.web("#9a42c8"), Color.web("#c84164"), Color.web("#888888"));
					e.setValue(State.INSTANCE.historySeriesColors.get(lc.getTitle() + "/" + s.getName()) == null ? defaultColors.get(index % defaultColors.size()) : State.INSTANCE.historySeriesColors.get(lc.getTitle() + "/" + s.getName()));
					item.setSymbol(e);
				}
			}
		}
		State.INSTANCE.historyCharts.get().clear();
		State.INSTANCE.historyCharts.get().addAll(charts);
		placeHistoryCharts();
	}

	private void placeHistoryCharts() {
			List< LineChart< String, Number > > charts = State.INSTANCE.historyCharts.get();
			resultsHistoryGrid.getChildren().clear();
			resultsHistoryGrid.getColumnConstraints().clear();
			resultsHistoryGrid.getRowConstraints().clear();
			final int noOfColumns = Math.min(Integer.parseInt(resultsColumnsTextField.getText()), charts.size());
			if (noOfColumns > 0) {
				for ( int i = 0; i < noOfColumns; i++ ) {
					final ColumnConstraints columnConstraints = new ColumnConstraints();
					columnConstraints.setPercentWidth(100.0 / noOfColumns);
					resultsHistoryGrid.getColumnConstraints().add(columnConstraints);
				}
				final int noOfRows = charts.size() / noOfColumns + 1;
				for ( int i = 0; i < noOfRows; i++ ) {
					List< LineChart< String, Number > > chartsInRow = new ArrayList<>();
					int n = 0;
					while ( i * noOfColumns + n < charts.size() && n < noOfColumns ) {
						chartsInRow.add(charts.get(i * noOfColumns + n));
						n++;
					}
					resultsHistoryGrid.addRow(i, chartsInRow.toArray(new LineChart[0]));
				}
			}
		}

	private void placeCharts() {
		List< BarChart< String, Number > > charts = State.INSTANCE.charts.get();
		if ( charts != null ) {
			resultsGrid.getChildren().clear();
			resultsGrid.getColumnConstraints().clear();
			resultsGrid.getRowConstraints().clear();
			final int noOfColumns = Math.min(Integer.parseInt(resultsColumnsTextField.getText()), charts.size());
			if (noOfColumns > 0) {
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
		colorSeries();
	}

	private void colorSeries() {
		final List< BarChart< String, Number > > charts = State.INSTANCE.charts.get();
		final List< Series > series = State.INSTANCE.series.get();

		for (int i = 0; i < series.size(); i++) {
			final int index = i;
			Series s = series.get(i);
			Legend.LegendItem item = charts.stream()
					.map(c -> c.getChildrenUnmodifiable())
					.map(n -> n.stream()
							.filter(x -> x instanceof Legend).collect(Collectors.toList()))
					.filter(l -> !l.isEmpty())
					.map(l -> ((Legend)l.get(0)).getItems())
					.map(it -> it.stream()
							.filter(r -> r.getText().equals(s.getName())).collect(Collectors.toList()))
					.filter(l -> !l.isEmpty())
					.map(w -> w.get(0)).findAny().orElse(null);
			if (item != null) {
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
					State.INSTANCE.seriesColors.put(s.getName(), newValue);
				});
				final List< Color > defaultColors = Arrays.asList(Color.web("#f3622d"), Color.web("#fba71b"), Color.web("#57b757"), Color.web("#41a9c9"), Color.web("#4258c9"), Color.web("#9a42c8"), Color.web("#c84164"), Color.web("#888888"));

				e.setValue(State.INSTANCE.seriesColors.get(s.getName()) == null ? defaultColors.get(index % defaultColors.size()) : State.INSTANCE.seriesColors.get(s.getName()));
				item.setSymbol(e);
			}
		}
	}

	private List<Series > generateSeries() {
		Map< String, double[] > results = State.INSTANCE.result.getValue().results;
		List<Series> series = new ArrayList<>();
		for ( Map.Entry< String, double[] > e : results.entrySet() ) {
			Series series1 = new Series();
			series1.setName(e.getKey());
			for ( int i = 0; i < e.getValue().length; i++ ) {
				series1.getData().add(new XYChart.Data(imagesList.getItems().get(i), e.getValue()[i]));
			}
			series.add(series1);
		}
		return series;
	}

	@FXML
	void loadImages( ActionEvent event ) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load images");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.tif"));
		List< File > selectedFiles = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
		if ( selectedFiles != null ) {
			for ( File f : selectedFiles ) {
				String filePath = f.getAbsolutePath();
				String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
				Mat image = Imgcodecs.imread(filePath, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
				Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(image), image.width(), image.height(),
						image.channels(), PixelFormat.getByteRgbInstance());
				State.INSTANCE.images.put(fileName, new ImageData(fxImage, image));
				imagesList.getItems().remove(fileName);
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
	void deleteImage() {
		String key = imagesList.getSelectionModel().getSelectedItem();
		if ( key != null ) {
			imagesList.getSelectionModel().clearSelection();
			imagesList.getItems().remove(key);
			State.INSTANCE.images.remove(key);
		}
	}

	@FXML
	void clearImages() {
			imagesList.getSelectionModel().clearSelection();
			imagesList.getItems().clear();
			State.INSTANCE.images.clear();
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
		assert deleteImageButton != null : "fx:id=\"deleteImageButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert clearImagesButton != null : "fx:id=\"clearImagesButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsRefreshButton != null : "fx:id=\"resultsRefreshButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert historyRadioButton != null : "fx:id=\"historyRadioButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsMergeButton != null : "fx:id=\"resultsMergeButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsShiftButton != null : "fx:id=\"resultsShiftButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsDeleteButton != null : "fx:id=\"resultsDeleteButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsGraphsInfo != null : "fx:id=\"resultsGraphsInfo\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imagesListInfo != null : "fx:id=\"imagesListInfo\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert imagesListToolbar != null : "fx:id=\"imagesListToolbar\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsGraphsToolbar != null : "fx:id=\"resultsGraphsToolbar\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsGraphsHBox != null : "fx:id=\"resultsGraphsHBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsHistoryGrid != null : "fx:id=\"resultsHistoryGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert resultsHistoryGridScrollPane != null : "fx:id=\"resultsHistoryGridScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";

		scaleCombo.itemsProperty().get().addAll(
				"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%", "250%", "500%", "1000%"
		);
		scaleCombo.setValue("100%");
		resultsScaleCombo.itemsProperty().get().addAll(
				"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%", "250%", "500%", "1000%"
		);
		resultsScaleCombo.setValue("100%");

		resultsShiftButton.setDisable(true);
		resultsDeleteButton.setDisable(true);
		resultsMergeButton.setDisable(true);
		createCheckBoxes();
		setVisibilityBindings();
		setEnablementBindings();
		setSelectionListeners();
		rectangleFillColor.setValue(Color.color(0.1, 0.1, 0.1, 0.1));
		rectangleStrokeColor.setValue(Color.color(0.9, 0.9, 0.9, 0.8));
		triangleFillColor.setValue(Color.color(0.1, 0.1, 0.1, 0.1));
		triangleStrokeColor.setValue(Color.color(0.9, 0.9, 0.9, 0.8));
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
		resultsColumnsTextField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			placeCharts(); placeHistoryCharts();
		});
		horizontalFlipButton.setTooltip(new Tooltip("Flip horizontally"));
		verticalFlipButton.setTooltip(new Tooltip("Flip vertically"));
		rotateLeftButton.setTooltip(new Tooltip("Rotate left by 90 degrees"));
		rotateRightButton.setTooltip(new Tooltip("Rotate right by 90 degrees"));
		loadImagesButton.setTooltip(new Tooltip("Load images"));
		deleteImageButton.setTooltip(new Tooltip("Delete image"));
		clearImagesButton.setTooltip(new Tooltip("Clear list"));
		resultsRefreshButton.setTooltip(new Tooltip("Restore graphs"));
		resultsMergeButton.setTooltip(new Tooltip("Merge graphs"));
		resultsShiftButton.setTooltip(new Tooltip("Shift graphs"));
		resultsDeleteButton.setTooltip(new Tooltip("Remove graphs"));
		root.setOnKeyPressed(event -> {
			if (resultsTab.isSelected() && graphsRadioButton.isSelected()) {
							if (event.getCode().equals(KeyCode.ENTER) && resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).count() > 1) {
								merge(null);
							} else if (event.getCode().equals(KeyCode.DELETE)) {
								delete(null);
							} else if (event.getCode().equals(KeyCode.SHIFT) && resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).count() == 1
									&& resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> ((BarChart<String, Number>) n).getData().size()).reduce(Integer::sum).orElse(0) > 1) {
								shift(null);
							}
			} else if (event.getCode().equals(KeyCode.ADD) && event.isControlDown() && imagesTab.isSelected() && imageView.getImage() != null) {
				imageView.setScaleX(imageView.getScaleX() * 1.05);
			} else if (event.getCode().equals(KeyCode.SUBTRACT) && event.isControlDown() && imagesTab.isSelected() && imageView.getImage() != null) {
				imageView.setScaleX(imageView.getScaleX() * 0.95);
			} else if (event.getCode().equals(KeyCode.ADD) && event.isControlDown() && resultsTab.isSelected() && resultsImageView.getImage() != null) {
				resultsImageView.setScaleX(resultsImageView.getScaleX() * 1.05);
			} else if (event.getCode().equals(KeyCode.SUBTRACT) && event.isControlDown() && resultsTab.isSelected() && resultsImageView.getImage() != null) {
				resultsImageView.setScaleX(resultsImageView.getScaleX() * 0.95);
			}
		});
		imageView.scaleYProperty().addListener(( observable, oldValue, newValue ) -> {
			final double scale = newValue.doubleValue();
			triangle.setScaleX(scale);
			triangle.setScaleY(scale);
			rectangle.setScaleX(scale);
			rectangle.setScaleY(scale);
			recalculateTranslates(scale);
		});
		imagesList.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.DELETE)) {
				deleteImage();
			}
		});
		imageViewGroup.setOnMouseMoved(event -> {
			mousePositionLabel.setText((int) ( event.getX() / imageView.getScaleX() ) + " : " + (int) ( event.getY() / imageView.getScaleY() ));
			if ( triangleRadioButton.isSelected() ) {
				State.INSTANCE.setSelection(
						triangle.getIndexOfNearestPointInRadius(
								event.getX() / imageView.getScaleX(),
								event.getY() / imageView.getScaleY(),
								7.0 / triangle.getScaleX()
						)
				);
				if ( State.INSTANCE.getTriangleSelection() != State.TriangleSelection.NONE
						&& State.INSTANCE.getTriangleSelection() != State.TriangleSelection.MOVE ) {
					imageViewGroup.getScene().setCursor(Cursor.OPEN_HAND);
				} else if ( State.INSTANCE.getTriangleSelection() == State.TriangleSelection.NONE ) {
					imageViewGroup.getScene().setCursor(Cursor.DEFAULT);
				}
			} else if (rectangleRadioButton.isSelected() && State.INSTANCE.getRectangleSelection() != State.RectangleSelection.DRAG) {
				double dX = event.getX() / imageView.getScaleX() - rectangle.getX();
				double dY = event.getY() / imageView.getScaleY() - rectangle.getY();
				if (dX >= -7.0 / rectangle.getScaleX() && dY >= -7.0 / rectangle.getScaleX() && dX <= rectangle.getWidth() + 7.0 / rectangle.getScaleX() && dY <= rectangle.getHeight() + 7.0 / rectangle.getScaleX()) {
					if ( Math.abs(rectangle.getWidth() - dX) < 7.0 / rectangle.getScaleX() && Math.abs(rectangle.getHeight() - dY) < 7.0 / rectangle.getScaleX() ) {
						imageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.SE);
					} else if ( Math.abs(dX) < 7.0 / rectangle.getScaleX() && Math.abs(dY) < 7.0 / rectangle.getScaleX() ) {
						imageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.NW);
					} else if ( Math.abs(rectangle.getWidth() - dX) < 7.0 / rectangle.getScaleX() && Math.abs(dY) < 7.0 / rectangle.getScaleX() ) {
						imageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.NE);
					} else if ( Math.abs(dX) < 7.0 / rectangle.getScaleX() && Math.abs(rectangle.getHeight() - dY) < 7.0 / rectangle.getScaleX() ) {
						imageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.SW);
					} else if ( Math.abs(rectangle.getWidth() - dX) < 7.0 / rectangle.getScaleX() ) {
						imageViewGroup.getScene().setCursor(Cursor.H_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.E);
					} else if ( Math.abs(dX) < 7.0 / rectangle.getScaleX() ) {
						imageViewGroup.getScene().setCursor(Cursor.H_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.W);
					} else if ( Math.abs(rectangle.getHeight() - dY) < 7.0 / rectangle.getScaleX() ) {
						imageViewGroup.getScene().setCursor(Cursor.V_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.S);
					} else if ( Math.abs(dY) < 7.0 / rectangle.getScaleX() ) {
						imageViewGroup.getScene().setCursor(Cursor.V_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.N);
					} else {
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
						imageViewGroup.getScene().setCursor(Cursor.DEFAULT);
					}
				} else {
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
					imageViewGroup.getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});

		rectangle.setOnMouseMoved(event -> {
			double dX = event.getX() / rectangle.getScaleX() - rectangle.getX();
			double dY = event.getY() / rectangle.getScaleY() - rectangle.getY();
			if (dX > 7.0 / rectangle.getScaleX() && dY > 7.0 / rectangle.getScaleY()
				&& rectangle.getWidth() - dX > 7.0 / rectangle.getScaleX()
				&& rectangle.getHeight() - dY > 7.0 / rectangle.getScaleY() &&
					State.INSTANCE.getRectangleSelection() == State.RectangleSelection.NONE) {
				State.INSTANCE.setRectangleSelection(State.RectangleSelection.DRAG);
				imageViewGroup.getScene().setCursor(Cursor.MOVE);
			}
			else if (!(dX > 7.0 / rectangle.getScaleX() && dY > 7.0 / rectangle.getScaleY()
					&& rectangle.getWidth() - dX > 7.0 / rectangle.getScaleX()
					&& rectangle.getHeight() - dY > 7.0 / rectangle.getScaleY()) &&
					State.INSTANCE.getRectangleSelection() == State.RectangleSelection.DRAG) {
				State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
				imageViewGroup.getScene().setCursor(Cursor.DEFAULT);
			}
		});
		rectangle.setOnMouseExited(event -> {
			if (State.INSTANCE.getRectangleSelection() == State.RectangleSelection.DRAG) {
				State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
				imageViewGroup.getScene().setCursor(Cursor.DEFAULT);
			}
		});
		triangle.setOnMouseMoved(event -> {
			if (State.INSTANCE.getTriangleSelection() == State.TriangleSelection.NONE) {
				State.INSTANCE.setMoveTriangle();
				imageViewGroup.getScene().setCursor(Cursor.MOVE);
			}
		});
		triangle.setOnMouseExited(event -> {
			if (State.INSTANCE.getTriangleSelection() == State.TriangleSelection.MOVE) {
				State.INSTANCE.setNoSelection();
				imageViewGroup.getScene().setCursor(Cursor.DEFAULT);
			}
		});
		imageViewGroup.setOnMouseDragged(event -> {
			final long x = Math.round(event.getX() / imageView.getScaleX());
			final long y = Math.round(event.getY() / imageView.getScaleY());
			if (triangleRadioButton.isSelected()) {
				if ( State.INSTANCE.getTriangleSelection() == State.TriangleSelection.MOVE ) {
					final ObservableList< Double > points = triangle.getPoints();
					int minX = (int) getMin(points.get(0), points.get(2), points.get(4));
					int maxX = (int) getMax(points.get(0), points.get(2), points.get(4));
					int minY = (int) getMin(points.get(1), points.get(3), points.get(5));
					int maxY = (int) getMax(points.get(1), points.get(3), points.get(5));
					final long dX = x - State.INSTANCE.x;
					final long dY = y - State.INSTANCE.y;
					if ( minX + dX >= 0 && maxX + dX <= imageView.getImage().getWidth()
							&& minY + dY >= 0 && maxY + dY <= imageView.getImage().getHeight() )
						triangle.moveTriangleBy(dX, dY);

					State.INSTANCE.x = x;
					State.INSTANCE.y = y;
				} else if ( x >= 0 && x <= imageView.getImage().getWidth()
						&& y >= 0 && y <= imageView.getImage().getHeight() ) {
					switch ( State.INSTANCE.getTriangleSelection() ) {
						case FIRST_POINT:
							imageViewGroup.getScene().setCursor(Cursor.CLOSED_HAND);
							triangle.moveFirstPointTo(x, y);
							break;
						case SECOND_POINT:
							imageViewGroup.getScene().setCursor(Cursor.CLOSED_HAND);
							triangle.moveSecondPointTo(x, y);
							break;
						case THIRD_POINT:
							imageViewGroup.getScene().setCursor(Cursor.CLOSED_HAND);
							triangle.moveThirdPointTo(x, y);
							break;
						default:
							break;
					}
				}
			}
			else if (rectangleRadioButton.isSelected()) {
				final long dX = x - State.INSTANCE.x;
				final long dY = y - State.INSTANCE.y;
				State.INSTANCE.x = x;
				State.INSTANCE.y = y;
				switch (State.INSTANCE.getRectangleSelection()) {
					case NW:
						resizeNW(dX, dY);
						break;
					case NE:
						resizeNE(dX, dY);
						break;
					case SE:
						resizeSE(dX, dY);
						break;
					case SW:
						resizeSW(dX, dY);
						break;
					case W:
						resizeW(dX, dY);
						break;
					case E:
						resizeE(dX, dY);
						break;
					case S:
						resizeS(dX, dY);
						break;
					case N:
						resizeN(dX, dY);
						break;
					case DRAG:
						drag(dX, dY);
						break;
					default:
						break;
				}
			}
		});
		imageViewGroup.setOnMousePressed(event -> {
			if (State.INSTANCE.getTriangleSelection() == State.TriangleSelection.MOVE
					|| State.INSTANCE.getRectangleSelection() != State.RectangleSelection.NONE) {
				State.INSTANCE.x = (int) (event.getX() / imageView.getScaleX());
				State.INSTANCE.y = (int) (event.getY() / imageView.getScaleY());
			}
		});
		imageViewGroup.setOnMouseReleased(event -> {
			if (State.INSTANCE.getTriangleSelection() == State.TriangleSelection.MOVE
					|| State.INSTANCE.getRectangleSelection() != State.RectangleSelection.NONE) {
				State.INSTANCE.x = 0;
				State.INSTANCE.y = 0;
			}
		});
		
	}

	public void resizeNW(final long deltaX, final long deltaY) {
		Image image = imageView.getImage();

		final double newWidth = rectangle.getWidth() - deltaX;
		final double newHeight = rectangle.getHeight() - deltaY;

		if (!(Math.abs(rectangle.getX()) < 0.000001) || newWidth < rectangle.getWidth()) {
			rectangle.setX(Math.max(rectangle.getX() + deltaX, 0));
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
		}
		if (!(Math.abs(rectangle.getY()) < 0.000001) || newHeight < rectangle.getHeight()) {
			rectangle.setY(Math.max(rectangle.getY() + deltaY, 0));
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));
		}
//		if (!(rectangle.getX() + deltaX < 0 || rectangle.getY() + deltaY < 0)) {
//			rectangle.setX(newWidth > 5 ? rectangle.getX() + deltaX : rectangle.getX());
//			rectangle.setWidth(newWidth > 5 ? newWidth : 5);
//			rectangle.setY(newHeight > 5 ? rectangle.getY() + deltaY : rectangle.getY());
//			rectangle.setHeight(newHeight > 5 ? newHeight : 5);
//		}
	}

	public void resizeSE(final long deltaX, final long deltaY) {
		Image image = imageView.getImage();

		final double newWidth = rectangle.getWidth() + deltaX;
		final double newHeight = rectangle.getHeight() + deltaY;

//		if (!(newWidth + rectangle.getX() + deltaX > image.getWidth()
//				|| newHeight + rectangle.getY() + deltaY > image.getHeight())) {
//			rectangle.setWidth(newWidth > 5 ? newWidth : 5);
//			rectangle.setHeight(newHeight > 5 ? newHeight : 5);
//		}
		if (!(rectangle.getX() + rectangle.getWidth() - image.getWidth() > 0) || newWidth < rectangle.getWidth())
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
		if (!(rectangle.getY() + rectangle.getHeight() - image.getHeight() > 0) || newHeight < rectangle.getHeight())
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));
	}

	public void resizeSW(final long deltaX, final long deltaY) {
		Image image = imageView.getImage();

		final double newWidth = rectangle.getWidth() - deltaX;
		final double newHeight = rectangle.getHeight() + deltaY;

//		if (!(rectangle.getX() + deltaX < 0
//				|| newHeight + rectangle.getY() + deltaY > image.getHeight() - 0)) {
//			rectangle.setHeight(newHeight > 5 ? newHeight : 5);
//			rectangle.setWidth(newWidth > 5 ? newWidth : 5);
//			rectangle.setX(newWidth > 5 ? rectangle.getX() + deltaX : rectangle.getX());
//		}
		if (!(Math.abs(rectangle.getX()) < 0.000001) || newWidth < rectangle.getWidth()) {
			rectangle.setX(Math.max(rectangle.getX() + deltaX, 0));
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
		}
		if (!(rectangle.getY() + rectangle.getHeight() - image.getHeight() > 0) || newHeight < rectangle.getHeight())
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));

	}

	public void resizeNE(final long deltaX, final long deltaY) {
		Image image = imageView.getImage();

		final double newWidth = rectangle.getWidth() + deltaX;
		final double newHeight = rectangle.getHeight() - deltaY;

//		if (!(newWidth + rectangle.getX() + deltaX > image.getWidth() - 0
//				|| rectangle.getY() + deltaY < 0)) {
//			rectangle.setY(newHeight > 5 ? rectangle.getY() + deltaY : rectangle.getY());
//			rectangle.setWidth(newWidth > 5 ? newWidth : 5);
//			rectangle.setHeight(newHeight > 5 ? newHeight : 5);
//		}
		if (!(Math.abs(rectangle.getY()) < 0.000001) || newHeight < rectangle.getHeight()) {
			rectangle.setY(Math.max(rectangle.getY() + deltaY, 0));
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));
		}
		if (!(rectangle.getX() + rectangle.getWidth() - image.getWidth() > 0) || newWidth < rectangle.getWidth())
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
	}

	public void resizeE(final long deltaX, final long deltaY) {
		Image image = imageView.getImage();

		final double newWidth = rectangle.getWidth() + deltaX;

//		if (!(newWidth + rectangle.getX() + deltaX > image.getWidth() - 0)) {
//			rectangle.setWidth(newWidth > 5 ? newWidth : 5);
//		}
		if (!(rectangle.getX() + rectangle.getWidth() - image.getWidth() > 0) || newWidth < rectangle.getWidth())
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
	}

	public void resizeW(final long deltaX, final long deltaY) {
		Image image = imageView.getImage();

		final double newWidth = rectangle.getWidth() - deltaX;

//		if (!(rectangle.getX() + deltaX < 0)) {
//			rectangle.setX(newWidth > 5 ? rectangle.getX() + deltaX : rectangle.getX());
//			rectangle.setWidth(newWidth > 5 ? newWidth : 5);
//		}
		if (!(Math.abs(rectangle.getX()) < 0.000001) || newWidth < rectangle.getWidth()) {
			rectangle.setX(Math.max(rectangle.getX() + deltaX, 0));
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
		}
	}

	public void resizeS( long deltaX, final long deltaY) {
		Image image = imageView.getImage();

		final double newHeight = rectangle.getHeight() + deltaY;

//		if (!(newHeight + rectangle.getY() + deltaY > image.getHeight() - 0)) {
//			rectangle.setHeight(newHeight > 5 ? newHeight : 5);
//		}
		if (!(rectangle.getY() + rectangle.getHeight() - image.getHeight() > 0) || newHeight < rectangle.getHeight())
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));
		
	}

	public void resizeN(final long deltaX, final long deltaY) {
		Image image = imageView.getImage();
		final double newHeight = rectangle.getHeight() - deltaY;

//		if (!(rectangle.getY() + deltaY < 0)) {
//			rectangle.setY(newHeight > 5 ? rectangle.getY() + deltaY : rectangle.getY());
//			rectangle.setHeight(newHeight > 5 ? newHeight : 5);
//		}
		if (!(Math.abs(rectangle.getY()) < 0.000001) || newHeight < rectangle.getHeight()) {
			rectangle.setY(Math.max(rectangle.getY() + deltaY, 0));
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));
		}
	}

	public void drag(final long deltaX, final long deltaY) {
		Image image = imageView.getImage();

//		if (!(rectangle.getX() + deltaX + rectangle.getWidth() > image.getWidth() - 0
//				|| rectangle.getX() + deltaX < 0
//				|| rectangle.getY() + deltaY + rectangle.getHeight() > image.getHeight() - 0
//				|| rectangle.getY() + deltaY < 0)) {
//			rectangle.setX(rectangle.getX() + deltaX);
//			rectangle.setY(rectangle.getY() + deltaY);
//
//		}
		if (!(rectangle.getX() + rectangle.getWidth() - image.getWidth() > 0) || deltaX < 0)
			rectangle.setX(Math.min(Math.max(rectangle.getX() + deltaX, 0), image.getWidth() - rectangle.getWidth()));
		if (!(rectangle.getY() + rectangle.getHeight() - image.getHeight() > 0) || deltaY < 0)
			rectangle.setY(Math.min(Math.max(rectangle.getY() + deltaY, 0), image.getHeight() - rectangle.getHeight()));
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
		imageViewGroup.setOnMouseMoved(event -> mousePositionLabel.setText((int) (event.getX() / imageView.getScaleX()) + " : " + (int) (event.getY() / imageView.getScaleY())));
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
		imageScrollPane.addEventFilter(ScrollEvent.ANY, event -> {
			if ( event.isControlDown() ) {
				double deltaY = event.getDeltaY();
				if ( deltaY > 0 ) {
					imageView.setScaleX(imageView.getScaleX() * 1.05);
				} else {
					imageView.setScaleX(imageView.getScaleX() * 0.95);
				}
				event.consume();
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
		double minX = getMin(firstPointX, secondPointX, thirdPointX);
		double maxX = getMax(firstPointX, secondPointX, thirdPointX);
		return ( maxX - minX ) / 2.0 + minX;
	}

	private double getMin( final double firstPointX, final double secondPointX, final double thirdPointX ) {
		return Math.min(firstPointX, Math.min(secondPointX, thirdPointX));
	}

	private double getMax( final double firstPointY, final double secondPointY, final double thirdPointY ) {
		return Math.max(firstPointY, Math.max(secondPointY, thirdPointY));
	}

	private double getTriangleMiddleY() {
		double firstPointY = Double.parseDouble(firstPointYTextField.getText());
		double secondPointY = Double.parseDouble(secondPointYTextField.getText());
		double thirdPointY = Double.parseDouble(thirdPointYTextField.getText());
		double minY = getMin(firstPointY, secondPointY, thirdPointY);
		double maxY = getMax(firstPointY, secondPointY, thirdPointY);
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
		imageViewGroup.visibleProperty().bind(imageView.imageProperty().isNotNull());
		scaleCombo.visibleProperty().bind(imageView.imageProperty().isNotNull());
		mousePositionLabel.visibleProperty().bind(imageView.imageProperty().isNotNull());
		rightVBox.visibleProperty().bind(imageView.imageProperty().isNotNull());
		resultsButton.visibleProperty().bind(method.selectedToggleProperty().isNotNull());
		triangle.visibleProperty().bind(triangleRadioButton.selectedProperty());
		triangleGroupGrid.visibleProperty().bind(triangleRadioButton.selectedProperty());
		rectangle.visibleProperty().bind(rectangleRadioButton.selectedProperty());
		rectangleGroupGrid.visibleProperty().bind(rectangleRadioButton.selectedProperty());
		resultsImageScrollPane.visibleProperty().bind(resultImageRadioButton.selectedProperty());
		resultsScaleCombo.visibleProperty().bind(resultImageRadioButton.selectedProperty());
		resultsMousePositionLabel.visibleProperty().bind(resultImageRadioButton.selectedProperty());
		resultsGridScrollPane.visibleProperty().bind(graphsRadioButton.selectedProperty());
		resultsHistoryGridScrollPane.visibleProperty().bind(historyRadioButton.selectedProperty());
		resultsColumnsLabel.visibleProperty().bind(Bindings.or(graphsRadioButton.selectedProperty(), historyRadioButton.selectedProperty()));
		resultsColumnsTextField.visibleProperty().bind(Bindings.or(graphsRadioButton.selectedProperty(), historyRadioButton.selectedProperty()));
		resultsGraphsToolbar.visibleProperty().bind(graphsRadioButton.selectedProperty());
//		seriesColorPickers.visibleProperty().bind(graphsRadioButton.selectedProperty());
		resultsGraphsHBox.visibleProperty().bind(graphsRadioButton.selectedProperty());
		imagesListInfo.visibleProperty().bind(Bindings.isEmpty(imagesList.getItems()));
	}

	private void setEnablementBindings() {
		deleteImageButton.disableProperty().bind(imagesList.getSelectionModel().selectedItemProperty().isNull());
		clearImagesButton.disableProperty().bind(Bindings.isEmpty(imagesList.getItems()));
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

	@FXML  void merge( ActionEvent actionEvent ) {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart< String, Number > bc = new BarChart<>(xAxis, yAxis);
		yAxis.setLabel("Value");
		resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> ((BarChart<String, Number>) n).getData()).forEach(series -> {
			bc.getData().addAll(series);
		});
		bc.setOnMouseClicked(e -> {
			if (bc.getStyle().equals(GRAPH_SELECTION_STYLE))
				bc.setStyle("-fx-border-color: null;");
			else
				bc.setStyle(GRAPH_SELECTION_STYLE);

			final List< Node > nodes = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
			final Integer integer = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
			resultsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
			resultsDeleteButton.setDisable(nodes.isEmpty());
			resultsMergeButton.setDisable(nodes.size() < 2);
		});
		Optional< Integer > index = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> State.INSTANCE.charts.get().indexOf(n)).min(Integer::compareTo);
		State.INSTANCE.charts.get().removeAll(resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		State.INSTANCE.charts.get().add(index.orElse(0), bc);
		placeCharts();


		final List< Node > nodes = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
		final Integer integer = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
		resultsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
		resultsDeleteButton.setDisable(nodes.isEmpty());
		resultsMergeButton.setDisable(nodes.size() < 2);
	}

	@FXML void shift( ActionEvent actionEvent ) {
		final CategoryAxis xAxis1 = new CategoryAxis();
		final NumberAxis yAxis1 = new NumberAxis();
		final CategoryAxis xAxis2 = new CategoryAxis();
		final NumberAxis yAxis2 = new NumberAxis();
		final BarChart< String, Number > bc1 = new BarChart<>(xAxis1, yAxis1);
		final BarChart< String, Number > bc2 = new BarChart<>(xAxis2, yAxis2);
		yAxis1.setLabel("Value");
		yAxis2.setLabel("Value");
		BarChart< String, Number > chart = (BarChart< String, Number > ) resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()).get(0); //.map(n -> ((BarChart<String, Number>) n).getData()).forEach(series -> {
		Series s = State.INSTANCE.series.get().stream().filter(n -> n.getName().equals(chart.getData().get(chart.getData().size() - 1).getName())).collect(Collectors.toList()).get(0);
		List<String> names = chart.getData().stream().filter(n -> !n.getName().equals(chart.getData().get(chart.getData().size() - 1).getName())).map(n -> n.getName()).collect(Collectors.toList());
		bc1.getData().add(s);
		bc2.getData().addAll(State.INSTANCE.series.get().stream().filter(n -> names.contains(n.getName())).toArray(Series[]::new));
		//								bc1.setTitle(bc1.getData().get(0).getName());
		if (bc2.getData().size() == 1) {
			//									bc2.setTitle(bc2.getData().get(0).getName());
			//									bc2.setLegendVisible(false);
		}
		bc1.setOnMouseClicked(e -> {
			if (bc1.getStyle().equals(GRAPH_SELECTION_STYLE))
				bc1.setStyle("-fx-border-color: null;");
			else
				bc1.setStyle(GRAPH_SELECTION_STYLE);

			final List< Node > nodes = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
			final Integer integer = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
			resultsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
			resultsDeleteButton.setDisable(nodes.isEmpty());
			resultsMergeButton.setDisable(nodes.size() < 2);
		});
		bc2.setOnMouseClicked(e -> {
			if (bc2.getStyle().equals(GRAPH_SELECTION_STYLE))
				bc2.setStyle("-fx-border-color: null;");
			else
				bc2.setStyle(GRAPH_SELECTION_STYLE);

			final List< Node > nodes = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
			final Integer integer = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
			resultsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
			resultsDeleteButton.setDisable(nodes.isEmpty());
			resultsMergeButton.setDisable(nodes.size() < 2);

		});
		//								bc1.setLegendVisible(false);
		Optional< Integer > index = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> State.INSTANCE.charts.get().indexOf(n)).min(Integer::compareTo);
		//								State.INSTANCE.charts.get().removeAll(resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		int i = index.orElse(State.INSTANCE.charts.get().size() - 1);
		State.INSTANCE.charts.get().remove(chart);
		State.INSTANCE.charts.get().add(i, bc1);
		State.INSTANCE.charts.get().add(i, bc2);
		placeCharts();


		final List< Node > nodes = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
		final Integer integer = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
		resultsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
		resultsDeleteButton.setDisable(nodes.isEmpty());
		resultsMergeButton.setDisable(nodes.size() < 2);
	}

	@FXML  void delete( ActionEvent actionEvent ) {
		State.INSTANCE.charts.get().removeAll(resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		placeCharts();


		final List< Node > nodes = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
		final Integer integer = resultsGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
		resultsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
		resultsDeleteButton.setDisable(nodes.isEmpty());
		resultsMergeButton.setDisable(nodes.size() < 2);
	}

	@FXML void showHistory( ActionEvent actionEvent ) {

	}
}
