package light;

import material.Material;
import object.GeometricObject;
import tracer.ShadeRec;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class AreaLight extends Light {
    private GeometricLightSource object;
    private Material material;

    public AreaLight() {
    }
    
    public AreaLight (GeometricLightSource object) {
        this(object, object.getMaterial(), true);
    }

    public AreaLight(GeometricLightSource object, Material material) {
        this(object, material, true);
    }

    public AreaLight(GeometricLightSource object, Material material, boolean shadows) {
        super(shadows);
        this.object = object;
        this.material = material;
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        sr.samplePoint = object.sample();
        sr.lightNormal = object.getNormal(sr.samplePoint);
        sr.wi = sr.samplePoint.subtract(sr.hitPoint);
        sr.wi.normalize();
        
        return sr.wi;
    }

    @Override
    public RGBColor L(ShadeRec sr) {
        double ndotd = sr.lightNormal.negate().dot(sr.wi);
        
        if (ndotd > 0.0)
            return material.getLe(sr);
        return new RGBColor(0);
    }

    @Override
    public boolean inShadow(Ray r, ShadeRec sr) {
        double d = sr.samplePoint.subtract(r.o).dot(r.d);

        for (GeometricObject go : sr.world.getObjects()) {
            if (go != object && go.hitShadow(r) < d)
                return true;
        }

        return false;
    }

    @Override
    public float G(ShadeRec sr) {
        double ndotd = sr.lightNormal.negate().dot(sr.wi);
        double d2 = sr.samplePoint.distanceSquared(sr.hitPoint);
        
        return (float) (ndotd / d2);
    }

    @Override
    public double pdf(ShadeRec sr) {
        return object.pdf(sr);
    }

    public GeometricLightSource getObject() {
        return object;
    }

    public void setObject(GeometricObject obj) {
        if (!(obj instanceof GeometricLightSource))
            throw new RuntimeException("Not a valid Geometric Light Source.");
        object = (GeometricLightSource) obj;
        material = obj.getMaterial();
    }
}
