package pwr.chrzescijanek.filip.gifa.controller;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.*;
import javafx.util.Pair;
import org.opencv.core.*;
import pwr.chrzescijanek.filip.gifa.model.image.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.sample.BaseSample;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static org.opencv.imgproc.Imgproc.ellipse;

public final class ControllerUtils {

    private ControllerUtils() {}

    public static void prepareImage(BaseSample sample, int index, Mat[] images, SamplesImageData samplesImageData) {
        final int x = (int) sample.sampleArea.getX();
        final int y = (int) sample.sampleArea.getY();
        int width = (int) sample.sampleArea.getWidth();
        int height = (int) sample.sampleArea.getHeight();
        images[index] = samplesImageData.imageData
                .submat(new Rect(x, y, width, height)).clone();
        Mat zeros = Mat.zeros(images[index].rows(), images[index].cols(), images[index].type());
        ellipse(zeros, new Point(sample.getCenterX() - x, sample.getCenterY() - y), new Size(sample.getRadiusX(), sample.getRadiusY()), sample.getRotate(), 0.0, 360.0,
                new Scalar(255,
                        255, 255, 255), Core.FILLED);
        Core.bitwise_and(images[index], zeros, images[index]);
    }

    public static void writeImage(File selectedDirectory, List<Pair<String, ImageView>> currentSamples, int i, int j) throws IOException {
        BufferedImage image = SwingFXUtils.fromFXImage(currentSamples.get(j).getValue().getImage(), null);
        ImageIO.write(image, "png",
                new File(selectedDirectory.getCanonicalPath()
                        + File.separator + "sample#" + (i + 1) + "_" + currentSamples.get(j).getKey() + ".png"));
    }

    public static File getFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export results to CSV file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma-separated values", "*.csv"));
        return fileChooser.showSaveDialog(window);
    }

    public static List<File> getFiles(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load images");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.tif"));
        return fileChooser.showOpenMultipleDialog(window);
    }

    public static File getDirectory(Window window) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose directory");
        return chooser.showDialog(window);
    }

    public static WebView getHelpView() {
        WebView view = new WebView();
        view.getEngine().load(ControllerUtils.class.getResource("/static/help.html").toExternalForm());
        return view;
    }

    public static Alert getAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Global image features analyzer\n" +
                        "GitHub repository: https://github.com/lazzymf/gifa/\n" +
                        "\nCopyright © 2016 Filip Chrześcijanek\nfilip.chrzescijanek@gmail.com", ButtonType.OK);
        alert.setTitle("About");
        alert.setHeaderText("gifa");
        alert.setGraphic(new ImageView(new Image(ControllerUtils.class.getResourceAsStream("/images/icon-big.png"))));
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image(ControllerUtils.class.getResourceAsStream("/images/icon-small.png")));
        return alert;
    }

    public static Alert getErrorAlert(final String content) {
        return new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
    }

    public static Stage initDialog(Window window) {
        final Stage dialog = new Stage();
        dialog.initOwner(window);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        return dialog;
    }

    public static HBox getHBoxWithLabel(String info) {
        final Label label = new Label(info);
        label.setAlignment(Pos.CENTER);
        final HBox box = new HBox(label, new ProgressIndicator(-1.0));
        box.setSpacing(30.0);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        return box;
    }

    public static void startRunnable(final Task<? extends Void> task) {
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public static String getWebColor(Color newValue) {
        return String.format("#%02X%02X%02X%02X",
                (int) (newValue.getRed() * 255),
                (int) (newValue.getGreen() * 255),
                (int) (newValue.getBlue() * 255),
                (int) (newValue.getOpacity() * 255));
    }

}
