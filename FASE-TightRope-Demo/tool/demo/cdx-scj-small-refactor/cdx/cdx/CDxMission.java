package cdx;

import javax.safetycritical.Mission;

class CDxMission extends Mission {
    protected void initialize() {
      try {            
         new CollisionDetectorHandler();

         if (Constants.DEBUG_DETECTOR) {
            System.out.println("Detector thread is " + Thread.currentThread());
            System.out.println(
               "Entering detector loop, detector thread priority is " +
               +Thread.currentThread().getPriority() +
               " (NORM_PRIORITY is " + Thread.NORM_PRIORITY +
               ", MIN_PRIORITY is " + Thread.MIN_PRIORITY +
               ", MAX_PRIORITY is " + Thread.MAX_PRIORITY + ")");
            }
      } catch (Throwable e) {
         System.out.println("e: " + e.getMessage());
         e.printStackTrace();
      }
   }

	public void cleanup() {
      System.err.println("Cleanup called.");
  	}	

   public long missionMemorySize() {
      return Constants.PERSISTENT_DETECTOR_SCOPE_SIZE;
   }
}
