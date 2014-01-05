package camera;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
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
    private double zoom = 1; //zoom factor
    
    public Pinhole(Point3D eye, Point3D lookat, Vector3D up, double viewPlaneDistance) {
        super(eye, lookat, up);
        d = viewPlaneDistance;
    }
    
    public Pinhole(Point3D eye, Point3D lookat, double viewPlaneDistance) {
        this(eye, lookat, new Vector3D(0, 1, 0), viewPlaneDistance);
    }

    @Override
    public void renderScene(World world, BufferedImage img) {
        ViewPlane vp = world.getViewPlane();
        WritableRaster raster = img.getRaster();
        double s = vp.getS() / zoom;

        //TODO: In the future, introduce some type of multithreaded handling of rendering.
        for (int r = 0; r < vp.getVres(); r++) {
            for (int c = 0; c < vp.getHres(); c++) {
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
                int[] ints = {(int)(255 * L.r),
                              (int)(255 * L.g),
                              (int)(255 * L.b)
                             };
                raster.setPixel(c, vp.getHres() - r - 1, ints);
            }
        }
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
