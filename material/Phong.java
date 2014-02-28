package material;

import light.Light;
import material.brdf.GlossySpecular;
import material.brdf.Lambertian;
import tracer.ShadeRec;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class Phong extends Material {
    private Lambertian ambientBRDF, diffuseBRDF;
    private GlossySpecular specularBRDF;

    public Phong() {
        ambientBRDF = new Lambertian(1, RGBColor.WHITE);
        diffuseBRDF = new Lambertian(1, RGBColor.WHITE);
        specularBRDF = new GlossySpecular(1, RGBColor.WHITE, 1);
    }

    public void setKa(float f) {
        ambientBRDF.setIntensity(f);
    }

    public void setKd(float f) {
        diffuseBRDF.setIntensity(f);
    }

    public void setKs(float f) {
        specularBRDF.setIntensity(f);
    }

    public void setExp(float f) {
        specularBRDF.setExp(f);
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
                boolean inShadow = false;

                if (l.castsShadows()) {
                    Ray shadowRay = new Ray(sr.hitPoint, wi);
                    inShadow = l.inShadow(shadowRay, sr);
                }

                if (!inShadow) {
                    RGBColor total = diffuseBRDF.f(sr, wi, wo).add(specularBRDF.f(sr, wi, wo));
                    L.addTo(total.colorProduct(l.L(sr)).scale((float) ndotwi)); //diffuse part
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
        RGBColor L = new RGBColor(0);
        L.addTo(ambientBRDF.rho(sr, wo).colorProduct(sr.world.getAmbient().L(sr)));

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
                    RGBColor total = diffuseBRDF.f(sr, wi, wo).add(specularBRDF.f(sr, wi, wo));
                    L.addTo(total.colorProduct(l.L(sr)).scale(l.G(sr)).scale(((float) ndotwi)/((float) l.pdf(sr)))); //diffuse part
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
