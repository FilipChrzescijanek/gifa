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

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class BaseController {

    public static final String THEME_PREFERENCE_KEY = "gifa.theme";
    public static final String THEME_DARK = "/css/theme-dark.css";
    public static final String THEME_LIGHT = "/css/theme-light.css";

    protected static final StringProperty theme = new SimpleStringProperty(BaseController.THEME_LIGHT);

    protected final IntegerProperty noOfColumns = new SimpleIntegerProperty(1);
    protected final IntegerProperty noOfRows = new SimpleIntegerProperty(1);
    protected final SharedState state;

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

    protected void bindSize(ImageView view, ScrollPane gridScrollPane) {
        bindSize(view, gridScrollPane, 1);
    }

    protected void bindSize(ImageView view, ScrollPane gridScrollPane, double scale) {
        view.fitWidthProperty().bind((gridScrollPane.widthProperty().subtract(30).subtract((noOfColumns.subtract(1)).multiply(10)))
                .divide(noOfColumns));
        view.fitHeightProperty().bind(gridScrollPane.heightProperty().subtract(new SimpleDoubleProperty(scale).multiply(20)));
    }

    protected void injectStylesheets(Parent node) {
        setTheme(node);
        theme.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty())
                setTheme(node);
        });
    }

    private void setTheme(Parent node) {
        node.getStylesheets().clear();
        node.getStylesheets().add(theme.get());
    }

    protected void calculateColumnsAndRows(TextField maxColumns, List<? extends Node> nodes) {
        noOfColumns.set(Math.min(Integer.parseInt(maxColumns.getText()), nodes.size()));
        if (noOfColumns.get() > 0)
            noOfRows.set(nodes.size() / noOfColumns.get() + (nodes.size() % noOfColumns.get() == 0 ? 0 : 1));
        else
            noOfRows.set(0);
    }

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
