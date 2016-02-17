package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.circus.SCJApplication;

/**
 * Data model for a {@code @BoundEvent} annotation. This annotation captures
 * a particular external event bound to an aperiodic handler.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class BoundEventModel extends DataModel {
   /**
    * Name of the communication channel that releases the handler.
    */
   public final String channel;
   /**
    * Z type of an input, or {@code null} if no input is communicated
    * through the channel.
    */
   public final String type;

   /**
    * Constructs a data model for a bound <i>input</i> event.
    *
    * @param context SCJ application context of the data model.
    * @param channel Name of the associated (typed) channel.
    * @param type Z type of the input communicated through the channel.
    */
   public BoundEventModel(SCJApplication context, String channel, String type) {
      super(context);
      assert channel != null;
      assert type != null;
      this.channel = channel;
      this.type = type;
   }

   /**
    * Constructs a data model for a bound <i>synchronisation</i> event.
    *
    * @param context SCJ application context of the data model.
    * @param channel Name of the associated (typeless) channel.
    */
   public BoundEventModel(SCJApplication context, String channel) {
      super(context);
      assert channel != null;
      this.channel = channel;
      this.type = null;
   }

   /**
    * Returns the channel name of the bound event.
    *
    * @return Channel name as a {@code String}.
    */
   public String getChannel() {
      return channel;
   }

   /**
    * Returns the channel type of the bound event.
    *
    * @return Channel name as a {@code String}.
    */
   public String getType() {
      return type;
   }

   /**
    * Determines if an input value is communicated through the channel.
    *
    * @return True if the channel has a type and otherwise false if the
    * channel is typeless. 
    */
   public boolean hasType() {
      return type != null;
   }
}
