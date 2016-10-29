package pwr.chrzescijanek.filip.gifa.controller;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.charts.Legend;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import pwr.chrzescijanek.filip.gifa.Main;
import pwr.chrzescijanek.filip.gifa.core.generator.DataGeneratorFactory;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;
import pwr.chrzescijanek.filip.gifa.core.util.Result;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.opencv.imgproc.Imgproc.*;

public class Controller implements Initializable {

	//static strings
	public static final String THEME_PREFERENCE_KEY = "gifa.theme";
	public static final String THEME_DARK = "/theme-dark.css";
	public static final String THEME_LIGHT = "/theme-light.css";
	public static final String GRAPH_SELECTION_STYLE = "-fx-border-color: yellow; -fx-border-width: 3px;";

	//fields
	@Inject private DataGeneratorFactory factory;
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
	public VBox samplesModeVBox;
	public MenuItem editMenuDeleteSample;
	public Label chartsSampleLabel;
	public MenuItem runMenuTransformButton;
	public MenuItem editMenuCreateMode;
	public MenuItem editMenuSelectMode;

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
		ObservableList< String > stylesheets = root.getStylesheets();
		stylesheets.clear();
		stylesheets.add(theme);
	}

	@FXML
	void refresh( ActionEvent actionEvent ) {
		final Integer index = chartsSampleComboBox.getValue() - 1;
		createCharts(index);
		placeCharts();
		validateChartsControlsDisableProperties();
	}

	private void createCharts( final int index ) {
		List< Series > series = State.INSTANCE.series.get().get(index);
		List< BarChart< String, Number > > charts = State.INSTANCE.charts.get().get(index);
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
	void deselectAllFunctions( ActionEvent event ) {
		samplesTab(null);
		if ( !featuresTab.isSelected() ) rightVBoxTabPane.getSelectionModel().select(featuresTab);
		for ( Node chb : featuresVBox.getChildren() ) {
			( (CheckBox) chb ).setSelected(false);
		}
		factory.clearChosenFunctions();
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
		final Set< String > collect = State.INSTANCE.results.get().stream()
				.flatMap(r -> r.getResults().keySet().stream()).collect(Collectors.toSet());
		TreeSet< String > functions = new TreeSet<>(collect);
		String csvContents = "Sample,Image";
		for ( String s : functions )
			csvContents += ",\"" + s + "\"";
		int no = 0;
		for ( Result r : State.INSTANCE.results.get() ) {
			no++;
			Map< String, UnmodifiableArrayList<Double> > results = r.getResults();
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
	void flipHorizontal( ActionEvent event ) {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = State.INSTANCE.transformImages.get(imageName);
		Core.flip(img.imageData, img.imageData, 1);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		cvtColor(imageCopy, imageCopy, COLOR_BGR2RGB);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		refreshTransformImage(img, fxImage);
		}

	private void refreshTransformImage(final TransformImageData img, final Image fxImage ) {
		img.image = fxImage;
		img.writableImage.set(new WritableImage(fxImage.getPixelReader(), (int) fxImage.getWidth(), (int) fxImage.getHeight()));
		transformImageView.setImage(fxImage);
		img.rectangles[0].setRectangle(fxImage.getWidth() * 2 / 5, fxImage.getHeight() / 5, fxImage.getWidth() / 5, fxImage.getHeight() / 5, fxImage);
		img.rectangles[1].setRectangle(fxImage.getWidth() * 1 / 5, fxImage.getHeight() * 3 / 5, fxImage.getWidth() / 5, fxImage.getHeight() / 5, fxImage);
		img.rectangles[2].setRectangle(fxImage.getWidth() * 3 / 5, fxImage.getHeight() * 3 / 5, fxImage.getWidth() / 5, fxImage.getHeight() / 5, fxImage);
		transformImageView.setTranslateX(transformImageView.getImage().getWidth() * 0.5 * ( transformImageView.getScaleX() - 1.0 ));
		transformImageView.setTranslateY(transformImageView.getImage().getHeight() * 0.5 * ( transformImageView.getScaleY() - 1.0 ));
		recalculateTranslates(transformImageView.getScaleX());
		transformImageSizeLabel.setText((int) fxImage.getWidth() + "x" + (int) fxImage.getHeight() + " px");
	}

	@FXML
	void flipVertical( ActionEvent event ) {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = State.INSTANCE.transformImages.get(imageName);
		Core.flip(img.imageData, img.imageData, 0);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		cvtColor(imageCopy, imageCopy, COLOR_BGR2RGB);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		refreshTransformImage(img, fxImage);
	}

	private void createHistoryCharts() {
		List< LineChart< String, Number > > charts = new ArrayList<>();
		List< Result > history = State.INSTANCE.results.get();
		final Set< String > collect = history.stream().flatMap(r -> r.getResults().keySet().stream()).collect(Collectors.toSet());
		Set< String > functions = new TreeSet<>(collect);
		for ( String s : functions ) {
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			final LineChart< String, Number > lc = new LineChart<>(xAxis, yAxis);
			lc.setTitle(s);
			yAxis.setLabel("Value");
			xAxis.setLabel("Sample");
			List< Double[] > results = history.stream().map(r -> r.getResults().get(s).toArray(new Double[]{})).collect(Collectors.toList());
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
						final ObservableList< XYChart.Series > data = lc.getData();
						s.getNode().setStyle("-fx-stroke: " + web + ";");
						( (ObservableList< XYChart.Data >) s.getData() ).forEach(n -> n.getNode().setStyle("-fx-background-color: " + web + ", white;"));
						State.INSTANCE.resultsSeriesColors.put(lc.getTitle() + "/" + s.getName(), newValue);
					});
					final List< Color > defaultColors = Arrays.asList(Color.web("#f3622d"), Color.web("#fba71b"), Color.web("#57b757"), Color.web("#41a9c9"),
							Color.web("#4258c9"), Color.web("#9a42c8"), Color.web("#c84164"), Color.web("#888888"));
					e.setValue(State.INSTANCE.resultsSeriesColors.get(lc.getTitle() + "/" + s.getName()) == null ? defaultColors.get(index % defaultColors
							.size()) : State.INSTANCE.resultsSeriesColors.get(lc.getTitle() + "/" + s.getName()));
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
		final Integer index = chartsSampleComboBox.getValue() - 1;
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
	}

	private void colorSeries( int idx ) {
		final List< BarChart< String, Number > > charts = State.INSTANCE.charts.get().get(idx);
		final List< Series > series = State.INSTANCE.series.get().get(idx);

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
					State.INSTANCE.seriesColors.get(idx).put(s.getName(), newValue);
				});
				final List< Color > defaultColors = Arrays.asList(Color.web("#f3622d"), Color.web("#fba71b"), Color.web("#57b757"), Color.web("#41a9c9"),
						Color.web("#4258c9"), Color.web("#9a42c8"), Color.web("#c84164"), Color.web("#888888"));
				e.setValue(State.INSTANCE.seriesColors.get(idx).get(s.getName()) == null ? defaultColors.get(index % defaultColors.size()) : State.INSTANCE
						.seriesColors.get(idx).get(s.getName()));
				item.setSymbol(e);
			}
		}
	}

	private List< Series > generateSeries( final Map< String, UnmodifiableArrayList<Double> > results ) {
		List< Series > series = new ArrayList<>();
		for ( Map.Entry< String, UnmodifiableArrayList<Double> > e : results.entrySet() ) {
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
	void selectAllFunctions( ActionEvent event ) {
		samplesTab(null);
		if ( !featuresTab.isSelected() ) rightVBoxTabPane.getSelectionModel().select(featuresTab);
		for ( Node chb : featuresVBox.getChildren() ) {
			( (CheckBox) chb ).setSelected(true);
		}
		factory.chooseAllAvailableFunctions();
	}

	private void addNumberTextFieldListener( TextField textField ) {
		textField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			if ( !newValue.matches("\\d+") )
				textField.setText(oldValue);
		});
	}

	private void createCheckBoxes() {
		for ( String s : factory.getAvailableFunctionsNames() ) {
			final CheckBox checkBox = new CheckBox(s);
			featuresVBox.getChildren().add(checkBox);
			checkBox.selectedProperty().addListener(( observable, oldValue, newValue ) -> {
				if ( newValue ) factory.chooseFunction(s);
				else factory.deselectFunction(s);
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

			validateChartsControlsDisableProperties();
		});
		Optional< Integer > index = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> State.INSTANCE
				.charts.get().get(chartsSampleComboBox.getValue() - 1).indexOf(n)).min(Integer::compareTo);
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).removeAll(chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).add(index.orElse(0), bc);
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
		final Integer index = chartsSampleComboBox.getValue() - 1;
		Series s = State.INSTANCE.series.get().get(index).stream().filter(n -> n.getName().equals(chart.getData().get(chart.getData().size() - 1).getName())).collect
				(Collectors.toList()).get(0);
		List< String > names = chart.getData().stream().filter(n -> !n.getName().equals(chart.getData().get(chart.getData().size() - 1).getName())).map(n -> n
				.getName()).collect(Collectors.toList());
		bc1.getData().add(s);
		bc2.getData().addAll(State.INSTANCE.series.get().get(index).stream().filter(n -> names.contains(n.getName())).toArray(Series[]::new));
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
		Optional< Integer > idx = chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals(GRAPH_SELECTION_STYLE)).map(n -> State.INSTANCE
				.charts.get().get(chartsSampleComboBox.getValue() - 1).indexOf(n)).min(Integer::compareTo);
		//								State.INSTANCE.chartsBySample.get().removeAll(chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle()
		// .equals(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		int i = idx.orElse(State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).size() - 1);
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).remove(chart);
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).add(i, bc1);
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).add(i, bc2);
		placeCharts();


		validateChartsControlsDisableProperties();
	}

	@FXML
	void delete( ActionEvent actionEvent ) {
		State.INSTANCE.charts.get().get(chartsSampleComboBox.getValue() - 1).removeAll(chartsBySampleGrid.getChildren().stream().filter(n -> n.getStyle().equals
				(GRAPH_SELECTION_STYLE)).collect(Collectors.toList()));
		placeCharts();
		validateChartsControlsDisableProperties();
	}

	@FXML
	void showAllCharts( ActionEvent actionEvent ) {	}

	public void transformTab( ActionEvent actionEvent ) {
		if ( !transformTab.isSelected() ) mainTabPane.getSelectionModel().select(transformTab);
	}

	public void samplesTab( ActionEvent actionEvent ) {
		if ( !samplesTab.isSelected() ) mainTabPane.getSelectionModel().select(samplesTab);
	}

	public void chartsTab( ActionEvent actionEvent ) {if ( !chartsTab.isSelected() ) mainTabPane.getSelectionModel().select(chartsTab);
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
			updateScrollbars(transformImageView, transformScrollPane, 1);
		else if ( samplesTab.isSelected() )
			updateScrollbars(samplesImageView, samplesScrollPane, 1);
	}

	public void zoomOut( ActionEvent actionEvent ) {
		if ( transformTab.isSelected() )
			updateScrollbars(transformImageView, transformScrollPane, -1);
		else if ( samplesTab.isSelected() )
			updateScrollbars(samplesImageView, samplesScrollPane, -1);
	}

	@FXML
	void calculateResults() {
		State.INSTANCE.setNoSelection();
		samplesImageViewGroup.getScene().setCursor(Cursor.DEFAULT);
		doSample();
	}

	private void doSample() {
		try {
			SamplesImageData img = State.INSTANCE.samplesImages.get(samplesImageList.getSelectionModel().getSelectedItem());
			State.INSTANCE.results.set(new ArrayList<>());
			State.INSTANCE.series.get().clear();
			State.INSTANCE.charts.get().clear();
			State.INSTANCE.samplesCharts.get().clear();
			State.INSTANCE.seriesColors.clear();
			State.INSTANCE.resultsSeriesColors.clear();
			if (img != null) {
				for ( int j = 0; j < img.rectangles.size(); j++) {
					int i = 0;
					RectangleOfInterest r = img.rectangles.get(j);
					final Mat[] images = new Mat[State.INSTANCE.samplesImages.size()];
					for ( String key : samplesImageList.getItems() ) {
						final SamplesImageData samplesImageData = State.INSTANCE.samplesImages.get(key);
						images[i] = samplesImageData.imageData
								.submat(new Rect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight()));
						Mat zeros = Mat.zeros(images[i].rows(), images[i].cols(), images[i].type());
						ellipse(zeros, new Point(r.getWidth() / 2, r.getHeight() / 2), new Size(r.getWidth() / 2, r.getHeight() / 2), 0.0, 0.0, 360.0, new Scalar(255, 255, 255, 255), Core.FILLED);
						Core.bitwise_and(images[i], zeros, images[i]);
//						Imgcodecs.imwrite("img" + i + ".png", images[i]);
						i++;
					}
					final Result result = new Result(samplesImageList.getItems(), factory.createGenerator().generateData(ImageUtils.getImagesCopy(images)));
					State.INSTANCE.results.get().add(result);
					State.INSTANCE.series.get().add(generateSeries(result.getResults()));
					State.INSTANCE.charts.get().add(new ArrayList<>());
					State.INSTANCE.seriesColors.add(new HashMap<>());
					createCharts(j);
				}
				chartsSampleComboBox.setItems(FXCollections.observableArrayList(IntStream.range(1, State.INSTANCE.results.get().size() + 1).boxed().collect(Collectors.toList())));
				chartsSampleComboBox.setValue(1);
				createHistoryCharts();
				placeCharts();
				validateChartsControlsDisableProperties();
				mainTabPane.getSelectionModel().select(chartsTab);
				chartsBySampleRadioButton.setSelected(true);
			}
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
					cvtColor(image, imageCopy, COLOR_BGR2RGB);
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
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = State.INSTANCE.transformImages.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 0);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		cvtColor(imageCopy, imageCopy, COLOR_BGR2RGB);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		refreshTransformImage(img, fxImage);
	}

	@FXML
	void rotateRight( ActionEvent event ) {
		String imageName = transformImageList.getSelectionModel().getSelectedItem();
		TransformImageData img = State.INSTANCE.transformImages.get(imageName);
		Core.transpose(img.imageData, img.imageData);
		Core.flip(img.imageData, img.imageData, 1);
		Mat imageCopy = ImageUtils.getImageCopy(img.imageData);
		cvtColor(imageCopy, imageCopy, COLOR_BGR2RGB);
		Image fxImage = ImageUtils.createImage(ImageUtils.getImageData(imageCopy), img.imageData.width(), img.imageData.height(),
				img.imageData.channels(), PixelFormat.getByteRgbInstance());
		refreshTransformImage(img, fxImage);
	}

	@FXML
	void deleteImage() {
		String key = transformImageList.getSelectionModel().getSelectedItem();
		if ( key != null ) {
			transformImageList.getSelectionModel().clearSelection();
			final int index = transformImageList.getItems().indexOf(key);
			transformImageList.getItems().remove(key);
			State.INSTANCE.imageViews.forEach(
					l -> l.remove(index)
			);
			State.INSTANCE.transformImages.remove(key);
		}
	}

	@FXML
	void clearImages() {
		transformImageList.getSelectionModel().clearSelection();
		transformImageList.getItems().clear();
		State.INSTANCE.transformImages.clear();
		State.INSTANCE.imageViews.forEach(List< ImageView >::clear);
		State.INSTANCE.imageViews.clear();
	}

	@Override
	public void initialize( final URL location, final ResourceBundle resources ) {

		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String s = prefs.get(Controller.THEME_PREFERENCE_KEY, Controller.THEME_LIGHT);
		if (s.equals(Controller.THEME_LIGHT)) {
			getThemeToggleGroup().selectToggle(getLightThemeToggle());
			root.getStylesheets().add(Controller.THEME_LIGHT);
		} else {
			getThemeToggleGroup().selectToggle(getDarkThemeToggle());
			root.getStylesheets().add(Controller.THEME_DARK);
		}
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
		chartsSampleComboBox.getSelectionModel().selectedItemProperty().addListener(( observable, oldValue, newValue ) -> {
			if (newValue != null) {
				placeCharts();
				validateChartsControlsDisableProperties();
			}
		});
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
				State.INSTANCE.selectedRectangle.get().sample.setFill(newValue));
		transformStrokeColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedRectangle.get().sample.setStroke(newValue));
		transformBorderColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedRectangle.get().setStroke(newValue));
		samplesFillColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedSample.get().sample.setFill(newValue));
		samplesStrokeColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedSample.get().sample.setStroke(newValue));
		samplesBorderColor.valueProperty().addListener(( observable, oldValue, newValue ) ->
				State.INSTANCE.selectedSample.get().setStroke(newValue));
		State.INSTANCE.selectedRectangle.addListener(( observable, oldValue, newValue ) -> {
			if ( newValue != null ) {
				transformFillColor.setValue((Color) newValue.sample.getFill());
				transformStrokeColor.setValue((Color) newValue.sample.getStroke());
				transformBorderColor.setValue((Color) newValue.getStroke());
				for ( Map.Entry< String, TransformImageData > e : State.INSTANCE.transformImages.entrySet() ) {
					if ( Arrays.asList(e.getValue().rectangles).contains(newValue) ) {
						transformImageList.getSelectionModel().select(e.getKey());
						break;
					}
				}

				if (State.INSTANCE.zoom) {
				double newX = Math.max(0, newValue.getX() - 50);
				double newY = Math.max(0, newValue.getY() - 50);
				double newWidth = newValue.getWidth() + 100;
				double newHeight = newValue.getHeight() + 100;
				final double scale = transformScrollPane.getWidth() / transformScrollPane.getHeight() > newWidth / newHeight ?
						transformScrollPane.getHeight() / newHeight
						: transformScrollPane.getWidth() / newWidth;
				transformImageView.setScaleX(scale);
				double newHDenominator = calculateDenominator(
						transformImageView.getScaleX(), transformImageView.getImage().getWidth(), transformScrollPane.getWidth());
				double newVDenominator = calculateDenominator(
						transformImageView.getScaleX(), transformImageView.getImage().getHeight(), transformScrollPane.getHeight());
				transformScrollPane.setHvalue(
						(Math.max(0, (newX + newWidth / 2) * transformImageView.getScaleX() - (transformScrollPane.getWidth() / 2)) / (transformScrollPane.getWidth() / 2))
								/ newHDenominator);
				transformScrollPane.setVvalue(
						(Math.max(0, (newY + newHeight / 2) * transformImageView.getScaleX() - (transformScrollPane.getHeight() / 2)) / (transformScrollPane.getHeight() / 2))
						/ newVDenominator);
				}
				State.INSTANCE.zoom = false;
			}
		});
		State.INSTANCE.selectedSample.addListener(( observable, oldValue, newValue ) -> {
			if (createRadioButton.isSelected()) {
				State.INSTANCE.selectedSample.set(null);
			} else {
				if ( newValue != null ) {
					samplesFillColor.setValue((Color) newValue.sample.getFill());
					samplesStrokeColor.setValue((Color) newValue.sample.getStroke());
					samplesBorderColor.setValue((Color) newValue.getStroke());
					for ( Map.Entry< String, SamplesImageData > e : State.INSTANCE.samplesImages.entrySet() ) {
						if ( e.getValue().rectangles.contains(newValue) ) {
							samplesImageList.getSelectionModel().select(e.getKey());
							break;
						}
					}
					if (State.INSTANCE.zoom) {
						double newX = Math.max(0, newValue.getX() - 50);
						double newY = Math.max(0, newValue.getY() - 50);
						double newWidth = newValue.getWidth() + 100;
						double newHeight = newValue.getHeight() + 100;
						final double scale = samplesScrollPane.getWidth() / samplesScrollPane.getHeight() > newWidth / newHeight ?
								samplesScrollPane.getHeight() / newHeight
								: samplesScrollPane.getWidth() / newWidth;
						samplesImageView.setScaleX(scale);
						double newHDenominator = calculateDenominator(
								samplesImageView.getScaleX(), samplesImageView.getImage().getWidth(), samplesScrollPane.getWidth());
						double newVDenominator = calculateDenominator(
								samplesImageView.getScaleX(), samplesImageView.getImage().getHeight(), samplesScrollPane.getHeight());
						samplesScrollPane.setHvalue(
								( Math.max(0, ( newX + newWidth / 2 ) * samplesImageView.getScaleX() - ( samplesScrollPane.getWidth() / 2 )) / ( transformScrollPane.getWidth() / 2 ) )

										/ newHDenominator);
						samplesScrollPane.setVvalue(
								( Math.max(0, ( newY + newHeight / 2 ) * samplesImageView.getScaleX() - ( samplesScrollPane.getHeight() / 2 )) / ( transformScrollPane.getHeight() / 2 ) )
										/ newVDenominator);
					}
					State.INSTANCE.zoom = false;
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
					transformMousePositionLabel.setText((int) ( event.getX() / transformImageView.getScaleX() ) + " : " + (int) ( event.getY() / transformImageView
							.getScaleY() ));

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
					RectangleOfInterest r = new RectangleOfInterest(recX, recY, width, height, img.image);
					r.setScaleX(img.scale);
					r.setScaleY(img.scale);
					img.add(r);
				}
				final List< RectangleOfInterest > rectangles = imageData
						.rectangles;
				final RectangleOfInterest rectangleOfInterest = rectangles.get(rectangles.size() - 1);
				rectangleOfInterest.setVisible(false);
				samplesImageViewAnchor.getChildren().add(rectangleOfInterest);
				samplesImageViewAnchor.getChildren().add(rectangleOfInterest.sample);
				recalculateTranslates(samplesImageView.getScaleX());
			}
		});

		samplesImageViewGroup.setOnMouseMoved(event -> {
					samplesMousePositionLabel.setText((int) ( event.getX() / samplesImageView.getScaleX() ) + " : " + (int) ( event.getY() / samplesImageView
							.getScaleY() ));
				});

	}

	private void setTooltips() {
		horizontalFlipButton.setTooltip(new Tooltip("Flip horizontally"));
		verticalFlipButton.setTooltip(new Tooltip("Flip vertically"));
		rotateLeftButton.setTooltip(new Tooltip("Rotate left by 90°"));
		rotateRightButton.setTooltip(new Tooltip("Rotate right by 90°"));
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
				updateScrollbars(imageView, imageScrollPane, deltaY);
			}
		});
		imageScrollPane.addEventFilter(ScrollEvent.ANY, event -> {
			if ( event.isControlDown() ) {
				double deltaY = event.getDeltaY();
				updateScrollbars(imageView, imageScrollPane, deltaY);
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
				final double oldScale = imageView.getScaleX();
				final double hValue = imageScrollPane.getHvalue();
				final double vValue = imageScrollPane.getVvalue();
				imageView.setScaleX(scale);
				imageView.setScaleY(scale);
				imageView.setTranslateX(imageView.getImage().getWidth() * 0.5 * ( scale - 1.0 ));
				imageView.setTranslateY(imageView.getImage().getHeight() * 0.5 * ( scale - 1.0 ));
				if (Math.round(oldScale * 100) != Math.round(scale * 100)) {
					validateScrollbars(imageView, imageScrollPane, scale, oldScale, hValue, vValue);
				}
			}
		});
	}

	private void validateScrollbars( final ImageView imageView, final ScrollPane imageScrollPane, final double scale, final double oldScale,
									 final double hValue, final double vValue) {
		if ( (scale * imageView.getImage().getWidth() > imageScrollPane.getWidth())) {
			double oldHDenominator = calculateDenominator(oldScale, imageView.getImage().getWidth(), imageScrollPane.getWidth());
			double newHDenominator = calculateDenominator(scale, imageView.getImage().getWidth(), imageScrollPane.getWidth());
			imageScrollPane.setHvalue(calculateValue(scale, oldScale, hValue, oldHDenominator, newHDenominator));
		}
		if ((scale * imageView.getImage().getHeight() > imageScrollPane.getHeight())) {
			double oldVDenominator = calculateDenominator(oldScale, imageView.getImage().getHeight(), imageScrollPane.getHeight());
			double newVDenominator = calculateDenominator(scale, imageView.getImage().getHeight(), imageScrollPane.getHeight());
			imageScrollPane.setVvalue(calculateValue(scale, oldScale, vValue, oldVDenominator, newVDenominator));
		}
	}

	private double calculateValue( final double scale, final double oldScale, final double value, final double oldDenominator, final double newDenominator ) {
		return ((scale - 1) + (value * oldDenominator - (oldScale - 1)) / oldScale * scale) / newDenominator;
	}

	private double calculateDenominator( final double scale, final double imageSize, final double paneSize ) {
		return (scale * imageSize - paneSize) * 2 / paneSize;
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
		chartsSampleComboBox.visibleProperty().bind(chartsBySampleRadioButton.selectedProperty());
		chartsSampleLabel.visibleProperty().bind(chartsBySampleRadioButton.selectedProperty());
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
		editMenuCreateMode.disableProperty().bind(Bindings.or(samplesTab.selectedProperty().not(), createRadioButton.selectedProperty()));
		editMenuSelectMode.disableProperty().bind(Bindings.or(samplesTab.selectedProperty().not(), selectRadioButton.selectedProperty()));
		editMenuDeleteSample.disableProperty().bind(
				Bindings.or(
					Bindings.or(State.INSTANCE.selectedSample.isNull(), selectRadioButton.selectedProperty().not()),
					Bindings.or(samplesTab.selectedProperty().not(), samplesImageView.imageProperty().isNull())
				)
		);
		editMenuSelectAllFeatures.disableProperty().bind(Bindings.or(samplesImageView.imageProperty().isNull(), samplesTab.selectedProperty().not()));
		editMenuDeselectAllFeatures.disableProperty().bind(Bindings.or(samplesImageView.imageProperty().isNull(), samplesTab.selectedProperty().not()));
		editMenuRestoreCharts.disableProperty().bind(Bindings.or(chartsBySampleRadioButton.selectedProperty().not(), chartsTab.selectedProperty().not()));
		editMenuZoomIn.disableProperty().bind(Bindings.or(chartsTab.selectedProperty(), Bindings.or(Bindings.and(transformImageView.imageProperty().isNull(), transformTab.selectedProperty()), Bindings
				.and(samplesImageView.imageProperty().isNull(), samplesTab.selectedProperty()))));
		editMenuZoomOut.disableProperty().bind(Bindings.or(chartsTab.selectedProperty(), Bindings.or(Bindings.and(transformImageView.imageProperty().isNull(), transformTab.selectedProperty()),
				Bindings.and(samplesImageView.imageProperty().isNull(), samplesTab.selectedProperty()))));
		navMenuSamples.disableProperty().bind(State.INSTANCE.results.isNull());
		navMenuChartsBySample.disableProperty().bind(State.INSTANCE.results.isNull());
		navMenuAllCharts.disableProperty().bind(State.INSTANCE.results.isNull());
		navMenuCharts.disableProperty().bind(State.INSTANCE.results.isNull());
		deleteImageButton.disableProperty().bind(transformImageList.getSelectionModel().selectedItemProperty().isNull());
		clearImagesButton.disableProperty().bind(Bindings.isEmpty(transformImageList.getItems()));
		fileMenuExportToCsv.disableProperty().bind(State.INSTANCE.results.isNull());
		IntegerProperty featuresSize = new SimpleIntegerProperty(featuresVBox.getChildren().size());
		BooleanBinding noFeaturesAvailable = Bindings.equal(0, featuresSize);
		BooleanBinding noFeaturesChosen = Bindings.createBooleanBinding(
				() -> featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
						.map(CheckBox.class::cast).noneMatch(CheckBox::isSelected),
				featuresVBox.getChildren().stream().filter(CheckBox.class::isInstance)
						.map(CheckBox.class::cast).map(CheckBox::selectedProperty).toArray(Observable[]::new)
		);

		resultsButton.disableProperty().bind(Bindings.or(Bindings.isEmpty(State.INSTANCE.sampleImageViews),
				Bindings.or(Bindings.isEmpty(samplesImageList.getItems()), Bindings.or(noFeaturesAvailable, noFeaturesChosen))));
		runMenuResultsButton.disableProperty().bind(Bindings.or(samplesTab.selectedProperty().not(),
				Bindings.or(Bindings.isEmpty(State.INSTANCE.sampleImageViews),
						Bindings.or(Bindings.isEmpty(samplesImageList.getItems()), Bindings.or(noFeaturesAvailable, noFeaturesChosen)))));
		transformImagesButton.disableProperty().bind(Bindings.isEmpty(transformImageList.getItems()));
		runMenuTransformButton.disableProperty().bind(Bindings.or(transformTab.selectedProperty().not(),
				Bindings.isEmpty(transformImageList.getItems())));
		samplesTab.disableProperty().bind(Bindings.isEmpty(samplesImageList.getItems()));
		chartsTab.disableProperty().bind(State.INSTANCE.results.isNull());

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
						transformImageViewAnchor.getChildren().removeAll(img.rectangles);
						transformImageViewAnchor.getChildren().removeAll(
								Arrays.stream(img.rectangles).map(r -> r.sample).toArray(Ellipse[]::new)
						);
						State.INSTANCE.selectedRectangle.set(null);
					}
					if ( newValue != null ) {
						TransformImageData img = State.INSTANCE.transformImages.get(newValue);
						transformImageView.setImage(img.image);
						transformImageViewAnchor.getChildren().add(img.triangle);
						transformImageViewAnchor.getChildren().addAll(img.rectangles);
						transformImageViewAnchor.getChildren().addAll(
								Arrays.stream(img.rectangles).map(r -> r.sample).toArray(Ellipse[]::new)
						);
						Arrays.stream(img.rectangles).forEach(r -> r.setVisible(false));
						transformImageView.setScaleX(img.scale);
						transformScrollPane.setHvalue(img.hScrollPos);
						transformScrollPane.setVvalue(img.vScrollPos);
						transformImageView.setTranslateX(transformImageView.getImage().getWidth() * 0.5 * ( transformImageView.getScaleX() - 1.0 ));
						transformImageView.setTranslateY(transformImageView.getImage().getHeight() * 0.5 * ( transformImageView.getScaleY() - 1.0 ));
						recalculateTranslates(transformImageView.getScaleX());
						transformImageSizeLabel.setText((int) img.image.getWidth() + "x" + (int) img.image.getHeight() + " px");
						transformTriangleFillColor.setValue((Color) img.triangle.getFill());
						transformTriangleStrokeColor.setValue((Color) img.triangle.getStroke());
						State.INSTANCE.selectedRectangle.set(null);
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
						samplesImageViewAnchor.getChildren().removeAll(img.rectangles);
						samplesImageViewAnchor.getChildren().removeAll(
								img.rectangles.stream().map(r -> r.sample).toArray(Ellipse[]::new)
						);
						State.INSTANCE.selectedSample.set(null);
					}
					if ( newValue != null ) {
						SamplesImageData img = State.INSTANCE.samplesImages.get(newValue);
						samplesImageView.setImage(img.image);
						samplesImageViewAnchor.getChildren().addAll(img.rectangles);
						samplesImageViewAnchor.getChildren().addAll(
								img.rectangles.stream().map(r -> r.sample).toArray(Ellipse[]::new)
						);
						img.rectangles.forEach(r -> r.setVisible(false));
						samplesImageView.setScaleX(img.scale);
						samplesScrollPane.setHvalue(img.hScrollPos);
						samplesScrollPane.setVvalue(img.vScrollPos);
						samplesImageView.setTranslateX(samplesImageView.getImage().getWidth() * 0.5 * ( samplesImageView.getScaleX() - 1.0 ));
						samplesImageView.setTranslateY(samplesImageView.getImage().getHeight() * 0.5 * ( samplesImageView.getScaleY() - 1.0 ));
						recalculateTranslates(samplesImageView.getScaleX());
						samplesImageSizeLabel.setText((int) img.image.getWidth() + "x" + (int) img.image.getHeight() + " px");
						State.INSTANCE.selectedSample.set(null);
					} else {
						samplesImageView.setImage(null);
						samplesImageSizeLabel.setText("");
						State.INSTANCE.selectedSample.set(null);
					}
				}
		);
	}

	public void transform( ActionEvent actionEvent ) {
		samplesImageList.getSelectionModel().clearSelection();
		samplesImageList.getItems().clear();
		State.INSTANCE.sampleImageViews.forEach(List< ImageView >::clear);
		State.INSTANCE.sampleImageViews.clear();
		State.INSTANCE.samplesImages.clear();
		samplesImageViewAnchor.getChildren().removeIf(n -> !(n instanceof ImageView));
		final Mat[] images = new Mat[State.INSTANCE.transformImages.size()];
		int interpolation = cubicRadioButton.isSelected() ?
				INTER_CUBIC : linearRadioButton.isSelected() ?
				INTER_LINEAR : INTER_NEAREST;
		try {
			final MatOfPoint2f[] points = new MatOfPoint2f[State.INSTANCE.transformImages.size()];
			int i = 0;
			for ( String key : transformImageList.getItems() ) {
				images[i] = State.INSTANCE.transformImages.get(key).imageData;
				points[i] = State.INSTANCE.transformImages.get(key).triangle.getMatOfPoints();
				i++;
			}
			final SamplesImageData[] result = transform(ImageUtils.getImagesCopy(images), points, interpolation);
			samplesImageList.getItems().addAll(transformImageList.getItems());
			for ( int j = 0; j < result.length; j++ )
				State.INSTANCE.samplesImages.put(samplesImageList.getItems().get(j), result[j]);
			mainTabPane.getSelectionModel().select(samplesTab);
			samplesImageList.getSelectionModel().selectFirst();
		} catch ( CvException e ) {
			showAlert("Transforming transformImages failed! Please check selected points.");
		}
	}

	private SamplesImageData[] transform( final Mat[] images, final MatOfPoint2f[] points, int interpolation) {
		if ( images.length != points.length )
			throw new IllegalArgumentException("Images count does not match passed vertices count!");
		ImageUtils.performAffineTransformations(images, points, interpolation);
		return Arrays.stream(images)
				.map(i ->
						new SamplesImageData(
								ImageUtils.createImage(
										ImageUtils.getImageData(i), i.width(), i.height(), i.channels(), PixelFormat.getByteBgraPreInstance()
								), i
						)
				).toArray(SamplesImageData[]::new);
	}

	public void deleteSample( ActionEvent actionEvent ) {
		RectangleOfInterest r = State.INSTANCE.selectedSample.get();
		if (r != null) {
			int index = -1;
			for (SamplesImageData img : State.INSTANCE.samplesImages.values()) {
				index = img.rectangles.indexOf(r);
				if (index >= 0) break;
			}
			for (SamplesImageData img : State.INSTANCE.samplesImages.values()) {
				img.rectangles.remove(index);
			}
			State.INSTANCE.selectedSample.set(null);
			State.INSTANCE.sampleImageViews.remove(index);
			samplesImageViewAnchor.getChildren().remove(r.sample);
			samplesImageViewAnchor.getChildren().remove(r);
		}
	}

	public void setCreateMode( ActionEvent actionEvent ) {
		if (!createRadioButton.isSelected()) createRadioButton.setSelected(true);
	}

	public void setSelectMode( ActionEvent actionEvent ) {
		if (!selectRadioButton.isSelected()) selectRadioButton.setSelected(true);
	}

}
