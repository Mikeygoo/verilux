package tracer;

import object.World;
import util.Normal;
import util.Point3D;
import util.RGBColor;

/**
 *
 * @author michael
 */
public class ShadeRec {
    public boolean hitAnObject;
    public double hitDistance;
    public Point3D localHitPoint;
    public Normal normal;
    public RGBColor color;
    public World world;

    public ShadeRec() {
    }

    public ShadeRec(World w) {
        world = w;
        hitAnObject = false;
        hitDistance = Double.MAX_VALUE;
    }
}
