package object;

import camera.Buffer;
import camera.BufferedImageWrappingBuffer;
import camera.Camera;
import camera.IndependentBuffer;
import camera.Pinhole;
import constant.Constants;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import light.Ambient;
import light.AmbientOccluder;
import light.AreaLight;
import light.JitteredPointLight;
import light.Light;
import material.Emissive;
import material.Matte;
import material.Phong;
import sampler.MultiJittered;
import tracer.AreaLighting;
import tracer.RayCast;
import tracer.ShadeRec;
import tracer.Tracer;
import util.Normal;
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

        final BufferedImage bi = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);

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

    public static void mainOld(String[] args) {
        World w = new World();
        IndependentBuffer buffer = new IndependentBuffer(Constants.WIDTH, Constants.HEIGHT);

        System.out.println("Beginning render.");
        long millis = System.currentTimeMillis();
        w.renderScene(buffer);
        System.out.println("Done!\nIt took " + (System.currentTimeMillis() - millis) + " milliseconds to render.");

        System.out.println("Beginning write.");
        buffer.writeToFile("image.ppm");
        System.out.println("Done writing.");
        System.out.flush();
        System.exit(0);
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
        this.vp = new ViewPlane(Constants.WIDTH, Constants.HEIGHT, 1, 1f, new MultiJittered(Constants.SAMPLES, 83));
        this.backgroundColor = new RGBColor(0.8f);
        //this.tracer = new RayCast(this);
        this.tracer = new AreaLighting(this);

        //AmbientOccluder ambient = new AmbientOccluder();
        Ambient ambient = new Ambient();
        ambient.setRadiance(.5f);
        this.ambient = ambient; //set it to world.

        Pinhole pinhole = new Pinhole(new Point3D(-500, 200, 50), new Point3D(-5, 0, 0), 850.0f);
        pinhole.setZoom(Constants.ZOOM_FACTOR);
        //pinhole.roll(20);
        this.camera = pinhole;

        /////////////////////////////////////////////////////////////////////////////////
        //                                    ***                                      //
        //////////////////////////////// ADD THE OBJECTS ////////////////////////////////
        Matte matte_2 = new Matte();
        matte_2.setKa(0.15f);
        matte_2.setKd(0.85f);
        matte_2.setColor(new RGBColor(0.75f, 0.75f, 0.00f));
        Sphere sphere_1 = new Sphere(matte_2, new Point3D(10, -5, 0), 27);
        this.objects.add(sphere_1);
        
        Emissive emissive_2 = new Emissive(0.005f, new RGBColor(1));
        //Sphere sphere_2 = new Sphere(matte_2, new Point3D(-25, 10, -35), 27);
        Vector3D a = new Vector3D(40, 0, 0), b = new Vector3D(0, 0, 40);
        Rectangle rectangle_2 = new Rectangle(emissive_2, new Point3D(-25, 50, -35), a, b, new Normal(0, -1, 0));
        rectangle_2.setSampler(new MultiJittered(Constants.SAMPLES, 83));
        this.objects.add(rectangle_2);

        Matte matte_3 = new Matte();
        matte_3.setKa(0.05f);
        matte_3.setKd(0.15f);
        matte_3.setColor(new RGBColor(0.5f, 0.0f, 0.5f));
        Plane plane_3 = new Plane(matte_3, new Point3D(0, -32, 0), new Normal(0, 1, 0));
        this.objects.add(plane_3);

        /////////////////////////////////////////////////////////////////////////////////
        //                                    ***                                      //
        //////////////////////////////// ADD THE LIGHTS /////////////////////////////////

//        JitteredPointLight pointLight = new JitteredPointLight(new Point3D(100, 50, 150));
//        pointLight.setColor(new RGBColor(0.9f, 0.15f, 0.15f));
//        pointLight.setLightRadius(5.0f);
//        pointLight.setRadiance(20f);
//        //this.lights.add(pointLight);
//
//        JitteredPointLight pointLight_2 = new JitteredPointLight(new Point3D(0, 500, 0));
//        pointLight_2.setColor(new RGBColor(1f, 1f, 1f));
//        pointLight_2.setLightRadius(50.0f);
//        pointLight_2.setRadiance(3f);
//        this.lights.add(pointLight_2);
        
        AreaLight areaLight = new AreaLight(rectangle_2);
        lights.add(areaLight);
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

    public Iterable<Light> getLights() {
        return lights;
    }

    public Iterable<GeometricObject> getObjects() {
        return objects;
    }
}
