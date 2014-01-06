package light;

import tracer.ShadeRec;
import util.Point3D;
import util.RGBColor;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class PointLight extends Light {
    private float ls;
    RGBColor color;
    private Point3D location;

    public PointLight(Point3D location) {
        this(1f, RGBColor.WHITE, location);
    }

    public PointLight(float ls, RGBColor color, Point3D location) {
        this(ls, color, location, true);
    }

    public PointLight(float ls, RGBColor color, Point3D location, boolean shadows) {
        super(shadows);
        this.ls = ls;
        this.color = color;
        this.location = location;
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        return location.subtract(sr.localHitPoint).normalize();
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

    public void setIntensity(float ls) {
        this.ls = ls;
    }

    public Point3D getLocation() {
        return location;
    }

    public void setLocation(Point3D location) {
        this.location = location;
    }
}
