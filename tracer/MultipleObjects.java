package tracer;

import object.World;
import util.RGBColor;
import util.Ray;

/**
 *
 * @author michael
 */
public class MultipleObjects extends Tracer {
    public MultipleObjects(World w) {
        super(w);
    }

    @Override
    public RGBColor traceRay(Ray r) {
        ShadeRec s = world.hitBareBonesObjects(r);

        if (s.hitAnObject)
            return s.color;
        else
            return world.getBackgroundColor();
    }
}
