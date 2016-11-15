package pwr.chrzescijanek.filip.gifa.core.util;

import org.opencv.core.Mat;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.*;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.getImageData;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.getImagesData;

/**
 * Provides utility methods for evaluating functions.
 */
public final class FunctionUtils {

    private static final Logger LOGGER = Logger.getLogger(FunctionUtils.class.getName());

    private FunctionUtils() {}

    /**
     * Calculates mean of given channel for every given image.
     * @param images OpenCV images
     * @param channel channel index
     * @return channel means
     */
    public static double[] calculateMeans(final Mat[] images, final int channel) {
        final byte[][] imagesData = getImagesData(images);
        final double[] means = new double[images.length];
        for (int i = 0; i < means.length; i++) {
            means[i] = calculateMean(imagesData[i], images[i].channels(), channel);
        }
        return means;
    }

    /**
     * Calculates mean of given channel for given image.
     * @param image OpenCV image
     * @param channel channel index
     * @return channel mean
     */
    public static double calculateMean(final Mat image, final int channel) {
        return calculateMean(getImageData(image), image.channels(), channel);
    }

    /**
     * Calculates mean of given channel for given byte image data.
     * @param imageData byte image data
     * @param channels image channels
     * @param channel channel index
     * @return channel mean
     */
    public static double calculateMean(final byte[] imageData, final int channels, final int channel) {
        checkIfChannelExists(channels, channel);
        double mean = 0.0;
        int counter = 0;
        for (int i = 0; i < imageData.length; i += channels) {
            mean += toDouble(imageData[i + channel]);
            counter++;
        }
        mean /= counter * 1.0;
        return mean;
    }

    /**
     * Calculates variance of given channel for every given image.
     * @param images OpenCV images
     * @param channel channel index
     * @return channel variances
     */
    public static double[] calculateVariances(final Mat[] images, final int channel) {
        final byte[][] imagesData = getImagesData(images);
        final double[] variances = new double[images.length];
        for (int i = 0; i < variances.length; i++) {
            variances[i] = calculateVariance(imagesData[i], images[i].channels(), channel);
        }
        return variances;
    }

    /**
     * Calculates variance of given channel for given image.
     * @param image OpenCV image
     * @param channel channel index
     * @return channel variance
     */
    public static double calculateVariance(final Mat image, final int channel) {
        return calculateVariance(getImageData(image), image.channels(), channel);
    }

    /**
     * Calculates variance of given channel for given byte image data.
     * @param imageData byte image data
     * @param channels image channels
     * @param channel channel index
     * @return channel variance
     */
    public static double calculateVariance(final byte[] imageData, final int channels, final int channel) {
        final double mean = calculateMean(imageData, channels, channel);
        double variance = 0.0;
        int counter = 0;
        for (int i = 0; i < imageData.length; i += channels) {
            variance += pow(toDouble(imageData[i + channel]) - mean, 2);
            counter++;
        }
        variance /= counter * 1.0;
        return variance;
    }

    /**
     * Calculates standard deviation of given channel for every given image.
     * @param images OpenCV images
     * @param channel channel index
     * @return channel standard deviations
     */
    public static double[] calculateStdDeviations(final Mat[] images, final int channel) {
        final double[] stdDeviations = calculateVariances(images, channel);
        for (int i = 0; i < stdDeviations.length; i++) {
            stdDeviations[i] = sqrt(stdDeviations[i]);
        }
        return stdDeviations;
    }

    /**
     * Calculates standard deviation of given channel for given image.
     * @param image OpenCV image
     * @param channel channel index
     * @return channel standard deviation
     */
    public static double calculateStdDeviation(final Mat image, final int channel) {
        return calculateStdDeviation(getImageData(image), image.channels(), channel);
    }

    /**
     * Calculates standard deviation of given channel for given byte image data.
     * @param imageData byte image data
     * @param channels image channels
     * @param channel channel index
     * @return channel standard deviation
     */
    public static double calculateStdDeviation(final byte[] imageData, final int channels, final int channel) {
        return sqrt(calculateVariance(imageData, channels, channel));
    }

    /**
     * Calculates linear fuzziness of given channel for every given image.
     * @param images OpenCV images
     * @param channel channel index
     * @return channel linear fuzzinesses
     */
    public static double[] calculateLinearFuzzinesses(final Mat[] images, final int channel) {
        final byte[][] imagesData = getImagesData(images);
        final double[] fuzzinesses = new double[images.length];
        for (int i = 0; i < fuzzinesses.length; i++) {
            fuzzinesses[i] = calculateLinearFuzziness(imagesData[i], images[i].channels(), channel);
        }
        return fuzzinesses;
    }

    /**
     * Calculates linear fuzziness of given channel for given image.
     * @param image OpenCV image
     * @param channel channel index
     * @return channel linear fuzziness
     */
    public static double calculateLinearFuzziness(final Mat image, final int channel) {
        return calculateLinearFuzziness(getImageData(image), image.channels(), channel);
    }

