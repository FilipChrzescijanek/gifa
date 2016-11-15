package pwr.chrzescijanek.filip.gifa.controller;

import javafx.beans.property.*;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import pwr.chrzescijanek.filip.gifa.Main;
import pwr.chrzescijanek.filip.gifa.util.SharedState;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Base class for controllers.
 */
public class BaseController {

    private static final String THEME_PREFERENCE_KEY = "gifa.theme";
    private static final String THEME_DARK = "/css/theme-dark.css";
    private static final String THEME_LIGHT = "/css/theme-light.css";

    /**
     * Controller theme.
     */
    protected static final StringProperty theme = new SimpleStringProperty(BaseController.THEME_LIGHT);

    /**
     * Panel columns.
     */
    protected final IntegerProperty noOfColumns = new SimpleIntegerProperty(1);

    /**
     * Panel rows.
     */
    protected final IntegerProperty noOfRows = new SimpleIntegerProperty(1);

    /**
     * Shared state.
     */
    protected final SharedState state;

    /**
     * Constructs new BaseController with given shared state.
     * @param state shared state
     */
    protected BaseController(final SharedState state) {
        this.state = state;
        loadTheme();
    }

    private void loadTheme() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String s = prefs.get(BaseController.THEME_PREFERENCE_KEY, BaseController.THEME_LIGHT);
        if (s.equals(BaseController.THEME_LIGHT))
            theme.set(BaseController.THEME_LIGHT);
        else
            theme.set(BaseController.THEME_DARK);
    }

    /**
     * Binds given panel view size to panel cell size, calculated based on given scroll pane.
     * @param view panel view
     * @param gridScrollPane panel's scroll pane containing grid pane
     */
    protected void bindSize(ImageView view, ScrollPane gridScrollPane) {
        bindSize(view, gridScrollPane, 1);
    }

    /**
     * Binds given panel view size to panel cell size, calculated based on given scroll pane,
     * using scale (the bigger the scale, the lower the single cell height).
     * @param view panel view
     * @param gridScrollPane panel's scroll pane containing grid pane
     * @param scale alignment scale
     */
    protected void bindSize(ImageView view, ScrollPane gridScrollPane, double scale) {
        view.fitWidthProperty().bind((gridScrollPane.widthProperty().subtract(30).subtract((noOfColumns.subtract(1)).multiply(10)))
                .divide(noOfColumns));
        view.fitHeightProperty().bind(gridScrollPane.heightProperty().subtract(new SimpleDoubleProperty(scale).multiply(20)));
    }

    /**
     * Injects current theme stylesheets to given parent and adds on theme changed listener.
     * @param parent parent to be styled
     */
    protected void injectStylesheets(Parent parent) {
        setTheme(parent);
        theme.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty())
                setTheme(parent);
        });
    }

    private void setTheme(Parent node) {
        node.getStylesheets().clear();
        node.getStylesheets().add(theme.get());
    }

    /**
     * Sets dark theme.
     */
    protected void setDarkTheme() {
        theme.set(THEME_DARK);
    }

    /**
     * Sets light theme.
     */
    protected void setLightTheme() {
        theme.set(THEME_LIGHT);
    }

    /**
     * @return true if light theme is selected; false otherwise
     */
    protected boolean isLightThemeSelected() {
        return theme.get().equals(THEME_LIGHT);
    }

    /**
     * @return true if dark theme is selected; false otherwise
     */
    protected boolean isDarkThemeSelected() {
        return theme.get().equals(THEME_DARK);
    }

    /**
     * Calculates the number of panel columns and rows based on given number of max. columns
     * and nodes that need to placed onto the panel.
     * @param maxColumns max. number of columns
     * @param nodes nodes that need to be placed onto the panel
     */
    protected void calculateColumnsAndRows(TextField maxColumns, List<? extends Node> nodes) {
        noOfColumns.set(Math.min(Integer.parseInt(maxColumns.getText()), nodes.size()));
        if (noOfColumns.get() > 0)
            noOfRows.set(nodes.size() / noOfColumns.get() + (nodes.size() % noOfColumns.get() == 0 ? 0 : 1));
        else
            noOfRows.set(0);
    }

    /**
     * Places given nodes into given panel grid.
     * @param nodes nodes to be placed
     * @param grid panel grid
     */
    protected void placeNodes(List<? extends Node> nodes, GridPane grid) {
        if (nodes != null && !nodes.isEmpty()) {
            resetGrid(grid);
            setColumnConstraints(grid);
            setRowConstraints(grid);
            populateGrid(nodes, grid);
        }
    }

    private void resetGrid(GridPane grid) {
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
    }

    private void setColumnConstraints(GridPane grid) {
        for (int i = 0; i < noOfColumns.get(); i++) {
            final ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / noOfColumns.get());
            columnConstraints.setHalignment(HPos.CENTER);
            grid.getColumnConstraints().add(columnConstraints);
        }
    }

    private void setRowConstraints(GridPane grid) {
        for (int i = 0; i < noOfRows.get(); i++) {
            final RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / noOfRows.get());
            rowConstraints.setValignment(VPos.CENTER);
            grid.getRowConstraints().add(rowConstraints);
        }
    }

    private void populateGrid(List<? extends Node> nodes, GridPane grid) {
        for (int i = 0; i < noOfRows.get(); i++) {
            List<Node> nodesInRow = populateRow(nodes, i);
            grid.addRow(i, nodesInRow.toArray(new Node[0]));
        }
    }

    private List<Node> populateRow(List<? extends Node> nodes, int i) {
        List<Node> nodesInRow = new ArrayList<>();
        int n = 0;
        while (nodesLeft(nodes, i, n)) {
            nodesInRow.add(nodes.get(i * noOfColumns.get() + n));
            n++;
        }
        return nodesInRow;
    }

    private boolean nodesLeft(List<? extends Node> nodes, int i, int n) {
        return i * noOfColumns.get() + n < nodes.size() && n < noOfColumns.get();
    }

}
