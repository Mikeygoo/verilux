package object;

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

    public Sphere() {
        this(RGBColor.WHITE, new Point3D(0), 1);
    }

    public Sphere(RGBColor col, Point3D ctr, double rad) {
        super(col);
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
    public boolean hit(Ray r, ShadeRec sr) {
        double t;
        Vector3D temp = r.o.subtract(center);
        double a = r.d.dot(r.d);
        double b = 2.0 * temp.dot(r.d);
        double c = temp.dot(temp) - radius * radius;
        double disc = b * b - 4.0 * a * c;

        if (disc < 0.0)
            return false;
        else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;

            t = (-b - e) / denom;

            if (t > sr.hitDistance) //if the smallest is too big, ditch now.
                return false;

            if (t > K_EPSILON) {
                sr.hitDistance = t;
                sr.normal = new Normal((temp.add(r.d.scale(t))).scale(1.0 / radius)); // (temp + t * r.d) / radius
                sr.localHitPoint = r.o.add(r.d.scale(t));
                return true;
            }

            t = (-b + e) / denom;

            if (t > K_EPSILON && t < sr.hitDistance) {
                sr.hitDistance = t;
                sr.normal = new Normal((temp.add(r.d.scale(t))).scale(1.0 / radius)); // (temp + t * r.d) / radius
                sr.localHitPoint = r.o.add(r.d.scale(t));
                return true;
            }
        }

        return false;
    }
}
