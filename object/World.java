package object;

import camera.Buffer;
import camera.BufferedImageWrappingBuffer;
import camera.Camera;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import light.Light;
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

        final BufferedImage bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);

        final JFrame jf = new JFrame() {
            @Override
            public void paint(Graphics grphcs) {
                grphcs.drawImage(bi, 0, 0, getWidth(), getHeight(), this);
            }
        };

        jf.setSize(1000, 1000);
        jf.setVisible(true);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    jf.repaint();
                }
            }
        } .start();

        long millis = System.currentTimeMillis();
        System.out.println("Beginning render of scene.");
        w.renderScene(new BufferedImageWrappingBuffer(bi));
        System.out.println("It took " + (System.currentTimeMillis() - millis) + " milliseconds to render.");

        ImageIO.write(bi, "png", new File("outfile.png")); //save image
        System.out.println("Image saved as outfile.png");
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
        new XMLBuilder(this).build();
    }
    
    public ShadeRec hitObjects(Ray r) {
        ShadeRec sr = new ShadeRec(this);
        double tmin = Double.MAX_VALUE;
        Normal normal = null;
        Point3D localHitPoint = null;

        for (GeometricObject g : objects) {
            double t;

            if ((t = g.hit(r, sr)) < tmin) {
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

    public void renderScene(Buffer img) {
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

    public void setAmbient(Light ambient) {
        this.ambient = ambient;
    }

    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setBackgroundColor(RGBColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setViewPlane(ViewPlane vp) {
        this.vp = vp;
    }

    public Iterable<Light> getLights() {
        return lights;
    }
    
    public void addLight(Light l) {
        lights.add(l);
    }

    public Iterable<GeometricObject> getObjects() {
        return objects;
    }
    
    public void addObject(GeometricObject g) {
        objects.add(g);
    }
}
