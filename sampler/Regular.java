package sampler;

import util.Point2D;

/**
 *
 * @author michael
 */
public class Regular extends Sampler {
    public Regular(int nSamps, int nSets) {
        super(nSamps, nSets);
    }

    @Override
    protected void generateSamples() {
        int n = (int) Math.sqrt((float)numSamples);

        for (int j = 0; j < numSets; j++)
            for (int p = 0; p < n; p++)
                for (int q = 0; q < n; q++)
                    samples.add(new Point2D((q + 0.5) / n, (p + 0.5) / n));
    }
}
