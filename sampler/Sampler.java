package sampler;

import constant.Constants;
import constant.Maths;
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

    public Sampler(int nSamps, int nSets, float hemisphereCosinePower) {
        numSamples = nSamps;
        numSets = nSets;
        samples = new ArrayList<Point2D>();
        diskSamples = new ArrayList<Point2D>();
        hemisphereSamples = new ArrayList<Point3D>();
        shuffledIndices = new ArrayList<Integer>();

        generateSamples();
        setupShuffledIndices();
        mapSamplesToUnitDisk();
        mapSamplesToUnitHemisphere(hemisphereCosinePower);
    }

    public Sampler(int nSamps, int nSets) {
        this(nSamps, nSets, 0);
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

    public void mapSamplesToUnitHemisphere(double e) {
        hemisphereSamples.ensureCapacity(samples.size());

        for (Point2D squareSample : samples) {
            double cosPhi = Math.cos(Constants.TWO_PI * squareSample.x);
            double sinPhi = Math.sin(Constants.TWO_PI * squareSample.x);
            double cosTheta = Math.pow(1.0 - squareSample.y, 1.0 / (e + 1.0));
            double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);

            double pu = sinTheta * cosPhi;
            double pv = sinTheta * sinPhi;
            double pw = cosTheta;

            hemisphereSamples.add(new Point3D(pu, pv, pw));
        }
    }

    public Point2D sampleUnitSquare(SamplerKey sk) {
        if (sk.getCount() % numSamples == 0)
            sk.setJump((int)(Maths.randFloat() * numSets) * numSamples);

        return samples.get(sk.getJump() + shuffledIndices.get(sk.getJump() + sk.getAndIncrementCount() % numSamples));
    }

    public Point2D sampleUnitDisk(SamplerKey sk) {
        if (sk.getCount() % numSamples == 0) {
            sk.setJump((int)(Maths.randFloat() * numSets) * numSamples);
            sk.setCount(0);
        }

        return diskSamples.get(sk.getJump() + shuffledIndices.get(sk.getJump() + sk.getAndIncrementCount() % numSamples));
    }

    public Point3D sampleUnitHemisphere(SamplerKey sk) {
        if (sk.getCount() % numSamples == 0)
            sk.setJump((int)(Maths.randFloat() * numSets) * numSamples);

        return hemisphereSamples.get(sk.getJump() + shuffledIndices.get(sk.getJump() + sk.getAndIncrementCount() % numSamples));
    }

    public static class SamplerKey {
        private ThreadLocal<AtomicInteger> count = new ThreadLocal<AtomicInteger>() {
            @Override
            protected AtomicInteger initialValue() {
                return new AtomicInteger();
            }
        };
        
        private ThreadLocal<AtomicInteger> jump = new ThreadLocal<AtomicInteger>() {
            @Override
            protected AtomicInteger initialValue() {
                return new AtomicInteger();
            }
        };

        public SamplerKey() {
        }

        public int getCount() {
            return count.get().get();
        }
        
        public int getAndIncrementCount() {
            return this.count.get().getAndIncrement();
        }

        public void setCount(int count) {
            this.count.get().set(count);
        }

        public int getJump() {
            return jump.get().get();
        }

        public void setJump(int jump) {
            this.jump.get().set(jump);
        }
    }
}
