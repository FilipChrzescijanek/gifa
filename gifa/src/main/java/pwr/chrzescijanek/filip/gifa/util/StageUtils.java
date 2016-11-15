package pwr.chrzescijanek.filip.gifa.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import pwr.chrzescijanek.filip.gifa.controller.PanelController;
import pwr.chrzescijanek.filip.gifa.model.panel.PanelView;
import pwr.chrzescijanek.filip.gifa.view.FXView;

import java.util.List;

import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.initializeController;

/**
 * Provides utility methods for handling stages.
 */
public final class StageUtils {

	private StageUtils() { }

	/**
	 * @return about dialog
	 */
	public static Alert getAboutDialog() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION,
				"Global image features analyzer\n" +
						"GitHub repository: https://github.com/FilipChrzescijanek/gifa/\n" +
						"\nCopyright © 2016 Filip Chrześcijanek\nfilip.chrzescijanek@gmail.com", ButtonType.OK);
		alert.setTitle("About");
		alert.setHeaderText("gifa");
		alert.setGraphic(new ImageView(new Image(ControllerUtils.class.getResourceAsStream("/images/icon-big.png"))));
		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
				.add(new Image(ControllerUtils.class.getResourceAsStream("/images/icon-small.png")));
		return alert;
	}

	/**
	 * @param content alert content
	 * @return error alert
	 */
	public static Alert getErrorAlert(final String content) {
		return new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
	}

	/**
	 * @param window application window
	 * @return new undecorated modal dialog
	 */
	public static Stage initDialog(Window window) {
		final Stage dialog = new Stage();
		dialog.initOwner(window);
		dialog.initStyle(StageStyle.UNDECORATED);
		dialog.initModality(Modality.APPLICATION_MODAL);
		return dialog;
	}

	/**
	 * Constructs new application's panel stage with FXML view from given path, label, title and panel views.
	 * @param viewPath FXML view path
	 * @param info label
	 * @param title stage title
	 * @param views panel views
     * @return new application's panel stage
     */
	public static Stage getNewStage( String viewPath, String info, String title, List< ? extends PanelView> views ) {
		final Stage newStage = new Stage();
		FXView fxView = new FXView(viewPath);
		final PanelController controller = initializeController(info, views, fxView);
		showStage(newStage, fxView, controller, title);
		return newStage;
	}

	/**
	 * Shows given applications' panel stage.
	 * @param newStage stage
	 * @param fxView FXML view
	 * @param controller panel controller
     * @param title stage title
     */
	public static void showStage(final Stage newStage, final FXView fxView, final PanelController controller, final String title) {
		StageUtils.preparePanelStage(newStage, title, fxView);
		controller.refresh();
		newStage.show();
	}

	/**
	 * Prepares application's help view stage.
	 * @param stage stage on which help will be shown
	 * @param root help view
     */
	public static void prepareHelpStage(final Stage stage, final Parent root) {
		setTitleAndIcon(stage, "Help");
		setScene(stage, root, 960, 540);
	}

	/**
	 * Prepares applications' panel stage.
	 * @param stage stage on which panel will be shown
	 * @param title stage title
	 * @param view panel view
     */
	public static void preparePanelStage(final Stage stage, String title, FXView view) {
		setTitleAndIcon(stage, title);
		setPanelScene(stage, view);
	}

	/**
	 * Prepares application stages.
	 * @param stage stage on which given view will be shown
	 * @param title stage title
	 * @param view FXML view
     */
	public static void prepareStage(final Stage stage, final String title, final FXView view) {
		setTitleAndIcon(stage, title);
		setScene(stage, view);
	}

	private static void setPanelScene(final Stage stage, final FXView fxView ) {
		final Parent root = fxView.getView();
		setScene(stage, root, 720, 360);
	}

	private static void setScene(Stage stage, Parent root, int width, int height) {
		Scene scene = new Scene(root, width, height);
		stage.setScene(scene);
	}

	private static void setScene(final Stage stage, final FXView fxView ) {
		final Parent root = fxView.getView();
		Scene scene = new Scene(root, root.minWidth(-1), root.minHeight(-1));
		stage.setMinWidth(960);
		stage.setMinHeight(540);
		stage.setScene(scene);
	}

	private static void setTitleAndIcon(final Stage stage, final String title) {
		stage.setTitle(title);
		Image icon = new Image(StageUtils.class.getResourceAsStream("/images/icon-small.png"));
		stage.getIcons().add(icon);
	}

}
