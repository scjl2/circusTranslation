package jtres;

import javax.realtime.PriorityParameters;

import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

public class Outputs {
   /* Aperiodic Handlers for Outputs */
   public final ConnectHandler connect_handler;
   public final SendHandler send_handler;
   public final DisconnectHandler disconnect_handler;

   public Outputs(Events events, Network network, List list) {
      /* Created in Mission Memory */
      connect_handler = new ConnectHandler(network,
         new PriorityParameters(
            PriorityScheduler.instance().getMaxPriority()),
         new StorageParameters(4096, 4096, 4096),
         events.connect);

      /* Created in Mission Memory */
      send_handler = new SendHandler(network, list,
         new PriorityParameters(
            PriorityScheduler.instance().getMaxPriority()),
         new StorageParameters(4096, 4096, 4096),
         events.send);

      /* Created in Mission Memory */
      disconnect_handler = new DisconnectHandler(network,
         new PriorityParameters(
            PriorityScheduler.instance().getMaxPriority()),
         new StorageParameters(4096, 4096, 4096),
         events.disconnect);
   }

   /* Register Output Handlers with the current Mission */
   public void register() {
      connect_handler.register();
      send_handler.register();
      disconnect_handler.register();
   }
}
