package hijac.tools.utils;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.modelgen.SCJApplication;

import java.lang.annotation.Inherited;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Utility class that facilitates processing of JDK model elementsand
 * types.</p>
 *
 * @author Frank Zeyda
 * @version $Revision: 212 $
 */
public class ModelUtils {
   /**
    * <p>Utility constant that is initialised with the {@code TypeMirror} of
    * the {@code @Inherited} annotation.</p>
    */
   protected final TypeMirror INHERITED;

   /**
    * <p>{@code Elements} utility object used by this class.</p>
    */
   public final Elements ELEMENTS;

   /**
    * <p>{@code Types} utility object used by this class.</p>
    */
   public final Types TYPES;

   /**
    * <p>Core constructor of the class.</p>
    *
    * <p>We have to provide instances of the {@link Elements} and
    * {@link Types} utility classes of the JDK model API.
    *
    * @param elements {@code Elements} utility to be used.
    * @param types {@code Types} utility to be used.
    */
   public ModelUtils(Elements elements, Types types) {
      assert elements != null;
      assert types != null;
      ELEMENTS = elements;
      TYPES = types;
      INHERITED = ELEMENTS.getTypeElement(
         "java.lang.annotation.Inherited").asType();
   }

   /**
    * <p>Constructs a class object from an SCJ analysis object.</p>
    *
    * @param analysis {@link SCJAnalysis} object that is used to obtain
    * the  required {@link Elements} and {@link Types} utility objects.
    */
   public ModelUtils(SCJAnalysis analysis) {
      this(analysis.ELEMENTS, analysis.TYPES);
   }

   /**
    * <p>Constructs a class object from an SCJ application context.</p>
    *
    * @param context {@link SCJApplication} object that is used to obtain
    * the  required {@link Elements} and {@link Types} utility objects.
    */
   public ModelUtils(SCJApplication context) {
      this(context.ANALYSIS);
   }

   /**
    * <p>Returns all interfaces of a given type element. This includes the
    * interfaces of superclasses as well as subinterfaces.</p>
    *
    * @param type_element Type element which may be a class or interface.
    *
    * @return All interfaces of this type element.
    */
   public List<? extends TypeMirror> getAllInterfaces(
      TypeElement type_element) {
      List<TypeMirror> result = new ArrayList<TypeMirror>();
      getAllInterfaces(type_element, result);
      return result;
   }

   /**
    * <p>Utility method to implement {@link #getAllInterfaces(TypeElement)}.</p>
    *
    * @param type_element Type element which may be a class or interface.
    * @param result Mutable list that is used to accumulate the result.
    */
   protected void getAllInterfaces(
      TypeElement type_element, List<TypeMirror> result) {
      assert result != null;
      if (type_element != null) {
         @SuppressWarnings("unchecked")
         List<TypeMirror> interface_types =
            (List<TypeMirror>) type_element.getInterfaces();
         for (TypeMirror interface_type : interface_types) {
            if (!result.contains(interface_type)) {
               result.add(interface_type);
               TypeElement interface_element = (TypeElement)
                  TYPES.asElement(interface_type);
               getAllInterfaces(interface_element, result);
            }
         }
         /* Note that getSuperclass() eventually returns NoType. */
         TypeElement super_class = (TypeElement)
            TYPES.asElement(type_element.getSuperclass());
         getAllInterfaces(super_class, result);
      }
   }

   /**
    * <p>For a given method, obtains the interface method that it
    * implements.</p>
    *
    * @param method Method for which to obtain the interface method.
    *
    * @return Interface method implemented by the given method, or
    * {@code null} if the method does not implement an interface method.
    */
   protected ExecutableElement findInterfaceMethod(ExecutableElement method) {
      TypeElement type_element =
         (TypeElement) method.getEnclosingElement();
      assert type_element.getKind() == ElementKind.CLASS;
      for (TypeMirror interface_type : getAllInterfaces(type_element)) {
         TypeElement interface_element = (TypeElement)
            TYPES.asElement(interface_type);
         for (ExecutableElement interface_method :
            ElementFilter.methodsIn(
               interface_element.getEnclosedElements())) {
            if (isSameMethod(method, interface_method)) {
               return interface_method;
            }
         }
      }
      return null;
   }

