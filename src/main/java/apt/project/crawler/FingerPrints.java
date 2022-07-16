package apt.project.crawler;

/*
 * simple threadsafe wrapper class for fingerprint set that contains the signatures of the urls seen so far
 * */

import java.util.BitSet;
import java.util.HashSet;
import java.util.stream.IntStream;

import static com.google.common.hash.Hashing.md5;

public class FingerPrints {
    private final HashSet<BitSet> fingerPrints;

    public FingerPrints() {
        fingerPrints = new HashSet<>();
    }

    public FingerPrints(HashSet<BitSet> fingerPrints) {
        this.fingerPrints = fingerPrints;
    }

    public synchronized void add(BitSet fingerPrint) {
        fingerPrints.add(fingerPrint);
    }

    public synchronized boolean contains(BitSet fingerPrint){
        return fingerPrints.contains(fingerPrint);
    }

    public HashSet<BitSet> infrastructure(){
        return fingerPrints;
    }

    /*
     * converts the bitset into a binary string to be stored efficiently
     * */
    public static String toString(BitSet bitSet){
        int nBits = md5().bits();
        final StringBuilder buffer = new StringBuilder(nBits);
        IntStream.range(0, nBits).mapToObj(i -> bitSet.get(i) ? '1' : '0').forEach(buffer::append);
        return buffer.toString();
    }

    /*
     * converts a binary string into a bitset
     * */
    public static BitSet toBitSet(String binary){
        BitSet bitset = new BitSet(binary.length());
        int nBits = binary.length();

        for (int i = 0; i < nBits; i++) {
            if (binary.charAt(i) == '1')
                bitset.set(i);
        }
        return bitset;
    }
}
