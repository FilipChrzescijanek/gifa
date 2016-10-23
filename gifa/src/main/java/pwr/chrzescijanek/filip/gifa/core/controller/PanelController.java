package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class PanelController {

	public BorderPane root;
	public HBox panelHBox;
	public Label panelInfo;
	public HBox panelControls;
	public Label panelColumnsLabel;
	public TextField panelColumnsTextField;
	public ScrollPane panelGridScrollPane;
	public GridPane panelGrid;
	private int index = 0;

	@FXML
	void initialize() {
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert panelHBox != null : "fx:id=\"panelHBox\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert panelInfo != null : "fx:id=\"panelInfo\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert panelControls != null : "fx:id=\"panelControls\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert panelColumnsLabel != null : "fx:id=\"panelColumnsLabel\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert panelColumnsTextField != null : "fx:id=\"panelColumnsTextField\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert panelGridScrollPane != null : "fx:id=\"panelGridScrollPane\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		assert panelGrid != null : "fx:id=\"panelGrid\" was not injected: check your FXML file 'gifa-gui.fxml'.";
		panelColumnsTextField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			if ( !newValue.matches("\\d+") )
				panelColumnsTextField.setText(oldValue);
			else
				placeImages();
		});
		panelGridScrollPane.viewportBoundsProperty().addListener(( observable, oldValue, newValue ) -> placeImages());
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void placeImages() {
			List< ImageView > imageViews = State.INSTANCE.imageViews.get(index);
			if ( imageViews != null ) {
				panelGrid.getChildren().clear();
				panelGrid.getColumnConstraints().clear();
				panelGrid.getRowConstraints().clear();
				Bounds bounds = panelGridScrollPane.getViewportBounds();
				final int noOfColumns = Math.min(Integer.parseInt(panelColumnsTextField.getText()), imageViews.size());
				if ( noOfColumns > 0 ) {
					for ( int i = 0; i < noOfColumns; i++ ) {
						final ColumnConstraints columnConstraints = new ColumnConstraints();
						columnConstraints.setPercentWidth(100.0 / noOfColumns);
						columnConstraints.setHalignment(HPos.CENTER);
						panelGrid.getColumnConstraints().add(columnConstraints);
					}
					final int noOfRows = imageViews.size() / noOfColumns + (imageViews.size() % noOfColumns == 0 ? 0 : 1);
					for ( int i = 0; i < noOfRows; i++ ) {
						final RowConstraints rowConstraints = new RowConstraints();
						rowConstraints.setPercentHeight(100.0 / noOfRows);
						rowConstraints.setValignment(VPos.CENTER);
						panelGrid.getRowConstraints().add(rowConstraints);
					}
//					double fitWidth = Math.max(bounds.getWidth() / noOfColumns, 150.0);
					double fitHeight = Math.max(bounds.getHeight() / noOfRows, 150.0);
					for ( int i = 0; i < noOfRows; i++ ) {
						List< ImageView > imageViewsInRow = new ArrayList<>();
						int n = 0;
						while ( i * noOfColumns + n < imageViews.size() && n < noOfColumns ) {
							final ImageView view = imageViews.get(i * noOfColumns + n);
							view.setPreserveRatio(true);
//							view.setFitWidth(fitWidth);
							view.setFitHeight(fitHeight);
							imageViewsInRow.add(view);
							final int id = i;
							final int nd = n;
							view.fitWidthProperty().addListener(( observable, oldValue, newValue ) -> System.out.println((id * noOfColumns + nd ) + ": " + newValue));
							n++;
						}
						panelGrid.addRow(i, imageViewsInRow.toArray(new ImageView[0]));
					}
				}
			}
		}


}
