package pwr.chrzescijanek.filip.gifa.core.util;

import org.opencv.core.Mat;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.*;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.getImageData;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.getImagesData;

public final class FunctionUtils {

    private static final Logger LOGGER = Logger.getLogger(FunctionUtils.class.getName());

    private FunctionUtils() {
    }

    public static double[] calculateMeans(final Mat[] images, final int valueIndex) {
        final byte[][] imagesData = getImagesData(images);
        final double[] means = new double[images.length];
        for (int i = 0; i < means.length; i++) {
            means[i] = calculateMean(imagesData[i], images[i].channels(), valueIndex);
        }
        return means;
    }

    public static double calculateMean(final Mat image, final int valueIndex) {
        return calculateMean(getImageData(image), image.channels(), valueIndex);
    }

    public static double calculateMean(final byte[] imageData, final int channels, final int valueIndex) {
        checkIndex(channels, valueIndex);
        double mean = 0.0;
        int counter = 0;
        for (int i = 0; i < imageData.length; i += channels) {
            mean += toDouble(imageData[i + valueIndex]);
            counter++;
        }
        mean /= counter * 1.0;
        return mean;
    }

    public static double[] calculateVariances(final Mat[] images, final int valueIndex) {
        final byte[][] imagesData = getImagesData(images);
        final double[] variances = new double[images.length];
        for (int i = 0; i < variances.length; i++) {
            variances[i] = calculateVariance(imagesData[i], images[i].channels(), valueIndex);
        }
        return variances;
    }

    public static double calculateVariance(final Mat image, final int valueIndex) {
        return calculateVariance(getImageData(image), image.channels(), valueIndex);
    }

    public static double calculateVariance(final byte[] imageData, final int channels, final int valueIndex) {
        final double mean = calculateMean(imageData, channels, valueIndex);
        double variance = 0.0;
        int counter = 0;
        for (int i = 0; i < imageData.length; i += channels) {
            variance += pow(toDouble(imageData[i + valueIndex]) - mean, 2);
            counter++;
        }
        variance /= counter * 1.0;
        return variance;
    }

    public static double[] calculateStdDeviations(final Mat[] images, final int valueIndex) {
        final double[] stdDeviations = calculateVariances(images, valueIndex);
        for (int i = 0; i < stdDeviations.length; i++) {
            stdDeviations[i] = sqrt(stdDeviations[i]);
        }
        return stdDeviations;
    }

    public static double calculateStdDeviation(final Mat image, final int valueIndex) {
        return calculateStdDeviation(getImageData(image), image.channels(), valueIndex);
    }

    public static double calculateStdDeviation(final byte[] imageData, final int channels, final int valueIndex) {
        return sqrt(calculateVariance(imageData, channels, valueIndex));
    }

    public static double[] calculateLinearFuzzinesses(final Mat[] images, final int valueIndex) {
        final byte[][] imagesData = getImagesData(images);
        final double[] fuzzinesses = new double[images.length];
        for (int i = 0; i < fuzzinesses.length; i++) {
            fuzzinesses[i] = calculateLinearFuzziness(imagesData[i], images[i].channels(), valueIndex);
        }
        return fuzzinesses;
    }

    public static double calculateLinearFuzziness(final Mat image, final int valueIndex) {
        return calculateLinearFuzziness(getImageData(image), image.channels(), valueIndex);
    }

    public static double calculateLinearFuzziness(final byte[] imageData, final int channels, final int valueIndex) {
        final double max = calculateMax(imageData, channels, valueIndex);
        if (abs(max) < 0.00000001) return 0;
        double sum = 0.0;
        for (int i = 0; i < imageData.length; i += channels) {
            final double current = toDouble(imageData[i + valueIndex]);
            final double functionValue = current / max;
            sum += min(functionValue, 1.0 - functionValue);
        }
        return sum * 2 / (imageData.length / channels);
    }

    public static double[] calculateQuadraticFuzzinesses(final Mat[] images, final int valueIndex) {
        final byte[][] imagesData = getImagesData(images);
        final double[] fuzzinesses = new double[images.length];
        for (int i = 0; i < fuzzinesses.length; i++) {
            fuzzinesses[i] = calculateQuadraticFuzziness(imagesData[i], images[i].channels(), valueIndex);
        }
        return fuzzinesses;
    }

    public static double calculateQuadraticFuzziness(final Mat image, final int valueIndex) {
        return calculateQuadraticFuzziness(getImageData(image), image.channels(), valueIndex);
    }

    public static double calculateQuadraticFuzziness(final byte[] imageData, final int channels, final int valueIndex) {
        final double max = calculateMax(imageData, channels, valueIndex);
        if (abs(max) < 0.00000001) return 0;
        double sum = 0.0;
        for (int i = 0; i < imageData.length; i += channels) {
            final double current = toDouble(imageData[i + valueIndex]);
            final double functionValue = current / max;
            sum += pow(min(functionValue, 1.0 - functionValue), 2);
        }
        return sqrt(sum) * 2 / sqrt((imageData.length / channels));
    }

    public static double[] calculateEntropies(final Mat[] images, final int valueIndex) {
        final byte[][] imagesData = getImagesData(images);
        final double[] entropies = new double[images.length];
        for (int i = 0; i < entropies.length; i++) {
            entropies[i] = calculateEntropy(imagesData[i], images[i].channels(), valueIndex);
        }
        return entropies;
    }

    public static double calculateEntropy(final Mat image, final int valueIndex) {
        return calculateEntropy(getImageData(image), image.channels(), valueIndex);
    }

    public static double calculateEntropy(final byte[] imageData, final int channels, final int valueIndex) {
        checkIndex(channels, valueIndex);
        double sum = 0.0;
        double log2 = log(2);
        for (int i = 0; i < 256; i++) {
            final double current = ratio(i, imageData, channels, valueIndex);
            if (abs(current) < 0.00000001) continue;
            final double log = log(current) / log2;
            sum -= current * log;
        }
        return sum;
    }

    public static double calculateMax(final byte[] imageData, final int channels, final int valueIndex) {
        checkIndex(channels, valueIndex);
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < imageData.length; i += channels) {
            max = max(max, toDouble(imageData[i + valueIndex]));
        }
        return max;
    }

    public static double calculateMin(final byte[] imageData, final int channels, final int valueIndex) {
        checkIndex(channels, valueIndex);
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < imageData.length; i += channels) {
            min = min(min, toDouble(imageData[i + valueIndex]));
        }
        return min;
    }

    private static double ratio(final int value, final byte[] imageData, final int channels, final int valueIndex) {
        int counter = 0;
        for (int i = 0; i < imageData.length; i += channels) {
            if (Byte.toUnsignedInt(imageData[i + valueIndex]) == value)
                counter++;
        }
        return counter / ((imageData.length / channels) * 1.0);
    }

    private static double toDouble(final byte x) {
        return Byte.toUnsignedInt(x) / 255.0;
    }

    private static void checkIndex(final int channels, final int valueIndex) {
        if (valueIndex < 0 || valueIndex >= channels) {
            final IllegalArgumentException ex = new IllegalArgumentException(
                    String.format("Value does not exist (index is negative or not enough channels)" +
                            "Index: %d, needed channels: %d, actual number of channels: %d", valueIndex, valueIndex + 1, channels)
            );
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }
}
