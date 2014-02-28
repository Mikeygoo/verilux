package material;

import material.brdf.Lambertian;
import light.Light;
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
        ambientBRDF = new Lambertian(1, RGBColor.WHITE);
        diffuseBRDF = new Lambertian(1, RGBColor.WHITE);
    }

    public void setKa(float f) {
        ambientBRDF.setIntensity(f);
    }

    public void setKd(float f) {
        diffuseBRDF.setIntensity(f);
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
                    L.addTo(diffuseBRDF.f(sr, wi, wo).colorProduct(l.L(sr)).scale(l.G(sr)).scale(((float) ndotwi) / ((float) l.pdf(sr))));
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
