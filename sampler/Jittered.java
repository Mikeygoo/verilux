package sampler;

import constants.Maths;
import util.Point2D;

/**
 *
 * @author michael
 */
public class Jittered extends Sampler {
    public Jittered(int numSamps, int numSets) {
        super(numSamps, numSets);
    }

    @Override
    public void generateSamples() {
        int n = (int) Math.sqrt(numSamples);
        
        for (int p = 0; p < numSets; p++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    Point2D sp = new Point2D();
                    sp.x = (k + Maths.randFloat()) / n;
                    sp.y = (j + Maths.randFloat()) / n;
                    samples.add(sp);
                }
            }
        }
    }
}
