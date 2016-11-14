package pwr.chrzescijanek.filip.gifa.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pwr.chrzescijanek.filip.gifa.view.FXView;

public final class StageUtils {

	private StageUtils() { }

	public static void prepareHelpStage(final Stage stage, final Parent root) {
		setTitleAndIcon(stage, "Help");
		setScene(stage, root, 960, 540);
	}

	public static void preparePanelStage(final Stage stage, String title, FXView view) {
		setTitleAndIcon(stage, title);
		setPanelScene(stage, view);
	}

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
