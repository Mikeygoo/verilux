package brdf;

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
        this(new MultiJittered(25, 83));
    }

    public BRDF(Sampler sampler) {
        this.sampler = sampler;
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
