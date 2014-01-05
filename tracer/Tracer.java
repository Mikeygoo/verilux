package tracer;

import object.World;
import util.RGBColor;
import util.Ray;

/**
 *
 * @author michael
 */
public abstract class Tracer {
    protected World world;

    public Tracer() {

    }

    public Tracer(World world) {
        this.world = world;
    }

    public abstract RGBColor traceRay(Ray r);
    //public abstract RGBColor traceRay(Ray r, int depth);
    //public abstract RGBColor traceRay(Ray r, AtomicDouble tReference, int depth); //TODO: Program AtomicDouble wrapping AtomicLong (double bits to long etc.)
}
