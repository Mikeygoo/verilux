package camera;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import object.ViewPlane;
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
    
    public void renderScene(final World world, final BufferedImage img) {
        final ViewPlane vp = world.getViewPlane();
        final int slicesVertical = 2, slicesHorizontal = 4;
        ExecutorCompletionService workers = new ExecutorCompletionService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        
        Queue<Future<Object>> futures = new LinkedList<Future<Object>>();
        for (int i = 0; i < slicesHorizontal; i++) {
            for (int j = 0; j < slicesVertical; j++) {
                final int fi = i, fj = j;
                Future<Object> f = workers.submit(new Runnable() {
                    @Override
                    public void run() {
                        renderSceneSlice(world, img, (int) (vp.getVres() * 1.0 / slicesVertical * fj), (int) (vp.getVres() * 1.0 / slicesVertical * (fj + 1)),
                                                     (int) (vp.getHres() * 1.0 / slicesHorizontal * fi), (int) (vp.getHres() * 1.0 / slicesHorizontal * (fi + 1)));
                    }
                }, new Object());
                futures.offer(f);
            }
        }
        
        while (!futures.isEmpty()) {
            if (futures.peek().isDone())
                futures.poll();
        }
    }
    
    public abstract void renderSceneSlice(World world, BufferedImage img, int lr, int hr, int lc, int hc);

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
