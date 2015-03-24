/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
package cdx;


public class Benchmarker {


    public static char      RAPITA_GENERATOR            = 1;
    public static char      RAPITA_ROUNDUP              = 2;
    public static char      RAPITA_PERIOD               = 3;
    public static char      RAPITA_DETECTOR_STEP        = 4;
    public static char      RAPITA_SETFRAME             = 5;
    public static char      RAPITA_REDUCER_INIT         = 6;
    public static char      RAPITA_CREATE_MOTIONS       = 7;
    public static char      RAPITA_DETECTOR_CLEANUP     = 8;
    public static char      RAPITA_REDUCER              = 9;
    public static char      RAPITA_VOXELHASHING         = 10;
    public static char      RAPITA_VOXELHASH_DFS        = 11;
    public static char      RAPITA_ISINVOXEL            = 12;
    public static char      RAPITA_VOXELHASH_EXPANDING  = 13;
    public static char      RAPITA_DETERMINE_COLLISIONS = 14;
    public static char      RAPITA_COLLISION_DUPLICATES = 15;
    public static char      RAPITA_FIND_INTERSECTION    = 16;
    public static char      RAPITA_BENCHMARK            = 255;
    public static char      RAPITA_DONE                 = 128;
    public static char      LOOK_FOR_COLLISIONS         = 17;
    public static char      REDUCE_COLLISIONS           = 18;
    public static char      VOXEL_HASHING               = 19;
    public static char      REDUCE_LOOP                 = 20;

    public static final int TRACE_SIZE                  = 10;
    private static int      _tracePtr;
    private static char[]   trace                       = new char[TRACE_SIZE];
    private static int[]    traceTime                   = new int[TRACE_SIZE];
    private static int      _traceStart;

    public static void initialize() {
    }

    public static void set(int what) {
    }

    public static void done(int i) {
    }

    public static void dump() {
    }
}