package object;

import material.Material;
import tracer.ShadeRec;
import util.Normal;
import util.Point3D;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class Sphere extends GeometricObject {
    private static final double K_EPSILON = 0.00001;
    private Point3D center;
    private double radius;

    public Sphere(Material m, Point3D ctr, double rad) {
        super(m);
        center = ctr;
        radius = rad;
    }

    public Point3D getCenter() {
        return center;
    }

    public void setCenter(Point3D center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public double hit(Ray r, ShadeRec sr) {
        double t;
        Vector3D temp = r.o.subtract(center);
        double a = r.d.dot(r.d);
        double b = 2.0 * temp.dot(r.d);
        double c = temp.dot(temp) - radius * radius;
        double disc = b * b - 4.0 * a * c;

        if (disc < 0.0)
            return Double.POSITIVE_INFINITY;
        else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;

            t = (-b - e) / denom;

            if (t > K_EPSILON) {
                sr.normal = new Normal((temp.add(r.d.scale(t))).scale(1.0 / radius)); // (temp + t * r.d) / radius
                sr.localHitPoint = r.o.add(r.d.scale(t));
                return t;
            }

            t = (-b + e) / denom;

            if (t > K_EPSILON) {
                sr.normal = new Normal((temp.add(r.d.scale(t))).scale(1.0 / radius)); // (temp + t * r.d) / radius
                sr.localHitPoint = r.o.add(r.d.scale(t));
                return t;
            }
        }

        return Double.POSITIVE_INFINITY;
    }
}
