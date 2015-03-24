import javax.realtime.PriorityParameters;

import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageConfigurationParameters;

public class MainMissionSequencer extends MissionSequencer {
   public boolean mission_done = false;

   public MainMissionSequencer() {
      super(
         new PriorityParameters(
            PriorityScheduler.instance().getNormPriority()),
         new StorageConfigurationParameters(131072, 4096, 4096));
   }

   public Mission getNextMission() {
      if (!mission_done) {
         mission_done = true;
         return new MainMission();
      }
      else {
         return null;
      }
   }
}
