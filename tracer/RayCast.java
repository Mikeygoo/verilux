package tracer;

import object.World;
import util.RGBColor;
import util.Ray;

/**
 *
 * @author michael
 */
public class RayCast extends Tracer {
    public RayCast(World w) {
        super(w);
    }

    @Override
    public RGBColor traceRay(Ray r) {
        ShadeRec sr = (ShadeRec) world.hitObjects(r);//.clone(); //TODO: is this necessary?

        if (sr.hitAnObject) {
            sr.ray = r;
            return sr.material.shade(sr);
        } else
            return world.getBackgroundColor();
    }
}
