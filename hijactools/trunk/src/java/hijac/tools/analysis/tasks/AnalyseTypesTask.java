package hijac.tools.analysis.tasks;

import hijac.tools.analysis.AbstractAnalysisTask;
import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.analysis.SCJDataKey;
import hijac.tools.config.Hijac;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class AnalyseTypesTask extends AbstractAnalysisTask {
   public static final SCJDataKey[] REQUIRES =
      SCJTypesTask.GENERATES;

   public static final SCJDataKey[] GENERATES = {
      Hijac.key("SafeletTypes"),
      Hijac.key("MissionSequencerTypes"),
      Hijac.key("MissionTypes"),
      Hijac.key("PeriodicEventHandlerTypes"),
      Hijac.key("AperiodicEventHandlerTypes")
   };

   public AnalyseTypesTask() {
      super("AnalyseTypesTask", REQUIRES, GENERATES);
   }

   public void findSubTypeElements(String name) {
      final TypeMirror type = (TypeMirror)
         ANALYSIS.get(Hijac.key(name + "Type"));
      List<TypeElement> list = new ArrayList<TypeElement>();
      for (TypeElement element : ANALYSIS.getTypeElements()) {
         if (element.getKind() == ElementKind.CLASS &&
               ANALYSIS.TYPES.isSubtype(element.asType(), type)) {
            list.add(element);
         }
      }
      ANALYSIS.put(Hijac.key(name + "Types"), list);
   }

   public void doTask() {
      findSubTypeElements("Safelet");
      findSubTypeElements("MissionSequencer");
      findSubTypeElements("Mission");
      findSubTypeElements("PeriodicEventHandler");
      findSubTypeElements("AperiodicEventHandler");
   }
}
