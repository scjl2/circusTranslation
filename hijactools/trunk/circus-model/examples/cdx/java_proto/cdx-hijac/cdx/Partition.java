package cdx;

import javacp.util.LinkedList;
import javacp.util.List;

/* Class added by Frank Zeyda */

public class Partition {
    public final List[] parts;
    public int index;

    public Partition() {
        parts = new LinkedList[Constants.DETECTORS];
        for (int i = 0; i < Constants.DETECTORS; i++) {
            parts[i] = new LinkedList();
        }
        index = 0;
    }

    public synchronized void clear() {
        /* The implementation is flawed since the memory that was previously
         * allocated for Motion objects in the lists is never freed.  A
         * solution acknowledged by Andy Wellings is to use mutable objects. */
        for (int i = 0; i < Constants.DETECTORS; i++) {
            /* This creates a memory leak. */
            parts[i].clear();
        }
        index = 0;
    }

    public synchronized void recordVoxelMotions(List motions) {
        parts[index].add(motions);
        index = (index + 1) % Constants.DETECTORS;
    }

    public synchronized List getDetectorWork(int id) {
        return parts[id-1];
    }
}
