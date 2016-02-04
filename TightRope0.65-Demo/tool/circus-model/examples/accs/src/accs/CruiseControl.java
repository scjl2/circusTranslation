package accs;

public class CruiseControl {
   private ThrottleController throttle;
   private SpeedMonitor speedo;
   /*private ShaftSimulator shaft;*/

   /*private boolean cruiseActive = false;*/
   private boolean engineActive = false;
   private boolean topGear = false;
   private boolean braking = false;
   private boolean accelerating = false;
   private boolean cruising = false;
   private boolean throttleStarted = false;

   public CruiseControl(ThrottleController throttle, SpeedMonitor speedo) {
      this.throttle = throttle;
      this.speedo = speedo;
   }

   public synchronized void engineOn() {
      engineActive = true;
      braking = false;
      topGear = false;
      cruising = false;
   }

   public synchronized void engineOff() {
      engineActive = false;
      braking = false;
      topGear = false;
      if (cruising) {
         cruising = false;
         throttle.deschedulePeriodic();
      }
   }

   public synchronized void brakeEngaged() {
      if (engineActive) {
         if (cruising) {
            cruising = false;
            throttle.deschedulePeriodic();
         }
         braking = true;
         /*shaft.braking = true;*/
      }
   }

   public synchronized void brakeDisengaged() {
      if (engineActive) {
         braking = false;
         /*shaft.braking = false;*/
      }
   }

   public synchronized void topGearEngaged() {
      if (engineActive) topGear = true;
   }

   public synchronized void topGearDisengaged() {
      if (engineActive) {
         topGear = false;
         if (cruising) {
            cruising = false;
            throttle.deschedulePeriodic();
         }
      }
   }

   public synchronized void activate() {
      if (engineActive & topGear & !braking) {
         cruising = true;
         throttle.setCruiseSpeed(speedo.getCurrentSpeed());
         if (throttleStarted) {
            throttle.schedulePeriodic();
         }
         else {
            throttleStarted = true;
            throttle.schedulePeriodic();
         }
      }
   }

   public synchronized void deactivate() {
      /* Why not just if (cruising) { ... } */
      if (engineActive & topGear & !braking & cruising) {
         cruising = false;
         throttle.deschedulePeriodic();
      }
   }

   public synchronized void resume() {
      if (topGear & !braking & throttleStarted) {
         cruising = true;
         throttle.schedulePeriodic();
      }
   }

   public synchronized void startAcceleration() {
      if (engineActive & topGear & !braking) {
         accelerating = true;
         if (throttleStarted) {
            throttle.schedulePeriodic();
         }
         else {
            throttleStarted = true;
            throttle.schedulePeriodic();
         }
         throttle.accelerate();
      }
   }

   public synchronized void stopAcceleration() {
      if (engineActive & topGear & !braking & accelerating) {
         accelerating = false;
         cruising = true;
         throttle.setCruiseSpeed(speedo.getCurrentSpeed());
      }
   }
}
