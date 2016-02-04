package hijac.tools.modelgen;

import hijac.tools.analysis.SCJAnalysis;

import javax.lang.model.type.TypeMirror;

/**
 * Represents an SCJ application that has been parsed, type-checked and
 * analysed.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class SCJApplication {
   /**
    * Associated {@link SCJAnalysis} object of the application.
    */
   public final SCJAnalysis ANALYSIS;

   /**
    * Constructs an SCJ application context for a given analysis.
    */
   public SCJApplication(SCJAnalysis analysis) {
      assert analysis != null;
      ANALYSIS = analysis;
   }
   
 

   /**
    * Returns the analysis of the application context.
    *
    * @return {@link SCJAnalysis} object of the application.
    */
   public SCJAnalysis getAnalysis() {
      return ANALYSIS;
   }
}
