package jtres;

import javax.realtime.*;

import javax.safetycritical.*;

public class ConnectHandler extends AperiodicEventHandler {
   /* Object to access the network. (Resides in MissionMemory) */
   private final Network network;

   public ConnectHandler(Network network,
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicEvent event) {
      super(priority, storage, event, "ConnectHandler");
      this.network = network;
   }

   public void handleAsyncEvent() {
      /* The method call below carries out the device access. */
      network.connect();
   }
}
