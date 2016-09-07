package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller {

	@FXML
	private ImageView view;

	public void setImage( final Image img ) {
		view.setImage(img);
	}

}
