package pwr.chrzescijanek.filip.gifa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.nio.ByteBuffer;

public class Main extends Application {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private static final int BLUE = 0;
	private static final int GREEN = 1;
	private static final int RED = 2;
	private static final int ALPHA = 3;

	public static void main( String[] args ) {
		launch(args);
	}

	@Override
	public void start( Stage primaryStage ) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
		Parent root = loader.load();
		Controller controller = loader.getController();
		doSample(controller);
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 640, 480));
		primaryStage.show();
	}

	private void performTransformations( Mat[] images, MatOfPoint2f[] points, Controller c ) {
		final int noOfImages = images.length;
		assert ( noOfImages == points.length );

		for ( Mat m : images )
			Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2BGRA);

		for ( int i = 1; i < noOfImages; i++ ) {
//			Mat warpMat = Imgproc.getAffineTransform(points[i], points[0]);
			Mat warpMat = Video.estimateRigidTransform(images[i], images[0], true);
			Imgproc.warpAffine(images[i], images[i], warpMat, images[0].size(),
					Imgproc.INTER_NEAREST, Core.BORDER_CONSTANT, new Scalar(0, 0, 0, 0));
		}

		final int noOfBytes = (int) images[0].total() * images[0].channels();
		byte[][] imagesData = new byte[noOfImages][noOfBytes];
		for ( int i = 0; i < noOfImages; i++ ) {
			images[i].get(0, 0, imagesData[i]);
		}

		byte[] resultData = new byte[noOfBytes];
		boolean[] mask = new boolean[(int) images[0].total()];
		for ( int i = 0; i < noOfImages; i++ ) {
			byte[] currentImage = imagesData[i];
			for ( int j = 0; j < noOfBytes; j += 4 ) {
				int opacity = Byte.toUnsignedInt(currentImage[j + ALPHA]);
				double constant = opacity / ( noOfImages * 255.0 );
				int b = Byte.toUnsignedInt(resultData[j + BLUE]);
				int g = Byte.toUnsignedInt(resultData[j + GREEN]);
				int r = Byte.toUnsignedInt(resultData[j + RED]);
				b += Byte.toUnsignedInt(currentImage[j + BLUE]) * constant;
				g += Byte.toUnsignedInt(currentImage[j + GREEN]) * constant;
				r += Byte.toUnsignedInt(currentImage[j + RED]) * constant;
				resultData[j + BLUE] = (byte) b;
				resultData[j + GREEN] = (byte) g;
				resultData[j + RED] = (byte) r;
				resultData[j + ALPHA] = -1;
				if ( opacity == 0 ) {
					int pixel = j / 4;
					mask[pixel] = true;
				}
			}
		}

		WritableImage img = new WritableImage(images[0].width(), images[0].height());
		PixelWriter pw = img.getPixelWriter();
		pw.setPixels(0, 0, images[0].width(), images[0].height(), PixelFormat.getByteBgraPreInstance(), ByteBuffer.wrap(resultData), images[0].width() *
				images[0].channels());
		c.setImage(img);

//		for ( int i = 0; i < mask.length; i++ ) {
//			if ( mask[i] ) {
//				for ( Mat m : images ) {
//					m.put(i / m.width(), i % m.width(), new double[] { 0, 0, 255, 255 });
//				}
//			}
//		}

		for ( Mat m : images )
			Imgproc.cvtColor(m, m, Imgproc.COLOR_BGRA2BGR);

//		for ( int i = 0; i < noOfImages; i++ ) {
//			Imgcodecs.imwrite("src/main/resources/image" + i + ".png", images[i]);
//		}

		new StatisticsCalculator(images, mask).calculateMeanHue();
	}

	private void doSample( Controller c ) {
		Mat[] images = new Mat[2];
		images[0] = Imgcodecs.imread("src/main/resources/tri.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
		images[1] = Imgcodecs.imread("src/main/resources/tri-aff.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
//		images[2] = Imgcodecs.imread("src/main/resources/tri-new.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
		MatOfPoint2f[] points = new MatOfPoint2f[2];
		points[0] = new MatOfPoint2f(new Point(58, 10), new Point(13, 97), new Point(103, 97));
		points[1] = new MatOfPoint2f(new Point(70, 10), new Point(25, 97), new Point(115, 97));
//		points[2] = new MatOfPoint2f(new Point(127, 57), new Point(40, 12), new Point(40, 102));
		performTransformations(images, points, c);
	}
}
