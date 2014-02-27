package tracer;

import object.World;
import util.RGBColor;
import util.Ray;

/**
 *
 * @author michael
 */
public class White extends Tracer {
    public White(World w) {
        super(w);
    }

    @Override
    public RGBColor traceRay(Ray r) {
        ShadeRec sr = (ShadeRec) world.hitObjects(r);//.clone(); //TODO: is this necessary?

        if (sr.hitAnObject) {
            return RGBColor.WHITE;
        } else
            return world.getBackgroundColor();
    }
}
