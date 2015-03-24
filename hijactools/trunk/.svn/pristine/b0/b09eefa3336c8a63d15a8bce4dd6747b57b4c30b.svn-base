package accs;

import javax.realtime.PriorityParameters;

import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

public class MainMissionSequencer extends MissionSequencer {
   private boolean mission_done;

   public MainMissionSequencer() {
      super(
         /* Let MainMissionSequencer run at max priority. */
         new PriorityParameters(
            PriorityScheduler.instance().getMaxPriority()),
         new StorageParameters(10000, 10000, 10000));
      mission_done = false;
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
