package accs;

import javax.safetycritical.annotate.*;

@PassiveClass
public class Controller {
  private ThrottleController throttle;
  private SpeedMonitor speedo;

  /*private boolean cruiseActive = false;*/
  private boolean engineActive = false;
  private boolean topGear = false;
  private boolean braking = false;
  private boolean accelerating = false;
  private boolean cruising = false;
  private boolean throttleStarted = false;

  public Controller(ThrottleController throttle, SpeedMonitor speedo) {
    this.throttle = throttle;
    this.speedo = speedo;
  }

  public synchronized void engineOn() {
    //System.out.println("ENGINE ON");
    engineActive = true;
    braking = false;
    topGear = false;
    cruising = false;
  }

  public synchronized void engineOff() {
    //System.out.println("ENGINE OFF");
    engineActive = false;
    braking = false;
    topGear = false;
    if (cruising) {
      cruising = false;
      throttle.deschedulePeriodic();
    }
  }

  public synchronized void brakePressed() {
    //System.out.println("BRAKE ON");
    if (engineActive) {
      if (cruising) {
        cruising = false;
        throttle.deschedulePeriodic();
      }
      braking = true;
    }
  }

  public synchronized void brakeReleased() {
    //System.out.println("BRAKE OFF");
    if (engineActive) {
      braking = false;
    }
  }

  public synchronized void topGearEngaged() {
    //System.out.println("TOP GEAR ENGAGED");
    if (engineActive) {
      topGear = true;
    }
  }

  public synchronized void topGearDisengaged() {
    //System.out.println("TOP GEAR DISENGAGED");
    if (engineActive) {
      topGear = false;
      if (cruising) {
        cruising = false;
        throttle.deschedulePeriodic();
      }
    }
  }

  /* I modified the method below according to the journal paper. */

  public synchronized void activate() {
    //System.out.println("ACTIVATING");
    if (engineActive & topGear & !braking) {
      cruising = true;
      int cruise_speed;
      cruise_speed = speedo.getCurrentSpeed();
      throttle.setCruiseSpeed(cruise_speed);
      throttle.schedulePeriodic();
      throttleStarted = true;
    }
  }

  public synchronized void deactivate() {
    //System.out.println("DEACTIVATING");
    /* Why not just if (cruising) {... } ? */
    if (engineActive & topGear & !braking & cruising) {
      cruising = false;
      throttle.deschedulePeriodic();
    }
  }

  public synchronized void startAccelerating() {
    //System.out.println("ACCELERATING");
    if (engineActive & topGear & !braking) {
      accelerating = true;
      if (throttleStarted) {
        throttle.schedulePeriodic();
      }
      else {
        throttleStarted = true;
        /* Redundant now since the throttle is never 'stopped'. */
        /*throttle.start();*/
        throttle.schedulePeriodic();
      }
      throttle.accelerate();
    }
  }

  public synchronized void stopAccelerating() {
    //System.out.println("STOP ACCELERATING");
    if (engineActive & topGear & !braking & accelerating) {
      accelerating = false;
      cruising = true;
      throttle.setCruiseSpeed(speedo.getCurrentSpeed());
    }
  }

  public synchronized void resume() {
    //System.out.println("RESUME");
    if (topGear & !braking & throttleStarted) {
      cruising = true;
      throttle.schedulePeriodic();
    }
  }
}
