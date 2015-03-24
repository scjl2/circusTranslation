package hijac.tools.tightrope.translators;

import hijac.tools.modelgen.AbstractTranslator;
import hijac.tools.modelgen.CompositeTranslator;
import hijac.tools.modelgen.circus.SCJApplication;

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class Level2Translator extends CompositeTranslator<SCJApplication> {
   public Level2Translator() {
      super("level2Translator");
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
