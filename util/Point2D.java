package util;

/**
 *
 * @author michael
 */
public class Point2D {
    public double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(double n) {
        this(n, n);
    }

    public Point2D() {
        this(0, 0);
    }

    public Point2D scale(double d) {
        return new Point2D(d * x, d * y);
    }
}
