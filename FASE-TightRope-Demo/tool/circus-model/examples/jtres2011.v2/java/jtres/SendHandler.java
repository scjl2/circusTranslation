package jtres;

import javax.realtime.*;

import javax.safetycritical.*;

public class SendHandler extends AperiodicEventHandler {
   /* Object to access the network. (Resides in MissionMemory) */
   private final Network network;

   /* Object used to deduce the output value. (Resides in MissionMemory) */
   private List list;

   public SendHandler(Network network, List list,
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicEvent event) {
      super(priority, storage, event, "SendHandler");
      this.network = network;
   }

   public void handleAsyncEvent() {
      /* Here java.util.Set is used in place of the type \power \num. */
      java.util.Set elems = list.elems();
      /* The method call below carries out the device access. */
      network.send(elems);
   }
}
