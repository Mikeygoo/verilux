package object;

import material.Material;
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

    public Plane(Material m, Point3D p, Normal n) {
        super(m);
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
    public double hit(Ray r, ShadeRec sr) {
        double t = (point.subtract(r.o)).dot(normal) / r.d.dot(normal);

        if (t > K_EPSILON) {
            sr.normal = normal;
            sr.localHitPoint = r.o.add(r.d.scale(t)); // r.o + t * r.d
            return t;
        } else
            return Double.POSITIVE_INFINITY;
    }
    
    @Override
    public double hitShadow(Ray r) {
        double t = (point.subtract(r.o)).dot(normal) / r.d.dot(normal);

        if (t > K_EPSILON) {
            return t;
        } else
            return Double.POSITIVE_INFINITY;
    }
}
