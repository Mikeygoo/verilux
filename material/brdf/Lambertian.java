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

    public Lambertian(float kd, RGBColor cd, Sampler sampler) {
        super(sampler);
        this.kd = kd;
        this.cd = cd;
    }

    public RGBColor getColor() {
        return cd;
    }

    public void setColor(RGBColor cd) {
        this.cd = cd;
    }

    public float getIntensity() {
        return kd;
    }

    public void setIntensity(float kd) {
        this.kd = kd;
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
}
