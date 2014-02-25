package tracer;

import object.World;
import util.RGBColor;
import util.Ray;

/**
 *
 * @author michael
 */
public class AreaLighting extends Tracer {
    public AreaLighting(World w) {
        super(w);
    }

    @Override
    public RGBColor traceRay(Ray r) {
        ShadeRec sr = (ShadeRec) world.hitObjects(r);//.clone(); //TODO: is this necessary?
        throw new RuntimeException("TODO: Implement");
    }
}
