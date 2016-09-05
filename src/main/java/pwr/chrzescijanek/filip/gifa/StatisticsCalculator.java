package pwr.chrzescijanek.filip.gifa;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

public class StatisticsCalculator {

	private final Mat[] images;
	private final boolean[] mask;

	public StatisticsCalculator( Mat[] images, boolean[] mask ) {
		this.images = images;
		this.mask = mask;
	}

	public void calculateMeanHue() {
		for (Mat m : images) {
			Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2HSV_FULL);
		}
		final int noOfImages = images.length;
		final int noOfBytes = (int) images[0].total() * images[0].channels();
		byte[][] imagesData = new byte[noOfImages][noOfBytes];
		for ( int i = 0; i < noOfImages; i++) {
			images[i].get(0, 0, imagesData[i]);
		}
		double[] mean = new double[noOfImages];
		for ( int i = 0; i < noOfImages; i++) {
			byte[] currentImage = imagesData[i];
			int counter = 0;
			for (int j = 0; j < noOfBytes; j += images[0].channels()) {
				if (!mask[j / images[0].channels()]) {
					mean[i] += Byte.toUnsignedInt(currentImage[j + 2]);
					counter++;
				}
			}
			mean[i] /= counter * 1.0;
		}
		System.out.println(Arrays.toString(mean));
	}

}
