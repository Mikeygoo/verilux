package object;

import camera.Camera;
import camera.Pinhole;
import constant.Constants;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import light.Ambient;
import light.AmbientOccluder;
import light.AreaLight;
import light.DirectionalLight;
import light.EnvironmentLight;
import light.JitteredPointLight;
import light.Light;
import light.PointLight;
import material.Emissive;
import material.Material;
import material.Matte;
import material.Phong;
import material.brdf.Lambertian;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sampler.Jittered;
import sampler.MultiJittered;
import sampler.NRooks;
import sampler.PureRandom;
import sampler.Regular;
import sampler.Sampler;
import tracer.AreaLighting;
import tracer.RayCast;
import tracer.Tracer;
import tracer.White;
import util.Normal;
import util.Point3D;
import util.RGBColor;
import util.Vector3D;

/**
 *
 * @author michael
 */
public class XMLBuilder {
    private int numberSamples = 0, numberSets = 0;
    private World w;

    public XMLBuilder(World w) {
        this.w = w;
    }
    
    public void build() {
        Document XML = null;
        
        try {
            File f = new File("xml.xml");
            XML = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        NodeList children = XML.getElementsByTagName("world");
        Node world = children.item(0);
        
        if (children.getLength() != 1 || !world.getNodeName().equals("world")) {
            throw new RuntimeException("The (only) top-level tag must be named <world>.");
        }
        
        numberSamples = getIntegerAttribute(world, "samples", 4);
        Constants.samples = numberSamples;
        numberSets = getIntegerAttribute(world, "sets", 83);
        Constants.sets = numberSets;
        
        children = world.getChildNodes();
        
        for (int i = 0; i < children.getLength(); i++) {
            Node worldNode = children.item(i);
            
            if (worldNode.getNodeName().startsWith("#"))
                continue;
            
            parseSubNode(worldNode);
        }
        
        int vpWidth = getIntegerAttribute(world, "width", 500);
        int vpHeight = getIntegerAttribute(world, "height", 500);
        double zoom = getDoubleAttribute(world, "zoom", 1.0);
        Sampler sampler = parseSamplerNode(world, "viewplane-sampler");
        ViewPlane vp = new ViewPlane(vpHeight, vpWidth, (float) zoom, 1.0f, sampler);
        w.setViewPlane(vp);
    }
    
    private void parseSubNode(Node n) {
        switch (n.getNodeName()) {
            case "ambient": {
                Light lig = parseAmbientNode(n);
                w.setAmbient(lig);
            } break;
            case "light": {
                Light lig = parseLightNode(n);
                
                if (lig instanceof AreaLight)
                    w.addObject((GeometricObject) ((AreaLight) lig).getObject());
                    
                w.addLight(lig);
            } break;
            case "object": {
                GeometricObject obj = parseObjectNode(n);
                w.addObject(obj);
            } break;
            case "camera": {
                Camera cam = parseCameraNode(n);
                w.setCamera(cam);
            } break;
            case "tracer": {
                Tracer tr = parseTracerNode(n);
                w.setTracer(tr);
            } break;
            case "background": {
                RGBColor color = parseColorNode(n, "color");
                w.setBackgroundColor(color);
            } break;
            default: {
                System.out.println("Did not handle "+n);
            }
        }
    }
    
    private Light parseAmbientNode(Node n) {
        switch (getStringAttribute(n, "ambient")) {
            case "ambient": {
                Ambient a = new Ambient();
                double ls = getDoubleAttribute(getSubNode(n, "radiance"), "ls", 1);
                a.setRadiance((float) ls);
                RGBColor c = parseColorNode(n, "color");
                a.setColor(c);
                return a;
            }
            case "ambient-occlusion": {
                AmbientOccluder a = new AmbientOccluder();
                double ls = getDoubleAttribute(getSubNode(n, "radiance"), "ls", 1);
                a.setRadiance((float) ls);
                RGBColor c = parseColorNode(n, "color");
                a.setColor(c);
                double minLs = getDoubleAttribute(getSubNode(n, "min-radiance"), "ls", 0);
                a.setMinAmount((float) minLs);
                return a;
            }
        }
        
        return null;
    }
    
    private Light parseLightNode(Node n) {
        if (n == null) {
            exitWith("No light found.");
        }
        
        switch (getStringAttribute(n, "type")) {
            case "area-light": {
                AreaLight al = new AreaLight();
                GeometricObject obj = parseObjectNode(getSubNode(n, "object"));
                al.setObject(obj);
                return al;
            }
            case "jittered-point-light": {
                JitteredPointLight jpl = new JitteredPointLight();
                double ls = getDoubleAttribute(getSubNode(n, "radiance"), "ls", 1);
                jpl.setRadiance((float) ls);
                RGBColor c = parseColorNode(n, "color");
                jpl.setColor(c);
                Point3D pos = parsePointNode(n, "position");
                jpl.setLocation(pos);
                double r = getDoubleAttribute(getSubNode(n, "radius"), "r", 1);
                jpl.setRadius((float) r);
                return jpl;
            }
            case "point-light": {
                PointLight pl = new PointLight();
                double ls = getDoubleAttribute(getSubNode(n, "radiance"), "ls", 1);
                pl.setRadiance((float) ls);
                RGBColor c = parseColorNode(n, "color");
                pl.setColor(c);
                Point3D pos = parsePointNode(n, "position");
                pl.setLocation(pos);
                return pl;
            }
            case "directional-light": {
                DirectionalLight dl = new DirectionalLight();
                double ls = getDoubleAttribute(getSubNode(n, "radiance"), "ls", 1);
                dl.setRadiance((float) ls);
                RGBColor c = parseColorNode(n, "color");
                dl.setColor(c);
                Vector3D v = parseVectorNode(n, "position");
                v.normalizeTo();
                dl.setDirection(v);
                return dl;
            }
            case "environmental-light": {
                EnvironmentLight el = new EnvironmentLight();
                Material m = parseMaterialNode(n, "material");
                el.setMaterial(m);
                Sampler s = parseSamplerNode(n, "sampler");
                el.setSampler(s);
                return el;
            }
        }
        
        return null;
    }

    private GeometricObject parseObjectNode(Node n) {
        Material m = parseMaterialNode(n, "material");
        
        switch (getStringAttribute(n, "type")) {
            case "plane": {
                Plane p = new Plane();
                p.setMaterial(m);
                Point3D pos = parsePointNode(n, "point");
                p.setPoint(pos);
                Normal norm = parseNormalNode(n, "normal");
                p.setNormal(norm);
                return p;
            }
            case "sphere": {
                Sphere s = new Sphere();
                Point3D pos = parsePointNode(n, "point");
                s.setCenter(pos);
                double r = getDoubleAttribute(getSubNode(n, "radius"), "r", 1);
                s.setRadius((float) r);
                return s;
            }
            case "rectangle": {
                Rectangle r = new Rectangle();
                Point3D pos = parsePointNode(n, "point");
                r.setPoint(pos);
                Vector3D a = parseVectorNode(n, "a");
                Vector3D b = parseVectorNode(n, "b");
                r.setVectors(a, b);
                Normal norm = parseNormalNode(n, "normal");
                r.setNormal(norm);
                Sampler s = parseSamplerNode(n, "sampler");
                r.setSampler(s);
                return r;
            }
        }
        
        return null;
    }

    private Camera parseCameraNode(Node n) {
        switch (getStringAttribute(n, "type")) {
            case "pinhole": {
                Point3D eye = parsePointNode(n, "eye");
                Point3D lookat = parsePointNode(n, "look-at");
                Vector3D up = parseVectorNode(n, "up");
                double viewPlaneDistance = getDoubleAttribute(getSubNode(n, "viewplane-distance"), "d", 1);
                double roll = getDoubleAttribute(getSubNode(n, "roll"), "val", 0);
                Camera c = new Pinhole(eye, lookat, up, viewPlaneDistance);
                c.roll(roll);
                return c;
            }
        }
        
        return null;
    }
    
    private Node getSubNode(Node n, String name) {
        NodeList childNodes = n.getChildNodes();
        
        //System.out.println("-#-");
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node n2 = childNodes.item(i);
            //System.out.println(n2.getNodeName() + " is "+name);
            if (n2.getNodeName().equals(name)) {
                //System.out.println("\n");
                return childNodes.item(i);
            }
        }
        
        System.out.println("Could not find subnode named "+name + " in node "+n.getNodeName());
        //System.out.println("---\n");
        return null;
    }
    
