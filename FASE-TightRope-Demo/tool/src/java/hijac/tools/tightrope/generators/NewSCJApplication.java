package hijac.tools.tightrope.generators;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.modelgen.circus.AnnotationTypes;
import hijac.tools.modelgen.circus.templates.CircusTemplates;

/**
 * Represents an SCJ application that has been parsed, type-checked and
 * analysed in the context of the <i>Circus</i> translator.
 *

 */
public class NewSCJApplication extends hijac.tools.modelgen.circus.SCJApplication {
   /**
    * Utility objects that provides access to annotation types.
    */
   public final AnnotationTypes ANNOTS;

   /**
    * Utility objects that provides access to FreeMarker templates.
    */
   public final NewCircusTemplates TEMPLATES;

   /**
    * Constructs an SCJ application context for a given analysis.
    */
   public NewSCJApplication(SCJAnalysis analysis) {
	   super(analysis, true);
      ANNOTS = new AnnotationTypes(this);
      TEMPLATES = new NewCircusTemplates(this);
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
