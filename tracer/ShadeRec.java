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
    public Ray ray;                 // the ray
    public Vector3D direction;      // the direction ray is fired
    public Normal normal;           // the hit normal
    public Material material;       // the normal of the hit location
    public World world;             // the world
    public int depth;               // the depth into a transparent object.
    
    /* members for Area Lighting. */
    /* included in this class to prevent interleaved synchronization error */
    public Point3D samplePoint;     // sample point on emissive material
    public Normal lightNormal;      // normal at sample point
    public Vector3D wi;             //unit vector from hit point to sample point

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
        //neue.depth = depth;
        return neue;
    }
}
