package pwr.chrzescijanek.filip.gifa.core.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.nio.ByteBuffer;
import java.util.Arrays;

public final class ImageUtils {

	private ImageUtils() { }

	public static boolean pixelIsShared( final boolean[] mask, final int arrayIndex, final int channels ) {
		if ( arrayIndex % channels != 0 )
			throw new IllegalArgumentException("Pixel index (arrayIndex / channels) is not an integer!");
		return !mask[arrayIndex / channels];
	}

	public static boolean pixelIsShared( final boolean[] mask, final int row, final int column, final int width, final int channels ) {
		int arrayIndex = column + row * width;
		if ( arrayIndex % channels != 0 )
			throw new IllegalArgumentException("Pixel index ((column + row * width) / channels) is not an integer!");
		return !mask[arrayIndex / channels];
	}

	public static void convertType( final Mat[] images, final int type ) {
		for ( final Mat m : images )
			convertType(m, type);
	}

	public static void convertType( final Mat image, final int type ) {
		Imgproc.cvtColor(image, image, type);
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

	public static boolean[] getMaskCopy( final boolean[] mask ) {
		return Arrays.copyOf(mask, mask.length);
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

	public static void performAffineTransformations( final Mat[] images, final MatOfPoint2f[] points, final boolean usePoints ) {
		final int noOfImages = images.length;
		for ( final Mat m : images )
			Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2BGRA);
		for ( int i = 1; i < noOfImages; i++ ) {
			final Mat warpMat = usePoints ?
					Imgproc.getAffineTransform(points[i], points[0]) : Video.estimateRigidTransform(images[i], images[0], true);
			Imgproc.warpAffine(images[i], images[i], warpMat, images[0].size(),
					Imgproc.INTER_NEAREST, Core.BORDER_CONSTANT, new Scalar(0, 0, 0, 0));
		}
	}

	public static Image createImage( final byte[] data, final int width, final int height, final int noOfChannels ) {
		final WritableImage img = new WritableImage(width, height);
		final PixelWriter pw = img.getPixelWriter();
		pw.setPixels(0, 0, width, height, PixelFormat.getByteBgraPreInstance(), ByteBuffer.wrap(data),
				width * noOfChannels);
		return img;
	}

	public static ResultImage getResultImage( final Mat[] images ) {
		final int noOfImages = images.length;
		final Mat referenceImage = images[0];

		final int noOfPixels = (int) referenceImage.total();
		final int noOfChannels = referenceImage.channels();
		final int noOfBytes = noOfPixels * noOfChannels;

		final byte[] resultData = new byte[noOfBytes];
		final boolean[] mask = new boolean[noOfPixels];

		final byte[][] imagesData = getImagesData(images);

		final int blueIndex = 0;
		final int greenIndex = 1;
		final int redIndex = 2;
		final int alphaIndex = 3;

		for ( int i = 0; i < noOfImages; i++ ) {
			final byte[] currentImage = imagesData[i];
			for ( int j = 0; j < noOfBytes; j += 4 ) {
				final int opacity = Byte.toUnsignedInt(currentImage[j + alphaIndex]);
				final double constant = opacity / ( noOfImages * 255.0 );
				int blue = Byte.toUnsignedInt(resultData[j + blueIndex]);
				int green = Byte.toUnsignedInt(resultData[j + greenIndex]);
				int red = Byte.toUnsignedInt(resultData[j + redIndex]);
				blue += Byte.toUnsignedInt(currentImage[j + blueIndex]) * constant;
				green += Byte.toUnsignedInt(currentImage[j + greenIndex]) * constant;
				red += Byte.toUnsignedInt(currentImage[j + redIndex]) * constant;
				resultData[j + blueIndex] = (byte) blue;
				resultData[j + greenIndex] = (byte) green;
				resultData[j + redIndex] = (byte) red;
				resultData[j + alphaIndex] = -1;
				if ( opacity == 0 ) {
					final int pixel = j / 4;
					mask[pixel] = true;
				}
			}
		}

		return new ResultImage(resultData, mask, referenceImage.width(), referenceImage.height(), noOfChannels);
	}
}