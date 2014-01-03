package sampler;

import constants.Maths;
import java.util.ArrayList;
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
        shuffleSamples();
    }

    public int getNumSamples() {
        return numSamples;
    }

    public int getNumSets() {
        return numSets;
    }
    
    public abstract void generateSamples();
    
    public void setupShuffledIndices() {
        //TODO
    }
    
    public void shuffleSamples() {
        //TODO
    }
    
    public Point2D sampleUnitSquare(SamplerKey sk) {
        if (sk.count.get() % numSamples == 0)
            sk.jump.set((Maths.randInt() % numSets) * numSamples);
        
        return samples.get(sk.jump.get() + sk.count.getAndIncrement() % numSamples);
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