    private RGBColor parseColorNode(Node n, String name) {
        Node n2 = getSubNode(n, name);
        double r = getDoubleAttribute(n2, "r", 0);
        double g = getDoubleAttribute(n2, "g", 0);
        double b = getDoubleAttribute(n2, "b", 0);
        return new RGBColor((float) r, (float) g, (float) b);
    }
    
    private Point3D parsePointNode(Node n, String name) {
        Node n2 = getSubNode(n, name);
        double x = getDoubleAttribute(n2, "x", 0);
        double y = getDoubleAttribute(n2, "y", 0);
        double z = getDoubleAttribute(n2, "z", 0);
        return new Point3D(x, y, z);
        
    }
    
    private Vector3D parseVectorNode(Node n, String name) {
        Node n2 = getSubNode(n, name);
        double x = getDoubleAttribute(n2, "x", 0);
        double y = getDoubleAttribute(n2, "y", 0);
        double z = getDoubleAttribute(n2, "z", 0);
        return new Vector3D(x, y, z);
        
    }
    
    private Normal parseNormalNode(Node n, String name) {
        Node n2 = getSubNode(n, name);
        double x = getDoubleAttribute(n2, "x", 0);
        double y = getDoubleAttribute(n2, "y", 0);
        double z = getDoubleAttribute(n2, "z", 0);
        return new Normal(x, y, z);
        
    }
    
