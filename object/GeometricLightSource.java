package object;

import sampler.Sampler;
import tracer.ShadeRec;
import util.Normal;
import util.Point3D;

/**
 *
 * @author michael
 */
public interface GeometricLightSource {
    Sampler getSampler();
    void setSampler(Sampler s);
    
    Point3D sample();
    double pdf(ShadeRec sr);
    Normal getNormal();
}
