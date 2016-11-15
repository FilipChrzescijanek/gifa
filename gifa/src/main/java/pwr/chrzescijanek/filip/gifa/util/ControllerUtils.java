package pwr.chrzescijanek.filip.gifa.util;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.*;
import javafx.util.Pair;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import pwr.chrzescijanek.filip.gifa.controller.PanelController;
import pwr.chrzescijanek.filip.gifa.core.util.ImageUtils;
import pwr.chrzescijanek.filip.gifa.model.image.SamplesImageData;
import pwr.chrzescijanek.filip.gifa.model.panel.PanelView;
import pwr.chrzescijanek.filip.gifa.model.sample.BasicSample;
import pwr.chrzescijanek.filip.gifa.view.FXView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.COLOR_RGBA2BGRA;
import static org.opencv.imgproc.Imgproc.ellipse;
import static pwr.chrzescijanek.filip.gifa.core.util.ImageUtils.createMat;

/**
 * Provides utility methods for handling controllers.
 */
public final class ControllerUtils {

    private ControllerUtils() {}

    /**
     * Loads panel controller from given FXML view and initializes it with given label and panel views.
     * @param info label
     * @param views panel views
     * @param fxView FXML view
     * @return new PanelController class instance
     */
    public static PanelController initializeController(final String info, final List<? extends PanelView> views, final FXView fxView) {
        final PanelController controller = (PanelController) fxView.getController();
        controller.setInfo(info);
        controller.setPanelViews(views);
        return controller;
    }

    /**
     * Prepares given user-defined sample for comparison.
     * @param sample user-defined sample
     * @param index sample index
     * @param images array of images of samples
     * @param samplesImageData aligned image of which given sample was taken
     */
    public static void prepareImage(BasicSample sample, int index, Mat[] images, SamplesImageData samplesImageData) {
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

    /**
     * Writes image of sample to given directory.
     * @param selectedDirectory directory
     * @param currentSamples user-defined samples
     * @param sampleIndex sample index
     * @param imageIndex index of aligned image of which given sample was taken
     * @throws IOException if image could not be written
     */
    public static void writeImage(File selectedDirectory, List<Pair<String, ImageView>> currentSamples,
                                  int sampleIndex, int imageIndex) throws IOException {
        Mat image = createMat(currentSamples.get(imageIndex).getValue().getImage());
        final String key = currentSamples.get(imageIndex).getKey();
        String extension = key.substring(key.indexOf('.') + 1);
        final int indexOf = extension.indexOf('_');
        if (indexOf >= 0) extension = extension.substring(0, indexOf);
        Imgcodecs.imwrite(selectedDirectory.getCanonicalPath()
                + File.separator + "sample#" + (sampleIndex + 1) + "_" + key + "." + extension, image);
    }

    /**
     * Shows file chooser dialog and gets CSV file.
     * @param window application window
     * @return CSV file
     */
    public static File getCSVFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export results to CSV file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma-separated values", "*.csv"));
        return fileChooser.showSaveDialog(window);
    }

    /**
     * Shows file chooser dialog and gets image files.
     * @param window application window
     * @return image files
     */
    public static List<File> getImageFiles(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load images");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.tif"));
        return fileChooser.showOpenMultipleDialog(window);
    }

    /**
     * Shows directory chooser dialog and gets directory.
     * @param window application window
     * @return directory
     */
    public static File getDirectory(Window window) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose directory");
        return chooser.showDialog(window);
    }

    /**
     * @return help view
     */
    public static WebView getHelpView() {
        WebView view = new WebView();
        view.getEngine().load(ControllerUtils.class.getResource("/static/help.html").toExternalForm());
        return view;
    }

    /**
     * @param info label
     * @return customized, centered horizontal box with given label and progress indicator
     */
    public static HBox getHBoxWithLabelAndProgressIndicator(String info) {
        final Label label = new Label(info);
        label.setAlignment(Pos.CENTER);
        final HBox box = new HBox(label, new ProgressIndicator(-1.0));
        box.setSpacing(30.0);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        return box;
    }

    /**
     * Starts given task
     * @param task task to start
     */
    public static void startTask(final Task<? extends Void> task) {
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * @param color JavaFX color
     * @return given color in web color format
     */
    public static String getWebColor(Color color) {
        return String.format("#%02X%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                (int) (color.getOpacity() * 255));
    }

}
