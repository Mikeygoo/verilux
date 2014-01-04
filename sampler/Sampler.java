package sampler;

import constants.Constants;
import constants.Maths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import util.Point2D;
import util.Point3D;

/**
 *
 * @author michael
 */
public abstract class Sampler {
    protected int numSamples;
    protected int numSets;
    protected ArrayList<Point2D> samples, diskSamples;
    protected ArrayList<Point3D> hemisphereSamples;
    protected ArrayList<Integer> shuffledIndices;

    public Sampler(int nSamps, int nSets) {
        numSamples = nSamps;
        numSets = nSets;
        samples = new ArrayList<Point2D>();
        shuffledIndices = new ArrayList<Integer>();

        generateSamples();
        setupShuffledIndices();
        mapSamplesToUnitDisk();
    }

    public int getNumSamples() {
        return numSamples;
    }

    public int getNumSets() {
        return numSets;
    }

    protected abstract void generateSamples();

    private void setupShuffledIndices() {
        shuffledIndices.ensureCapacity(numSamples * numSets);

        ArrayList<Integer> setIndices = new ArrayList<Integer>();

        for (int i = 0; i < numSamples; i++)
            setIndices.add(i);

        for (int i = 0; i < numSets; i++) {
            Collections.shuffle(setIndices, Maths.getThreadRandom());
            shuffledIndices.addAll(setIndices);
        }
    }

    public void mapSamplesToUnitDisk() {
        diskSamples.ensureCapacity(samples.size());

        for (Point2D squareSample : samples) {
            Point2D sp = new Point2D();
            double r, phi;
            sp.x = 2.0 * squareSample.x - 1.0;
            sp.y = 2.0 * squareSample.y - 1.0;

            if (sp.x > -sp.y) {
                if (sp.x > sp.y) {
                    r = sp.x;
                    phi = sp.y / sp.x;
                } else {
                    r = sp.y;
                    phi = 2 - sp.x / sp.y;
                }
            } else {
                if (sp.x < sp.y) {
                    r = -sp.x;
                    phi = 4 + sp.y / sp.x;
                } else {
                    r = -sp.y;
                    phi = 6 - sp.x / sp.y;
                }
            }

            phi *= Constants.PI_OVER_FOUR;

            sp.x = r * Math.cos(phi);
            sp.y = r * Math.sin(phi);
            diskSamples.add(sp);
        }
    }

    public Point2D sampleUnitSquare(SamplerKey sk) {
        if (sk.count.get() % numSamples == 0)
            sk.jump.set((int)(Maths.randFloat() * numSets) * numSamples);

        return samples.get(sk.jump.get() + shuffledIndices.get(sk.count.getAndIncrement() % numSamples));
    }

    public Point2D sampleUnitDisk(SamplerKey sk) {
        if (sk.count.get() % numSamples == 0)
            sk.jump.set((int)(Maths.randFloat() * numSets) * numSamples);

        return diskSamples.get(sk.jump.get() + shuffledIndices.get(sk.count.getAndIncrement() % numSamples));
    }

    public static class SamplerKey {
        private AtomicInteger count;
        private AtomicInteger jump;

        public SamplerKey() {
            count = new AtomicInteger(0);
            jump = new AtomicInteger(0);
        }
    }
}
