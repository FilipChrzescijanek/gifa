package pwr.chrzescijanek.filip.gifa.core.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import pwr.chrzescijanek.filip.gifa.Main;

import java.util.prefs.Preferences;

public class Controller {

	public static final String THEME_PREFERENCE_KEY = "gifa.theme";
	public static final String THEME_DARK = "/theme-dark.css";
	public static final String THEME_LIGHT = "/theme-light.css";

	@FXML
	private VBox root;

	@FXML
	private MenuBar menuBar;

	@FXML
	private Menu fileMenu;

	@FXML
	private MenuItem fileMenuLoadImages;

	@FXML
	private MenuItem fileMenuExportToCsv;

	@FXML
	private MenuItem fileMenuExit;

	@FXML
	private Menu optionsMenu;

	@FXML
	private Menu optionsMenuTheme;

	@FXML
	private RadioMenuItem optionsMenuThemeLight;

	@FXML
	private ToggleGroup theme;

	@FXML
	private RadioMenuItem optionsMenuThemeDark;

	@FXML
	private Menu helpMenu;

	@FXML
	private MenuItem helpMenuAbout;

	@FXML
	private ImageView view;

	@FXML
	private ToggleGroup method;

	@FXML
	void about( ActionEvent event ) {

	}

	@FXML
	void applyDarkTheme( ActionEvent event ) {
		applyTheme(THEME_DARK);
	}

	@FXML
	void applyLightTheme( ActionEvent event ) {
		applyTheme(THEME_LIGHT);
	}

	public ToggleGroup getThemeToggleGroup() {
		return theme;
	}

	public Toggle getDarkThemeToggle() {
		return optionsMenuThemeDark;
	}

	public Toggle getLightThemeToggle() {
		return optionsMenuThemeLight;
	}

	private void applyTheme( final String theme ) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		prefs.put(THEME_PREFERENCE_KEY, theme);
		ObservableList< String > stylesheets = root.getScene().getStylesheets();
		stylesheets.clear();
		stylesheets.add(theme);
	}

	@FXML
	void exit( ActionEvent event ) {
		root.getScene().getWindow().hide();
	}

	@FXML
	void exportToCsv( ActionEvent event ) {

	}

	@FXML
	void loadImages( ActionEvent event ) {

	}

	@FXML
	void initialize() {
		assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'sample.fxml'.";
		assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'sample.fxml'.";
		assert fileMenu != null : "fx:id=\"fileMenu\" was not injected: check your FXML file 'sample.fxml'.";
		assert fileMenuLoadImages != null : "fx:id=\"fileMenuLoadImages\" was not injected: check your FXML file 'sample.fxml'.";
		assert fileMenuExportToCsv != null : "fx:id=\"fileMenuExportToCsv\" was not injected: check your FXML file 'sample.fxml'.";
		assert fileMenuExit != null : "fx:id=\"fileMenuExit\" was not injected: check your FXML file 'sample.fxml'.";
		assert optionsMenu != null : "fx:id=\"optionsMenu\" was not injected: check your FXML file 'sample.fxml'.";
		assert optionsMenuTheme != null : "fx:id=\"optionsMenuTheme\" was not injected: check your FXML file 'sample.fxml'.";
		assert optionsMenuThemeLight != null : "fx:id=\"optionsMenuThemeLight\" was not injected: check your FXML file 'sample.fxml'.";
		assert theme != null : "fx:id=\"theme\" was not injected: check your FXML file 'sample.fxml'.";
		assert optionsMenuThemeDark != null : "fx:id=\"optionsMenuThemeDark\" was not injected: check your FXML file 'sample.fxml'.";
		assert helpMenu != null : "fx:id=\"helpMenu\" was not injected: check your FXML file 'sample.fxml'.";
		assert helpMenuAbout != null : "fx:id=\"helpMenuAbout\" was not injected: check your FXML file 'sample.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'sample.fxml'.";
		assert method != null : "fx:id=\"method\" was not injected: check your FXML file 'sample.fxml'.";
	}

	public void setImage( final Image img ) {
		view.setImage(img);
	}

}