package sampler;

import constants.Maths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import util.Point2D;

/**
 *
 * @author michael
 */
public abstract class Sampler {
    protected int numSamples;
    protected int numSets;
    protected ArrayList<Point2D> samples;
    protected ArrayList<Integer> shuffledIndices;
    
    public Sampler(int nSamps, int nSets) {
        numSamples = nSamps;
        numSets = nSets;
        samples = new ArrayList<Point2D>();
        shuffledIndices = new ArrayList<Integer>();
        
        generateSamples();
        setupShuffledIndices();
    }

    public int getNumSamples() {
        return numSamples;
    }

    public int getNumSets() {
        return numSets;
    }
    
    public abstract void generateSamples();
    
    public void setupShuffledIndices() {
        shuffledIndices.ensureCapacity(numSamples * numSets);
        
        ArrayList<Integer> setIndices = new ArrayList<Integer>();
        for (int i = 0; i < numSamples; i++)
            setIndices.add(i);
        
        for (int i = 0; i < numSets; i++) {
            Collections.shuffle(setIndices, Maths.getThreadRandom());
            shuffledIndices.addAll(setIndices);
        }
    }
    
    public Point2D sampleUnitSquare(SamplerKey sk) {
        if (sk.count.get() % numSamples == 0)
            sk.jump.set((int) (Maths.randFloat() * numSets) * numSamples);
        
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
