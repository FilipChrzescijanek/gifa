package pwr.chrzescijanek.filip.gifa.core.controller;

import com.sun.javafx.charts.Legend;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import pwr.chrzescijanek.filip.gifa.Main;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;
import pwr.chrzescijanek.filip.gifa.core.util.Result;
import pwr.chrzescijanek.filip.gifa.generator.DataGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class Controller {

	//static strings
	public static final String THEME_PREFERENCE_KEY = "gifa.theme";
	public static final String THEME_DARK = "/theme-dark.css";
	public static final String THEME_LIGHT = "/theme-light.css";
	public static final String GRAPH_SELECTION_STYLE = "-fx-border-color: yellow; -fx-border-width: 3px;";

	//fields
	public MenuItem fileMenuRemoveImage;
	public MenuItem fileMenuClear;
	public MenuItem editMenuVerticalFlip;
	public MenuItem editMenuHorizontalFlip;
	public MenuItem editMenuRotateLeft;
	public MenuItem editMenuRotateRight;
	public MenuItem editMenuRestoreCharts;
	public MenuItem editMenuMergeCharts;
	public MenuItem editMenuExtractChart;
	public MenuItem editMenuRemoveCharts;
	public MenuItem editMenuSelectAllFeatures;
	public MenuItem editMenuDeselectAllFeatures;
	public MenuItem navMenuTransform;
	public MenuItem navMenuSamples;
	public MenuItem navMenuCharts;
	public MenuItem navMenuChartsBySample;
	public MenuItem navMenuAllCharts;
	public MenuItem editMenuZoomIn;
	public MenuItem editMenuZoomOut;
	public Menu editMenu;
	public Menu navMenu;
	public MenuItem runMenuResultsButton;
	public RadioButton nearestRadioButton;
	public ToggleGroup interpolation;
	public RadioButton linearRadioButton;
	public RadioButton cubicRadioButton;
	public Menu runMenu;
	public VBox interpolationVBox;
	public GridPane transformColorControls;
	public ColorPicker transformBorderColor;
	public Label transformBorderLabel;
	public Tab samplesTab;
	public GridPane samplesMainPane;
	public GridPane samplesLeftGridPane;
	public VBox samplesToolbarVBox;
	public RadioButton selectRadioButton;
	public RadioButton createRadioButton;
	public ToggleGroup drawMethod;
	public GridPane samplesColorControls;
	public ColorPicker samplesFillColor;
	public ColorPicker samplesStrokeColor;
	public ColorPicker samplesBorderColor;
	public Label samplesFillLabel;
	public Label samplesStrokeLabel;
	public Label samplesBorderLabel;
	public BorderPane samplesBorderPane;
	public ScrollPane samplesScrollPane;
	public Group samplesImageViewGroup;
	public AnchorPane samplesImageViewAnchor;
	public ImageView samplesImageView;
	public GridPane samplesBottomGrid;
	public Label samplesImageSizeLabel;
	public ComboBox< String > samplesScaleCombo;
	public Label samplesMousePositionLabel;
	public GridPane samplesImageListGrid;
	public ListView< String > samplesImageList;
	public ComboBox< Integer > chartsSampleComboBox;
	public ColorPicker transformTriangleFillColor;
	public ColorPicker transformTriangleStrokeColor;
	public Label transformTriangleFillLabel;
	public Label transformTriangleStrokeLabel;
	public Button transformImagesButton;
	public HBox samplesTopHBox;
	public Label samplesInfo;
	public HBox transformTopHBox;
	public Label transformInfo;
	public Button horizontalFlipSamplesButton;
	public Button rotateRightSamplesButton;
	public Button rotateLeftSamplesButton;
	public Button verticalFlipSamplesButton;
	public VBox samplesModeVBox;

	@FXML
	private ScrollPane allChartsGridScrollPane;
	@FXML
	private GridPane allChartsGrid;
	@FXML
	private HBox chartsGraphsHBox;
	@FXML
	private HBox chartsGraphsToolbar;
	@FXML
	private Label chartsGraphsInfo;
	@FXML
	private Label transformImageListInfo;
	@FXML
	private HBox transformImageListToolbar;
	@FXML
	private RadioButton allChartsRadioButton;
	@FXML
	private Button chartsMergeButton;
	@FXML
	private Button chartsShiftButton;
	@FXML
	private Button chartsDeleteButton;

	@FXML
	private Button chartsRefreshButton;
	@FXML
	private Button deleteImageButton;
	@FXML
	private Button clearImagesButton;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private GridPane root;

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
	private Tab transformTab;

	@FXML
	private GridPane transformMainPane;

	@FXML
	private GridPane transformImageListGrid;

	@FXML
	private ListView< String > transformImageList;

	@FXML
	private Button loadImagesButton;

	@FXML
	private BorderPane transformBorderPane;

	@FXML
	private ScrollPane transformScrollPane;

	@FXML
	private AnchorPane transformImageViewAnchor;

	@FXML
	private Group transformImageViewGroup;

	@FXML
	private ImageView transformImageView;

	@FXML
	private GridPane transformBottomGrid;

	@FXML
	private Label transformImageSizeLabel;

	@FXML
	private ComboBox< String > transformScaleCombo;

	@FXML
	private Label transformMousePositionLabel;

	@FXML
	private VBox rightVBox;

	@FXML
	private TabPane rightVBoxTabPane;

	@FXML
	private Tab toolboxTab;

	@FXML
	private GridPane transformLeftGridPane;

	@FXML
	private ColorPicker transformFillColor;

	@FXML
	private ColorPicker transformStrokeColor;

	@FXML
	private Label transformFillLabel;

	@FXML
	private Label transformStrokeLabel;

	@FXML
	private Button verticalFlipButton;

	@FXML
	private Button horizontalFlipButton;

	@FXML
	private VBox transformToolbarVBox;

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
	private Tab chartsTab;

	@FXML
	private BorderPane chartsMainPane;

	@FXML
	private ScrollPane chartsBySampleGridScrollPane;

	@FXML
	private GridPane chartsBySampleGrid;

	@FXML
	private GridPane chartsControls;

	@FXML
	private ToggleGroup result;

	@FXML
	private RadioButton chartsBySampleRadioButton;

	@FXML
	private Label chartsColumnsLabel;

	@FXML
	private TextField chartsColumnsTextField;

	//others
	@FXML
	void about( ActionEvent event ) {
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
		alert.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icon-big.png"))));
		( (Stage) alert.getDialogPane().getScene().getWindow() ).getIcons().add(new Image(getClass().getResourceAsStream("/icon-small.png")));
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
		List< BarChart< String, Number > > charts = State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue());
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

				final List< Node > nodes = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
						(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
				final Integer integer = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
						(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
				chartsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
				chartsDeleteButton.setDisable(nodes.isEmpty());
				chartsMergeButton.setDisable(nodes.size() < 2);
				editMenuMergeCharts.setDisable(nodes.size() < 2);
				editMenuExtractChart.setDisable(nodes.size() != 1 || integer <= 1);
				editMenuRemoveCharts.setDisable(nodes.isEmpty());
			});
			charts.add(bc);
		}
		placeCharts();

		final List< Node > nodes = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
		final Integer integer = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
		chartsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
		chartsDeleteButton.setDisable(nodes.isEmpty());
		chartsMergeButton.setDisable(nodes.size() < 2);
		editMenuMergeCharts.setDisable(nodes.size() < 2);
		editMenuExtractChart.setDisable(nodes.size() != 1 || integer <= 1);
		editMenuRemoveCharts.setDisable(nodes.isEmpty());
	}

	@FXML
	void deselectAllFunctions( ActionEvent event ) {
		if ( !featuresTab.isSelected() ) rightVBoxTabPane.getSelectionModel().select(featuresTab);
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
				showAlert("Save failed! Check your write permissions.");
			}
		}
	}

	private String createCsvContents() {
		final Set< String > collect = State.INSTANCE.history.get().stream()
				.flatMap(r -> r.results.keySet().stream()).collect(Collectors.toSet());
		TreeSet< String > functions = new TreeSet<>(collect);
		String csvContents = "Sample,Image";
		for ( String s : functions )
			csvContents += ",\"" + s + "\"";
		int no = 0;
		for ( Result r : State.INSTANCE.history.get() ) {
			no++;
			Map< String, double[] > results = r.results;
			final List< String > images = r.imageNames;
			for ( int i = 0; i < images.size(); i++ ) {
				csvContents += "\r\n" + no + ",\"" + images.get(i) + "\"";
				for ( String s : functions ) {
					final double[] doubles = results.get(s);
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
	void flipHorizontal( ActionEvent event ) {
		if ( transformTab.isSelected() )
			flipTransformImageHorizontally();
		else if ( samplesTab.isSelected() )
			flipSamplesImageHorizontally();
	}

	private void flipTransformImageHorizontally() {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = State.INSTANCE.transformImages.get(imageName);
		Core.flip(img.imageData, img.imageData, 1);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Imgproc.cvtColor(imageCopy, imageCopy, Imgproc.COLOR_BGR2RGB);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		final TransformImageData newImageData = new TransformImageData(fxImage, img.imageData);
		refreshTransformImage(imageName, img, fxImage, newImageData);
	}

	private void refreshTransformImage( final String imageName, final TransformImageData img, final Image fxImage, final TransformImageData newImageData ) {
		State.INSTANCE.transformImages.put(imageName, newImageData);
		transformImageView.setImage(fxImage);
		transformImageViewGroup.getChildren().remove(img.triangle);
		for ( RectangleOfInterest r : img.rectangles )
			transformImageViewGroup.getChildren().remove(r);
		newImageData.triangle.setScaleX(transformImageView.getScaleX());
		newImageData.triangle.setScaleY(transformImageView.getScaleY());
		transformImageViewGroup.getChildren().add(newImageData.triangle);
		for ( RectangleOfInterest r : newImageData.rectangles ) {
			r.setScaleX(transformImageView.getScaleX());
			r.setScaleY(transformImageView.getScaleY());
		}
		transformImageViewGroup.getChildren().addAll(newImageData.rectangles);
		transformImageView.setTranslateX(transformImageView.getImage().getWidth() * 0.5 * ( transformImageView.getScaleX() - 1.0 ));
		transformImageView.setTranslateY(transformImageView.getImage().getHeight() * 0.5 * ( transformImageView.getScaleY() - 1.0 ));
		recalculateTranslates(transformImageView.getScaleX());
		transformImageSizeLabel.setText((int) newImageData.image.getWidth() + "x" + (int) newImageData.image.getHeight() + " px");
	}

	private void flipSamplesImageHorizontally() {
		String imageName = samplesImageList.getSelectionModel().getSelectedItem();
		SamplesImageData img = State.INSTANCE.samplesImages.get(imageName);
		Core.flip(img.imageData, img.imageData, 1);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteBgraPreInstance());
		final SamplesImageData newImageData = new SamplesImageData(fxImage, img.imageData);
		refreshSamplesImage(imageName, img, fxImage, newImageData);
	}

	private void refreshSamplesImage( final String imageName, final SamplesImageData img, final Image fxImage, final SamplesImageData newImageData ) {
		State.INSTANCE.samplesImages.put(imageName, newImageData);
		samplesImageView.setImage(fxImage);
		for ( RectangleOfInterest r : img.rectangles )
			samplesImageViewGroup.getChildren().remove(r);
		samplesImageView.setTranslateX(samplesImageView.getImage().getWidth() * 0.5 * ( samplesImageView.getScaleX() - 1.0 ));
		samplesImageView.setTranslateY(samplesImageView.getImage().getHeight() * 0.5 * ( samplesImageView.getScaleY() - 1.0 ));
		recalculateTranslates(samplesImageView.getScaleX());
		samplesImageSizeLabel.setText((int) newImageData.image.getWidth() + "x" + (int) newImageData.image.getHeight() + " px");
	}

	@FXML
	void flipVertical( ActionEvent event ) {
		if ( transformTab.isSelected() )
			flipTransformImageVertically();
		else if ( samplesTab.isSelected() )
			flipSamplesImageVertically();
	}

	private void flipTransformImageVertically() {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = State.INSTANCE.transformImages.get(imageName);
		Core.flip(img.imageData, img.imageData, 0);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Imgproc.cvtColor(imageCopy, imageCopy, Imgproc.COLOR_BGR2RGB);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		final TransformImageData newImageData = new TransformImageData(fxImage, img.imageData);
		refreshTransformImage(imageName, img, fxImage, newImageData);
	}

	private void flipSamplesImageVertically() {
		String imageName = samplesImageList.getSelectionModel().getSelectedItem();
		SamplesImageData img = State.INSTANCE.samplesImages.get(imageName);
		Core.flip(img.imageData, img.imageData, 0);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteBgraPreInstance());
		final SamplesImageData newImageData = new SamplesImageData(fxImage, img.imageData);
		refreshSamplesImage(imageName, img, fxImage, newImageData);
	}

	private void createHistoryCharts() {
		List< LineChart< String, Number > > charts = new ArrayList<>();
		List< Result > history = State.INSTANCE.history.get();
		final Set< String > collect = history.stream().flatMap(r -> r.results.keySet().stream()).collect(Collectors.toSet());
		Set< String > functions = new TreeSet<>(collect);
		for ( String s : functions ) {
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			final LineChart< String, Number > lc = new LineChart<>(xAxis, yAxis);
			lc.setTitle(s);
			yAxis.setLabel("Value");
			xAxis.setLabel("Sample");
			List< double[] > results = history.stream().map(r -> r.results.get(s)).collect(Collectors.toList());
			List< Series > series = new ArrayList<>();
			for ( int i = 0; i < results.size(); i++ ) {
				double[] r = results.get(i);
				if ( r != null ) {
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
						final ObservableList< XYChart.Series > data = lc.getData();
						s.getNode().setStyle("-fx-stroke: " + web + ";");
						( (ObservableList< XYChart.Data >) s.getData() ).forEach(n -> n.getNode().setStyle("-fx-background-color: " + web + ", white;"));
						State.INSTANCE.historySeriesColors.put(lc.getTitle() + "/" + s.getName(), newValue);
					});
					final List< Color > defaultColors = Arrays.asList(Color.web("#f3622d"), Color.web("#fba71b"), Color.web("#57b757"), Color.web("#41a9c9"),
							Color.web("#4258c9"), Color.web("#9a42c8"), Color.web("#c84164"), Color.web("#888888"));
					e.setValue(State.INSTANCE.historySeriesColors.get(lc.getTitle() + "/" + s.getName()) == null ? defaultColors.get(index % defaultColors
							.size()) : State.INSTANCE.historySeriesColors.get(lc.getTitle() + "/" + s.getName()));
					item.setSymbol(e);
				}
			}
		}
		State.INSTANCE.samplesCharts.get().clear();
		State.INSTANCE.samplesCharts.get().addAll(charts);
		placeHistoryCharts();
	}

	private void placeHistoryCharts() {
		List< LineChart< String, Number > > charts = State.INSTANCE.samplesCharts.get();
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
		int index = 0;
		for ( RectangleOfInterest r : State.INSTANCE.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem()).rectangles ) {
			List< BarChart< String, Number > > charts = State.INSTANCE.charts.get().get(index);
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
			index++;
		}
	}

	private void colorSeries( int idx ) {
		final List< BarChart< String, Number > > charts = State.INSTANCE.charts.get().get(idx);
		final List< Series > series = State.INSTANCE.series.get();

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
					State.INSTANCE.seriesColors.put(s.getName(), newValue);
				});
				final List< Color > defaultColors = Arrays.asList(Color.web("#f3622d"), Color.web("#fba71b"), Color.web("#57b757"), Color.web("#41a9c9"),
						Color.web("#4258c9"), Color.web("#9a42c8"), Color.web("#c84164"), Color.web("#888888"));

				e.setValue(State.INSTANCE.seriesColors.get(s.getName()) == null ? defaultColors.get(index % defaultColors.size()) : State.INSTANCE
						.seriesColors.get(s.getName()));
				item.setSymbol(e);
			}
		}
	}

	private List< Series > generateSeries() {
		Map< String, double[] > results = State.INSTANCE.result.getValue().results;
		List< Series > series = new ArrayList<>();
		for ( Map.Entry< String, double[] > e : results.entrySet() ) {
			Series series1 = new Series();
			series1.setName(e.getKey());
			for ( int i = 0; i < e.getValue().length; i++ ) {
				series1.getData().add(new XYChart.Data(transformImageList.getItems().get(i), e.getValue()[i]));
			}
			series.add(series1);
		}
		return series;
	}

	@FXML
	void selectAllFunctions( ActionEvent event ) {
		if ( !featuresTab.isSelected() ) rightVBoxTabPane.getSelectionModel().select(featuresTab);
		for ( Node chb : featuresVBox.getChildren() ) {
			( (CheckBox) chb ).setSelected(true);
		}
		DataGenerator.INSTANCE.chooseAllAvailableFunctions();
	}

	private void addNumberTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			if ( !newValue.matches("\\d+") )
				textField.setText(oldValue);
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

	@FXML
	void merge( ActionEvent actionEvent ) {
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

			final List< Node > nodes = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
			final Integer integer = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
			chartsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
			chartsDeleteButton.setDisable(nodes.isEmpty());
			chartsMergeButton.setDisable(nodes.size() < 2);
			editMenuMergeCharts.setDisable(nodes.size() < 2);
			editMenuExtractChart.setDisable(nodes.size() != 1 || integer <= 1);
			editMenuRemoveCharts.setDisable(nodes.isEmpty());
		});
		Optional< Integer > index = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> State.INSTANCE
				.charts.get().get(chartsSampleComboBox.getValue()).indexOf(n)).min(Integer::compareTo);
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue()).removeAll(chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue()).add(index.orElse(0), bc);
		placeCharts();


		final List< Node > nodes = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
		final Integer integer = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
		chartsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
		chartsDeleteButton.setDisable(nodes.isEmpty());
		chartsMergeButton.setDisable(nodes.size() < 2);
		editMenuMergeCharts.setDisable(nodes.size() < 2);
		editMenuExtractChart.setDisable(nodes.size() != 1 || integer <= 1);
		editMenuRemoveCharts.setDisable(nodes.isEmpty());
	}

	@FXML
	void shift( ActionEvent actionEvent ) {
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
		Series s = State.INSTANCE.series.get().stream().filter(n -> n.getName().equals(chart.getData().get(chart.getData().size() - 1).getName())).collect
				(Collectors.toList()).get(0);
		List< String > names = chart.getData().stream().filter(n -> !n.getName().equals(chart.getData().get(chart.getData().size() - 1).getName())).map(n -> n
				.getName()).collect(Collectors.toList());
		bc1.getData().add(s);
		bc2.getData().addAll(State.INSTANCE.series.get().stream().filter(n -> names.contains(n.getName())).toArray(Series[]::new));
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

			final List< Node > nodes = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
			final Integer integer = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
			chartsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
			chartsDeleteButton.setDisable(nodes.isEmpty());
			chartsMergeButton.setDisable(nodes.size() < 2);
			editMenuMergeCharts.setDisable(nodes.size() < 2);
			editMenuExtractChart.setDisable(nodes.size() != 1 || integer <= 1);
			editMenuRemoveCharts.setDisable(nodes.isEmpty());
		});
		bc2.setOnMouseClicked(e -> {
			if ( bc2.getStyle().equals(GRAPH_SELECTION_STYLE) )
				bc2.setStyle("-fx-border-color: null;");
			else
				bc2.setStyle(GRAPH_SELECTION_STYLE);

			final List< Node > nodes = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
			final Integer integer = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
					(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
			chartsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
			chartsDeleteButton.setDisable(nodes.isEmpty());
			chartsMergeButton.setDisable(nodes.size() < 2);
			editMenuMergeCharts.setDisable(nodes.size() < 2);
			editMenuExtractChart.setDisable(nodes.size() != 1 || integer <= 1);
			editMenuRemoveCharts.setDisable(nodes.isEmpty());

		});
		//								bc1.setLegendVisible(false);
		Optional< Integer > index = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> State.INSTANCE
				.charts.get().get(chartsSampleComboBox.getValue()).indexOf(n)).min(Integer::compareTo);
		//								State.INSTANCE.chartsBySample.get().removeAll(chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle()
		// .equals(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		int i = index.orElse(State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue()).size() - 1);
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue()).remove(chart);
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue()).add(i, bc1);
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue()).add(i, bc2);
		placeCharts();


		final List< Node > nodes = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
		final Integer integer = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
		chartsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
		chartsDeleteButton.setDisable(nodes.isEmpty());
		chartsMergeButton.setDisable(nodes.size() < 2);
		editMenuMergeCharts.setDisable(nodes.size() < 2);
		editMenuExtractChart.setDisable(nodes.size() != 1 || integer <= 1);
		editMenuRemoveCharts.setDisable(nodes.isEmpty());
	}

	@FXML
	void delete( ActionEvent actionEvent ) {
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue()).removeAll(chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		placeCharts();


		final List< Node > nodes = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList());
		final Integer integer = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).map(n -> ( (BarChart< String, Number >) n ).getData().size()).reduce(Integer::sum).orElse(0);
		chartsShiftButton.setDisable(nodes.size() != 1 || integer <= 1);
		chartsDeleteButton.setDisable(nodes.isEmpty());
		chartsMergeButton.setDisable(nodes.size() < 2);
		editMenuMergeCharts.setDisable(nodes.size() < 2);
		editMenuExtractChart.setDisable(nodes.size() != 1 || integer <= 1);
		editMenuRemoveCharts.setDisable(nodes.isEmpty());
	}

	@FXML
	void showAllCharts( ActionEvent actionEvent ) {

	}

	public void transformTab( ActionEvent actionEvent ) {
		if ( !transformTab.isSelected() ) mainTabPane.getSelectionModel().select(transformTab);
	}

	public void samplesTab( ActionEvent actionEvent ) {
		if ( !chartsTab.isSelected() ) mainTabPane.getSelectionModel().select(chartsTab);
	}

	public void chartsTab( ActionEvent actionEvent ) {
		if ( !transformTab.isSelected() ) mainTabPane.getSelectionModel().select(transformTab);
		if ( !featuresTab.isSelected() ) rightVBoxTabPane.getSelectionModel().select(featuresTab);
	}

	public void chartsBySample( ActionEvent actionEvent ) {
		if ( !chartsTab.isSelected() ) mainTabPane.getSelectionModel().select(chartsTab);
		chartsBySampleRadioButton.setSelected(true);
	}

	public void allCharts( ActionEvent actionEvent ) {
		if ( !chartsTab.isSelected() ) mainTabPane.getSelectionModel().select(chartsTab);
		allChartsRadioButton.setSelected(true);
	}

	public void zoomIn( ActionEvent actionEvent ) {
		if ( transformTab.isSelected() )
			transformImageView.setScaleX(transformImageView.getScaleX() * 1.05);
		else if ( samplesTab.isSelected() )
			samplesImageView.setScaleX(samplesImageView.getScaleX() * 1.05);
	}

	public void zoomOut( ActionEvent actionEvent ) {
		if ( transformTab.isSelected() )
			transformImageView.setScaleX(transformImageView.getScaleX() * 0.95);
		else if ( samplesTab.isSelected() )
			samplesImageView.setScaleX(samplesImageView.getScaleX() * 0.95);
	}

	@FXML
	void getResults( ActionEvent event ) {
		State.INSTANCE.setNoSelection();
		samplesImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
		doSample();
	}

	private void doSample() {
		final Mat[] images = new Mat[State.INSTANCE.samplesImages.size()];
		try {
			int i = 0;
			for ( String key : samplesImageList.getItems() ) {
				images[i] = State.INSTANCE.samplesImages.get(key).imageData;
				i++;
			}
			final Result result = DataGenerator.INSTANCE.generateData(ImageUtils.getImagesCopy(images));
			result.imageNames.addAll(samplesImageList.getItems());
			State.INSTANCE.result.setValue(result);
			State.INSTANCE.history.get().add(State.INSTANCE.result.get());
			State.INSTANCE.series.setValue(generateSeries());
			createHistoryCharts();
			refresh(null);
			mainTabPane.getSelectionModel().select(chartsTab);
			chartsBySampleRadioButton.setSelected(true);
		} catch ( CvException e ) {
			showAlert("Generating results failed! If you are using custom function, check them for errors.");
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
	void loadImages( ActionEvent event ) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load transformImages");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.tif"));
		List< File > selectedFiles = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
		if ( selectedFiles != null ) {
			for ( File f : selectedFiles ) {
				String filePath = null;
				try {
					filePath = f.getCanonicalPath();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
				String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
				Mat image = null;
				try {
					image = Imgcodecs.imread(filePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
					if ( image.dataAddr() == 0 )
						throw new CvException("Imgcodecs.imread failed to load image!");
				} catch ( CvException e ) {
					e.printStackTrace();
					showAlert("Loading failed!\nImages might be corrupted or you do not have sufficient read permissions.");
				}
				if ( image != null ) {
					final Mat imageCopy = ImageUtils.getImageCopy(image);
					Imgproc.cvtColor(image, imageCopy, Imgproc.COLOR_BGR2RGB);
					Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), imageCopy.width(), imageCopy.height(),
							imageCopy.channels(), PixelFormat.getByteRgbInstance());
					State.INSTANCE.transformImages.put(fileName, new TransformImageData(fxImage, image));
					transformImageList.getItems().remove(fileName);
					transformImageList.getItems().add(fileName);
					transformImageList.getSelectionModel().selectFirst();
				}
			}
		}
	}

	@FXML
	void rotateLeft( ActionEvent event ) {
		if ( transformTab.isSelected() )
			rotateLeftTransformImage();
		else if ( samplesTab.isSelected() )
			rotateLeftSamplesImage();
	}

	private void rotateLeftTransformImage() {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = State.INSTANCE.transformImages.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 0);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Imgproc.cvtColor(imageCopy, imageCopy, Imgproc.COLOR_BGR2RGB);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		final TransformImageData newImageData = new TransformImageData(fxImage, img.imageData);
		refreshTransformImage(imageName, img, fxImage, newImageData);
	}

	private void rotateLeftSamplesImage() {
		String imageName = samplesImageList.getSelectionModel().getSelectedItem();
		SamplesImageData img = State.INSTANCE.samplesImages.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 0);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteBgraPreInstance());
		final SamplesImageData newImageData = new SamplesImageData(fxImage, img.imageData);
		refreshSamplesImage(imageName, img, fxImage, newImageData);
	}

	@FXML
	void rotateRight( ActionEvent event ) {
		if ( transformTab.isSelected() )
			rotateRightTransformImage();
		else if ( samplesTab.isSelected() )
			rotateRightSamplesImage();
	}

	private void rotateRightTransformImage() {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = State.INSTANCE.transformImages.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 1);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Imgproc.cvtColor(imageCopy, imageCopy, Imgproc.COLOR_BGR2RGB);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		final TransformImageData newImageData = new TransformImageData(fxImage, img.imageData);
		refreshTransformImage(imageName, img, fxImage, newImageData);
	}

	private void rotateRightSamplesImage() {
		String imageName = samplesImageList.getSelectionModel().getSelectedItem();
		SamplesImageData img = State.INSTANCE.samplesImages.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 1);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteBgraPreInstance());
		final SamplesImageData newImageData = new SamplesImageData(fxImage, img.imageData);
		refreshSamplesImage(imageName, img, fxImage, newImageData);
	}

	@FXML
	void deleteImage() {
		String key = transformImageList.getSelectionModel().getSelectedItem();
		if ( key != null ) {
			transformImageList.getSelectionModel().clearSelection();
			transformImageList.getItems().remove(key);
			State.INSTANCE.transformImages.remove(key);
		}
	}

	@FXML
	void clearImages() {
		transformImageList.getSelectionModel().clearSelection();
		transformImageList.getItems().clear();
		State.INSTANCE.transformImages.clear();
	}

	@FXML
	void initialize() {
		//assertions
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
		assert transformLeftGridPane != null : "fx:id=\"transformLeftGridPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformFillColor != null : "fx:id=\"transformFillColor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformStrokeColor != null : "fx:id=\"transformStrokeColor\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformFillLabel != null : "fx:id=\"transformFillLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformStrokeLabel != null : "fx:id=\"transformStrokeLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert transformToolbarVBox != null : "fx:id=\"transformToolbarVBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
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
		assert allChartsRadioButton != null : "fx:id=\"allChartsRadioButton\" was not injected: check your FXML file 'gifa-gui.fxml'.";
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
		editMenuMergeCharts.setDisable(true);
		editMenuExtractChart.setDisable(true);
		editMenuRemoveCharts.setDisable(true);
		chartsColumnsTextField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			placeCharts();
			placeHistoryCharts();
		});
		setTooltips();
		createCheckBoxes();
		setVisibilityBindings();
		setEnablementBindings();
		setSelectionListeners();
		transformTriangleFillColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.transformImages.get(transformImageList.getSelectionModel().getSelectedItem()).triangle.setFill(newValue));
		transformTriangleStrokeColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.transformImages.get(transformImageList.getSelectionModel().getSelectedItem()).triangle.setStroke(newValue));
		transformFillColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedRectangle.get().setFill(newValue));
		transformStrokeColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedRectangle.get().setStroke(newValue));
		transformBorderColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedRectangle.get().setStroke(newValue));
		samplesFillColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedSample.get().setFill(newValue));
		samplesStrokeColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedSample.get().setStroke(newValue));
		samplesBorderColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedSample.get().setStroke(newValue));
		State.INSTANCE.selectedRectangle.addListener(( observable, oldValue, newValue ) -> {
			if ( newValue != null ) {
				transformFillColor.setValue((Color) newValue.getFill());
				transformStrokeColor.setValue((Color) newValue.getStroke());
				transformBorderColor.setValue((Color) newValue.getStroke());
				for ( Map.Entry< String, TransformImageData > e : State.INSTANCE.transformImages.entrySet() ) {
					if ( Arrays.asList(e.getValue().rectangles).contains(newValue) ) {
						transformImageList.getSelectionModel().select(e.getKey());
						break;
					}
				}
			}
		});
		State.INSTANCE.selectedSample.addListener(( observable, oldValue, newValue ) -> {
			if (createRadioButton.isSelected()) {
				State.INSTANCE.selectedSample.set(null);
			} else {
				if ( newValue != null ) {
					samplesFillColor.setValue((Color) newValue.getFill());
					samplesStrokeColor.setValue((Color) newValue.getStroke());
					samplesBorderColor.setValue((Color) newValue.getStroke());
				}
				for ( Map.Entry< String, SamplesImageData > e : State.INSTANCE.samplesImages.entrySet() ) {
					if (containsRectangle(newValue, e)) break;
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
			TransformImageData img = State.INSTANCE.transformImages.get(transformImageList.getSelectionModel().getSelectedItem());
			img.triangle.setScaleX(scale);
			img.triangle.setScaleY(scale);
			for ( RectangleOfInterest r : img.rectangles ) {
				r.setScaleX(scale);
				r.setScaleY(scale);
			}
			recalculateTranslates(scale);
		});

		samplesImageView.scaleXProperty().addListener(( observable, oldValue, newValue ) -> {
			final double scale = newValue.doubleValue();
			SamplesImageData img = State.INSTANCE.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem());
			for ( RectangleOfInterest r : img.rectangles ) {
				r.setScaleX(scale);
				r.setScaleY(scale);
			}
			recalculateTranslates(scale);
		});
		mainTabPane.setOnMouseReleased(event -> {
			State.INSTANCE.dragStarted = false;
			State.INSTANCE.setNoSelection();
			transformImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
			samplesImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
		});

		transformImageViewGroup.setOnMouseMoved(event -> {
			RectangleOfInterest rectangle = State.INSTANCE.selectedRectangle.get();
			transformMousePositionLabel.setText((int) ( event.getX() / transformImageView.getScaleX() ) + " : " + (int) ( event.getY() / transformImageView
					.getScaleY() ));
			if ( rectangle != null && State.INSTANCE.getRectangleSelection() != State.RectangleSelection.DRAG ) {
				double dX = event.getX() / transformImageView.getScaleX() - rectangle.getX();
				double dY = event.getY() / transformImageView.getScaleY() - rectangle.getY();
				if ( dX >= -7.0 / rectangle.getScaleX() && dY >= -7.0 / rectangle.getScaleX() && dX <= rectangle.getWidth() + 7.0 / rectangle.getScaleX() &&
						dY <= rectangle.getHeight() + 7.0 / rectangle.getScaleX() ) {
					if ( Math.abs(rectangle.getWidth() - dX) < 7.0 / rectangle.getScaleX() && Math.abs(rectangle.getHeight() - dY) < 7.0 / rectangle.getScaleX
							() ) {
						transformImageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.SE);
					} else if ( Math.abs(dX) < 7.0 / rectangle.getScaleX() && Math.abs(dY) < 7.0 / rectangle.getScaleX() ) {
						transformImageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.NW);
					} else if ( Math.abs(rectangle.getWidth() - dX) < 7.0 / rectangle.getScaleX() && Math.abs(dY) < 7.0 / rectangle.getScaleX() ) {
						transformImageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.NE);
					} else if ( Math.abs(dX) < 7.0 / rectangle.getScaleX() && Math.abs(rectangle.getHeight() - dY) < 7.0 / rectangle.getScaleX() ) {
						transformImageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.SW);
					} else if ( Math.abs(rectangle.getWidth() - dX) < 7.0 / rectangle.getScaleX() ) {
						transformImageViewGroup.getScene().setCursor(Cursor.H_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.E);
					} else if ( Math.abs(dX) < 7.0 / rectangle.getScaleX() ) {
						transformImageViewGroup.getScene().setCursor(Cursor.H_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.W);
					} else if ( Math.abs(rectangle.getHeight() - dY) < 7.0 / rectangle.getScaleX() ) {
						transformImageViewGroup.getScene().setCursor(Cursor.V_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.S);
					} else if ( Math.abs(dY) < 7.0 / rectangle.getScaleX() ) {
						transformImageViewGroup.getScene().setCursor(Cursor.V_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.N);
					} else {
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
						transformImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
					}
				} else {
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
					transformImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});
		transformImageViewGroup.setOnMouseExited(event -> {
			if ( !State.INSTANCE.dragStarted ) {
				State.INSTANCE.setNoSelection();
				transformImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
			}
		});
		transformImageViewGroup.setOnMouseDragged(event -> {
			double x = event.getX();
			double y = event.getY();
			State.INSTANCE.dragStarted = true;
			final double dX = ( x - State.INSTANCE.x ) / transformImageView.getScaleX();
			final double dY = ( y - State.INSTANCE.y ) / transformImageView.getScaleY();
			State.INSTANCE.x = x;
			State.INSTANCE.y = y;
			RectangleOfInterest rectangle = State.INSTANCE.selectedRectangle.get();
			Image image = transformImageView.getImage();
			switch ( State.INSTANCE.getRectangleSelection() ) {
				case NW:
					resizeNW(rectangle, image, dX, dY);
					break;
				case NE:
					resizeNE(rectangle, image, dX, dY);
					break;
				case SE:
					resizeSE(rectangle, image, dX, dY);
					break;
				case SW:
					resizeSW(rectangle, image, dX, dY);
					break;
				case W:
					resizeW(rectangle, image, dX, dY);
					break;
				case E:
					resizeE(rectangle, image, dX, dY);
					break;
				case S:
					resizeS(rectangle, image, dX, dY);
					break;
				case N:
					resizeN(rectangle, image, dX, dY);
					break;
				case DRAG:
					drag(rectangle, image, dX, dY);
					break;
				default:
					State.INSTANCE.dragStarted = false;
					break;
			}
			recalculateTranslates(transformImageView.getScaleX());
		});
		transformImageViewGroup.setOnMousePressed(event -> {
			if ( State.INSTANCE.getRectangleSelection() != State.RectangleSelection.NONE ) {
				State.INSTANCE.x = event.getX();
				State.INSTANCE.y = event.getY();
			}
		});
		transformImageViewGroup.setOnMouseReleased(event -> {
			if ( State.INSTANCE.getRectangleSelection() != State.RectangleSelection.NONE ) {
				State.INSTANCE.x = 0;
				State.INSTANCE.y = 0;
			}
		});

		samplesImageViewGroup.setOnMouseClicked(event -> {
			if ( createRadioButton.isSelected() ) {
				final SamplesImageData imageData = State.INSTANCE.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem());
				imageData.scale = samplesImageView.getScaleX();
				Image image = imageData.image;
				final double x = event.getX() / samplesImageView.getScaleX();
				final double y = event.getY() / samplesImageView.getScaleY();
				final double recX = Math.max(x - 50, 0);
				final double recY = Math.max(y - 50, 0);
				final double width = Math.min(100, image.getWidth() - recX);
				final double height = Math.min(100, image.getHeight() - recY);
				for ( SamplesImageData img : State.INSTANCE.samplesImages.values() ) {
					RectangleOfInterest r = new RectangleOfInterest(recX, recY, width, height);
					r.setScaleX(img.scale);
					r.setScaleY(img.scale);
					img.add(r);
				}
				final List< RectangleOfInterest > rectangles = imageData
						.rectangles;
				samplesImageViewAnchor.getChildren().add(rectangles.get(rectangles.size() - 1));
				recalculateTranslates(samplesImageView.getScaleX());
			}
		});

		samplesImageViewGroup.setOnMouseMoved(event -> {
			RectangleOfInterest rectangle = State.INSTANCE.selectedSample.get();
			samplesMousePositionLabel.setText((int) ( event.getX() / samplesImageView.getScaleX() ) + " : " + (int) ( event.getY() / samplesImageView
					.getScaleY() ));
			if ( rectangle != null && State.INSTANCE.getRectangleSelection() != State.RectangleSelection.DRAG ) {
				double dX = event.getX() / samplesImageView.getScaleX() - rectangle.getX();
				double dY = event.getY() / samplesImageView.getScaleY() - rectangle.getY();
				if ( dX >= -7.0 / rectangle.getScaleX() && dY >= -7.0 / rectangle.getScaleX() && dX <= rectangle.getWidth() + 7.0 / rectangle.getScaleX() &&
						dY <= rectangle.getHeight() + 7.0 / rectangle.getScaleX() ) {
					if ( Math.abs(rectangle.getWidth() - dX) < 7.0 / rectangle.getScaleX() && Math.abs(rectangle.getHeight() - dY) < 7.0 / rectangle.getScaleX
							() ) {
						samplesImageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.SE);
					} else if ( Math.abs(dX) < 7.0 / rectangle.getScaleX() && Math.abs(dY) < 7.0 / rectangle.getScaleX() ) {
						samplesImageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.NW);
					} else if ( Math.abs(rectangle.getWidth() - dX) < 7.0 / rectangle.getScaleX() && Math.abs(dY) < 7.0 / rectangle.getScaleX() ) {
						samplesImageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.NE);
					} else if ( Math.abs(dX) < 7.0 / rectangle.getScaleX() && Math.abs(rectangle.getHeight() - dY) < 7.0 / rectangle.getScaleX() ) {
						samplesImageViewGroup.getScene().setCursor(Cursor.CROSSHAIR);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.SW);
					} else if ( Math.abs(rectangle.getWidth() - dX) < 7.0 / rectangle.getScaleX() ) {
						samplesImageViewGroup.getScene().setCursor(Cursor.H_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.E);
					} else if ( Math.abs(dX) < 7.0 / rectangle.getScaleX() ) {
						samplesImageViewGroup.getScene().setCursor(Cursor.H_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.W);
					} else if ( Math.abs(rectangle.getHeight() - dY) < 7.0 / rectangle.getScaleX() ) {
						samplesImageViewGroup.getScene().setCursor(Cursor.V_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.S);
					} else if ( Math.abs(dY) < 7.0 / rectangle.getScaleX() ) {
						samplesImageViewGroup.getScene().setCursor(Cursor.V_RESIZE);
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.N);
					} else {
						State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
						samplesImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
					}
				} else {
					State.INSTANCE.setRectangleSelection(State.RectangleSelection.NONE);
					samplesImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});
		samplesImageViewGroup.setOnMouseExited(event -> {
			if ( !State.INSTANCE.dragStarted ) {
				State.INSTANCE.setNoSelection();
				samplesImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
			}
		});
		samplesImageViewGroup.setOnMouseDragged(event -> {
			double x = event.getX();
			double y = event.getY();
			State.INSTANCE.dragStarted = true;
			final double dX = ( x - State.INSTANCE.x ) / samplesImageView.getScaleX();
			final double dY = ( y - State.INSTANCE.y ) / samplesImageView.getScaleY();
			State.INSTANCE.x = x;
			State.INSTANCE.y = y;
			RectangleOfInterest rectangle = State.INSTANCE.selectedSample.get();
			Image image = samplesImageView.getImage();
			switch ( State.INSTANCE.getRectangleSelection() ) {
				case NW:
					resizeNW(rectangle, image, dX, dY);
					break;
				case NE:
					resizeNE(rectangle, image, dX, dY);
					break;
				case SE:
					resizeSE(rectangle, image, dX, dY);
					break;
				case SW:
					resizeSW(rectangle, image, dX, dY);
					break;
				case W:
					resizeW(rectangle, image, dX, dY);
					break;
				case E:
					resizeE(rectangle, image, dX, dY);
					break;
				case S:
					resizeS(rectangle, image, dX, dY);
					break;
				case N:
					resizeN(rectangle, image, dX, dY);
					break;
				case DRAG:
					drag(rectangle, image, dX, dY);
					break;
				default:
					State.INSTANCE.dragStarted = false;
					break;
			}
			recalculateTranslates(samplesImageView.getScaleX());
		});
		samplesImageViewGroup.setOnMousePressed(event -> {
			if ( State.INSTANCE.getRectangleSelection() != State.RectangleSelection.NONE ) {
				State.INSTANCE.x = event.getX();
				State.INSTANCE.y = event.getY();
			}
		});
		samplesImageViewGroup.setOnMouseReleased(event -> {
			if ( State.INSTANCE.getRectangleSelection() != State.RectangleSelection.NONE ) {
				State.INSTANCE.x = 0;
				State.INSTANCE.y = 0;
			}
		});

	}

	private boolean containsRectangle( final RectangleOfInterest newValue, final Map.Entry< String, SamplesImageData > e ) {
		boolean contains = false;
		for (RectangleOfInterest r : e.getValue().rectangles) {
			if (r == newValue) {
				samplesImageList.getSelectionModel().select(e.getKey());
				contains = true;
				break;
			}
		}
		return contains;
	}

	public void resizeNW( final RectangleOfInterest rectangle, final Image image, final double deltaX, final double deltaY ) {
		final double newWidth = rectangle.getWidth() - deltaX;
		final double newHeight = rectangle.getHeight() - deltaY;

		if ( !( Math.abs(rectangle.getX()) < 0.000001 ) || newWidth < rectangle.getWidth() ) {
			rectangle.setX(Math.max(rectangle.getX() + deltaX, 0));
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
		}
		if ( !( Math.abs(rectangle.getY()) < 0.000001 ) || newHeight < rectangle.getHeight() ) {
			rectangle.setY(Math.max(rectangle.getY() + deltaY, 0));
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));
		}
	}

	public void resizeSE( final RectangleOfInterest rectangle, final Image image, final double deltaX, final double deltaY ) {
		final double newWidth = rectangle.getWidth() + deltaX;
		final double newHeight = rectangle.getHeight() + deltaY;

		if ( !( rectangle.getX() + rectangle.getWidth() - image.getWidth() > 0 ) || newWidth < rectangle.getWidth() )
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
		if ( !( rectangle.getY() + rectangle.getHeight() - image.getHeight() > 0 ) || newHeight < rectangle.getHeight() )
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));
	}

	public void resizeSW( final RectangleOfInterest rectangle, final Image image, final double deltaX, final double deltaY ) {
		final double newWidth = rectangle.getWidth() - deltaX;
		final double newHeight = rectangle.getHeight() + deltaY;

		if ( !( Math.abs(rectangle.getX()) < 0.000001 ) || newWidth < rectangle.getWidth() ) {
			rectangle.setX(Math.max(rectangle.getX() + deltaX, 0));
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
		}
		if ( !( rectangle.getY() + rectangle.getHeight() - image.getHeight() > 0 ) || newHeight < rectangle.getHeight() )
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));

	}

	public void resizeNE( final RectangleOfInterest rectangle, final Image image, final double deltaX, final double deltaY ) {
		final double newWidth = rectangle.getWidth() + deltaX;
		final double newHeight = rectangle.getHeight() - deltaY;

		if ( !( Math.abs(rectangle.getY()) < 0.000001 ) || newHeight < rectangle.getHeight() ) {
			rectangle.setY(Math.max(rectangle.getY() + deltaY, 0));
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));
		}
		if ( !( rectangle.getX() + rectangle.getWidth() - image.getWidth() > 0 ) || newWidth < rectangle.getWidth() )
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
	}

	public void resizeE( final RectangleOfInterest rectangle, final Image image, final double deltaX, final double deltaY ) {
		final double newWidth = rectangle.getWidth() + deltaX;

		if ( !( rectangle.getX() + rectangle.getWidth() - image.getWidth() > 0 ) || newWidth < rectangle.getWidth() )
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
	}

	public void resizeW( final RectangleOfInterest rectangle, final Image image, final double deltaX, final double deltaY ) {
		final double newWidth = rectangle.getWidth() - deltaX;

		if ( !( Math.abs(rectangle.getX()) < 0.000001 ) || newWidth < rectangle.getWidth() ) {
			rectangle.setX(Math.max(rectangle.getX() + deltaX, 0));
			rectangle.setWidth(Math.min(newWidth, image.getWidth()));
		}
	}

	public void resizeS( final RectangleOfInterest rectangle, final Image image, final double deltaX, final double deltaY ) {
		final double newHeight = rectangle.getHeight() + deltaY;

		if ( !( rectangle.getY() + rectangle.getHeight() - image.getHeight() > 0 ) || newHeight < rectangle.getHeight() )
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));

	}

	public void resizeN( final RectangleOfInterest rectangle, final Image image, final double deltaX, final double deltaY ) {
		final double newHeight = rectangle.getHeight() - deltaY;

		if ( !( Math.abs(rectangle.getY()) < 0.000001 ) || newHeight < rectangle.getHeight() ) {
			rectangle.setY(Math.max(rectangle.getY() + deltaY, 0));
			rectangle.setHeight(Math.min(newHeight, image.getHeight()));
		}
	}

	public void drag( final RectangleOfInterest rectangle, final Image image, final double deltaX, final double deltaY ) {
		if ( !( rectangle.getX() + rectangle.getWidth() - image.getWidth() > 0 ) || deltaX < 0 )
			rectangle.setX(Math.min(Math.max(rectangle.getX() + deltaX, 0), image.getWidth() - rectangle.getWidth()));
		if ( !( rectangle.getY() + rectangle.getHeight() - image.getHeight() > 0 ) || deltaY < 0 )
			rectangle.setY(Math.min(Math.max(rectangle.getY() + deltaY, 0), image.getHeight() - rectangle.getHeight()));
	}

	private void setTooltips() {
		horizontalFlipButton.setTooltip(new Tooltip("Flip horizontally"));
		verticalFlipButton.setTooltip(new Tooltip("Flip vertically"));
		rotateLeftButton.setTooltip(new Tooltip("Rotate left by 90°"));
		rotateRightButton.setTooltip(new Tooltip("Rotate right by 90°"));
		horizontalFlipSamplesButton.setTooltip(new Tooltip("Flip horizontally"));
		verticalFlipSamplesButton.setTooltip(new Tooltip("Flip vertically"));
		rotateLeftSamplesButton.setTooltip(new Tooltip("Rotate left by 90°"));
		rotateRightSamplesButton.setTooltip(new Tooltip("Rotate right by 90°"));
		loadImagesButton.setTooltip(new Tooltip("Load transformImages"));
		deleteImageButton.setTooltip(new Tooltip("Remove image"));
		clearImagesButton.setTooltip(new Tooltip("Clear image list"));
		chartsRefreshButton.setTooltip(new Tooltip("Restore charts"));
		chartsMergeButton.setTooltip(new Tooltip("Merge chartse"));
		chartsShiftButton.setTooltip(new Tooltip("Extract chart"));
		chartsDeleteButton.setTooltip(new Tooltip("Remove charts"));
	}

	private void recalculateTranslates( final double scale ) {
		if ( transformTab.isSelected() )
			recalculateForTransformImage(scale);
		else if ( samplesTab.isSelected() )
			recalculateForSamplesImage(scale);

	}

	private void recalculateForTransformImage( final double scale ) {
		final TransformImageData imageData = State.INSTANCE.transformImages.get(transformImageList.getSelectionModel().getSelectedItem());
		imageData.triangle.setTranslateX(( transformImageView.getImage().getWidth() * 0.5 * ( scale - 1.0 ) ) -
				( transformImageView.getImage().getWidth() * 0.5 - getTriangleMiddleX() ) * ( scale - 1.0 ));
		imageData.triangle.setTranslateY(( transformImageView.getImage().getHeight() * 0.5 * ( scale - 1.0 ) ) -
				( transformImageView.getImage().getHeight() * 0.5 - getTriangleMiddleY() ) * ( scale - 1.0 ));
		for ( RectangleOfInterest r : imageData.rectangles ) {
			r.setTranslateX(( transformImageView.getImage().getWidth() * 0.5 * ( scale - 1.0 ) ) -
					( transformImageView.getImage().getWidth() * 0.5 - ( r.getX() + r.getWidth() * 0.5 ) ) * ( scale - 1.0 ));
			r.setTranslateY(( transformImageView.getImage().getHeight() * 0.5 * ( scale - 1.0 ) ) -
					( transformImageView.getImage().getHeight() * 0.5 - ( r.getY() + r.getHeight() * 0.5 ) ) * ( scale - 1.0 ));
		}
	}

	private void recalculateForSamplesImage( final double scale ) {
		final SamplesImageData imageData = State.INSTANCE.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem());
		for ( RectangleOfInterest r : imageData.rectangles ) {
			r.setTranslateX(( samplesImageView.getImage().getWidth() * 0.5 * ( scale - 1.0 ) ) -
					( samplesImageView.getImage().getWidth() * 0.5 - ( r.getX() + r.getWidth() * 0.5 ) ) * ( scale - 1.0 ));
			r.setTranslateY(( samplesImageView.getImage().getHeight() * 0.5 * ( scale - 1.0 ) ) -
					( samplesImageView.getImage().getHeight() * 0.5 - ( r.getY() + r.getHeight() * 0.5 ) ) * ( scale - 1.0 ));
		}
	}

	private double getTriangleMiddleX() {
		final TransformImageData imageData = State.INSTANCE.transformImages.get(transformImageList.getSelectionModel().getSelectedItem());
		double firstPointX = imageData.triangle.getPoints().get(0);
		double secondPointX = imageData.triangle.getPoints().get(2);
		double thirdPointX = imageData.triangle.getPoints().get(4);
		double minX = getMin(firstPointX, secondPointX, thirdPointX);
		double maxX = getMax(firstPointX, secondPointX, thirdPointX);
		return ( maxX - minX ) / 2.0 + minX;
	}

	private double getTriangleMiddleY() {
		final TransformImageData imageData = State.INSTANCE.transformImages.get(transformImageList.getSelectionModel().getSelectedItem());
		double firstPointY = imageData.triangle.getPoints().get(1);
		double secondPointY = imageData.triangle.getPoints().get(3);
		double thirdPointY = imageData.triangle.getPoints().get(5);
		double minY = getMin(firstPointY, secondPointY, thirdPointY);
		double maxY = getMax(firstPointY, secondPointY, thirdPointY);
		return ( maxY - minY ) / 2.0 + minY;
	}

	private double getMin( final double firstPointX, final double secondPointX, final double thirdPointX ) {
		return Math.min(firstPointX, Math.min(secondPointX, thirdPointX));
	}

	private double getMax( final double firstPointY, final double secondPointY, final double thirdPointY ) {
		return Math.max(firstPointY, Math.max(secondPointY, thirdPointY));
	}

	private void setImageViewControls( ImageView imageView, ScrollPane imageScrollPane, Group imageViewGroup, ComboBox< String > scaleCombo, Label
			mousePositionLabel ) {
		imageViewGroup.setOnMouseMoved(event -> mousePositionLabel.setText((int) ( event.getX() / imageView.getScaleX() ) + " : " + (int) ( event.getY() /
				imageView.getScaleY() )));
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

	private void setVisibilityBindings() {
		transformInfo.visibleProperty().bind(transformImageView.imageProperty().isNotNull());
		transformImageViewGroup.visibleProperty().bind(transformImageView.imageProperty().isNotNull());
		transformBottomGrid.visibleProperty().bind(transformImageView.imageProperty().isNotNull());
		transformLeftGridPane.visibleProperty().bind(transformImageView.imageProperty().isNotNull());
		samplesLeftGridPane.visibleProperty().bind(samplesImageView.imageProperty().isNotNull());
		samplesImageViewGroup.visibleProperty().bind(samplesImageView.imageProperty().isNotNull());
		samplesBottomGrid.visibleProperty().bind(samplesImageView.imageProperty().isNotNull());
		rightVBox.visibleProperty().bind(samplesImageView.imageProperty().isNotNull());
		chartsBySampleGridScrollPane.visibleProperty().bind(chartsBySampleRadioButton.selectedProperty());
		allChartsGridScrollPane.visibleProperty().bind(allChartsRadioButton.selectedProperty());
		chartsColumnsLabel.visibleProperty().bind(Bindings.or(chartsBySampleRadioButton.selectedProperty(), allChartsRadioButton.selectedProperty()));
		chartsColumnsTextField.visibleProperty().bind(Bindings.or(chartsBySampleRadioButton.selectedProperty(), allChartsRadioButton.selectedProperty()));
		chartsGraphsToolbar.visibleProperty().bind(chartsBySampleRadioButton.selectedProperty());
		chartsGraphsHBox.visibleProperty().bind(chartsBySampleRadioButton.selectedProperty());
		transformImageListInfo.visibleProperty().bind(Bindings.isEmpty(transformImageList.getItems()));
		samplesColorControls.visibleProperty().bind(Bindings.isNotNull(State.INSTANCE.selectedSample));
		transformFillColor.visibleProperty().bind(Bindings.isNotNull(State.INSTANCE.selectedRectangle));
		transformStrokeColor.visibleProperty().bind(Bindings.isNotNull(State.INSTANCE.selectedRectangle));
		transformBorderColor.visibleProperty().bind(Bindings.isNotNull(State.INSTANCE.selectedRectangle));
		transformFillLabel.visibleProperty().bind(Bindings.isNotNull(State.INSTANCE.selectedRectangle));
		transformStrokeLabel.visibleProperty().bind(Bindings.isNotNull(State.INSTANCE.selectedRectangle));
		transformBorderLabel.visibleProperty().bind(Bindings.isNotNull(State.INSTANCE.selectedRectangle));
	}

	private void setEnablementBindings() {
		fileMenuRemoveImage.disableProperty().bind(Bindings.or(transformImageList.getSelectionModel().selectedItemProperty().isNull(), transformTab
				.selectedProperty().not()));
		fileMenuClear.disableProperty().bind(Bindings.or(Bindings.isEmpty(transformImageList.getItems()), transformTab.selectedProperty().not()));
		editMenuVerticalFlip.disableProperty().bind(Bindings.or(transformImageView.imageProperty().isNull(), transformTab.selectedProperty().not()));
		editMenuHorizontalFlip.disableProperty().bind(Bindings.or(transformImageView.imageProperty().isNull(), transformTab.selectedProperty().not()));
		editMenuRotateLeft.disableProperty().bind(Bindings.or(transformImageView.imageProperty().isNull(), transformTab.selectedProperty().not()));
		editMenuRotateRight.disableProperty().bind(Bindings.or(transformImageView.imageProperty().isNull(), transformTab.selectedProperty().not()));
		editMenuSelectAllFeatures.disableProperty().bind(Bindings.or(samplesImageView.imageProperty().isNull(), transformTab.selectedProperty().not()));
		editMenuDeselectAllFeatures.disableProperty().bind(Bindings.or(samplesImageView.imageProperty().isNull(), transformTab.selectedProperty().not()));
		editMenuRestoreCharts.disableProperty().bind(chartsBySampleRadioButton.selectedProperty().not());
		editMenuZoomIn.disableProperty().bind(Bindings.or(Bindings.and(transformImageView.imageProperty().isNull(), transformTab.selectedProperty()), Bindings
				.and(samplesImageView.imageProperty().isNull(), samplesTab.selectedProperty())));
		editMenuZoomOut.disableProperty().bind(Bindings.or(Bindings.and(transformImageView.imageProperty().isNull(), transformTab.selectedProperty()),
				Bindings.and(samplesImageView.imageProperty().isNull(), samplesTab.selectedProperty())));
		navMenuSamples.disableProperty().bind(State.INSTANCE.result.isNull());
		navMenuChartsBySample.disableProperty().bind(State.INSTANCE.result.isNull());
		navMenuAllCharts.disableProperty().bind(State.INSTANCE.result.isNull());
		navMenuCharts.disableProperty().bind(State.INSTANCE.result.isNull());
		deleteImageButton.disableProperty().bind(transformImageList.getSelectionModel().selectedItemProperty().isNull());
		clearImagesButton.disableProperty().bind(Bindings.isEmpty(transformImageList.getItems()));
		fileMenuExportToCsv.disableProperty().bind(State.INSTANCE.result.isNull());
		IntegerProperty featuresSize = new SimpleIntegerProperty(featuresVBox.getChildren().size());
		BooleanBinding noFeaturesAvailable = Bindings.equal(0, featuresSize);
		BooleanBinding noFeaturesChosen = Bindings.createBooleanBinding(
				() -> featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
						.map(CheckBox.class::cast).noneMatch(CheckBox::isSelected),
				featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
						.map(CheckBox.class::cast).map(CheckBox::selectedProperty).toArray(Observable[]::new)
		);
		BooleanBinding emptySamples = Bindings.createBooleanBinding(
				() -> State.INSTANCE.samplesImages.entrySet().stream()
						.noneMatch(e -> e.getValue().rectangles.isEmpty()));

		BooleanBinding equalNoOfSamples = Bindings.createBooleanBinding(
				() -> State.INSTANCE.samplesImages.entrySet().stream()
						.map(e -> e.getValue().rectangles.size()).collect(Collectors.toSet()).size() == 1);

		BooleanBinding emptyOrNotEqual = Bindings.or(emptySamples, equalNoOfSamples);
		resultsButton.disableProperty().bind(Bindings.or(emptyOrNotEqual,
				Bindings.or(Bindings.isEmpty(samplesImageList.getItems()), Bindings.or(noFeaturesAvailable, noFeaturesChosen))));
		runMenuResultsButton.disableProperty().bind(Bindings.or(samplesTab.selectedProperty().not(),
				Bindings.or(emptyOrNotEqual,
						Bindings.or(Bindings.isEmpty(samplesImageList.getItems()), Bindings.or(noFeaturesAvailable, noFeaturesChosen)))));
		;
		transformImagesButton.disableProperty().bind(Bindings.isEmpty(transformImageList.getItems()));
		samplesTab.disableProperty().bind(Bindings.isEmpty(samplesImageList.getItems()));
		chartsTab.disableProperty().bind(State.INSTANCE.result.isNull());

	}

	private void setSelectionListeners() {
		transformImageList.getSelectionModel().selectedItemProperty().addListener(
				( observable, oldValue, newValue ) -> {
					if ( oldValue != null && !oldValue.isEmpty() ) {
						TransformImageData img = State.INSTANCE.transformImages.get(oldValue);
						img.scale = transformImageView.getScaleX();
						img.hScrollPos = transformScrollPane.getHvalue();
						img.vScrollPos = transformScrollPane.getVvalue();
						transformImageViewAnchor.getChildren().remove(img.triangle);
						for ( RectangleOfInterest r : img.rectangles )
							transformImageViewAnchor.getChildren().remove(r);
						State.INSTANCE.selectedRectangle.set(null);
					}
					if ( newValue != null ) {
						TransformImageData img = State.INSTANCE.transformImages.get(newValue);
						transformImageView.setImage(img.image);
						transformImageViewAnchor.getChildren().add(img.triangle);
						transformImageViewAnchor.getChildren().addAll(img.rectangles);
						transformImageView.setScaleX(img.scale);
						transformScrollPane.setHvalue(img.hScrollPos);
						transformScrollPane.setVvalue(img.vScrollPos);
						transformImageView.setTranslateX(transformImageView.getImage().getWidth() * 0.5 * ( transformImageView.getScaleX() - 1.0 ));
						transformImageView.setTranslateY(transformImageView.getImage().getHeight() * 0.5 * ( transformImageView.getScaleY() - 1.0 ));
						recalculateTranslates(transformImageView.getScaleX());
						transformImageSizeLabel.setText((int) img.image.getWidth() + "x" + (int) img.image.getHeight() + " px");
						transformTriangleFillColor.setValue((Color) img.triangle.getFill());
						transformTriangleStrokeColor.setValue((Color) img.triangle.getStroke());
					} else {
						transformImageView.setImage(null);
						transformImageSizeLabel.setText("");
						State.INSTANCE.selectedRectangle.set(null);
					}
				}
		);
		samplesImageList.getSelectionModel().selectedItemProperty().addListener(
				( observable, oldValue, newValue ) -> {
					if ( oldValue != null && !oldValue.isEmpty() ) {
						SamplesImageData img = State.INSTANCE.samplesImages.get(oldValue);
						img.scale = samplesImageView.getScaleX();
						img.hScrollPos = samplesScrollPane.getHvalue();
						img.vScrollPos = samplesScrollPane.getVvalue();
						for ( RectangleOfInterest r : img.rectangles )
							samplesImageViewAnchor.getChildren().remove(r);
						State.INSTANCE.selectedSample.set(null);
					}
					if ( newValue != null ) {
						SamplesImageData img = State.INSTANCE.samplesImages.get(newValue);
						samplesImageView.setImage(img.image);
						samplesImageViewAnchor.getChildren().addAll(img.rectangles);
						samplesImageView.setScaleX(img.scale);
						samplesScrollPane.setHvalue(img.hScrollPos);
						samplesScrollPane.setVvalue(img.vScrollPos);
						samplesImageView.setTranslateX(samplesImageView.getImage().getWidth() * 0.5 * ( samplesImageView.getScaleX() - 1.0 ));
						samplesImageView.setTranslateY(samplesImageView.getImage().getHeight() * 0.5 * ( samplesImageView.getScaleY() - 1.0 ));
						recalculateTranslates(samplesImageView.getScaleX());
						samplesImageSizeLabel.setText((int) img.image.getWidth() + "x" + (int) img.image.getHeight() + " px");
					} else {
						samplesImageView.setImage(null);
						samplesImageSizeLabel.setText("");
						State.INSTANCE.selectedSample.set(null);
					}
				}
		);
	}

	public void transform( ActionEvent actionEvent ) {
		for (List<ImageView> l : State.INSTANCE.sampleImageViews) l.clear();
		State.INSTANCE.sampleImageViews.clear();
		final Mat[] images = new Mat[State.INSTANCE.transformImages.size()];
		int interpolation = cubicRadioButton.isSelected() ?
				Imgproc.INTER_CUBIC : linearRadioButton.isSelected() ?
				Imgproc.INTER_LINEAR : Imgproc.INTER_NEAREST;
		try {
			final MatOfPoint2f[] points = new MatOfPoint2f[State.INSTANCE.transformImages.size()];
			int i = 0;
			for ( String key : transformImageList.getItems() ) {
				images[i] = State.INSTANCE.transformImages.get(key).imageData;
				points[i] = State.INSTANCE.transformImages.get(key).triangle.getMatOfPoints();
				i++;
			}
			final SamplesImageData[] result = DataGenerator.INSTANCE.transform(ImageUtils.getImagesCopy(images), points, interpolation);
			samplesImageList.getItems().addAll(transformImageList.getItems());
			for ( int j = 0; j < result.length; j++ )
				State.INSTANCE.samplesImages.put(samplesImageList.getItems().get(j), result[j]);
			mainTabPane.getSelectionModel().select(samplesTab);
			samplesImageList.getSelectionModel().selectFirst();
		} catch ( CvException e ) {
			showAlert("Transforming transformImages failed! Please check selected points.");
		}
	}
}
