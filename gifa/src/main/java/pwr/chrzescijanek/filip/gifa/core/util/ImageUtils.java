package pwr.chrzescijanek.filip.gifa.core.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;

import java.nio.ByteBuffer;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2BGRA;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.getAffineTransform;
import static org.opencv.imgproc.Imgproc.warpAffine;

public final class ImageUtils {

	private ImageUtils() { }

	public static void convertType( final Mat[] images, final int type ) {
		for ( final Mat m : images )
			convertType(m, type);
	}

	public static void convertType( final Mat image, final int type ) {
		cvtColor(image, image, type);
	}

	public static byte[][] getImagesData( final Mat[] images ) {
		final int noOfBytes = (int) images[0].total() * images[0].channels();
		final byte[][] imagesData = new byte[images.length][noOfBytes];
		for ( int i = 0; i < images.length; i++ ) {
			images[i].get(0, 0, imagesData[i]);
		}
		return imagesData;
	}

	public static byte[] getImageData( final Mat image ) {
		final int noOfBytes = (int) image.total() * image.channels();
		final byte[] imageData = new byte[noOfBytes];
		image.get(0, 0, imageData);
		return imageData;
	}

	public static Mat[] getImagesCopy( final Mat[] images ) {
		final Mat[] imagesCopy = new Mat[images.length];
		for ( int i = 0; i < images.length; i++ ) {
			imagesCopy[i] = getImageCopy(images[i]);
		}
		return imagesCopy;
	}

	public static Mat getImageCopy( final Mat image ) {
		final Mat imageCopy = new Mat(image.size(), image.type());
		image.copyTo(imageCopy);
		return imageCopy;
	}

	public static void performAffineTransformations( final Mat[] images, final MatOfPoint2f[] points, int interpolation ) {
		checkIfLengthsMatch(images, points);
		final int noOfImages = images.length;
		for ( final Mat m : images )
			cvtColor(m, m, COLOR_BGR2BGRA);
		for ( int i = 1; i < noOfImages; i++ ) {
			final Mat warpMat = getAffineTransform(points[i], points[0]);
			warpAffine(images[i], images[i], warpMat, images[0].size(),
					interpolation, Core.BORDER_CONSTANT, new Scalar(0, 0, 0, 0));
		}
	}

	private static void checkIfLengthsMatch( final Mat[] images, final MatOfPoint2f[] points ) {
		if ( images.length != points.length )
			throw new IllegalArgumentException(
					String.format("Images count: %d does not match passed triangles count: %d", images.length, points.length)
			);
	}

	public static Image createImage( final byte[] data, final int width, final int height, final int noOfChannels, final PixelFormat format ) {
		final WritableImage img = new WritableImage(width, height);
		final PixelWriter pw = img.getPixelWriter();
		pw.setPixels(0, 0, width, height, format, ByteBuffer.wrap(data), width * noOfChannels);
		return img;
	}

	public static Mat[] extractMeaningfulPixels( final Mat[] images ) {
		checkIfChannelsMatch(images, 4);

		final Mat referenceImage = images[0];
		final int noOfChannels = 4;
		final int noOfResultChannels = 3;
		final int noOfImages = images.length;
		final int noOfPixels = (int) referenceImage.total();
		final int noOfBytes = noOfPixels * noOfChannels;

		final byte[][] imagesData = getImagesData(images);

		final int blueIndex = 0;
		final int greenIndex = 1;
		final int redIndex = 2;
		final int alphaIndex = 3;

		final boolean[] mask = new boolean[noOfPixels];

		for ( int i = 0; i < noOfImages; i++ ) {
			final byte[] currentImage = imagesData[i];
			for ( int j = 0; j < noOfPixels; j++ ) {
				mask[j] = Byte.toUnsignedInt(currentImage[j * noOfChannels + alphaIndex]) == 0;
			}
		}

		int resultLength = 0;
		for ( boolean b : mask ) if ( !b ) resultLength++;

		final Mat[] resultImages = new Mat[noOfImages];

		for ( int i = 0; i < noOfImages; i++ ) {
			final byte[] currentImage = imagesData[i];
			final byte[] currentResultImage = new byte[resultLength * noOfResultChannels];
			int index = 0;
			for ( int j = 0; j < noOfBytes; j += noOfChannels ) {
				if ( !mask[j / noOfChannels] ) {
					currentResultImage[index + blueIndex] = currentImage[j + blueIndex];
					currentResultImage[index + greenIndex] = currentImage[j + greenIndex];
					currentResultImage[index + redIndex] = currentImage[j + redIndex];
					index += noOfResultChannels;
				}
			}
			final Mat current = new Mat(resultLength, 1, CvType.CV_8UC3);
			current.put(0, 0, currentResultImage);
			resultImages[i] = current;
		}

		return resultImages;
	}

	private static void checkIfChannelsMatch( final Mat[] images, final int channels ) {
		for ( Mat image : images )
			if ( image.channels() != channels )
				throw new IllegalArgumentException(
						String.format("Received an image with number of channels equal to %d " +
								"- all passed images pixels have to be in a BGRA format", image.channels())
				);
	}
}
