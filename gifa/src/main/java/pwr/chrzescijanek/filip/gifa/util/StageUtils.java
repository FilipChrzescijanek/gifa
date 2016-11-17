package pwr.chrzescijanek.filip.gifa.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import pwr.chrzescijanek.filip.gifa.controller.CompareViewController;
import pwr.chrzescijanek.filip.gifa.view.FXView;

import static javafx.scene.control.Alert.AlertType;
import static javafx.stage.Modality.APPLICATION_MODAL;
import static javafx.stage.StageStyle.UNDECORATED;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.initializeSampleController;

/**
 * Provides utility methods for handling stages.
 */
public final class StageUtils {

	private StageUtils() { }

	/**
	 * @return about dialog
	 */
	public static Alert getAboutDialog() {
		final Alert alert = new Alert(AlertType.INFORMATION,
		                              "Global image features analyzer\n" +
		                              "GitHub repository: https://github.com/FilipChrzescijanek/gifa/\n" +
		                              "\nCopyright © 2016 Filip Chrześcijanek\nfilip.chrzescijanek@gmail.com",
		                              ButtonType.OK);
		alert.setTitle("About");
		alert.setHeaderText("gifa");
		alert.setGraphic(new ImageView(new Image(StageUtils.class.getResourceAsStream("/images/icon-big.png"))));
		((Stage) alert.getDialogPane().getScene().getWindow())
				.getIcons().add(new Image(StageUtils.class.getResourceAsStream("/images/icon-small.png")));
		return alert;
	}

	/**
	 * @param content alert content
	 * @return error alert
	 */
	public static Alert getErrorAlert(final String content) {
		return new Alert(AlertType.ERROR, content, ButtonType.OK);
	}

	/**
	 * @param window application window
	 * @return new undecorated modal dialog
	 */
	public static Stage initDialog(final Window window) {
		final Stage dialog = new Stage();
		dialog.initOwner(window);
		dialog.initStyle(UNDECORATED);
		dialog.initModality(APPLICATION_MODAL);
		return dialog;
	}

	/**
	 * Constructs new application's compare view's stage with FXML view from given path, label, title and compare views
	 * of vertex with given index.
	 *
	 * @param viewPath    FXML view path
	 * @param info        label
	 * @param title       stage title
	 * @param sampleIndex sample index
	 * @return new application's compare stage
	 */
	public static Stage getCompareSampleStage(final String viewPath, final String info, final String title,
	                                          final int sampleIndex) {
		final Stage newStage = new Stage();
		final FXView fxView = new FXView(viewPath);
		final CompareViewController controller = initializeSampleController(info, sampleIndex, fxView);
		showStage(newStage, fxView, controller, title);
		return newStage;
	}

	/**
	 * Shows given applications' compare view's stage.
	 *
	 * @param newStage   stage
	 * @param fxView     FXML view
	 * @param controller compare view's controller
	 * @param title      stage title
	 */
	public static void showStage(final Stage newStage, final FXView fxView, final CompareViewController controller,
	                             final
	                             String title) {
		preparePanelStage(newStage, title, fxView);
		controller.refresh();
		newStage.show();
	}

	/**
	 * Prepares applications' compare view's stage.
	 *
	 * @param stage stage on which compare view will be shown
	 * @param title stage title
	 * @param view  compare view
	 */
	public static void preparePanelStage(final Stage stage, final String title, final FXView view) {
		setTitleAndIcon(stage, title);
		setPanelScene(stage, view);
	}

	private static void setTitleAndIcon(final Stage stage, final String title) {
		stage.setTitle(title);
		final Image icon = new Image(StageUtils.class.getResourceAsStream("/images/icon-small.png"));
		stage.getIcons().add(icon);
	}

	private static void setPanelScene(final Stage stage, final FXView fxView) {
		final Parent root = fxView.getView();
		setScene(stage, root, 720, 360);
	}

	private static void setScene(final Stage stage, final Parent root, final int width, final int height) {
		final Scene scene = new Scene(root, width, height);
		stage.setScene(scene);
	}

	/**
	 * Prepares application's help view stage.
	 *
	 * @param stage stage on which help will be shown
	 * @param root  help view
	 */
	public static void prepareHelpStage(final Stage stage, final Parent root) {
		setTitleAndIcon(stage, "Help");
		setScene(stage, root, 960, 540);
	}

	/**
	 * Prepares application stages.
	 *
	 * @param stage stage on which given view will be shown
	 * @param title stage title
	 * @param view  FXML view
	 */
	public static void prepareStage(final Stage stage, final String title, final FXView view) {
		setTitleAndIcon(stage, title);
		setScene(stage, view);
	}

	private static void setScene(final Stage stage, final FXView fxView) {
		final Parent root = fxView.getView();
		final Scene scene = new Scene(root, root.minWidth(-1), root.minHeight(-1));
		stage.setMinWidth(960);
		stage.setMinHeight(540);
		stage.setScene(scene);
	}

}
