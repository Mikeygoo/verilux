package util;

/**
 *
 * @author michael
 */
public class RGBColor {
    public static final RGBColor WHITE = new RGBColor(1);
    public static final RGBColor BLACK = new RGBColor(0);
    public static final RGBColor RED   = new RGBColor(1, 0, 0);
    public static final RGBColor GREEN = new RGBColor(0, 1, 0);
    public static final RGBColor BLUE  = new RGBColor(0, 0, 1);
    
    public float r, g, b;

    public RGBColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public RGBColor(float c) {
        this(c, c, c);
    }
    
    public RGBColor() {
        this(0, 0, 0);
    }
    
    public RGBColor add(RGBColor c) {
        return new RGBColor(r + c.r, g + c.g, b + c.b);
    }
    
    public void addTo(RGBColor c) {
        r += c.r;
        b += c.b;
        g += c.g;
    }
    
    public RGBColor pow(float p) {
        return new RGBColor((float) Math.pow(r, p), 
                            (float) Math.pow(g, p), 
                            (float) Math.pow(b, p));
    }

    public void powTo(float p) {
        r = (float) Math.pow(r, p);
        g = (float) Math.pow(g, p);
        b = (float) Math.pow(b, p);
    }
    
    public RGBColor scale(float f) {
        return new RGBColor(f * r, f * g, f * b);
    }
    
    public void scaleTo(float f) {
        r *= f;
        g *= f;
        b *= f;
    }
}
