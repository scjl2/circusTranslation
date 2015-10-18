package cdx;

import statetable.Vector3d;

/* Class added by Frank Zeyda. */

/* This class results from extracting the inner class cdx.StateTable.R. */

class StateTable_R implements Runnable {
   /* Added by Frank Zeyda */
   StateTable obj;
   /* End of Addition */

   CallSign callsign;
   float    x, y, z;

   /* Added by Frank Zeyda */
   public StateTable_R(StateTable objArg) {
      obj = objArg;
   }
   /* End of Addition */

   public void run() {
      Vector3d v = (Vector3d) obj.motionVectors.get(callsign);
      if (v == null) {
         v = obj.allocatedVectors[obj.usedVectors++];
         obj.motionVectors.put(callsign, v);
      }
      v.x = x;
      v.y = y;
      v.z = z;
   }
}
