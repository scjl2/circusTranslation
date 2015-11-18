package cdx;

import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;

import javax.safetycritical.MissionSequencer;
import javax.safetycritical.SingleMissionSequencer;
import javax.safetycritical.StorageParameters;

/* Class added by Frank Zeyda */

public class CDxMissionSequencer extends SingleMissionSequencer {
   /* The code had to be slightly changed here to return a new instance of
    * cdx.CDxMission rather than 'this'. */
   public CDxMissionSequencer() {
      super(
         new PriorityParameters(PriorityScheduler.instance().getMaxPriority()),
         new StorageParameters(
            Constants.PERSISTENT_DETECTOR_SCOPE_SIZE +
            Constants.TRANSIENT_DETECTOR_SCOPE_SIZE, 10000, 10000),
            new CDxMission());
   }


   public MissionSequencer getSequencer() {
      return null;
   }
}
