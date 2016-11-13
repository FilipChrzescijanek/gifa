package pwr.chrzescijanek.filip.gifa.model.image;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import pwr.chrzescijanek.filip.gifa.model.sample.Sample;

public class SamplesImageData extends ImageData {

	public final ObservableList<Sample> samples = FXCollections.observableArrayList();

	protected SamplesImageData( final Image image, final Mat imageData ) {
		super(image, imageData);
	}

	public void add( final Sample sample ) { this.samples.add(sample); }

}
