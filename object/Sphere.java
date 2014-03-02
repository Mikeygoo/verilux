package object;

import constant.Constants;
import light.GeometricLightSource;
import sampler.Sampler;
import tracer.ShadeRec;
import util.Normal;
import util.Point3D;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class Sphere extends GeometricObject implements GeometricLightSource {
    private static final double K_EPSILON = 0.00001;
    private Sampler sampler;
    private Point3D center;
    private double radius;
    private double invArea;
    
    private Sampler.SamplerKey samplerKey = new Sampler.SamplerKey();

    public Sphere() {
    }

    public void setCenter(Point3D center) {
        this.center = center;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.invArea = Constants.PI_OVER_FOUR * (1 / radius / radius);
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

    @Override
    public double hitShadow(Ray r) {
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
                return t;
            }

            t = (-b + e) / denom;

            if (t > K_EPSILON) {
                return t;
            }
        }

        return Double.POSITIVE_INFINITY;
    }

    @Override
    public Sampler getSampler() {
        return sampler;
    }

    @Override
    public void setSampler(Sampler s) {
        sampler = s;
    }

    @Override
    public Point3D sample() {
        Point3D h = sampler.sampleUnitHemisphere(samplerKey);
        return new Point3D(h.x + center.x, -h.y + center.y, h.z + center.z);
    }

    @Override
    public double pdf(ShadeRec sr) {
        return invArea;
    }

    @Override
    public Normal getNormal(Point3D p) {
        Normal n = new Normal(p.subtract(center));
        n.normalizeTo();
        return n;
    }
}
