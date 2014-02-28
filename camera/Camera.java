package camera;

import constant.Constants;
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
    protected float exposure = 1;
    protected double totalRoll = 0.0;

    public Camera(Point3D eye, Point3D lookat, Vector3D up) {
        this.eye = eye;
        this.lookat = lookat;
        this.up = up;

        computeUVW();
    }

    private void computeUVW() {
        //w = lookat.subtract(eye);
        w = eye.subtract(lookat);
        w.normalizeTo();

        if (Math.abs(w.dot(up)) > 0.9)
            up = new Vector3D(1, 0, 0);

        u = up.cross(w);
        u.normalizeTo();
        v = w.cross(u);
    }

    public void renderScene(final World world, final Buffer img) {
        final ViewPlane vp = world.getViewPlane();
        ExecutorCompletionService workers = new ExecutorCompletionService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        Queue<Future<Object>> futures = new LinkedList<Future<Object>>();

        for (int i = 0; i < Constants.slicesHorizontal; i++) {
            for (int j = 0; j < Constants.slicesVertical; j++) {
                final int fi = i, fj = j;
                Future<Object> f = workers.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            renderSceneSlice(world, img, (int)(vp.getVres() * 1.0 / Constants.slicesVertical * fj), (int)(vp.getVres() * 1.0 / Constants.slicesVertical * (fj + 1)),
                                             (int)(vp.getHres() * 1.0 / Constants.slicesHorizontal * fi), (int)(vp.getHres() * 1.0 / Constants.slicesHorizontal * (fi + 1)));
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
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

    public abstract void renderSceneSlice(World world, Buffer img, int lr, int hr, int lc, int hc);

    public void setExposure(float exposure) {
        this.exposure = exposure;
    }

    public void roll(double degrees) {
        if (degrees == 0)
            return;

        totalRoll += degrees;
        double u0 = w.x, v0 = w.y, w0 = w.z;
        double x = up.x, y = up.y, z = up.z;
        double t = degrees * Constants.PI_OVER_180;
        Vector3D upPrime = new Vector3D(u0 * (u0 * x + v0 * y + w0 * z) * (1 - Math.cos(t)) + x * Math.cos(t) + (v0 * z - w0 * y) * Math.sin(t),
                                        v0 * (u0 * x + v0 * y + w0 * z) * (1 - Math.cos(t)) + y * Math.cos(t) + (w0 * x - u0 * z) * Math.sin(t),
                                        w0 * (u0 * x + v0 * y + w0 * z) * (1 - Math.cos(t)) + z * Math.cos(t) + (u0 * y - v0 * x) * Math.sin(t));
        up = upPrime;
        computeUVW();
    }

    public void unroll() {
        roll(-totalRoll);
    }

    //TODO: yaw, pitch
}
