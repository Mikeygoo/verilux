package light;

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
public class AmbientOccluder extends Light {
    Sampler.SamplerKey sk = new Sampler.SamplerKey();
    private Sampler s;

    private float minAmount = 0.005f;
    private float ls;
    RGBColor color;

    public AmbientOccluder() {
        super(true);
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        return new Vector3D(0);
    }

    @Override
    public RGBColor L(ShadeRec sr) {
        Vector3D w = new Vector3D(sr.normal);
        Vector3D v = w.cross(new Vector3D(0.0002, 1.0, 0.0004));
        v.normalizeTo();
        Vector3D u = v.cross(w);
        u.normalizeTo();

        Point3D sp = s.sampleUnitHemisphere(sk);
        Vector3D dir = new Vector3D(0);
        dir.addTo(u.scale(sp.x));
        dir.addTo(v.scale(sp.y));
        dir.addTo(w.scale(sp.z));

        Ray shadowRay = new Ray(sr.hitPoint, dir);

        if (inShadow(shadowRay, sr)) {
            return color.scale(ls * minAmount);
        } else {
            return color.scale(ls);
        }
    }

    @Override
    public boolean inShadow(Ray r, ShadeRec sr) {
        for (GeometricObject go : sr.world.getObjects()) {
            if (go.hitShadow(r) < Double.MAX_VALUE)
                return true;
        }

        return false;
    }

    public void setColor(RGBColor color) {
        this.color = color;
    }

    public void setRadiance(float ls) {
        this.ls = ls;
    }

    public void setMinAmount(float minAmount) {
        this.minAmount = minAmount;
    }

    public void setSampler(Sampler s) {
        this.s = s;
    }
}
