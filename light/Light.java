package light;

import tracer.ShadeRec;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public abstract class Light {
    protected boolean shadows;

    public Light() {
        this(true);
    }

    public Light(boolean shadows) {
        this.shadows = shadows;
    }

    public abstract Vector3D getDirection(ShadeRec sr);

    public abstract RGBColor L(ShadeRec sr);

    public abstract boolean inShadow(Ray r, ShadeRec sr);
    
    public float G(ShadeRec sr) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    public double pdf(ShadeRec sr) {
        return 0;
    }

    public boolean castsShadows() {
        return shadows;
    }

    public void setShadows(boolean shadows) {
        this.shadows = shadows;
    }
}
