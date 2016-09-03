package pwr.chrzescijanek.filip.gifa;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

	@FXML
	private Label label;

	public void setLabel( String text ) {
		label.setText(text);
	}

}
