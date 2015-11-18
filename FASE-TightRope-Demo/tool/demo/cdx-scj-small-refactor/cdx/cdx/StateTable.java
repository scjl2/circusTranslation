/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
package cdx;

import javacp.util.HashMap;
import javax.realtime.MemoryArea;
import statetable.Vector3d;

/**
 * The instance lives and the constructor runs in the persistent detector scope. The put method and the get method are
 * called from the transient detector scope - see below. previous_state is map call signs to 3D vectors - the call signs
 * are in persistent detector scope - the vectors are in persistent detector scope (allocated here)
 */
public class StateTable {

    final private static int MAX_AIRPLANES = 10000;

   /* Modified by Frank Zeyda */
    /*private*/ protected Vector3d[]       allocatedVectors;
    /*private*/ protected int              usedVectors;
   /* End of Modification */

    /** Mapping Aircraft -> Vector3d. */
    final /*private*/ protected HashMap    motionVectors = new HashMap();

    StateTable() {
        allocatedVectors = new Vector3d[MAX_AIRPLANES];
        for (int i = 0; i < allocatedVectors.length; i++)
            allocatedVectors[i] = new Vector3d();

        usedVectors = 0;
    }

   /* Commented out by Frank Zeyda */
    /*private class R implements Runnable {
        CallSign callsign;
        float    x, y, z;

        public void run() {
            Vector3d v = (Vector3d) motionVectors.get(callsign);
            if (v == null) {
                v = allocatedVectors[usedVectors++];
                motionVectors.put(callsign, v);
            }
            v.x = x;
            v.y = y;
            v.z = z;
        }
    }*/
   /* End of Comment */

   /* Modified by Frank Zeyda */
    private final /*R*/ StateTable_R r = new /*R()*/ StateTable_R(this);
   /* End of Modification */

    public void put(final CallSign callsign, final float x, final float y, final float z) {
        r.callsign = callsign;
        r.x = x;
        r.y = y;
        r.z = z;
        MemoryArea.getMemoryArea(this).executeInArea(r);
    }

    public Vector3d get(final CallSign callsign) {
        return (Vector3d) motionVectors.get(callsign);
    }
}
