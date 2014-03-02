package util;

/**
 *
 * @author michael
 */
public class Normal {
    double x, y, z;

    public Normal(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Normal(Vector3D v) {
        this(v.x, v.y, v.z);
    }

    public Normal(double n) {
        this(n, n, n);
    }

    public Normal() {
        this(0, 0, 0);
    }

    public void set(Vector3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public void set(Point3D p) {
        x = p.x;
        y = p.y;
        z = p.z;
    }

    public Normal negate() {
        return new Normal(-x, -y, -z);
    }

    public Normal add(Normal n) {
        return new Normal(x + n.x, y + n.y, z + n.z);
    }

    public void addTo(Normal n) {
        x += n.x;
        y += n.y;
        z += n.z;
    }

    public double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Normal scale(double d) {
        return new Normal(d * x, d * y, d * z);
    }

    public void normalizeTo() {
        double l = Math.sqrt(x * x + y * y + z * z);
        x /= l;
        y /= l;
        z /= l;
    }
}
