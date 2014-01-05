package object;

import camera.Camera;
import camera.Pinhole;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import light.Ambient;
import light.Light;
import light.PointLight;
import material.Matte;
import sampler.PureRandom;
import tracer.RayCast;
import tracer.ShadeRec;
import tracer.Tracer;
import util.Normal;
import util.Point3D;
import util.RGBColor;
import util.Ray;

/**
 *
 * @author michael
 */
public class World {
    public static void main(String[] args) throws IOException {
        World w = new World();

        final BufferedImage bi = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        long millis = System.currentTimeMillis();
        w.renderScene(bi);
        System.out.println("It took " + (System.currentTimeMillis() - millis) + " milliseconds to render.");
        //ImageIO.write(bi, "png", new File("outfile.png")); //save image

        JFrame jf = new JFrame() {
            @Override
            public void paint(Graphics grphcs) {
                grphcs.drawImage(bi, 0, 0, getWidth(), getHeight(), this);
            }
        };

        jf.setSize(1000, 1000);
        jf.setVisible(true);
    }

    private ViewPlane vp;
    private RGBColor backgroundColor;
    private Light ambient;
    private Tracer tracer;
    private ArrayList<GeometricObject> objects;
    private ArrayList<Light> lights;
    private Camera camera;

    public World() {
        objects = new ArrayList<GeometricObject>();
        lights = new ArrayList<Light>();
        build();
    }

    public RGBColor getBackgroundColor() {
        return backgroundColor;
    }

    private void build() {
        /////////////////////////////////////////////////////////////////////////////////
        //                                    ***                                      //
        ////////////////////////////// CONSTRUCT THE BASICS /////////////////////////////
        vp = new ViewPlane(1000, 1000, 0.01f, 1, new PureRandom(25, 1));
        backgroundColor = RGBColor.BLACK;
        ambient = new Ambient();
        tracer = new RayCast(this);
        camera = new Pinhole(new Point3D(0, 2, 5), new Point3D(0, 0, 0), 1);

        /////////////////////////////////////////////////////////////////////////////////
        //                                    ***                                      //
        //////////////////////////////// ADD THE OBJECTS ////////////////////////////////
        Sphere sa = new Sphere(new Matte(0.01f, 0.7f, RGBColor.RED), new Point3D(0, 0, 0), 3);
        objects.add(sa);

        Sphere sb = new Sphere(new Matte(0.01f, 1f, RGBColor.GREEN), new Point3D(0, 2, 0.5), 2);
        objects.add(sb);

        Plane p = new Plane(new Matte(0.01f, 1f, RGBColor.BLUE), new Point3D(0, -10, 0), new Normal(0, 1, 0));
        objects.add(p);

        /////////////////////////////////////////////////////////////////////////////////
        //                                    ***                                      //
        //////////////////////////////// ADD THE LIGHTS /////////////////////////////////
        PointLight pl = new PointLight(new Point3D(10, 10, 10));
        pl.setLs(3);
        lights.add(pl);
    }

    public ShadeRec hitObjects(Ray r) {
        ShadeRec sr = new ShadeRec(this);
        double tmin = Double.MAX_VALUE;
        Normal normal = null;
        Point3D localHitPoint = null;

        for (GeometricObject g : objects) {
            double t;

            if ((t = g.hit(r, sr)) < tmin) {
                //System.out.println("Boom at "+sr.localHitPoint);
                sr.hitAnObject = true;
                tmin = t;
                sr.material = g.getMaterial();
                sr.hitPoint = r.o.add(r.d.scale(t));
                normal = sr.normal;
                localHitPoint = sr.localHitPoint;
            }
        }

        if (sr.hitAnObject) {
            sr.hitDistance = tmin;
            sr.normal = normal;
            sr.localHitPoint = localHitPoint;
        }

        return sr;
    }

    public void renderScene(BufferedImage img) {
        camera.renderScene(this, img);

        /* * * This is just Sampler testing code! * * */
        //<editor-fold defaultstate="collapsed" desc="testing code">
//        Graphics g = img.getGraphics();
//        g.setColor(Color.white);
//        g.fillRect(0, 0, 500, 500);
//
//        Sampler s = new MultiJittered(256, 100);
//        Sampler.SamplerKey sk = new Sampler.SamplerKey();
//
//        g.setColor(Color.black);
//
//        for (int i = 0; i < 256; i++) {
//            Point2D p = s.sampleUnitSquare(sk);
//            int x = (int) (p.x * 500);
//            int y = (int) (p.y * 500);
//            g.fillRect(x-2, y-2, 4, 4);
//        }
        //</editor-fold>
    }

    public ViewPlane getViewPlane() {
        return vp;
    }

    public Tracer getTracer() {
        return tracer;
    }

    public Light getAmbient() {
        return ambient;
    }

    public Iterable<Light> getLights() {
        return lights;
    }
}