   /**
    * <p>Returns all annotations of a method, included those defined in a
    * superclass and marked as {@code @Inherited}.</p>
    *
    * <p>Note that Java by default does not account for inherited
    * annotations in methods.</p>
    *
    * @param method Method for which the annotations are returned.
    *
    * @return All annotations of the method, including inherited ones.
    */
   public List<? extends AnnotationMirror> getAllMethodAnnotationMirrors(
         ExecutableElement method) {
      TypeElement type_element =
         (TypeElement) method.getEnclosingElement();
      assert
         type_element.getKind() == ElementKind.CLASS ||
         type_element.getKind() == ElementKind.INTERFACE;
      List<AnnotationMirror> result = new ArrayList<AnnotationMirror>();
      /* First gather direct and inherited annotations from subclasses. */
      boolean superclass = false;
      while (type_element != null) {
         for (ExecutableElement class_method : ElementFilter.methodsIn(
               type_element.getEnclosedElements())) {
            if (isSameMethod(method, class_method)) {
               List<? extends AnnotationMirror> annotations =
                  class_method.getAnnotationMirrors();
               for (AnnotationMirror annotation : annotations) {
                  if (!superclass || isInherited(annotation)) {
                     if (!result.contains(annotation)) {
                        result.add(annotation);
                     }
                  }
               }
            }
         }
         /* Note that getSuperclass() eventually returns NoType. */
         type_element = (TypeElement)
            TYPES.asElement(type_element.getSuperclass());
         superclass = true;
      }
      /* Secondly include annotation from interface methods. */
      if (method.getEnclosingElement().getKind() != ElementKind.INTERFACE) {
         ExecutableElement interface_method = findInterfaceMethod(method);
         if (interface_method != null) {
            List<? extends AnnotationMirror> annotations =
               interface_method.getAnnotationMirrors();
            for (AnnotationMirror annotation : annotations) {
               if (isInherited(annotation)) {
                  if (!result.contains(annotation)) {
                  result.add(annotation);
                  }
               }
            }
         }
      }
      return result;
   }

   /**
    * <p>Determines if two methods have the same name and signature.</p>
    *
    * <p>Note that they do not have to be the same methods, namely they
    * can be defined in different classes or interfaces.</p>
    *
    * @return True if both methods have the same name and signature.
    */
   protected boolean isSameMethod(
         ExecutableElement method1, ExecutableElement method2) {
      ExecutableType type1 = (ExecutableType) method1.asType();
      ExecutableType type2 = (ExecutableType) method2.asType();
      return method1.getSimpleName().equals(method2.getSimpleName()) &&
         TYPES.isSubsignature(type1, type2);
   }

   /**
    * <p>Probes if an annotation is annotated with {@code @Inherited}.</p>
    *
    * @return True if the annotations has a meta-annotation {@link Inherited}.
    */
   public boolean isInherited(AnnotationMirror annotation) {
      TypeElement annot_element = (TypeElement)
         annotation.getAnnotationType().asElement();
      List<? extends AnnotationMirror> meta_annotations =
         ELEMENTS.getAllAnnotationMirrors(annot_element);
      for (AnnotationMirror meta_annotation : meta_annotations) {
         if (meta_annotation.getAnnotationType().equals(INHERITED)){
            return true;
         }
      }
      return false;
   }

   /**
    * <p>Returns a string representation of a {@code TypeElement} using its
    * simple name.</p>
    *
    * @param type_element {@code TypeElement} to be converted into a string.
    *
    * @return Name Simple name of the type element.
    */
   public static String getSimpleName(TypeElement type_element) {
      return type_element.getSimpleName().toString();
   }

   /**
    * <p>Returns a string representation of a {@code TypeElement} using its
    * qualified name.</p>
    *
    * @param type_element {@code TypeElement} to be converted into a string.
    *
    * @return Name Qualified name of the type element.
    */
   public static String getQualifiedName(TypeElement type_element) {
      return type_element.getQualifiedName().toString();
   }

   /**
    * <p>Returns a string representation of a {@code TypeElement} with
    * {@literal "<anonymous ...>"} removed.</p>
    *
    * <p>This done by calling {@code toString()} and
    * {@link #removeAnonymous(String)} on the type element.</p>
    *
    * @param type_element {@code TypeElement} to be converted into a string.
    *
    * @return Name string corresponding to the type element.
    */
   public static String fromTypeElement(TypeElement type_element) {
      return removeAnonymous(type_element.toString());
   }

   /**
    * <p>Removes the {@literal "<anonymous ...>"} marker in strings
    * generated from type elements.</p>
    *
    * @param name String resulting form a call to {@code toString()} on a
    * {@link TypeElement}.
    *
    * @return Name string with {@literal "<anonymous ...>"} removed.
    */
   public static String removeAnonymous(String name) {
      if (name.startsWith("<anonymous ") && name.endsWith(">")) {
         name = name.substring(11, name.length() - 1);
      }
      return name;
   }

   /**
    * <p>Returns an annotation value for a key given as a {@link String}.</p>
    *
    * @param annotation Annotation of which a value is to be returned.
    * @param name Name of the annotation value to be returned.
    *
    * @return Respective value of the annotation.
    */
   public AnnotationValue getAnnotationValue(
         AnnotationMirror annotation, String name) {
      Map<? extends ExecutableElement, ? extends AnnotationValue>
         map = ELEMENTS.getElementValuesWithDefaults(annotation);
      for (ExecutableElement key : map.keySet()) {
         if (key.getSimpleName().toString().equals(name)) {
            return map.get(key);
         }
      }
      return null;
   }

   /**
    * <p>Returns the default value of an annotation.</p>
    *
    * @param annotation Annotation of which the value is to be returned.
    *
    * @return Default value of the annotation.
    */
   public AnnotationValue getAnnotationValue(
         AnnotationMirror annotation) {
      return getAnnotationValue(annotation, "value");
   }
}
