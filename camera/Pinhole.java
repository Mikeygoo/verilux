package camera;

import object.ViewPlane;
import object.World;
import sampler.Sampler;
import util.Point2D;
import util.Point3D;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class Pinhole extends Camera {
    private double d; //view plane distance
    protected double zoom = 1; //zoom factor

    public Pinhole(Point3D eye, Point3D lookat, Vector3D up, double viewPlaneDistance) {
        super(eye, lookat, up);
        d = viewPlaneDistance;
    }

    public Pinhole(Point3D eye, Point3D lookat, double viewPlaneDistance) {
        this(eye, lookat, new Vector3D(0, 1, 0), viewPlaneDistance);
    }

    @Override
    public void renderSceneSlice(World world, Buffer img, int lr, int hr, int lc, int hc) {
        ViewPlane vp = world.getViewPlane();
        double s = vp.getS() / zoom;

        for (int r = lr; r < hr; r++) {
            for (int c = lc; c < hc; c++) {
                Sampler.SamplerKey sk = new Sampler.SamplerKey();
                RGBColor L = new RGBColor(0); //BLACK
                Ray ray = new Ray();
                ray.o = eye;

                for (int i = 0; i < vp.getNumSamples(); i++) {
                    Point2D pp = new Point2D(0), sp = vp.getSampler().sampleUnitSquare(sk);
                    pp.x = s * (c - 0.5 * vp.getHres() + sp.x);
                    pp.y = s * (r - 0.5 * vp.getVres() + sp.y);
                    ray.d = getRayDirection(pp);
                    L.addTo(world.getTracer().traceRay(ray));
                }


                L.scaleTo(1.0f / vp.getNumSamples());
                L.powTo(1.0f / vp.getGamma());
                L.clampNormally();

                img.drawColor(c, vp.getVres() - r - 1, L);
            }
        }
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    private Vector3D getRayDirection(Point2D pp) {
        //in this method, I'll calculate each one by parts.
        double dx = pp.x * u.x + pp.y * v.x - d * w.x;
        double dy = pp.x * u.y + pp.y * v.y - d * w.y;
        double dz = pp.x * u.z + pp.y * v.z - d * w.z;
        Vector3D dir = new Vector3D(dx, dy, dz);
        dir.normalizeTo();
        return dir;
    }
}
