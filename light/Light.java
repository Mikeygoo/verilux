package light;

import tracer.ShadeRec;
import util.RGBColor;
import util.Vector3D;

/**
 *
 * @author michael
 */
public abstract class Light {
    protected boolean shadows;

    public Light() {
        this(false);
    }

    public Light(boolean shadows) {
        this.shadows = shadows;
    }

    public abstract Vector3D getDirection(ShadeRec sr);

    public abstract RGBColor L(ShadeRec sr);
}
