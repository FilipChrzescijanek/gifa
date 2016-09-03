package pwr.chrzescijanek.filip.gifa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class Main extends Application {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main( String[] args ) {
		launch(args);
	}

	@Override
	public void start( Stage primaryStage ) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
		Parent root = loader.load();
		Controller controller = loader.getController();
		printSample(controller);
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 640, 480));
		primaryStage.show();
	}

	private void printSample( final Controller controller ) {
		Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
		Mat m2 = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
		m.copyTo(m2);
		Mat mr1 = m.row(1);
		mr1.setTo(new Scalar(1));
		Mat mc5 = m.col(5);
		mc5.setTo(new Scalar(5));

		String label = String.format("Welcome to OpenCV %s\n\n"
						+ "OpenCV Mat: %s\n\n"
						+ "OpenCV Mat data:\n%s",
				Core.VERSION, m.toString(), m.dump());
		controller.setLabel(label);
	}
}
