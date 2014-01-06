package object;

import material.Material;
import tracer.ShadeRec;
import util.RGBColor;
import util.Ray;

/**
 *
 * @author michael
 */
public abstract class GeometricObject {
    private Material material;

    public GeometricObject() {
        //TODO: Default material.
    }

    public GeometricObject(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public abstract double hit(Ray r, ShadeRec sr);

    public abstract double hitShadow(Ray r);
}
