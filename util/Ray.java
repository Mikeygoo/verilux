package util;

/**
 *
 * @author michael
 */
public class Ray {
    public Point3D o;
    public Vector3D d;

    public Ray(Point3D o, Vector3D d) {
        this.o = o;
        this.d = d;
    }

    public Ray() {
        o = new Point3D(0, 0, 0);
        d = new Vector3D(0, 0, 0);
    }
}
