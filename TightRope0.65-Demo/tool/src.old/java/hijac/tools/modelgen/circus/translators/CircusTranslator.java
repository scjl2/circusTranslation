package hijac.tools.modelgen.circus.translators;

import hijac.tools.modelgen.CompositeTranslator;
import hijac.tools.modelgen.circus.SCJApplication;

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class CircusTranslator extends CompositeTranslator<SCJApplication> {
   public CircusTranslator() {
      super("CircusTranslator");
      add(new SafeletTranslator());
      add(new MissionSequencerTranslator());
      add(new MissionTranslator());
      add(new AperiodicHandlerTranslator());
      add(new PeriodicHandlerTranslator());
      add(new DataObjectTranslator());
   }

   public boolean execute(SCJApplication context) {
      try {
         setContext(context);
         return invokeOnAllTargets();
      }
      finally {
         getOutput().close();
      }
   }
}
