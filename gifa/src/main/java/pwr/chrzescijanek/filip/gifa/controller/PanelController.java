package pwr.chrzescijanek.filip.gifa.controller;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import pwr.chrzescijanek.filip.gifa.Main;
import pwr.chrzescijanek.filip.gifa.model.PanelView;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class PanelController {

	public BorderPane root;
	public HBox panelHBox;
	public Label panelInfo;
	public HBox panelControls;
	public Label panelColumnsLabel;
	public TextField panelColumnsTextField;
	public ScrollPane panelGridScrollPane;
	public GridPane panelGrid;
	private List< ? extends PanelView > imageViews = null;

	public void setImageViews( final List< ? extends PanelView > imageViews ) {
		this.imageViews = imageViews;
	}

	public void setInfo( String info ) {
		panelInfo.setText(info);
	}

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

		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String s = prefs.get(Controller.THEME_PREFERENCE_KEY, Controller.THEME_LIGHT);
		if ( s.equals(Controller.THEME_LIGHT) ) {
			root.getStylesheets().add(Controller.THEME_LIGHT);
		} else {
			root.getStylesheets().add(Controller.THEME_DARK);
		}

		panelColumnsTextField.textProperty().addListener(( observable, oldValue, newValue ) -> {
			if ( !newValue.matches("\\d+") )
				panelColumnsTextField.setText(oldValue);
			else
				placeImages();
		});
		panelHBox.visibleProperty().bind(panelInfo.textProperty().isNotNull());
		root.layoutBoundsProperty().addListener(( observable, oldValue, newValue ) -> placeImages());
	}

	public void placeImages() {
		if ( imageViews != null ) {
			panelGrid.getChildren().clear();
			panelGrid.getColumnConstraints().clear();
			panelGrid.getRowConstraints().clear();
			Bounds bounds = panelGridScrollPane.getLayoutBounds();
			final int noOfColumns = Math.min(Integer.parseInt(panelColumnsTextField.getText()), imageViews.size());
			if ( noOfColumns > 0 ) {
				for ( int i = 0; i < noOfColumns; i++ ) {
					final ColumnConstraints columnConstraints = new ColumnConstraints();
					columnConstraints.setPercentWidth(100.0 / noOfColumns);
					columnConstraints.setHalignment(HPos.CENTER);
					panelGrid.getColumnConstraints().add(columnConstraints);
				}
				final int noOfRows = imageViews.size() / noOfColumns + ( imageViews.size() % noOfColumns == 0 ? 0 : 1 );
				for ( int i = 0; i < noOfRows; i++ ) {
					final RowConstraints rowConstraints = new RowConstraints();
					rowConstraints.setPercentHeight(100.0 / noOfRows);
					rowConstraints.setValignment(VPos.CENTER);
					panelGrid.getRowConstraints().add(rowConstraints);
				}
				double fitWidth = Math.max(( bounds.getWidth() - 50.0 ) / noOfColumns, 150.0);
				double fitHeight = Math.max(( bounds.getHeight() - 50.0 ) / noOfRows, 150.0);
				for ( int i = 0; i < noOfRows; i++ ) {
					List< PanelView > imageViewsInRow = new ArrayList<>();
					int n = 0;
					while ( i * noOfColumns + n < imageViews.size() && n < noOfColumns ) {
						final PanelView view = imageViews.get(i * noOfColumns + n);
						view.setFitWidth(fitWidth);
						view.setFitHeight(fitHeight);
						imageViewsInRow.add(view);
						n++;
					}
					panelGrid.addRow(i, (Node[]) imageViewsInRow.toArray(new PanelView[0]));
				}
			}
		}
	}


}
