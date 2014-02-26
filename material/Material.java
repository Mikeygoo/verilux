package material;

import tracer.ShadeRec;
import util.RGBColor;

/**
 *
 * @author michael
 */
public abstract class Material {
    public abstract RGBColor shade(ShadeRec sr);
    public abstract RGBColor whittedShade(ShadeRec sr);
    public abstract RGBColor areaLightShade(ShadeRec sr);
    public abstract RGBColor pathShade(ShadeRec sr);
    
    public RGBColor getLe(ShadeRec sr) {
        return new RGBColor(0);
    }
}
