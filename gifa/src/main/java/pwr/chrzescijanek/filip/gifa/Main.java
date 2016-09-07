package pwr.chrzescijanek.filip.gifa;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import pwr.chrzescijanek.filip.gifa.core.controller.Controller;
import pwr.chrzescijanek.filip.gifa.generator.DataGenerator;

import java.util.ArrayList;

public class Main extends Application {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main( final String[] args ) {
		launch(args);
	}

	@Override
	public void start( final Stage primaryStage ) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
		final Parent root = loader.load();
		final Controller controller = loader.getController();
		doSample(controller);
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 640, 480));
		primaryStage.show();
	}

	private void doSample( final Controller c ) {
		final ObservableList< Mat > images = new ObservableListWrapper<>(new ArrayList<>());
		images.add(Imgcodecs.imread("src/main/resources/tri.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR));
		images.add(Imgcodecs.imread("src/main/resources/tri-aff.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR));
		images.add(Imgcodecs.imread("src/main/resources/tri-new.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR));
		final MatOfPoint2f[] points = new MatOfPoint2f[3];
		points[0] = new MatOfPoint2f(new Point(58, 10), new Point(13, 97), new Point(103, 97));
		points[1] = new MatOfPoint2f(new Point(70, 10), new Point(25, 97), new Point(115, 97));
		points[2] = new MatOfPoint2f(new Point(127, 57), new Point(40, 12), new Point(40, 102));
		DataGenerator.INSTANCE.generateData(images.toArray(new Mat[] {}), points, c);
	}
}
