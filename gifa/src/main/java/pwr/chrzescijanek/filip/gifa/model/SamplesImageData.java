package pwr.chrzescijanek.filip.gifa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.opencv.core.Mat;

public class SamplesImageData extends ImageData {

	public final ObservableList< Sample > samples = FXCollections.observableArrayList();

	SamplesImageData( final Image image, final Mat imageData ) {
		super(image, imageData);
	}

	void add( final Sample sample ) { this.samples.add(sample); }

}
