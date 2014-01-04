package sampler;

import constants.Maths;
import util.Point2D;

/**
 *
 * @author michael
 */
public class NRooks extends Sampler {
    public NRooks(int nSamps, int nSets) {
        super(nSamps, nSets);
    }

    @Override
    protected void generateSamples() {
        for (int p = 0; p < numSets; p++) {
            for (int j = 0; j < numSamples; j++) {
                Point2D sp = new Point2D();
                sp.x = (j + Maths.randFloat()) / numSamples;
                sp.y = (j + Maths.randFloat()) / numSamples;
                samples.add(sp);
            }
        }

        shuffleXCoordinates();
        shuffleYCoordinates();
    }

    private void shuffleXCoordinates() {
        for (int p = 0; p < numSets; p++) {
            for (int i = 0; i < numSamples - 1; i++) {
                int target = (int)(Maths.randFloat() * numSamples) + p * numSamples;
                Point2D p1 = samples.get(i + p * numSamples + 1);
                Point2D p2 = samples.get(target);
                double temp = p1.x;
                p1.x = p2.x;
                p2.x = temp;
            }
        }
    }

    private void shuffleYCoordinates() {
        for (int p = 0; p < numSets; p++) {
            for (int i = 0; i < numSamples - 1; i++) {
                int target = (int)(Maths.randFloat() * numSamples) + p * numSamples;
                Point2D p1 = samples.get(i + p * numSamples + 1);
                Point2D p2 = samples.get(target);
                double temp = p1.y;
                p1.y = p2.y;
                p2.y = temp;
            }
        }
    }
}
