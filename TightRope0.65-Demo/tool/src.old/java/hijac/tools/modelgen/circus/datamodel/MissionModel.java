package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.circus.SCJApplication;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Data model for translating {@code Mission} application classes.
 *
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class MissionModel extends ActiveClassModel {
   /**
    * Constructs a data model for a {@code Mission} class.
    *
    * @param context SCJ application context in which the class resides.
    * @param target Mission class for whose translation the data model is
    * used.
    */
   public MissionModel(SCJApplication context, ClassModel target) {
      super(context, target);
   }

   /**
    * Determines the id of the mission class by probing its annotations.
    *
    * @return Mission id as specified by the {@code @MissionId} annotation.
    */
   public String getmissionId() {
      TypeElement type_element = getclass().getTypeElement();
      Elements ELEMENTS = getContext().getAnalysis().ELEMENTS;
      List <? extends AnnotationMirror> annotations =
         ELEMENTS.getAllAnnotationMirrors(type_element);
      for (AnnotationMirror annotation : annotations) {
         if (annotation.getAnnotationType().equals(CONTEXT.ANNOTS.MISSION_ID)) {
            return MODEL.getAnnotationValue(annotation).getValue().toString();
         }
      }
      /* TODO: Throw a translation error here since the MissionId annotation
       * is not present in the concrete mission class. */
      assert false;
      return null;
   }
}
