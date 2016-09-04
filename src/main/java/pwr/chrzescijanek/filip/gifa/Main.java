package pwr.chrzescijanek.filip.gifa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

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
		controller.setLabel("Controller test");
		doSample();
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 640, 480));
		primaryStage.show();
	}

	private void performTransformations( Mat[] images, MatOfPoint2f[] points ) {
		assert ( images.length == points.length );
		for ( Mat m : images )
			Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2BGRA);
		Mat result = new Mat(images[0].size(), images[0].type());
		for ( int i = 1; i < images.length; i++ ) {
			Mat warpMat = Imgproc.getAffineTransform(points[i], points[0]);
			Imgproc.warpAffine(images[i], images[i], warpMat, images[0].size(),
					Imgproc.INTER_NEAREST, Core.BORDER_CONSTANT, new Scalar(0, 0, 0, 0));
		}
		for ( int i = 0; i < images[0].rows(); i++ ) {
			for ( int j = 0; j < images[0].cols(); j++ ) {
				double[] data = new double[4];
				for ( Mat m : images ) {
					double[] imgData = m.get(i, j);
					data[0] += imgData[0] * imgData[3] / ( images.length * 255 );
					data[1] += imgData[1] * imgData[3] / ( images.length * 255 );
					data[2] += imgData[2] * imgData[3] / ( images.length * 255 );
				}
				data[3] = 255;
				result.put(i, j, data);
			}
		}
		for ( int i = 0; i < result.rows(); i++ ) {
			for ( int j = 0; j < result.cols(); j++ ) {
				boolean isTransparent = false;
				for ( Mat m : images ) {
					double[] imgData = m.get(i, j);
					isTransparent = Math.abs(imgData[3] - 0.0) < 0.0000001;
					if ( isTransparent ) break;
				}
				if ( isTransparent ) {
					for ( Mat m : images ) {
						double[] data = { 0.0, 0.0, 0.0, 0.0 };
						m.put(i, j, data);
					}
				}
			}
		}
		Imgcodecs.imwrite("src/main/resources/result.png", result);
		for ( int i = 0; i < images.length; i++ ) {
			Imgcodecs.imwrite("src/main/resources/image" + i + ".png", images[i]);
		}
	}

	private void doSample() {
		Mat[] images = new Mat[3];
		images[0] = Imgcodecs.imread("src/main/resources/tri.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
		images[1] = Imgcodecs.imread("src/main/resources/tri-aff.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
		images[2] = Imgcodecs.imread("src/main/resources/tri-new.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
		MatOfPoint2f[] points = new MatOfPoint2f[3];
		points[0] = new MatOfPoint2f(new Point(58, 10), new Point(13, 97), new Point(103, 97));
		points[1] = new MatOfPoint2f(new Point(70, 10), new Point(25, 97), new Point(115, 97));
		points[2] = new MatOfPoint2f(new Point(127, 57), new Point(40, 12), new Point(40, 102));
		performTransformations(images, points);
	}
}
