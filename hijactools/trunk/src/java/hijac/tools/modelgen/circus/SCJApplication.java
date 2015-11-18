package hijac.tools.modelgen.circus;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.modelgen.circus.templates.CircusTemplates;

/**
 * Represents an SCJ application that has been parsed, type-checked and
 * analysed in the context of the <i>Circus</i> translator.
 *
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class SCJApplication extends hijac.tools.modelgen.SCJApplication {
   /**
    * Utility objects that provides access to annotation types.
    */
   public /*final*/ AnnotationTypes ANNOTS;

   /**
    * Utility objects that provides access to FreeMarker templates.
    */
   public /*final*/ CircusTemplates TEMPLATES;

   /**
    * Constructs an SCJ application context for a given analysis.
    */
   public SCJApplication(SCJAnalysis analysis) {
      super(analysis);
      ANNOTS = new AnnotationTypes(this);
      TEMPLATES = new CircusTemplates(this);
   }

   public SCJApplication(SCJAnalysis analysis, boolean skip)
   {
	   super(analysis);
   }
   /**
    * Returns the utility object providing annotation types.
    *
    * @return Utility object providing {@link TypeMirror}s of annotations.
    */
   public AnnotationTypes getAnnotationTypes() {
      return ANNOTS;
   }

   /**
    * Returns the utility object providing access to FreeMarker templates.
    *
    * @return Utility object for FreeMarker templates used by the
    * <i>Circus</i> translator.
    */
   public CircusTemplates getTemplates() {
      return TEMPLATES;
   }
}
