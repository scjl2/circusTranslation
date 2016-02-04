package hijac.tools.analysis.tasks;

import hijac.tools.analysis.AbstractAnalysisTask;
import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.analysis.SCJDataKey;
import hijac.tools.config.Types;
import hijac.tools.config.Hijac;
import hijac.tools.utils.Strings;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Frank Zeyda
 * @version $Revision: 207 $
 */
public class SCJTypesTask extends AbstractAnalysisTask {
   public static final SCJDataKey[] REQUIRES = { };
   public static final SCJDataKey[] GENERATES = {
      Hijac.key("SafeletType"),
      Hijac.key("MissionSequencerType"),
      Hijac.key("MissionType"),
      Hijac.key("AperiodicEventHandlerType"),
      Hijac.key("AperiodicLongEventHandlerType"),
      Hijac.key("PeriodicEventHandlerType")
   };

   public SCJTypesTask() {
      super("SCJTypesTask", REQUIRES, GENERATES);
   }

   public void findSCJType(String name) {
      String fullname = Types.SCJ_PACKAGE_PREFIX + "." + name;
      TypeElement element = ANALYSIS.getTypeElement(fullname);
      if (element == null) {
         addError("Failed to locate " + fullname + " class or interface.");
         return;
      }
      TypeMirror type = element.asType();
      if (type == null) {
         addError("Failed to determine type of " + fullname + ".");
         return;
      }
      ANALYSIS.put(Hijac.key(name + "Type"), type);
   }

   public void doTask() {
      findSCJType("Safelet");
      findSCJType("MissionSequencer");
      findSCJType("Mission");
      findSCJType("AperiodicEventHandler");
      findSCJType("AperiodicLongEventHandler");
      findSCJType("PeriodicEventHandler");
   }
}
