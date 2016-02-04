package jtres;

import javax.realtime.*;

import javax.safetycritical.*;

public class Events {
   /* Events */
   public final AperiodicEvent out;

   /* Note that there are no Aperiodic[Long]Event objects for the remaining
    * channels enable, send, and disable. The reason is that I modelled the
    * corresponding events by calls on a Network object which simulates the
    * network resource. An alternative and slightly more complex program
    * could indeed introduce an SCJ event for each of those channels and fire
    * associated handlers in a cascade. */

   /* Create Events */
   public Events() {
      /* Created in Mission Memory */
      out = new AperiodicEvent();
   }
}
