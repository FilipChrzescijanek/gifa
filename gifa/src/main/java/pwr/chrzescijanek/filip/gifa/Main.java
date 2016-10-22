package pwr.chrzescijanek.filip.gifa;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.opencv.core.Core;
import pwr.chrzescijanek.filip.gifa.core.controller.Controller;
import java.util.prefs.Preferences;

public class Main extends Application {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main( final String[] args ) {
		launch(args);
	}

	@Override
	public void start( final Stage primaryStage ) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gifa-gui.fxml"));
		final Parent root = loader.load();
		final Controller controller = loader.getController();
		primaryStage.setTitle("gifa");
		primaryStage.getIcons().clear();
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream( "/icon-small.png" )));
		Scene scene = new Scene(root, root.minWidth(-1), root.minHeight(-1));
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String theme = prefs.get(Controller.THEME_PREFERENCE_KEY, Controller.THEME_LIGHT);
		if (theme.equals(Controller.THEME_LIGHT)) {
			controller.getThemeToggleGroup().selectToggle(controller.getLightThemeToggle());
			scene.getStylesheets().add(Controller.THEME_LIGHT);
		} else {
			controller.getThemeToggleGroup().selectToggle(controller.getDarkThemeToggle());
			scene.getStylesheets().add(Controller.THEME_DARK);
		}
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> Platform.exit());
		primaryStage.show();
	}

}
