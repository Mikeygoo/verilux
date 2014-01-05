package camera;

import java.awt.image.BufferedImage;
import object.World;
import util.Point3D;
import util.Vector3D;

/**
 *
 * @author michael
 */
public abstract class Camera {
    protected Point3D eye, lookat;
    protected Vector3D up;
    protected Vector3D u, v, w;
    float exposure = 1;

    public Camera(Point3D eye, Point3D lookat, Vector3D up) {
        this.eye = eye;
        this.lookat = lookat;
        this.up = up;
        
        computeUVW();
    }

    private void computeUVW() {
        w = eye.subtract(lookat);
        w.normalizeTo();
        u = up.cross(w);
        u.normalizeTo();
        v = w.cross(u);
    }
    
    public abstract void renderScene(World world, BufferedImage img);

    public Point3D getEye() {
        return eye;
    }

    public Point3D getLookat() {
        return lookat;
    }

    public Vector3D getUp() {
        return up;
    }

    public Vector3D getU() {
        return u;
    }

    public Vector3D getV() {
        return v;
    }

    public Vector3D getW() {
        return w;
    }

    public float getExposure() {
        return exposure;
    }

    public void setExposure(float exposure) {
        this.exposure = exposure;
    }
}
