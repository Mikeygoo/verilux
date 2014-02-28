package material;

import tracer.ShadeRec;
import util.RGBColor;

/**
 *
 * @author michael
 */
public class Emissive extends Material {
    private float ls;
    private RGBColor ce;

    public Emissive() {
    }

    public void setLs(float ls) {
        this.ls = ls;
    }

    public void setCe(RGBColor ce) {
        this.ce = ce;
    }
    
    @Override
    public RGBColor getLe(ShadeRec sr) {
        return ce.scale(ls);
    }

    @Override
    public RGBColor shade(ShadeRec sr) {
        return areaLightShade(sr);
    }

    @Override
    public RGBColor whittedShade(ShadeRec sr) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public RGBColor areaLightShade(ShadeRec sr) {
        if (sr.normal.negate().dot(sr.ray.d) > 0.0)
            return getLe(sr);
        else
            return new RGBColor(0);
    }

    @Override
    public RGBColor pathShade(ShadeRec sr) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
