package pwr.chrzescijanek.filip.gifa.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pwr.chrzescijanek.filip.gifa.view.FXView;

public final class StageUtils {

	private StageUtils() { }

	public static void prepareStage( final Stage stage, String title, Image icon, FXView view ) {
		stage.setTitle(title);
		stage.getIcons().add(icon);
		setSceneTo(stage, view);
	}

	private static void setSceneTo( final Stage stage, final FXView fxView ) {
		final Parent root = fxView.getView();
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}

}
