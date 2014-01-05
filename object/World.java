package object;

import camera.Camera;
import camera.Pinhole;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sampler.Jittered;
import sampler.MultiJittered;
import sampler.NRooks;
import sampler.PureRandom;
import sampler.Regular;
import sampler.Sampler;
import tracer.MultipleObjects;
import tracer.ShadeRec;
import tracer.Tracer;
import util.Normal;
import util.Point2D;
import util.Point3D;
import util.RGBColor;
import util.Ray;
import util.Vector3D;

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
    private Tracer tracer;
    private ArrayList<GeometricObject> objects;
    private Camera camera;

    public World() {
        objects = new ArrayList<GeometricObject>();
        build();
    }

    public RGBColor getBackgroundColor() {
        return backgroundColor;
    }

    private void build() {
        vp = new ViewPlane(1000, 1000, 0.01f, 1, new PureRandom(25, 1));
        tracer = new MultipleObjects(this);
        backgroundColor = RGBColor.BLACK;
        camera = new Pinhole(new Point3D(0, 2, 5), new Point3D(0), 2);

        Sphere sa = new Sphere(new RGBColor(1, 0, 0), new Point3D(0, 0, 0), 3);
        objects.add(sa);

        Sphere sb = new Sphere(new RGBColor(0, 1, 0.5f), new Point3D(0, 2, 0.5), 2);
        objects.add(sb);

        Plane p = new Plane(new RGBColor(0, 0.25f, 0.25f), new Point3D(0, 0, 0), new Normal(0, 1, 1));
        objects.add(p);
    }

    public ShadeRec hitBareBonesObjects(Ray r) {
        ShadeRec s = new ShadeRec(this);

        for (GeometricObject g : objects) {
            if (g.hit(r, s)) {
                s.color = g.getColor();
                s.hitAnObject = true;
            }
        }

        return s;
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
}
