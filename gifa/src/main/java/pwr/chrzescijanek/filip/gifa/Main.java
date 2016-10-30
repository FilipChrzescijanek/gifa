package pwr.chrzescijanek.filip.gifa;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.opencv.core.Core;
import pwr.chrzescijanek.filip.gifa.inject.Injector;
import pwr.chrzescijanek.filip.gifa.util.StageUtils;
import pwr.chrzescijanek.filip.gifa.view.FXView;

public class Main extends Application {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main( final String... args ) {
		launch(args);
	}

	@Override
	public void start( final Stage primaryStage ) throws Exception {
		Image icon = new Image(getClass().getResourceAsStream("/images/icon-small.png"));
		FXView fxView = new FXView("/static/gifa.fxml");
		StageUtils.prepareStagePreservingRatio(primaryStage, "gifa", icon, fxView);
		primaryStage.setOnCloseRequest(event -> Platform.exit());
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		Injector.reset();
	}

}
