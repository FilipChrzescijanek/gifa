package pwr.chrzescijanek.filip.gifa.core.util;

import org.opencv.core.Mat;

public final class FunctionUtils {

	private FunctionUtils() { }

	public static double[] calculateMeans( final Mat[] images, final int valueIndex ) {
		final byte[][] imagesData = ImageUtils.getImagesData(images);
		final double[] means = new double[images.length];
		for ( int i = 0; i < means.length; i++ ) {
			means[i] = calculateMean(imagesData[i], images[i].channels(), valueIndex);
		}
		return means;
	}

	public static double calculateMean( final Mat image, final int valueIndex ) {
			return calculateMean(ImageUtils.getImageData(image), image.channels(), valueIndex);
	}

	public static double calculateMean( final byte[] imageData, final int channels, final int valueIndex ) {
		if ( valueIndex < 0 || valueIndex >= channels )
			throw new IllegalArgumentException(
					String.format("Value does not exist (index is negative or not enough channels)!" +
							"Index: %d, needed channels: %d, actual number of channels: %d", valueIndex, valueIndex + 1, channels)
			);
		double mean = 0.0;
		int counter = 0;
		for ( int j = 0; j < imageData.length; j += channels ) {
			mean += Byte.toUnsignedInt(imageData[j + valueIndex]);
			counter++;
		}
		mean /= counter * 1.0;
		return mean;
	}

	public static double[] calculateStdDeviations( final Mat[] images, final int valueIndex ) {
		final byte[][] imagesData = ImageUtils.getImagesData(images);
		final double[] stdDeviations = new double[images.length];
		for ( int i = 0; i < stdDeviations.length; i++ ) {
			stdDeviations[i] = calculateStdDeviation(imagesData[i], images[i].channels(), valueIndex);
		}
		return stdDeviations;
	}

	public static double calculateStdDeviation( final Mat image, final int valueIndex ) {
		return calculateStdDeviation(ImageUtils.getImageData(image), image.channels(), valueIndex);
	}

	public static double calculateStdDeviation( final byte[] imageData, final int channels, final int valueIndex ) {
		final double mean = calculateMean(imageData, channels, valueIndex);
		double variance = 0.0;
		int counter = 0;
		for ( int j = 0; j < imageData.length; j += channels ) {
			variance += Math.pow(Byte.toUnsignedInt(imageData[j + valueIndex]) - mean, 2);
			counter++;
		}
		variance /= counter * 1.0;
		return Math.sqrt(variance);
	}
}
