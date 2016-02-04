package hijac.tools.modelgen.circus;

import hijac.tools.config.Config;
import hijac.tools.utils.ModelUtils;

import java.util.List;
import java.util.ArrayList;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

public class AnnotationTypes {
   /**
    * Annotation type used to identify active classes.
    */
   public final TypeMirror ACTIVE_CLASS;

   /**
    * Annotation type used to identify data objects classes.
    */
   public final TypeMirror PASSIVE_CLASS;

   /**
    * Annotation type used to identify interaction classes.
    */
   public final TypeMirror INTERACTION_CLASS;

   /**
    * Annotation type used to identify interaction code.
    */
   public final TypeMirror INTERACTION_CODE;

   /**
    * Annotation type used to identify active methods of a class.
    */
   public final TypeMirror ACTIVE_METHOD;

   /**
    * Annotation type used to identify framework methods of a class.
    */
   public final TypeMirror FRAMEWORK_METHOD;

   /**
    * Annotation type used to identify methods for device access.
    */
   public final TypeMirror DEVICE_ACCESS;

   /**
    * Annotation type used to specify mission identifiers.
    */
   public final TypeMirror MISSION_ID;

   /**
    * Annotation type used to specify handler identifiers.
    */
   public final TypeMirror HANDLER_ID;

   /**
    * Annotation type used to associated handlers with single event.
    */
   public final TypeMirror BOUND_EVENT;

   /**
    * Annotation type used to associated handlers with multiple event.
    */
   public final TypeMirror BOUND_EVENTS;

   /**
    * Annotation type used to identify ignored elements of a class.
    */
   public final TypeMirror IGNORE;

   /**
    * Utility object used to obtain and process annotation.
    */
   public final ModelUtils MODEL;

   /**
    * The constructor initialises the public constants for the various
    * annotation types.
    *
    * @param context SCJ application context for which the annotation
    * types are constructed.
    */
   public AnnotationTypes(SCJApplication context) {
      MODEL = new ModelUtils(context);
      ACTIVE_CLASS = getAnnotationType("ActiveClass");
      PASSIVE_CLASS = getAnnotationType("PassiveClass");
      INTERACTION_CLASS = getAnnotationType("InteractionClass");
      INTERACTION_CODE = getAnnotationType("InteractionCode");
      ACTIVE_METHOD = getAnnotationType("ActiveMethod");
      FRAMEWORK_METHOD = getAnnotationType("FrameworkMethod");
      DEVICE_ACCESS = getAnnotationType("DeviceAccess");
      MISSION_ID = getAnnotationType("MissionId");
      HANDLER_ID = getAnnotationType("HandlerId");
      BOUND_EVENT = getAnnotationType("BoundEvent");
      BOUND_EVENTS = getAnnotationType("BoundEvents");
      IGNORE = getAnnotationType("Ignore");
   }

   /**
    * Utility method to facilitate creation of the annotation types.
    *
    * @param name Name of the annotation whose {@link TypeMirror} is obtained.
    */
   protected TypeMirror getAnnotationType(String name) {
	   System.out.println("Getting Annotation Type. Name = " + name + " SCJPackagePrefix = " + Config.getSCJPackagePrefix() );
	   System.out.println( Config.getSCJPackagePrefix() + ".annotate." + name);
	   
      return MODEL.ELEMENTS.getTypeElement(
         Config.getSCJPackagePrefix() + 
         ".annotate." + name)
         .asType();
   }