    /**
     * Calculates linear fuzziness of given channel for given byte image data.
     * @param imageData byte image data
     * @param channels image channels
     * @param channel channel index
     * @return channel linear fuzziness
     */
    public static double calculateLinearFuzziness(final byte[] imageData, final int channels, final int channel) {
        final double max = calculateMax(imageData, channels, channel);
        if (abs(max) < 0.00000001) return 0;
        double sum = 0.0;
        for (int i = 0; i < imageData.length; i += channels) {
            final double current = toDouble(imageData[i + channel]);
            final double functionValue = current / max;
            sum += min(functionValue, 1.0 - functionValue);
        }
        return sum * 2 / (imageData.length / channels);
    }

    /**
     * Calculates quadratic fuzziness of given channel for every given image.
     * @param images OpenCV images
     * @param channel channel index
     * @return channel quadratic fuzzinesses
     */
    public static double[] calculateQuadraticFuzzinesses(final Mat[] images, final int channel) {
        final byte[][] imagesData = getImagesData(images);
        final double[] fuzzinesses = new double[images.length];
        for (int i = 0; i < fuzzinesses.length; i++) {
            fuzzinesses[i] = calculateQuadraticFuzziness(imagesData[i], images[i].channels(), channel);
        }
        return fuzzinesses;
    }

    /**
     * Calculates quadratic fuzziness of given channel for given image.
     * @param image OpenCV image
     * @param channel channel index
     * @return channel quadratic fuzziness
     */
    public static double calculateQuadraticFuzziness(final Mat image, final int channel) {
        return calculateQuadraticFuzziness(getImageData(image), image.channels(), channel);
    }

    /**
     * Calculates quadratic fuzziness of given channel for given byte image data.
     * @param imageData byte image data
     * @param channels image channels
     * @param channel channel index
     * @return channel quadratic fuzziness
     */
    public static double calculateQuadraticFuzziness(final byte[] imageData, final int channels, final int channel) {
        final double max = calculateMax(imageData, channels, channel);
        if (abs(max) < 0.00000001) return 0;
        double sum = 0.0;
        for (int i = 0; i < imageData.length; i += channels) {
            final double current = toDouble(imageData[i + channel]);
            final double functionValue = current / max;
            sum += pow(min(functionValue, 1.0 - functionValue), 2);
        }
        return sqrt(sum) * 2 / sqrt((imageData.length / channels));
    }

    /**
     * Calculates entropy of given channel for every given image.
     * @param images OpenCV images
     * @param channel channel index
     * @return channel entropies
     */
    public static double[] calculateEntropies(final Mat[] images, final int channel) {
        final byte[][] imagesData = getImagesData(images);
        final double[] entropies = new double[images.length];
        for (int i = 0; i < entropies.length; i++) {
            entropies[i] = calculateEntropy(imagesData[i], images[i].channels(), channel);
        }
        return entropies;
    }

    /**
     * Calculates entropy of given channel for given image.
     * @param image OpenCV image
     * @param channel channel index
     * @return channel entropy
     */
    public static double calculateEntropy(final Mat image, final int channel) {
        return calculateEntropy(getImageData(image), image.channels(), channel);
    }

    /**
     * Calculates entropy of given channel for given byte image data.
     * @param imageData byte image data
     * @param channels image channels
     * @param channel channel index
     * @return channel entropy
     */
    public static double calculateEntropy(final byte[] imageData, final int channels, final int channel) {
        checkIfChannelExists(channels, channel);
        double sum = 0.0;
        double log2 = log(2);
        for (int i = 0; i < 256; i++) {
            final double current = ratio(i, imageData, channels, channel);
            if (abs(current) < 0.00000001) continue;
            final double log = log(current) / log2;
            sum -= current * log;
        }
        return sum;
    }

    private static double calculateMax(final byte[] imageData, final int channels, final int channel) {
        checkIfChannelExists(channels, channel);
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < imageData.length; i += channels) {
            max = max(max, toDouble(imageData[i + channel]));
        }
        return max;
    }

    private static double calculateMin(final byte[] imageData, final int channels, final int channel) {
        checkIfChannelExists(channels, channel);
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < imageData.length; i += channels) {
            min = min(min, toDouble(imageData[i + channel]));
        }
        return min;
    }

    private static double ratio(final int value, final byte[] imageData, final int channels, final int channel) {
        int counter = 0;
        for (int i = 0; i < imageData.length; i += channels) {
            if (Byte.toUnsignedInt(imageData[i + channel]) == value)
                counter++;
        }
        return counter / ((imageData.length / channels) * 1.0);
    }

    private static double toDouble(final byte x) {
        return Byte.toUnsignedInt(x) / 255.0;
    }

    private static void checkIfChannelExists(final int channels, final int channel) {
        if (channel < 0 || channel >= channels) {
            final IllegalArgumentException ex = new IllegalArgumentException(
                    String.format("Channel does not exist (channel is negative or not enough channels)" +
                            "Channel index: %d, needed channels: %d, actual number of channels: %d",
                            channel, channel + 1, channels)
            );
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        }
    }
}
