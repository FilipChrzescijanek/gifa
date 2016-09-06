package pwr.chrzescijanek.filip.gifa;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller {

	@FXML
	private ImageView view;

	public void setImage( Image img ) {
		view.setImage(img);
	}

}
