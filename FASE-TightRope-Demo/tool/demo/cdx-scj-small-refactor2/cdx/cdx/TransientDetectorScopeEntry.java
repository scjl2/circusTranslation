/**
 *  This file is part of miniCDx benchmark for oSCJ.
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
 
 package cdx;

//import javacp.util.HashMap;
//import javacp.util.HashSet;
//import javacp.util.Iterator;
//import javacp.util.LinkedList;
//import javacp.util.List;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.realtime.MemoryArea;
import javax.safetycritical.ManagedMemory;
import collision.Vector3d;

/**
 * The constructor runs and the instance lives in the persistent detector scope.
 * The state table passed to it lives in the persistent detector scope. The
 * thread runs in transient detector scope. The frame (currentFrame) lives in
 * immortal memory. The real collision detection starts here.
 */
public class TransientDetectorScopeEntry implements Runnable {

    private StateTable state;
    private float voxelSize;
    private RawFrame currentFrame;

    public TransientDetectorScopeEntry(StateTable s, float voxelSizeArg) {
        state = s;
        voxelSize = voxelSizeArg;
    }

    public void run() {
        Benchmarker.set(1);

        Benchmarker.set(Benchmarker.RAPITA_REDUCER_INIT);
        final Reducer reducer = new Reducer(voxelSize);
        Benchmarker.done(Benchmarker.RAPITA_REDUCER_INIT);

        Benchmarker.set(Benchmarker.LOOK_FOR_COLLISIONS);
        int numberOfCollisions = lookForCollisions(reducer, createMotions());
        Benchmarker.done(Benchmarker.LOOK_FOR_COLLISIONS);

        if (cdx.ImmortalEntry.recordedRuns < cdx.ImmortalEntry.maxDetectorRuns) {
            cdx.ImmortalEntry.detectedCollisions[cdx.ImmortalEntry.recordedRuns] = numberOfCollisions;
        }

        if (Constants.SYNCHRONOUS_DETECTOR || Constants.DEBUG_DETECTOR) {
            System.out.println("CD detected  " + numberOfCollisions
                    + " collisions.");
            int colIndex = 0;

            System.out.println("");
        }
        Benchmarker.done(1);
    }

    public int lookForCollisions(final Reducer reducer, final List motions) {
        Benchmarker.set(2);
        final List check = reduceCollisionSet(reducer, motions);

        int suspectedSize = check.size();
        if (cdx.ImmortalEntry.recordedRuns < cdx.ImmortalEntry.maxDetectorRuns) {
            cdx.ImmortalEntry.suspectedCollisions[cdx.ImmortalEntry.recordedRuns] = suspectedSize;
        }

        int c = 0;
        final List ret = new LinkedList();
        for (final Iterator iter = check.iterator(); iter.hasNext();)
            c += determineCollisions((List) iter.next(), ret);
        Benchmarker.done(2);
        return c; 
    }

    /**
     * Takes a List of Motions and returns an List of Lists of Motions, where
     * the inner lists implement RandomAccess. Each Vector of Motions that is
     * returned represents a set of Motions that might have collisions.
     */
    public List reduceCollisionSet(final Reducer it, final List motions) {
        Benchmarker.set(3);
        final HashMap voxel_map = new HashMap();
        final HashMap graph_colors = new HashMap();

        for (final Iterator iter = motions.iterator(); iter.hasNext();)
            it.performVoxelHashing((Motion) iter.next(), voxel_map,
                    graph_colors);

        final List ret = new LinkedList();
        for (final Iterator iter = voxel_map.values().iterator(); iter
                .hasNext();) {
            final List cur_set = (List) iter.next();
            if (cur_set.size() > 1)
                ret.add(cur_set);
        }
        Benchmarker.done(3);
        return ret;
    }

    public boolean checkForDuplicates(final List collisions, Motion one,
            Motion two) {
        byte c1 = one.getAircraft().getCallsign()[5];
        byte c2 = two.getAircraft().getCallsign()[5];
        for (final Iterator iter = collisions.iterator(); iter.hasNext();) {
            Collision c = (Collision) iter.next();
            if ((c.first().getCallsign()[5] == c1)
                    && (c.second().getCallsign()[5] == c2)) {
                // Benchmarker.done(4);
                return false;
            }
        }
        return true;
    }

    public int determineCollisions(final List motions, List ret) {
        Benchmarker.set(5);
        int _ret = 0;
        Motion[] _motions = (Motion[]) motions.toArray(new Motion[motions
                .size()]);
        for (int i = 0; i < _motions.length - 1; i++) {
            final Motion one = _motions[i]; // m2==two, m=one
            for (int j = i + 1; j < _motions.length; j++) {
                final Motion two = _motions[j];
                final Vector3d vec = one.findIntersection(two);
                if (vec != null) {
                    ret.add(new Collision(one.getAircraft(), two.getAircraft(),
                            vec));
                    _ret++;
                }
            }
        }
        Benchmarker.done(5);
        return _ret;
    }

