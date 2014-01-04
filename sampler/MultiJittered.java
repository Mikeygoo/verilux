package sampler;

import constants.Maths;
import util.Point2D;

/**
 *
 * @author michael
 */
public class MultiJittered extends Sampler {
    private int n;

    public MultiJittered(int numSamps, int numSets, int hemisphereCosinePower) {
        super(numSamps, numSets, hemisphereCosinePower);
    }

    public MultiJittered(int nSamps, int nSets) {
        super(nSamps, nSets);
    }

    @Override
    protected void generateSamples() {
        n = (int) Math.sqrt(numSamples);
        float subcellWidth = 1.0f / ((float) numSamples);

        for (int j = 0; j < numSamples * numSets; j++)
            samples.add(new Point2D());

        for (int p = 0; p < numSets; p++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Point2D sp = samples.get(i * n + j + p * numSamples);
                    sp.x = (i * n + j) * subcellWidth + Maths.randFloat(0, subcellWidth);
                    sp.y = (j * n + i) * subcellWidth + Maths.randFloat(0, subcellWidth);
                }
            }
        }

        shuffleXCoordinates();
        shuffleYCoordinates();
    }

    private void shuffleXCoordinates() {
        for (int p = 0; p < numSets; p++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int k = Maths.randInt(j, n);
                    Point2D sp1 = samples.get(i * n + j + p * numSamples);
                    Point2D sp2 = samples.get(i * n + k + p * numSamples);
                    double temp = sp1.x;
                    sp1.x = sp2.x;
                    sp2.x = temp;
                }
            }
        }
    }

    private void shuffleYCoordinates() {
        for (int p = 0; p < numSets; p++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int k = Maths.randInt(j, n);
                    Point2D sp1 = samples.get(j * n + i + p * numSamples);
                    Point2D sp2 = samples.get(k * n + i + p * numSamples);
                    double temp = sp1.y;
                    sp1.y = sp2.y;
                    sp2.y = temp;
                }
            }
        }
    }
}
