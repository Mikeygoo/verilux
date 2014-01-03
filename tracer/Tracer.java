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
}
