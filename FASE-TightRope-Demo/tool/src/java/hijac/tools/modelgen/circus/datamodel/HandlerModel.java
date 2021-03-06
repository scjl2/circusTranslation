package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.modelgen.circus.SCJApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Data model for translating the various handler classes.
 *
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class HandlerModel extends ActiveClassModel {
   /**
    * Constructs a data model for a handler class.
    *
    * @param context SCJ application context in which the class resides.
    * @param target Handler class for whose translation the data model is
    * used.
    */
   public HandlerModel(SCJApplication context, ClassModel target) {
      super(context, target);
   }

   /**
    * Determines the id of the handler class by probing its annotations.
    *
    * @return Handler id as specified by the {@code @HandlerId} annotation.
    */
   public String gethandlerId() {
      TypeElement type_element = getclass().getTypeElement();
      Elements ELEMENTS = getContext().getAnalysis().ELEMENTS;
      List <? extends AnnotationMirror> annotations =
         ELEMENTS.getAllAnnotationMirrors(type_element);
      for (AnnotationMirror annotation : annotations) {
         if (annotation.getAnnotationType().equals(CONTEXT.ANNOTS.HANDLER_ID)) {
            return MODEL.getAnnotationValue(annotation).getValue().toString();
         }
      }
      /* TODO: Throw a translation error here since the HandlerId annotation
       * is not present in the concrete handler class. */
      assert false;
      return null;
   }

   public List<BoundEventModel> getboundEvents() {
      List<BoundEventModel> result = new ArrayList<BoundEventModel>();
      TypeElement type_element = getclass().getTypeElement();
      Elements ELEMENTS = getContext().getAnalysis().ELEMENTS;
      List <? extends AnnotationMirror> annotations =
         ELEMENTS.getAllAnnotationMirrors(type_element);
      for (AnnotationMirror annotation : annotations) {
         if (annotation.getAnnotationType().equals(
               CONTEXT.ANNOTS.BOUND_EVENT)) {
            addBoundEventAnnotation(annotation, result);
         }
         if (annotation.getAnnotationType().equals(
               CONTEXT.ANNOTS.BOUND_EVENTS)) {
            @SuppressWarnings("unchecked")
            List<AnnotationMirror> nested_annotations =
               (List<AnnotationMirror>)
                  MODEL.getAnnotationValue(annotation).getValue();
            addBoundEventAnnotations(nested_annotations, result);
         }
      }
      return result;
   }

   public boolean hasInputType() {
      List<BoundEventModel> bound_events = getboundEvents();
      BoundEventModel bound_event = bound_events.get(0);
      return bound_event.hasType();
   }

   public String getinputType() {
      assert hasInputType();
      List<BoundEventModel> bound_events = getboundEvents();
      BoundEventModel bound_event = bound_events.get(0);
      return bound_event.getType();
   }

   protected void addBoundEventAnnotation(
      AnnotationMirror annotation, List<BoundEventModel> result) {
      assert annotation.getAnnotationType().equals(
         CONTEXT.ANNOTS.BOUND_EVENT);
      String channel = (String)
         MODEL.getAnnotationValue(annotation).getValue();
      String type = (String)
         MODEL.getAnnotationValue(annotation, "type").getValue();
      if (type.equals("")) {
         result.add(new BoundEventModel(CONTEXT, channel));
      }
      else {
         result.add(new BoundEventModel(CONTEXT, channel, type));
      }
   }

   protected void addBoundEventAnnotations(
      List<AnnotationMirror> annotations, List<BoundEventModel> result) {
      for (AnnotationMirror annotation : annotations) {
         if (annotation.getAnnotationType().equals(
               CONTEXT.ANNOTS.BOUND_EVENT)) {
            addBoundEventAnnotation(annotation, result);
         }
         else {
            /* Generate a translation error here? */
         }
      }
   }
}
