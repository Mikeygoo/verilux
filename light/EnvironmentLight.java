package light;

import material.Material;
import object.GeometricObject;
import sampler.Sampler;
import tracer.ShadeRec;
import util.Point3D;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class EnvironmentLight extends Light {
    private static Vector3D ALMOST_Y_AXIS = new Vector3D(0.0034, 1, 0.0071);
    
    private Material material;
    private Sampler sampler;
    private Sampler.SamplerKey sk = new Sampler.SamplerKey();

    public EnvironmentLight() {
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        Vector3D w = new Vector3D(sr.normal);
        Vector3D v = ALMOST_Y_AXIS.cross(w);
        v.normalizeTo();
        Vector3D u = v.cross(w);
        
        Point3D sp = sampler.sampleUnitHemisphere(sk);
        
        Vector3D wi = new Vector3D(0);
        wi.addTo(u.scale(sp.x));
        wi.addTo(v.scale(sp.y));
        wi.addTo(w.scale(sp.z));
        
        sr.wi = wi;
        wi.normalizeTo();
        return wi;
    }

    @Override
    public RGBColor L(ShadeRec sr) {
        return material.getLe(sr);
    }

    @Override
    public boolean inShadow(Ray r, ShadeRec sr) {
        for (GeometricObject go : sr.world.getObjects()) {
            if (go.hitShadow(r) < Double.MAX_VALUE)
                return true;
        }

        return false;
    }

    @Override
    public float G(ShadeRec sr) {
        return 1f;
    }

    @Override
    public double pdf(ShadeRec sr) {
        return 1;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }
}
