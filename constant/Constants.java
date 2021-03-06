package constant;

/**
 *
 * @author michael
 */
public class Constants {
    public static final double PI = 3.1415926535897932384;
    public static final double TWO_PI = PI * 2;
    public static final double PI_OVER_FOUR = PI / 4.0;
    public static final double PI_OVER_180 = PI / 180.0;
    public static final double INV_PI = 1.0 / PI;
    public static final double INV_TWO_PI = 1.0 / (2 * PI);

    public static final double CAMERA_EPSILON = 0.0001;
    public static final double CAMERA_HUGE_VALUE = 1.0E10;

    public static final long SEED = 23_013_800_210L;
    public static final long LARGE_PRIME = 151_703L;

    ///
    public static int samples = 0;
    public static int sets = 0;
    public static final int slicesVertical = 8;
    public static final int slicesHorizontal = 4;
}
