package pwr.chrzescijanek.filip.gifa;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.opencv.core.Core;
import pwr.chrzescijanek.filip.gifa.inject.Injector;
import pwr.chrzescijanek.filip.gifa.view.FXView;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static pwr.chrzescijanek.filip.gifa.util.StageUtils.prepareStage;

/**
 * Main application class.
 */
public class Main extends Application {

	private static final String LOGGING_FORMAT_PROPERTY = "java.util.logging.SimpleFormatter.format";

	private static final String LOGGING_FORMAT = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s: %5$s%6$s%n";

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.setProperty(LOGGING_FORMAT_PROPERTY, LOGGING_FORMAT);
		initializeLogger();
	}

	/**
	 * Default constructor.
	 */
	public Main() { }

	private static void initializeLogger() {
		try {
			final Handler fileHandler = new FileHandler("log", 10000, 5, true);
			fileHandler.setFormatter(new SimpleFormatter());
			Logger.getLogger(Main.class.getPackage().getName()).addHandler(fileHandler);
		} catch (final IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
	}

	/**
	 * Starts the application.
	 *
	 * @param args launch arguments
	 */
	public static void main(final String... args) {
		launch(args);
	}

	/**
	 * Prepares primary stage and shows GUI.
	 *
	 * @param primaryStage application's primary stage
	 * @throws Exception unhandled exception
	 */
	@Override
	public void start(final Stage primaryStage) throws Exception {
		final FXView fxView = new FXView("/static/gifa.fxml");
		prepareStage(primaryStage, "gifa", fxView);
		primaryStage.setOnCloseRequest(event -> Platform.exit());
		primaryStage.show();
	}

	/**
	 * Resets state on application stop.
	 *
	 * @throws Exception unhandled exception
	 */
	@Override
	public void stop() throws Exception {
		Injector.reset();
	}

}
