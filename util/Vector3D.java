package util;

/**
 *
 * @author michael
 */
public class Vector3D {
    public double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(double n) {
        this(n, n, n);
    }

    public Vector3D(Normal n) {
        this(n.x, n.y, n.z);
    }

    public Vector3D(Point3D p) {
        this(p.x, p.y, p.z);
    }

    public Vector3D() {
        this(0, 0, 0);
    }

    public Vector3D negate() {
        return new Vector3D(-x, -y, -z);
    }

    public Vector3D scale(double d) {
        return new Vector3D(d * x, d * y, d * z);
    }

    public Vector3D add(Vector3D v) {
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }

    public void addTo(Vector3D v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public Vector3D subtract(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    public Vector3D add(Normal n) {
        return new Vector3D(x + n.x, y + n.y, z + n.z);
    }

    public Vector3D subtract(Normal n) {
        return new Vector3D(x - n.x, y - n.y, z - n.z);
    }

    public Vector3D cross(Vector3D v) {
        return new Vector3D(y * v.z - z * v.y,
                            z * v.x - x * v.z,
                            x * v.y - y * v.x);
    }

    public double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public double dot(Normal n) {
        return x * n.x + y * n.y + z * n.z;
    }
    
    public Vector3D normalize() {
        double k = 1.0 * Math.sqrt(x * x + y * y + z * z);
        return new Vector3D(k * x, k * y, k * z);
    }
    
    public void normalizeTo() {
        double k = 1.0 / Math.sqrt(x * x + y * y + z * z);
        x *= k;
        y *= k;
        z *= k;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}