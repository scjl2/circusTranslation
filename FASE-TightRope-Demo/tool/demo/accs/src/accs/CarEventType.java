/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

/* I have slightly changed the design by introducing this enumeration to
 * replace the integer constants that were previously used for events. */

public enum CarEventType {
  ENGINE_OFF("ENGINE_OFF"),
  ENGINE_ON("ENGINE_ON"),
  LEVER_DEACTIVATE("LEVER_DEACTIVATE"),
  LEVER_RESUME("LEVER_RESUME"),
  LEVER_ACTIVATE("LEVER_ACTIVATE"),
  LEVER_START_ACCELERATING("LEVER_START_ACCELERATING"),
  LEVER_STOP_ACCELERATING("LEVER_STOP_ACCELERATING"),
  GEAR_TOP("GEAR_TOP"),
  GEAR_LOWER("GEAR_LOWER"),
  BRAKE_ON("BRAKE_ON"),
  BRAKE_OFF("BRAKE_OFF");

  private final String name;
  
  CarEventType(String name) {
    this.name = name;
  }

  /* Note that this name must also be used in the scenario file. */
  public String toString() {
    return name;
  }
}
