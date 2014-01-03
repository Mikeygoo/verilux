package object;

import tracer.ShadeRec;
import util.RGBColor;
import util.Ray;

/**
 *
 * @author michael
 */
public abstract class GeometricObject {
    private RGBColor color;

    public GeometricObject() {
        color = RGBColor.BLACK;
    }

    public GeometricObject(RGBColor color) {
        this.color = color;
    }

    public RGBColor getColor() {
        return color;
    }

    public void setColor(RGBColor color) {
        this.color = color;
    }
    
    public abstract boolean hit(Ray r, ShadeRec sr);
}
