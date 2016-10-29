package pwr.chrzescijanek.filip.gifa;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.opencv.core.Core;
import pwr.chrzescijanek.filip.gifa.inject.Injector;
import pwr.chrzescijanek.filip.gifa.view.FxmlView;

public class Main extends Application {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main( final String... args ) {
		launch(args);
	}

	@Override
	public void start( final Stage primaryStage ) throws Exception {
		prepareStage(primaryStage);
		primaryStage.show();
	}

	private void prepareStage( final Stage primaryStage ) {
		primaryStage.setTitle("gifa");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon-small.png")));
		setScene(primaryStage);
		primaryStage.setOnCloseRequest(event -> Platform.exit());
	}

	private void setScene( final Stage primaryStage ) {
		FxmlView appView = new FxmlView("/gifa.fxml");
		final Parent root = appView.getView();
		Scene scene = new Scene(root, root.minWidth(-1), root.minHeight(-1));
		primaryStage.setScene(scene);
	}

	@Override
	public void stop() throws Exception {
		Injector.reset();
	}

}