    public void dumpFrame(String debugPrefix) {

        String prefix = debugPrefix + frameno + " ";
        int offset = 0;
        for (int i = 0; i < currentFrame.planeCnt; i++) {

            int cslen = currentFrame.lengths[i];
            System.out.println(prefix
                    + new String(currentFrame.callsigns, offset, cslen) + " "
                    + currentFrame.positions[3 * i] + " "
                    + currentFrame.positions[3 * i + 1] + " "
                    + currentFrame.positions[3 * i + 2] + " ");
            offset += cslen;
        }
    }

    int frameno = -1; // just for debug

    public void setFrame(final RawFrame f) {
        if (Constants.DEBUG_DETECTOR || Constants.DUMP_RECEIVED_FRAMES
                || Constants.SYNCHRONOUS_DETECTOR) {
            frameno++;
        }
        currentFrame = f;
        if (Constants.DUMP_RECEIVED_FRAMES) {
            dumpFrame("CD-R-FRAME: ");
        }

    }

    /**
     * This method computes the motions and current positions of the aircraft
     * Afterwards, it stores the positions of the aircrafts into the StateTable
     * in the persistentScope
     * 
     * @return
     */
    public List createMotions() {
        Benchmarker.set(6);
        final List ret = new LinkedList();
        final HashSet poked = new HashSet();

        Aircraft craft;
        Vector3d new_pos;

        for (int i = 0, pos = 0; i < currentFrame.planeCnt; i++) {

            final float x = currentFrame.positions[3 * i], y = currentFrame.positions[3 * i + 1], z = currentFrame.positions[3 * i + 2];
            final byte[] cs = new byte[currentFrame.lengths[i]];
            for (int j = 0; j < cs.length; j++)
                cs[j] = currentFrame.callsigns[pos + j];
            pos += cs.length;
            craft = new Aircraft(cs);
            new_pos = new Vector3d(x, y, z);

            poked.add(craft);
            // get the last known position of this aircraft
			statetable.Vector3d old_pos = null;
			CallSign callsg_ = new CallSign(craft.getCallsign());
            old_pos = state.get(callsg_);

            if (old_pos == null) {
                // we have detected a new aircraft

                // here, we create a new callsign and store the aircraft into
                // the state table.
                byte[] airCS = craft.getCallsign();
                CallSign callsign = mkCallsignInPersistentScope(airCS);
                state.put(callsign, new_pos.x, new_pos.y, new_pos.z);

                final Motion m = new Motion(craft, new_pos);
                if (cdx.Constants.DEBUG_DETECTOR
                        || cdx.Constants.SYNCHRONOUS_DETECTOR) {
                    System.out
                            .println("createMotions: old position is null, adding motion: "
                                    + m);
                }

                ret.add(m);
            } else {
                // this is already detected aircraft, we we need to update its
                // position
                final Vector3d save_old_position = new Vector3d(old_pos.x,
                        old_pos.y, old_pos.z);
                // updating position in the StateTable
                old_pos.set(new_pos.x, new_pos.y, new_pos.z);

                final Motion m = new Motion(craft, save_old_position, new_pos);
                if (cdx.Constants.DEBUG_DETECTOR
                        || cdx.Constants.SYNCHRONOUS_DETECTOR) {
                    System.out.println("createMotions: adding motion: " + m);
                }

                ret.add(m);
            }
        }
        Benchmarker.done(6);
        return ret;
    }

    /**
     * This Runnable enters the StateTable in order to allocate the callsign in
     * the PersistentScope
     */
   /* Commented out by Frank Zeyda */
    /*static class R implements Runnable {
        CallSign c;
        byte[] cs;

        public void run() {
            c = new CallSign(cs);
        }
    }*/
    /* End of Comment */

   /* Modified by Frank Zeyda */
    private final /*R*/ TransientDetectorScopeEntry_R r =
      new /*R()*/ TransientDetectorScopeEntry_R();
   /* End of Modification */

    CallSign mkCallsignInPersistentScope(final byte[] cs) {

        /* Modified by Frank Zeyda (added try/catch block) */
        try {
          MemoryArea ma = MemoryArea.getMemoryArea(r);
          r.cs = (byte[]) ma.newArray(byte.class, cs.length);
        }
        catch(Exception e) { }
        /* End of Modification */

        for (int i = 0; i < cs.length; i++)
            r.cs[i] = cs[i];
        
        MemoryArea ma = MemoryArea.getMemoryArea(state);
//        ma.executeInArea(r);
        ManagedMemory.executeInAreaOf(ma, r);
        return r.c;
    }

}
