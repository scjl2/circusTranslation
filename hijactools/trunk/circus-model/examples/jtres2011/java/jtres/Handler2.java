package jtres;

import javax.realtime.*;

import javax.safetycritical.*;

public class Handler2 extends AperiodicEventHandler {
   /* Object to access the network (Resides in MissionMemory). */
   private Network network;

   /* Shared Data in Mission Memory */
   private List list;

   public Handler2(List list,
      PriorityParameters priority,
      StorageParameters storage,
      AperiodicEvent event) {
      super(priority, storage, event, "Handler2");
      /* Created in Mission Memory */
      Network network = new Network();

      /* Set ceiling to execute network methods at the highest priority. */
      Services.setCeiling(network,
         javax.safetycritical.PriorityScheduler.instance().getMaxPriority());
   }

   public void handleAsyncEvent() {
      int size = list.size();
      /* The following method calls all execute at a high priority. */
      /* We moreover consider their execution as instantaneous. */
      network.enable();
      network.send(size);
      network.disable();
   }
}
