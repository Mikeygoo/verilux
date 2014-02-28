package material.brdf;

import constant.Constants;
import sampler.MultiJittered;
import sampler.Sampler;
import tracer.ShadeRec;
import util.RGBColor;
import util.Vector3D;

/**
 *
 * @author michael
 */
public abstract class BRDF {
    private Sampler sampler;

    public BRDF() {
        sampler = new MultiJittered(Constants.samples, Constants.sets); //todo: add to XML interpreter for materials!
    }

    public Sampler getSampler() {
        return sampler;
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }

    public abstract RGBColor f(ShadeRec sr, Vector3D wi, Vector3D wo);

    public abstract RGBColor sample_f(ShadeRec sr, Vector3D wi, Vector3D wo);

    public abstract RGBColor rho(ShadeRec sr, Vector3D wo);
}
