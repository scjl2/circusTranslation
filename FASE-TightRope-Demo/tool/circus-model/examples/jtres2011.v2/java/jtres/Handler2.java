package jtres;

import javax.realtime.*;

import javax.safetycritical.*;

public class Handler2 extends AperiodicEventHandler {
   /* Object to access the network (Resides in MissionMemory). */
   private Network network;

   /* Object to access the output events. */
   private Events events;

   public Handler2(Network network, Events events,
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicEvent event) {
      super(priority, storage, event, "Handler2");
      this.network = network;
      this.events = events;
   }

   public void handleAsyncEvent() {
      /* I don't see a solution here other than busy wait. */
      /* The wait() and notify() mechanisms are only support at level 2. */
      while (network.getState() != NetworkState.DISCONNECTED) { }
      events.connect.fire();
      while (network.getState() != NetworkState.CONNECTED) { }
      events.send.fire();
      while (network.getState() != NetworkState.SENT) { }
      events.disconnect.fire();
   }
}
