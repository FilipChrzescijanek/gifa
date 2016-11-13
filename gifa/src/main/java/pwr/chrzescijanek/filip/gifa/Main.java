package pwr.chrzescijanek.filip.gifa;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.opencv.core.Core;
import pwr.chrzescijanek.filip.gifa.inject.Injector;
import pwr.chrzescijanek.filip.gifa.util.StageUtils;
import pwr.chrzescijanek.filip.gifa.view.FXView;

import java.io.IOException;
import java.util.logging.*;

public class Main extends Application {

	private static final String LOGGING_FORMAT_PROPERTY = "java.util.logging.SimpleFormatter.format";
	private static final String LOGGING_FORMAT = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s: %5$s%6$s%n";
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.setProperty(LOGGING_FORMAT_PROPERTY, LOGGING_FORMAT);
		initializeLogger();
	}

	private static void initializeLogger() {
		try {
			Handler fileHandler = new FileHandler("log", 10000, 5, true);
			fileHandler.setFormatter(new SimpleFormatter());
			Logger.getLogger(Main.class.getPackage().getName()).addHandler(fileHandler);
		} catch (IOException e) {
			LOGGER.log( Level.SEVERE, e.toString(), e );
		}
	}

	public static void main( final String... args ) {
		launch(args);
	}

	@Override
	public void start( final Stage primaryStage ) throws Exception {
		Image icon = new Image(getClass().getResourceAsStream("/images/icon-small.png"));
		FXView fxView = new FXView("/static/gifa.fxml");
		StageUtils.prepareMainStage(primaryStage, "gifa", icon, fxView);
		primaryStage.setOnCloseRequest(event -> Platform.exit());
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		Injector.reset();
	}

}
