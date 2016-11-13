package pwr.chrzescijanek.filip.gifa.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pwr.chrzescijanek.filip.gifa.view.FXView;

public final class StageUtils {

	private StageUtils() { }

	public static void prepareStage( final Stage stage, String title, Image icon, FXView view ) {
		setTitleAndIcon(stage, title, icon);
		setSceneTo(stage, view);
	}

	public static void prepareMainStage(final Stage stage, final String title, final Image icon, final FXView view ) {
		setTitleAndIcon(stage, title, icon);
		setMainScene(stage, view);
	}

	private static void setSceneTo( final Stage stage, final FXView fxView ) {
		final Parent root = fxView.getView();
		Scene scene = new Scene(root, 720, 360);
		stage.setScene(scene);
	}

	private static void setMainScene(final Stage stage, final FXView fxView ) {
		final Parent root = fxView.getView();
		Scene scene = new Scene(root, root.minWidth(-1), root.minHeight(-1));
		stage.setMinWidth(960);
		stage.setMinHeight(540);
		stage.setScene(scene);
	}

	private static void setTitleAndIcon( final Stage stage, final String title, final Image icon ) {
		stage.setTitle(title);
		stage.getIcons().add(icon);
	}

}
