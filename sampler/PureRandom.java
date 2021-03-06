package sampler;

import constant.Maths;
import util.Point2D;

/**
 *
 * @author michael
 */
public class PureRandom extends Sampler {
    public PureRandom(int numSamps, int numSets, int hemisphereCosinePower) {
        super(numSamps, numSets, hemisphereCosinePower);
    }

    public PureRandom(int numSamps, int numSets) {
        super(numSamps, numSets);
    }

    @Override
    protected void generateSamples() {
        for (int i = 0; i < numSamples * numSets; i++)
            samples.add(new Point2D(Maths.randFloat(), Maths.randFloat()));
    }
}
