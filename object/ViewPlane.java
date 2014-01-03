package object;

import sampler.Sampler;

/**
 *
 * @author michael
 */
public class ViewPlane {
    private int hres, vres;
    private double s;
    private float gamma;
    private int numSamples;
    private Sampler sampler;

    public ViewPlane(int hr, int vr, float sz, float g, Sampler sampr) {
        hres = hr;
        vres = vr;
        s = sz;
        gamma = g;
        numSamples = sampr.getNumSamples();
        sampler = sampr;
    }

    public void setHres(int hres) {
        this.hres = hres;
    }

    public int getHres() {
        return hres;
    }

    public int getVres() {
        return vres;
    }

    public void setVres(int vres) {
        this.vres = vres;
    }

    public void setS(double s) {
        this.s = s;
    }

    public double getS() {
        return s;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    public float getGamma() {
        return gamma;
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
        this.numSamples = sampler.getNumSamples();
    }

    public Sampler getSampler() {
        return sampler;
    }

    public int getNumSamples() {
        return numSamples;
    }
}