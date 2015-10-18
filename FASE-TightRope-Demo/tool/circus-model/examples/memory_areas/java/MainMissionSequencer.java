import javax.realtime.PriorityParameters;

import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageConfigurationParameters;

public class MainMissionSequencer extends MissionSequencer {
   public int mission_count;

   public MainMissionSequencer() {
      super(
         new PriorityParameters(
            PriorityScheduler.instance().getNormPriority()),
         new StorageConfigurationParameters(131072, 4096, 4096));
      mission_count = 1;
   }

   public Mission getNextMission() {
      int count = mission_count;
      mission_count++;
      switch (count) {
         case 1:
            return new Mission1();
         case 2:
            return new Mission2();
         case 3:
            return new Mission3();
         default:
            return null;
      }
   }
}
