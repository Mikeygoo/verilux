package constants;

import java.util.Random;

/**
 *
 * @author michael
 */
public class Maths {
    public static ThreadLocal<Random> r = new ThreadLocal<Random>() {
        @Override
        protected Random initialValue() {
            return new Random((Thread.currentThread().getId() * Constants.SEED) % (Constants.LARGE_PRIME));
        }
    };
    
    public static double min(double x0, double x1) {
        return (x0 < x1) ? x0 : x1;
    }
    
    public static float min(float x0, float x1) {
        return (x0 < x1) ? x0 : x1;
    }
    
    public static int randInt() {
        return Math.abs(r.get().nextInt());
    }
    
    public static float randFloat() {
        return r.get().nextFloat();
    }
    
    public static void randSeed(long seed) {
        r.get().setSeed(seed);
    }

    public static Random getThreadRandom() {
        return r.get();
    }
    
    public static int randInt(int l, int h) {
        return (int) randFloat(l, h);
    }
    
    public static float randFloat(float l, float h) {
        return r.get().nextFloat() * (h - l) + l;
    }
    
    public static double clamp(double x, double l, double h) {
        return x < h ? (x > l ? x : l) : h;
    }
    
    public static float clamp(float x, float l, float h) {
        return x < h ? (x > l ? x : l) : h;
    }
    
    public static int solveQuadratic(double[] c, double[] s) {
        return 0; //TODO
    }
    
    public static int solveCubic(double[] c, double[] s) {
        return 0; //TODO
    }
    
    public static int solveQuartic(double[] c, double[] s) {
        return 0; //TODO
    }
}