    private Sampler parseSamplerNode(Node n, String name) {
        Node n2 = getSubNode(n, name);
        
        int nSamps = getIntegerAttribute(n, "samples", numberSamples);
        int nSets = getIntegerAttribute(n, "sets", numberSets);
        int cosPow = getIntegerAttribute(n, "cosine-power", 0);
        
        switch (getStringAttribute(n2, "type")) {
            case "jittered":
                return new Jittered(nSamps, nSets, cosPow);
            case "multi-jittered":
                return new MultiJittered(nSamps, nSets, cosPow);
            case "n-rooks":
                return new NRooks(nSamps, nSets, cosPow);
            case "pure-random":
                return new PureRandom(nSamps, nSets, cosPow);
            case "regular":
                return new Regular(nSamps, nSets, cosPow);
        }
        
        return null;
    }
    
    private Material parseMaterialNode(Node n, String name) {
        Node n2 = getSubNode(n, name);
        
        switch (getStringAttribute(n2, "type")) {
            case "phong": {
                Phong p = new Phong();
                double ka = getDoubleAttribute(n2, "ka", 0);
                p.setKa((float) ka);
                double kd = getDoubleAttribute(n2, "kd", 0);
                p.setKd((float) kd);
                double ks = getDoubleAttribute(n2, "ks", 0);
                p.setKs((float) ks);
                double exp = getDoubleAttribute(n2, "exp", 1);
                p.setExp((float) exp);
                RGBColor col = parseColorNode(n2, "color");
                p.setColor(col);
                return p;
            }
            case "lambertian": {
                Matte m = new Matte();
                double ka = getDoubleAttribute(n2, "ka", 0);
                m.setKa((float) ka);
                double kd = getDoubleAttribute(n2, "kd", 0);
                m.setKd((float) kd);
                RGBColor col = parseColorNode(n2, "color");
                m.setColor(col);
                return m;
            }
            case "emissive": {
                Emissive em = new Emissive();
                double ls = getDoubleAttribute(n2, "ls", 1);
                em.setLs((float) ls);
                RGBColor col = parseColorNode(n2, "color");
                em.setCe(col);
                return em;
            }
        }
        
        return null;
    }
    
    private Tracer parseTracerNode(Node n) {
        switch (getStringAttribute(n, "type")) {
            case "ray-cast":
                return new RayCast(w);
            case "area-lighting":
                return new AreaLighting(w);
            case "white":
                return new White(w);
        }
        
        return null;
    }
    
    private double getDoubleAttribute(Node n, String name, double def) {
        if (n == null)
            return def;
        
        Node item = null;
        if (name == null)
            item = n;
        else
            n.getAttributes().getNamedItem(name);
        
        if (item == null)
            return def;
        
        return Double.parseDouble(item.getNodeValue());
    }
    
    private int getIntegerAttribute(Node n, String name, int def) {
        if (n == null)
            return def;
        
        Node item = null;
        if (name == null)
            item = n;
        else
            n.getAttributes().getNamedItem(name);
        
        if (item == null)
            return def;
        
        return Integer.parseInt(item.getNodeValue());
    }
    
    private String getStringAttribute(Node n, String name) {
        if (n == null)
            return "";
        
        Node item = n.getAttributes().getNamedItem(name);
        
        if (item == null)
            return "";
        
        return item.getNodeValue();
    }
    
    private void exitWith(String message) {
        System.out.println("Fatal error: "+message);
        System.exit(0);
    }
}
