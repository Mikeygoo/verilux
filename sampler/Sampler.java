package sampler;

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

    }

    public Point2D sampleUnitSquare(SamplerKey sk) {
        if (sk.count.get() % numSamples == 0)
            sk.jump.set((int)(Maths.randFloat() * numSets) * numSamples);

        return samples.get(sk.jump.get() + shuffledIndices.get(sk.count.getAndIncrement() % numSamples));
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
