package camera;

import java.awt.image.BufferedImage;
import util.RGBColor;

/**
 *
 * @author michael
 */
public class BufferedImageWrappingBuffer extends Buffer {
    private BufferedImage bi;

    public BufferedImageWrappingBuffer(BufferedImage bi) {
        this.bi = bi;
    }

    @Override
    void drawColor(int x, int y, RGBColor c) {
        y = bi.getHeight() - y - 1; /* I chose to do the "flip" here. (x, y) -> (x, height - y - 1) because of a difference in origin points in the code and rendering. */
        c.clampNormally();
        int[] ints = {(int) (c.r * 255), (int) (c.g * 255), (int) (c.b * 255)};
        bi.getRaster().setPixel(x, y, ints);
    }
}
