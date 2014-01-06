package material;

import light.Light;
import material.brdf.GlossySpecular;
import material.brdf.Lambertian;
import tracer.ShadeRec;
import util.RGBColor;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class Phong extends Material {
    private Lambertian ambientBRDF, diffuseBRDF;
    private GlossySpecular specularBRDF;

    public Phong() {
        this(1, 1, 1, 1, RGBColor.WHITE);
    }
    
    public Phong(float ka, float kd, float ks, float exp, RGBColor color) {
        ambientBRDF = new Lambertian(ka, color);
        diffuseBRDF = new Lambertian(kd, color);
        specularBRDF = new GlossySpecular(ks, color, exp);
    }

    public float getKa() {
        return ambientBRDF.getIntensity();
    }

    public void setKa(float f) {
        ambientBRDF.setIntensity(f);
    }

    public float getKd() {
        return diffuseBRDF.getIntensity();
    }

    public void setKd(float f) {
        diffuseBRDF.setIntensity(f);
    }
    
    public float getKs() {
        return specularBRDF.getIntensity();
    }
    
    public void setKs(float f) {
        specularBRDF.setIntensity(f);
    }
    
    public float getExp() {
        return specularBRDF.getExp();
    }
    
    public void setExp(float f) {
        specularBRDF.setExp(f);
    }

    public RGBColor getColor() {
        return diffuseBRDF.getColor();
    }

    public void setColor(RGBColor color) {
        ambientBRDF.setColor(color);
        diffuseBRDF.setColor(color);
        specularBRDF.setColor(color);
    }
    
    @Override
    public RGBColor shade(ShadeRec sr) {
        Vector3D wo = sr.ray.d.negate();
        RGBColor L = new RGBColor(0);
        L.addTo(ambientBRDF.rho(sr, wo).colorProduct(sr.world.getAmbient().L(sr)));

        for (Light l : sr.world.getLights()) {
            Vector3D wi = l.getDirection(sr);
            double ndotwi = sr.normal.dot(wi);

            if (ndotwi > 0.0) {
                RGBColor total = diffuseBRDF.f(sr, wi, wo).add(specularBRDF.f(sr, wi, wo));
                L.addTo(total.colorProduct(l.L(sr)).scale((float) ndotwi)); //diffuse part
            }
        }

        return L;
    }

    @Override
    public RGBColor whittedShade(ShadeRec sr) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public RGBColor areaLightShade(ShadeRec sr) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public RGBColor pathShade(ShadeRec sr) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
