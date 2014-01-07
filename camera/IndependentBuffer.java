package camera;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.RGBColor;

/**
 *
 * @author michael
 */
public class IndependentBuffer extends Buffer {
    private final RGBColor[][] buffer;
    private final int w, h;

    public IndependentBuffer(int w, int h) {
        buffer = new RGBColor[w][h];
        this.w = w;
        this.h = h;
    }

    @Override
    void drawColor(int x, int y, RGBColor c) {
        buffer[x][y] = c;
    }

    public void writeToFile(String imgname) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(imgname);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IndependentBuffer.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        ps.printf("P3\n%d %d\n16383\n", w, h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                ps.printf("%d %d %d   ", (int) (buffer[x][y].r * 16383), (int) (buffer[x][y].g * 16383), (int) (buffer[x][y].b * 16383));
            }
            ps.println();
        }
        
        ps.flush();
        ps.close();
    }

    public RGBColor get(int x, int y) {
        return buffer[x][y];
    }

    public void populateBlack() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                buffer[x][y] = new RGBColor(0);
            }
        }
    }
}
