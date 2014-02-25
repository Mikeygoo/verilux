package material;

import material.brdf.Lambertian;
import light.Light;
import object.World;
import tracer.ShadeRec;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class Matte extends Material {
    private Lambertian ambientBRDF, diffuseBRDF;

    public Matte() {
        this(1, 1, RGBColor.WHITE);
    }

    public Matte(float ka, float kd, RGBColor color) {
        ambientBRDF = new Lambertian(ka, color);
        diffuseBRDF = new Lambertian(kd, color);
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

    public RGBColor getColor() {
        return diffuseBRDF.getColor();
    }

    public void setColor(RGBColor color) {
        ambientBRDF.setColor(color);
        diffuseBRDF.setColor(color);
    }

    @Override
    public RGBColor shade(ShadeRec sr) {
        Vector3D wo = sr.ray.d.negate();
        RGBColor L = ambientBRDF.rho(sr, wo).colorProduct(sr.world.getAmbient().L(sr));

        for (Light l : sr.world.getLights()) {
            Vector3D wi = l.getDirection(sr);
            double ndotwi = sr.normal.dot(wi);

            if (ndotwi > 0.0) {
                boolean inShadow = false;

                if (l.castsShadows()) {
                    Ray shadowRay = new Ray(sr.hitPoint, wi);
                    inShadow = l.inShadow(shadowRay, sr);
                }

                if (!inShadow) {
                    L.addTo(diffuseBRDF.f(sr, wi, wo).colorProduct(l.L(sr)).scale((float) ndotwi));
                }
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
        Vector3D wo = sr.ray.d.negate();
        RGBColor L = ambientBRDF.rho(sr, wo).colorProduct(sr.world.getAmbient().L(sr));

        for (Light l : sr.world.getLights()) {
            Vector3D wi = l.getDirection(sr);
            double ndotwi = sr.normal.dot(wi);

            if (ndotwi > 0.0) {
                boolean inShadow = false;

                if (l.castsShadows()) {
                    Ray shadowRay = new Ray(sr.hitPoint, wi);
                    inShadow = l.inShadow(shadowRay, sr);
                }

                if (!inShadow) {
                    //L.addTo(diffuseBRDF.f(sr, wi, wo).colorProduct(l.L(sr)).colorProduct(l.G(sr)).scale(((float) ndotwi)/((float) l.pdf(sr))));
                }
            }
        }

        return L;
    }

    @Override
    public RGBColor pathShade(ShadeRec sr) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
