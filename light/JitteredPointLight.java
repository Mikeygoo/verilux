package light;

import constant.Maths;
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
public class JitteredPointLight extends Light {
    private float lr;
    private float ls;
    private RGBColor color;
    private Point3D location;

    public JitteredPointLight() {
        super(true);
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        Point3D fakeLocation = new Point3D(0);
        fakeLocation.x = location.x + lr * (2.0 * Maths.randFloat() - 1.0);
        fakeLocation.y = location.y + lr * (2.0 * Maths.randFloat() - 1.0);
        fakeLocation.z = location.z + lr * (2.0 * Maths.randFloat() - 1.0);
        return fakeLocation.subtract(sr.localHitPoint).normalize();
    }

    @Override
    public RGBColor L(ShadeRec sr) {
        return color.scale(ls);
    }

    @Override
    public boolean inShadow(Ray r, ShadeRec sr) {
        double d = location.distance(r.o);

        for (GeometricObject go : sr.world.getObjects()) {
            if (go.hitShadow(r) < d)
                return true;
        }

        return false;
    }

    public void setColor(RGBColor color) {
        this.color = color;
    }

    public void setRadiance(float ls) {
        this.ls = ls;
    }

    public void setLocation(Point3D location) {
        this.location = location;
    }

    public void setLightRadius(float f) {
        lr = f;
    }

    public void setRadius(float lr) {
        this.lr = lr;
    }
}
