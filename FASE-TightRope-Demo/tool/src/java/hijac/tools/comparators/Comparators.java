package hijac.tools.comparators;

import hijac.tools.analysis.AnalysisTask;

import java.util.Comparator;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

/* This class provides static instances of various universal comparators. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Comparators {
   public static final Comparator<ExecutableElement>
      ExecutableElement = new UniversalComparator<ExecutableElement>();

   public static final Comparator<TypeMirror>
      TypeMirror = new UniversalComparator<TypeMirror>();

   public static final Comparator<AnalysisTask>
      AnalysisTask = new UniversalComparator<AnalysisTask>();
}
