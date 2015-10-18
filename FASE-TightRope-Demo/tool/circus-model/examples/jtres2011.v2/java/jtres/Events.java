package jtres;

import javax.realtime.*;

import javax.safetycritical.*;

public class Events {
   /* Events */
   public final AperiodicLongEvent in;
   public final AperiodicEvent out;
   public final AperiodicEvent connect;
   public final AperiodicEvent send;
   public final AperiodicEvent disconnect;
   /*public final AperiodicEvent force;*/

   /* Create Events */
   public Events() {
      /* Created in Mission Memory */
      in = new AperiodicLongEvent();
      out = new AperiodicEvent();
      connect = new AperiodicEvent();
      send = new AperiodicEvent();
      disconnect = new AperiodicEvent();
      /*force = new AperiodicEvent();*/
   }
}
