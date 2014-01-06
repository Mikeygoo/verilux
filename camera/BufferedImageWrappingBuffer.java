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
        int[] ints = {(int) (c.r * 256), (int) (c.g * 256), (int) (c.b * 256)};
        bi.getRaster().setPixel(x, y, ints);
    }
}
