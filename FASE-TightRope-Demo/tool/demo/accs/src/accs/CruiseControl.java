package accs;

import javax.realtime.*;

import javax.safetycritical.*;

public class CruiseControl {
  /*private boolean cruiseActive = false;*/
  private boolean engineActive = false;
  private boolean topGear = false;
  private boolean braking = false;
  private boolean accelerating = false;
  private boolean cruising = false;
  private ThrottleController throttle;
  private boolean throttleStarted = false;
  private SpeedMonitor speed;
  private ShaftSimulator shaft;

  public CruiseControl(ThrottleController throttle, SpeedMonitor speed,
      ShaftSimulator shaft) {
    this.throttle = throttle;
    this.speed = speed;
    this.shaft = shaft;
  }

  public synchronized void activate() {
    System.out.println("CRUISE ACTIVATING");
    if (engineActive & topGear & !braking) {
      cruising = true;
      throttle.setCruiseSpeed(speed.getCurrentSpeed());
      if (throttleStarted) {
        throttle.schedulePeriodic();
      }
      else {
        throttleStarted = true;
        /* Redundant now since the throttle is never stopped. */
        /*throttle.start();*/
        throttle.schedulePeriodic();
      }
    }
  }

  public synchronized void deactivate() {
    System.out.println("CRUISE DEACTIVATING");
    if (engineActive & topGear & !braking & cruising) {
      /* why not just cruising */
      cruising = false;
      throttle.deschedulePeriodic();
    }
  }

  public synchronized void startAcceleration() {
    System.out.println("CRUISE ACCELERATING");
    if (engineActive & topGear & !braking) {
      accelerating = true;
      if (throttleStarted) {
        throttle.schedulePeriodic();
      }
      else {
        throttleStarted = true;
        /* Redundant now since the throttle is never stopped. */
        /*throttle.start();*/
        throttle.schedulePeriodic();
      }
      throttle.accelerate();
    }
  }

  public synchronized void stopAcceleration() {
    System.out.println("CRUISE DE-ACCELERATING");
    if (engineActive & topGear & !braking & accelerating) {
      accelerating = false;
      cruising = true;
      throttle.setCruiseSpeed(speed.getCurrentSpeed());
    }
  }

  public synchronized void resume() {
    System.out.println("CRUISE RESUME");
    if (topGear & !braking & throttleStarted) {
      System.out.println("CRUISE RESUME: start Throttle");
      cruising = true;
      throttle.schedulePeriodic();
    }
  }

  public synchronized void engineOn() {
    System.out.println("CRUISE ENGINE ON");
    engineActive = true;
    braking = false;
    topGear = false;
    cruising = false;
  }

  public synchronized void engineOff() {
    System.out.println("CRUISE ENGINE OFF");
    engineActive = false;
    braking = false;
    topGear = false;
    if (cruising) {
      cruising = false;
      throttle.deschedulePeriodic();
    }
  }

  public synchronized void topGearEngaged() {
    System.out.println("CRUISE TOP GEAR");
    if (engineActive) topGear = true;
  }

  public synchronized void topGearDisengaged() {
    System.out.println("CRUISE LOW GEAR");
    if (engineActive) {
      topGear = false;
      if (cruising) {
        cruising = false;
        throttle.deschedulePeriodic();
      }
    }
  }

  public synchronized void brakeEngaged() {
    System.out.println("CRUISE BRAKE ON");
    if (engineActive) {
      if (cruising) {
        cruising = false;
        throttle.deschedulePeriodic();
      }
      braking = true;
      /* Direct access replaced by a synchonized method call. */
      shaft.setBraking(true);
    }
  }

  public void brakeDisengaged() {
    System.out.println("CRUISE BRAKE OFF");
    if (engineActive) {
      braking = false;
      /* Direct access replaced by a synchonized method call. */
      shaft.setBraking(false);
    }
  }
}
