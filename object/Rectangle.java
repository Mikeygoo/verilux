package object;

import material.Material;
import sampler.PureRandom;
import sampler.Sampler;
import tracer.ShadeRec;
import util.Normal;
import util.Point2D;
import util.Point3D;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class Rectangle extends GeometricObject implements GeometricLightSource {
    private Point3D p0;
    private Vector3D a, b;
    private Normal normal;
    private Sampler sampler;
    
    private Sampler.SamplerKey samplerKey = new Sampler.SamplerKey();

    public Rectangle(Material material, Point3D p0, Vector3D a, Vector3D b, Normal normal, Sampler sampler) {
        super(material);
        this.p0 = p0;
        this.a = a;
        this.b = b;
        this.normal = normal;
        this.sampler = null;
    }
    
    public Rectangle(Material material, Point3D p0, Vector3D a, Vector3D b, Normal normal) {
        super(material);
        this.p0 = p0;
        this.a = a;
        this.b = b;
        this.normal = normal;
        this.sampler = new PureRandom(1, 87);
    }

    @Override
    public double hit(Ray r, ShadeRec sr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double hitShadow(Ray r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sampler getSampler() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSampler(Sampler s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point3D sample() {
        Point2D samplePoint = sampler.sampleUnitSquare(samplerKey);
        return p0.add(a.scale(samplePoint.x)).add(b.scale(samplePoint.y));
    }

    @Override
    public double pdf(ShadeRec sr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Normal getNormal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