   /**
    * Determines whether a class is annotated as active.
    *
    * @param type_element JDK model element to be probed.
    * @return True if the element is annotated with {@code @ActiveClass}.
    */
   public boolean isActiveClass(TypeElement type_element) {
      for (AnnotationMirror annotation :
            MODEL.ELEMENTS.getAllAnnotationMirrors(type_element)) {
         if (annotation.getAnnotationType().equals(ACTIVE_CLASS)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether a class is annotated as a data object.
    *
    * @param type_element JDK model element to be probed.
    * @return True if the element is annotated with {@code @PassiveClass}.
    */
   public boolean isPassiveClass(TypeElement type_element) {
      for (AnnotationMirror annotation :
            MODEL.ELEMENTS.getAllAnnotationMirrors(type_element)) {
         if (annotation.getAnnotationType().equals(PASSIVE_CLASS)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether a class is annotated as an interaction class.
    *
    * @param type_element JDK model element to be probed.
    * @return True if the element is annotated with {@code @InteractionClass}.
    */
   public boolean isInteractionClass(TypeElement type_element) {
      for (AnnotationMirror annotation :
            MODEL.ELEMENTS.getAllAnnotationMirrors(type_element)) {
         if (annotation.getAnnotationType().equals(INTERACTION_CLASS)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether a class is annotated as interaction code.
    *
    * @param type_element JDK model element to be probed.
    * @return True if the element is annotated with {@code @InteractionCode}.
    */
   public boolean isInteractionCode(TypeElement type_element) {
      for (AnnotationMirror annotation :
            MODEL.ELEMENTS.getAllAnnotationMirrors(type_element)) {
         if (annotation.getAnnotationType().equals(INTERACTION_CODE)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether a method is annotated as interaction code.
    *
    * <p>Note that the annotation probing here also properly deals with
    * inherited annotations of the method, which the JDK does not handle
    * as one might expect by default.</p>
    *
    * @param exec_element JDK model element to be probed.
    * @return True if the element is annotated with {@code @InteractionCode}.
    */
   public boolean isInteractionCode(ExecutableElement exec_element) {
      for (AnnotationMirror annotation :
            MODEL.getAllMethodAnnotationMirrors(exec_element)) {
         if (annotation.getAnnotationType().equals(INTERACTION_CODE)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether an element is annotated as interaction code.
    *
    * @param element JDK model element to be probed.
    * @return True if the element is annotated with {@code @InteractionCode}.
    */
   public boolean isInteractionCode(Element element) {
      for (AnnotationMirror annotation :
            MODEL.ELEMENTS.getAllAnnotationMirrors(element)) {
         if (annotation.getAnnotationType().equals(INTERACTION_CODE)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether a method is annotated as active.
    *
    * <p>Note that the annotation probing here also properly deals with
    * inherited annotations of the method, which the JDK does not handle
    * as one might expect by default.</p>
    *
    * @param exec_element JDK model element to be probed.
    * @return True if the element is annotated with {@code @ActiveMethod}.
    */
   public boolean isActiveMethod(ExecutableElement exec_element) {
      for (AnnotationMirror annotation :
            MODEL.getAllMethodAnnotationMirrors(exec_element)) {
         if (annotation.getAnnotationType().equals(ACTIVE_METHOD)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether a method is annotated as a framework method.
    *
    * <p>Note that the annotation probing here also properly deals with
    * inherited annotations of the method, which the JDK does not handle
    * as one might expect by default.</p>
    *
    * @param exec_element JDK model element to be probed.
    * @return True if the element is annotated with {@code @ActiveMethod}.
    */
   public boolean isFrameworkMethod(ExecutableElement exec_element) {
      for (AnnotationMirror annotation :
            MODEL.getAllMethodAnnotationMirrors(exec_element)) {
         if (annotation.getAnnotationType().equals(FRAMEWORK_METHOD)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether a method is annotated with a device access.
    *
    * <p>Note that the annotation probing here also properly deals with
    * inherited annotations of the method, which the JDK does not handle
    * as one might expect by default.</p>
    *
    * @param exec_element JDK model element to be probed.
    * @return True if the element is annotated with {@code @DeviceAccess}.
    */
   public boolean isDeviceAccess(ExecutableElement exec_element) {
      for (AnnotationMirror annotation :
            MODEL.getAllMethodAnnotationMirrors(exec_element)) {
         if (annotation.getAnnotationType().equals(DEVICE_ACCESS)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether a method is annotated to be ignored.
    *
    * <p>Note that the annotation probing here also properly deals with
    * inherited annotations of the method, which the JDK does not handle
    * as one might expect by default.</p>
    *
    * @param exec_element JDK model element to be probed.
    * @return True if the element is annotated with {@code @Ignore}.
    */
   public boolean isIgnored(ExecutableElement exec_element) {
      for (AnnotationMirror annotation :
            MODEL.getAllMethodAnnotationMirrors(exec_element)) {
         if (annotation.getAnnotationType().equals(IGNORE)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines an element is annotated to be ignored.
    *
    * @param element JDK model element to be probed.
    * @return True if the element is annotated with {@code @Ignore}.
    */
   public boolean isIgnored(Element element) {
      for (AnnotationMirror annotation :
            MODEL.ELEMENTS.getAllAnnotationMirrors(element)) {
         if (annotation.getAnnotationType().equals(IGNORE)) {
            return true;
         }
      }
      return false;
   }
}
