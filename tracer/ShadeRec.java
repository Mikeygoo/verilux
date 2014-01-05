package tracer;

import material.Material;
import object.World;
import util.Normal;
import util.Point3D;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class ShadeRec {
    public boolean hitAnObject;
    public double hitDistance;
    public Point3D localHitPoint;
    public Point3D hitPoint;
    public Ray ray;
    public Vector3D direction;
    public Normal normal;
    public Material material;
    public World world;
    public int depth;

    public ShadeRec(World w) {
        world = w;
        hitAnObject = false;
        hitDistance = Double.MAX_VALUE;
    }

    @Override
    public ShadeRec clone() {
        ShadeRec neue = new ShadeRec(world);
        neue.hitAnObject = hitAnObject;
        neue.hitDistance = hitDistance;
        neue.localHitPoint = localHitPoint;
        neue.hitPoint = hitPoint;
        neue.ray = ray;
        neue.direction = direction;
        neue.normal = normal;
        neue.material = material;
        neue.depth = depth;
        return neue;
    }
}
