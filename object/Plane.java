package object;

import tracer.ShadeRec;
import util.Normal;
import util.Point3D;
import util.RGBColor;
import util.Ray;

/**
 *
 * @author michael
 */
public class Plane extends GeometricObject {
    private static final double K_EPSILON = 0.00001;
    private Point3D point;
    private Normal normal;

    public Plane() {

    }

    public Plane(RGBColor col, Point3D p, Normal n) {
        super(col);
        point = p;
        normal = n;
    }

    public Point3D getPoint() {
        return point;
    }

    public void setPoint(Point3D point) {
        this.point = point;
    }

    public Normal getNormal() {
        return normal;
    }

    public void setNormal(Normal normal) {
        this.normal = normal;
    }

    @Override
    public boolean hit(Ray r, ShadeRec sr) {
        double t = (point.subtract(r.o)).dot(normal) / r.d.dot(normal);

        if (t > K_EPSILON && t < sr.hitDistance) {
            sr.hitDistance = t;
            sr.normal = normal;
            sr.localHitPoint = r.o.add(r.d.scale(t)); // r.o + t * r.d
            return true;
        } else
            return false;
    }
}
