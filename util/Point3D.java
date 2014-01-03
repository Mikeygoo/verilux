package util;

/**
 *
 * @author michael
 */
public class Point3D {
    double x, y, z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Point3D(double n) {
        this(n, n, n);
    }

    public Point3D() {
        this(0, 0, 0);
    }
    
    public Point3D negate() {
        return new Point3D(-x, -y, -z);
    }
    
    public Vector3D subtract(Point3D p) {
        return new Vector3D(x - p.x, y - p.y, z - p.z);
    }
    
    public Point3D add(Vector3D v) {
        return new Point3D(x + v.x, y + v.y, z + v.z);
    }
    
    public Point3D subtract(Vector3D v) {
        return new Point3D(x - v.x, y - v.y, z - v.z);
    }
    
    public Point3D scale(double d) {
        return new Point3D(d * x, d * y, d * z);
    }
    
    public double distanceSquared(Point3D p) {
        return (x - p.x) * (x - p.x) +
               (y - p.y) * (y - p.y) +
               (z - p.z) * (z - p.z);
    }
    
    public double distance(Point3D p) { 
        return Math.sqrt(distanceSquared(p));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
