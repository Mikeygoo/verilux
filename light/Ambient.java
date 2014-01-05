package light;

import tracer.ShadeRec;
import util.RGBColor;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class Ambient extends Light {
    private float ls;
    private RGBColor color;

    public Ambient() {
        this(1.0f, RGBColor.WHITE, true);
    }

    public Ambient(float ls, RGBColor color) {
        this(ls, color, true);
    }

    public Ambient(float ls, RGBColor color, boolean shadows) {
        super(shadows);
        this.ls = ls;
        this.color = color;
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        return new Vector3D(0);
    }

    @Override
    public RGBColor L(ShadeRec sr) {
        return color.scale(ls);
    }

    public RGBColor getColor() {
        return color;
    }

    public void setColor(RGBColor color) {
        this.color = color;
    }

    public float getLs() {
        return ls;
    }

    public void setLs(float ls) {
        this.ls = ls;
    }
}
