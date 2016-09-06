package pwr.chrzescijanek.filip.gifa;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticsCalculator {

	private final Mat[] images;
	private final boolean[] mask;
	private static final List< EvaluationFunction > FUNCTIONS = new ArrayList<>();

	public StatisticsCalculator( Mat[] images, boolean[] mask ) {
		this.images = images;
		this.mask = mask;
	}

	public static boolean pixelIsShared( boolean[] mask, int row, int column, int width, int channels ) {
		int arrayIndex = column + row * width;
		if ( arrayIndex % channels != 0 )
			throw new IllegalArgumentException("Pixel index ((column + row * width) / channels) is not an integer!");
		return !mask[arrayIndex / channels];
	}

	public static void convertType( Mat[] images, int type ) {
		for ( Mat m : images )
			convertType(m, type);
	}

	public static void convertType( Mat image, int type ) {
		Imgproc.cvtColor(image, image, type);
	}

	public static double[] calculateMeans( Mat[] images, boolean[] mask, int type ) {
		byte[][] imagesData = getImagesData(images);
		double[] means = new double[images.length];
		for ( int i = 0; i < means.length; i++ ) {
			means[i] = calculateMean(imagesData[i], mask, images[i].channels(), type);
		}
		return means;
	}

	public static byte[][] getImagesData( Mat[] images ) {
		final int noOfBytes = (int) images[0].total() * images[0].channels();
		byte[][] imagesData = new byte[images.length][noOfBytes];
		for ( int i = 0; i < images.length; i++ ) {
			images[i].get(0, 0, imagesData[i]);
		}
		return imagesData;
	}

	public static double calculateMean( byte[] imageData, boolean[] mask, int channels, int type ) {
		if ( type < 0 || type >= channels )
			throw new IllegalArgumentException("Wrong calculation type!");
		double mean = 0.0;
		int counter = 0;
		for ( int j = 0; j < imageData.length; j += channels ) {
			if ( pixelIsShared(mask, j, channels) ) {
				mean += Byte.toUnsignedInt(imageData[j + type]);
				counter++;
			}
		}
		mean /= counter * 1.0;
		return mean;
	}

	public static boolean pixelIsShared( boolean[] mask, int arrayIndex, int channels ) {
		if ( arrayIndex % channels != 0 )
			throw new IllegalArgumentException("Pixel index (arrayIndex / channels) is not an integer!");
		return !mask[arrayIndex / channels];
	}

	public static double calculateMean( Mat image, boolean[] mask, int type ) {
		return calculateMean(getImageData(image), mask, image.channels(), type);
	}

	public static byte[] getImageData( Mat image ) {
		final int noOfBytes = (int) image.total() * image.channels();
		byte[] imageData = new byte[noOfBytes];
		image.get(0, 0, imageData);
		return imageData;
	}

	public static double[] calculateStdDeviations( Mat[] images, boolean[] mask, int type ) {
		byte[][] imagesData = getImagesData(images);
		double[] stdDeviations = new double[images.length];
		for ( int i = 0; i < stdDeviations.length; i++ ) {
			stdDeviations[i] = calculateStdDeviation(imagesData[i], mask, images[i].channels(), type);
		}
		return stdDeviations;
	}

	public static double calculateStdDeviation( Mat image, boolean[] mask, int type ) {
		return calculateStdDeviation(getImageData(image), mask, image.channels(), type);
	}

	public static double calculateStdDeviation( byte[] imageData, boolean[] mask, int channels, int type ) {
		double mean = calculateMean(imageData, mask, channels, type);
		double variance = 0.0;
		int counter = 0;
		for ( int j = 0; j < imageData.length; j += channels ) {
			if ( pixelIsShared(mask, j, channels) ) {
				variance += Math.pow(Byte.toUnsignedInt(imageData[j + type]) - mean, 2);
				counter++;
			}
		}
		variance /= counter * 1.0;
		return Math.sqrt(variance);
	}

	public void calculateStatistics() {
		for ( EvaluationFunction f : FUNCTIONS ) {
			System.out.println(Arrays.toString(f.evaluate(getImagesCopy(images), getMaskCopy(mask))));
		}
	}

	public static boolean[] getMaskCopy( boolean[] mask ) {
		return Arrays.copyOf(mask, mask.length);
	}

	public static Mat[] getImagesCopy( Mat[] images ) {
		Mat[] imagesCopy = new Mat[images.length];
		for ( int i = 0; i < images.length; i++ ) {
			imagesCopy[i] = getImageCopy(images[i]);
		}
		return imagesCopy;
	}

	public static Mat getImageCopy( Mat image ) {
		Mat imageCopy = new Mat(image.size(), image.type());
		image.copyTo(imageCopy);
		return imageCopy;
	}

	public static void injectFunction( EvaluationFunction function ) {
		FUNCTIONS.add(function);
	}

}
