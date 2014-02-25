package object;

import light.GeometricLightSource;
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
    private static final double K_EPSILON = 0.00001;
    private Point3D p0;
    private Vector3D a, b;
    private Normal normal;
    private Sampler sampler;
    
    private double invArea;
    
    private Sampler.SamplerKey samplerKey = new Sampler.SamplerKey();

    public Rectangle(Material material, Point3D p0, Vector3D a, Vector3D b, Normal normal, Sampler sampler) {
        super(material);
        this.p0 = p0;
        this.a = a;
        this.b = b;
        this.normal = normal;
        normal.normalize();
        this.sampler = null;
        this.invArea = 1 / (a.length() * b.length());
    }
    
    public Rectangle(Material material, Point3D p0, Vector3D a, Vector3D b, Normal normal) {
        this(material, p0, a, b, normal, null);
    }

    @Override
    public double hit(Ray r, ShadeRec sr) {
	double t = p0.subtract(r.o).dot(normal)/r.d.dot(normal); 
	
	if (t <= K_EPSILON)
		return Double.POSITIVE_INFINITY;
			
	Point3D p = r.o.add(r.d.scale(t));
	Vector3D d = p.subtract(p0);
	
        double aLenSquared = a.lengthSquared(), bLenSquared = b.lengthSquared();
	double ddota = d.dot(a);
	
	if (ddota < 0.0 || ddota > aLenSquared)
		return Double.POSITIVE_INFINITY;
		
	double ddotb = d.dot(b);
	
	if (ddotb < 0.0 || ddotb > bLenSquared)
		return Double.POSITIVE_INFINITY;
        
	sr.normal = normal;
	sr.localHitPoint = p;
	
	return t;
    }

    @Override
    public double hitShadow(Ray r) {
        double t = p0.subtract(r.o).dot(normal)/r.d.dot(normal); 
	
	if (t <= K_EPSILON)
		return Double.POSITIVE_INFINITY;
			
	Point3D p = r.o.add(r.d.scale(t));
	Vector3D d = p.subtract(p0);
	
        double aLenSquared = a.lengthSquared(), bLenSquared = b.lengthSquared();
	double ddota = d.dot(a);
	
	if (ddota < 0.0 || ddota > aLenSquared)
		return Double.POSITIVE_INFINITY;
		
	double ddotb = d.dot(b);
	
	if (ddotb < 0.0 || ddotb > bLenSquared)
		return Double.POSITIVE_INFINITY;
	
	return t;
    }

    @Override
    public Sampler getSampler() {
        return sampler;
    }

    @Override
    public void setSampler(Sampler s) {
        sampler = s;
        samplerKey.setCount(0);
        samplerKey.setJump(0);
    }

    @Override
    public Point3D sample() {
        Point2D samplePoint = sampler.sampleUnitSquare(samplerKey);
        return p0.add(a.scale(samplePoint.x)).add(b.scale(samplePoint.y));
    }

    @Override
    public double pdf(ShadeRec sr) {
        return invArea;
    }

    @Override
    public Normal getNormal() {
        return normal;
    }
}
