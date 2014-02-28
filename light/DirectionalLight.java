package light;

import object.GeometricObject;
import tracer.ShadeRec;
import util.Point3D;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class DirectionalLight extends Light {
    private float ls;
    RGBColor color;
    Vector3D direction;

    public DirectionalLight() {
        super(true);
    }

    @Override
    public RGBColor L(ShadeRec sr) {
        return color.scale(ls);
    }

    @Override
    public boolean inShadow(Ray r, ShadeRec sr) {
        for (GeometricObject go : sr.world.getObjects()) {
            if (go.hitShadow(r) < Double.MAX_VALUE)
                return true;
        }

        return false;
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        return direction;
    }

    public void setColor(RGBColor color) {
        this.color = color;
    }

    public void setRadiance(float ls) {
        this.ls = ls;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }
}
