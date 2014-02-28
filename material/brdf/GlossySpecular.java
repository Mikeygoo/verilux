package material.brdf;

import tracer.ShadeRec;
import util.RGBColor;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class GlossySpecular extends BRDF {
    private float ks;
    private float exp;
    private RGBColor cs;

    public GlossySpecular(float ks, RGBColor cs, float exp) {
        this.ks = ks;
        this.cs = cs;
        this.exp = exp;
    }

    @Override
    public RGBColor f(ShadeRec sr, Vector3D wi, Vector3D wo) {
        RGBColor L = new RGBColor(0);
        double ndotwi = sr.normal.dot(wi);
        Vector3D r = wi.negate().add(sr.normal.scale(2 * ndotwi));
        double rdotwo = r.dot(wo);

        if (rdotwo > 0.0)
            L = cs.scale((float)(ks * Math.pow(rdotwo, exp)));

        return L;
    }

    @Override
    public RGBColor sample_f(ShadeRec sr, Vector3D wi, Vector3D wo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RGBColor rho(ShadeRec sr, Vector3D wo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setColor(RGBColor cs) {
        this.cs = cs;
    }

    public void setIntensity(float ks) {
        this.ks = ks;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }
}
