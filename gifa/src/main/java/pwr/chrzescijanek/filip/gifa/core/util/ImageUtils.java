package pwr.chrzescijanek.filip.gifa.core.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.opencv.core.Core.BORDER_DEFAULT;
import static org.opencv.core.Core.add;
import static org.opencv.core.Core.convertScaleAbs;
import static org.opencv.core.Core.pow;
import static org.opencv.core.Core.sqrt;
import static org.opencv.imgcodecs.Imgcodecs.imencode;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2BGRA;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.Scharr;
import static org.opencv.imgproc.Imgproc.Sobel;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.filter2D;
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
		checkIfTrianglesCountMatches(images, points);
		final int noOfImages = images.length;
		for ( final Mat m : images )
			cvtColor(m, m, COLOR_BGR2BGRA);
		for ( int i = 1; i < noOfImages; i++ ) {
			final Mat warpMat = getAffineTransform(points[i], points[0]);
			warpAffine(images[i], images[i], warpMat, images[0].size(),
					interpolation, Core.BORDER_CONSTANT, new Scalar(0, 0, 0, 0));
		}
	}

	private static void checkIfTrianglesCountMatches( final Mat[] images, final MatOfPoint2f[] points ) {
		if ( images.length != points.length )
			throw new IllegalArgumentException(
					String.format("Images count: %d does not match passed triangles count: %d", images.length, points.length)
			);
	}

	public static Image createImage( final Mat image ) {
		MatOfByte byteMat = new MatOfByte();
		imencode(".png", image, byteMat);
		return new Image(new ByteArrayInputStream(byteMat.toArray()));
	}

	public static Image createImage( final Mat image, final int width, final int height, final int noOfChannels, final PixelFormat format ) {
		return createImage(getImageData(image), width, height, noOfChannels, format);
	}

	public static Image createImage( final byte[] data, final int width, final int height, final int noOfChannels, final PixelFormat format ) {
		final WritableImage img = new WritableImage(width, height);
		final PixelWriter pw = img.getPixelWriter();
		pw.setPixels(0, 0, width, height, format, ByteBuffer.wrap(data), width * noOfChannels);
		return img;
	}

	public static void multiplyTransparencies(final Mat[] transparencySources, final Mat[] transparencyDestinations) {
		checkifSizesMatch(transparencySources, transparencyDestinations);
		for (int i = 0; i < transparencySources.length; i++)
			multiplyTransparencies(transparencySources[i], transparencyDestinations[i]);
	}

	private static void checkifSizesMatch( final Mat[] first, final Mat[] second ) {
		if ( first.length != second.length )
			throw new IllegalArgumentException(
					String.format("Images count does not match! Lengths of passed arrays: %d, %d", first.length, second.length)
			);
	}

	public static void multiplyTransparencies(final Mat transparencySource, final Mat transparencyDestination) {
		checkIfImageTypesMatch(transparencySource, transparencyDestination);
		final byte[] srcData = getImageData(transparencySource);
		final byte[] dstData = getImageData(transparencyDestination);
		final int alphaIndex = 3;
		for (int i = 0; i < srcData.length; i += 4) {
			if (Byte.toUnsignedInt(srcData[i + alphaIndex]) != 255)
				dstData[i + alphaIndex] = 0;
		}
		transparencyDestination.put(0, 0, dstData);
	}

	private static void checkIfImageTypesMatch( final Mat first, final Mat second ) {
		if ( first.total() != second.total() )
			throw new IllegalArgumentException(
					String.format("Images total pixel count does not match: %d, %d", first.total(), second.total())
			);
		if ( first.channels() != 4  )
			throw new IllegalArgumentException(
					String.format("Number of channels for the first image should be 4 but was %d", first.channels())
			);
		if ( second.channels() != 4  )
			throw new IllegalArgumentException(
					String.format("Number of channels for the second image should be 4 but was %d", second.channels())
			);
	}

	public static Mat[] extractChannel(final Mat[] images, final int channel) {
		Mat[] result = new Mat[images.length];
		for (int i = 0; i < images.length; i++)
			result[i] = extractChannel(images[i], channel);
		return result;
	}

	public static Mat extractChannel(final Mat image, final int channel) {
		checkIndex(image.channels(), channel);
		Mat result = new Mat();
		Core.extractChannel(image, result, channel);
		result.convertTo(result, CvType.CV_8UC1);
		return result;
	}

	private static void checkIndex( final int channels, final int channel ) {
		if ( channel < 0 || channel >= channels )
			throw new IllegalArgumentException(
					String.format("Channel does not exist (index is negative or not enough channels)" +
							"Index: %d, needed channels: %d, actual number of channels: %d", channel, channel + 1, channels)
			);
	}

	public static Mat[] sobelDerivative(final Mat[] images, final int kernelSize) {
		checkKernelSize(kernelSize);
		checkIfChannelsMatch(images, 1);
		Mat[] result = new Mat[images.length];
		for (int i = 0; i < images.length; i++)
			result[i] = sobelDerivative(images[i], kernelSize);
		return result;
	}

	private static void checkKernelSize( final int kernelSize ) {
		if ( !Arrays.asList(1, 3, 5, 7).contains(kernelSize) )
			throw new IllegalArgumentException(
					String.format("Given kernel size: %d does not match any of the available values: 1, 3, 5, 7", kernelSize)
			);
	}

	public static Mat sobelDerivative(final Mat image, final int kernelSize) {
		final Mat gradX = new Mat();
		final Mat gradY = new Mat();
		Sobel( image, gradX, -1, 1, 0, kernelSize, 1, 0, BORDER_DEFAULT );
		Sobel( image, gradY, -1, 0, 1, kernelSize, 1, 0, BORDER_DEFAULT );
		return approximateGradient(gradX, gradY);
	}

	public static Mat[] scharrDerivative(final Mat[] images) {
		checkIfChannelsMatch(images, 1);
		Mat[] result = new Mat[images.length];
		for (int i = 0; i < images.length; i++)
			result[i] = scharrDerivative(images[i]);
		return result;
	}

	public static Mat scharrDerivative(final Mat image) {
		final Mat gradX = new Mat();
		final Mat gradY = new Mat();
		Scharr( image, gradX, -1, 1, 0, 1, 0, BORDER_DEFAULT );
		Scharr( image, gradY, -1, 0, 1, 1, 0, BORDER_DEFAULT );
		return approximateGradient(gradX, gradY);
	}

	public static Mat[] robertsCross(final Mat[] images) {
		checkIfChannelsMatch(images, 1);
		Mat[] result = new Mat[images.length];
		for (int i = 0; i < images.length; i++)
			result[i] = robertsCross(images[i]);
		return result;
	}

	public static Mat robertsCross(final Mat image) {
		final Mat gradX = new Mat();
		final Mat gradY = new Mat();
		Mat gradientXKernel = Mat.zeros(2, 2, CvType.CV_32F);
		gradientXKernel.put(0, 0, 1);
		gradientXKernel.put(1, 1, -1);
		Mat gradientYKernel = Mat.zeros(2, 2, CvType.CV_32F);
		gradientYKernel.put(0, 1, 1);
		gradientYKernel.put(1, 0, -1);
		filter2D( image, gradX, -1, gradientXKernel, new Point(-1, -1), 0, BORDER_DEFAULT );
		filter2D( image, gradY, -1, gradientYKernel, new Point(-1, -1), 0, BORDER_DEFAULT );
		return approximateGradient(gradX, gradY);
	}

	private static Mat approximateGradient( final Mat gradX, final Mat gradY ) {
		final Mat result = new Mat();
		gradX.convertTo(gradX, CvType.CV_32F);
		gradY.convertTo(gradY, CvType.CV_32F);
		pow(gradX, 2.0, gradX);
		pow(gradY, 2.0, gradY);
		add(gradX, gradY, result);
		sqrt(result, result);
		result.convertTo(result, CvType.CV_8UC1);
		return result;
	}

	public static Mat[] cannyThreshold(final Mat[] images) {
		checkIfChannelsMatch(images, 1);
		Mat[] result = new Mat[images.length];
		for (int i = 0; i < images.length; i++)
			result[i] = cannyThreshold(images[i]);
		return result;
	}

	public static Mat cannyThreshold(final Mat image) {
		final Mat edges = new Mat();
		final Mat result = new Mat();
		Canny(image, edges, 25, 75, 3, true);
		image.copyTo(result, edges);
		return result;
	}

	public static Mat[] otsuThreshold(final Mat[] images) {
		checkIfChannelsMatch(images, 1);
		Mat[] result = new Mat[images.length];
		for (int i = 0; i < images.length; i++)
			result[i] = otsuThreshold(images[i]);
		return result;
	}

	public static Mat otsuThreshold(final Mat image) {
		final Mat result = new Mat();
		threshold(image, result, 0, 255, THRESH_BINARY | THRESH_OTSU);
		return result;
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
				mask[j] = Byte.toUnsignedInt(currentImage[j * noOfChannels + alphaIndex]) != 255;
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
								"when all passed images have to have %d channels", image.channels(), channels)
				);
	}
}
