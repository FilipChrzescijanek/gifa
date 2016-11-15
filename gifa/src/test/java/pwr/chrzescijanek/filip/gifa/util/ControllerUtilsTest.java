package pwr.chrzescijanek.filip.gifa.util;

import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static pwr.chrzescijanek.filip.gifa.util.ControllerUtils.getWebColor;

public class ControllerUtilsTest {

    @Test
    public void getWebColorTest() throws Exception {
        testColor(Color.ALICEBLUE, "#F0F8FFFF");
        testColor(Color.GRAY, "#808080FF");
        testColor(Color.WHITE, "#FFFFFFFF");
        testColor(Color.BLACK, "#000000FF");
        testColor(Color.color(1.0, 1.0, 0.0, 0.0), "#FFFF0000");
        testColor(Color.color(0.5, 0.5, 0.1, 0.5), "#7F7F197F");
    }
    private void testColor(Color color, String expected) {
        String webColor = getWebColor(color);
        assertEquals(expected, webColor);
    }

}