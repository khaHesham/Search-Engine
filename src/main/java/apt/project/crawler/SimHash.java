package apt.project.crawler;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;

import java.nio.charset.Charset;
import java.util.BitSet;

import static com.google.common.hash.Hashing.*;

/**
 * @author Julien Subercaze
 *
 *
 *         Simhash Implementation that supports hashing function defined by the
 *         Guava's {@link HashFunction}
 */
public class SimHash {
    /**
     * Function used as base for the SimHash
     */
    private final HashFunction hashFunction;

    public SimHash() {
        super();
        hashFunction = md5();
    }

    /**
     * Compute the unique hash for the weighted elements
     *
     * @param weightedStrings Maps that contains pairs of <String, Weight>
     * @return a unique hash
     */

    public BitSet hash(final String[] weightedStrings, Charset charset) {
        // The Weight array for summing
        final double[] weightVector = new double[hashFunction.bits()];
        for (final String entry : weightedStrings) {
            final HashCode hash = hashFunction.hashString(entry, charset);
            weightHashCode(hash, weightVector);
        }
        final BitSet result = new BitSet(hashFunction.bits());
        for (int i = 0; i < hashFunction.bits(); i++) {
            result.set(i, weightVector[i] >= 0);
        }
        return result;
    }

    private void weightHashCode(final HashCode hash, final double[] weighted) {
        // Add the weighted hashcode in the array
        final BitSet b = BitSet.valueOf(hash.asBytes());

        for (int i = 0; i < hash.bits(); i++) {
            weighted[i] += (b.get(i) ? 1 : -1);
        }
    }

    /**
     *
     * @param set1 first Bitset
     * @param set2 second Bitset
     * @return the normalized distance between 0 and 1 for this bit length. 0 when close, 1 when far
     */
    public double distance(final BitSet set1, final BitSet set2) {
        final BitSet clone = (BitSet) set1.clone();
        clone.xor(set2);
        return (double) clone.cardinality() / (double) hashFunction.bits();
    }
}