package material.brdf;

import constant.Constants;
import sampler.Sampler;
import tracer.ShadeRec;
import util.RGBColor;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class Lambertian extends BRDF {
    private float kd;
    private RGBColor cd;

    public Lambertian(float kd, RGBColor cd) {
        this.kd = kd;
        this.cd = cd;
    }

    @Override
    public RGBColor f(ShadeRec sr, Vector3D wi, Vector3D wo) {
        return cd.scale((float)(kd * Constants.INV_PI));
    }

    @Override
    public RGBColor rho(ShadeRec sr, Vector3D wo) {
        return cd.scale(kd);
    }

    @Override
    public RGBColor sample_f(ShadeRec sr, Vector3D wi, Vector3D wo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setColor(RGBColor cd) {
        this.cd = cd;
    }

    public void setIntensity(float kd) {
        this.kd = kd;
    }
}
