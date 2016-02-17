package hijac.tools.modelgen.circus.datamodel;

import hijac.tools.analysis.tasks.MethodCallDepTask;
import hijac.tools.collections.Relation;
import hijac.tools.config.Config;
import hijac.tools.config.Hijac;
import hijac.tools.modelgen.Target;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.utils.ModelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

/**
 * Data model for a class exposed within the templates. Classes act also as
 * translation targets, implementing the {@code @Target} marker interface.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class ClassModel extends DataModel implements Target {
   /**
    * JDK model element representing the class in the data model.
    */
   public final TypeElement type_element;

   /**
    * Constructs a data model for a class.
    *
    * <p>The parameter has to be a type element representing a Java class.</p>
    *
    * @param context SCJ application context of the data model.
    * @param type_element JDK model element representing the class.
    */
   public ClassModel(SCJApplication context, TypeElement type_element) {
      super(context);
      assert type_element != null;
      assert type_element.getKind() == ElementKind.CLASS;
      this.type_element = type_element;
   }

   /**
    * Returns the type element of the JDK model API associated with the class.
    *
    * @return Type element associated with the class.
    */
   public TypeElement getTypeElement() {
      return type_element;
   }

   /**
    * Determines whether the class has static fields or methods.
    *
    * @return True if the class has static fields or methods.
    */
   public boolean hasStaticMembers() {
      for (Element element : getTypeElement().getEnclosedElements()) {
         if (element.getModifiers().contains(Modifier.STATIC)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Returns all non-static fields of the class.
    *
    * @return A list of {@link FieldModel} objects for non-static fields.
    */
   public List<FieldModel> getFields() {
      List<FieldModel> result = new ArrayList<FieldModel>();
      for (VariableElement field : ElementFilter.fieldsIn(
            type_element.getEnclosedElements())) {
         if (!field.getModifiers().contains(Modifier.STATIC) &&
            !CONTEXT.ANNOTS.isInteractionCode(field) &&
            !CONTEXT.ANNOTS.isIgnored(field)) {
            result.add(new FieldModel(CONTEXT, field));
         }
      }
      return result;
   }

   /**
    * Determines whether the class has non-static fields.
    *
    * @return True if the class has non-static fields.
    */
   public boolean hasFields() {
      return !getFields().isEmpty();
   }

   /**
    * Returns all non-static methods of the class.
    *
    * @return A list of {@link MethodModel} objects.
    */
   public List<MethodModel> getMethods() {
      List<MethodModel> result = new ArrayList<MethodModel>();
      for (ExecutableElement method : ElementFilter.methodsIn(
            type_element.getEnclosedElements())) {
         if (!method.getModifiers().contains(Modifier.STATIC) &&
            !CONTEXT.ANNOTS.isInteractionCode(method) &&
            !CONTEXT.ANNOTS.isIgnored(method)) {
            result.add(new MethodModel(CONTEXT, this, method));
         }
      }
      return result;
   }

   /**
    * Determines whether the class has non-static methods.
    *
    * @return True if the class has non-static methods.
    */
   public boolean hasMethods() {
      return !getMethods().isEmpty();
   }

   /**
    * Obtains a particular method of the class given its name.
    *
    * @param name Name of the method to be returned.
    * @return A method whose name corresponds to the given name.
    */
   public MethodModel getMethod(String name) {
      List<MethodModel> result = new ArrayList<MethodModel>();
      for (ExecutableElement method : ElementFilter.methodsIn(
            type_element.getEnclosedElements())) {
         if (!method.getModifiers().contains(Modifier.STATIC)) {
            if (method.getSimpleName().toString().equals(name)) {
               return new MethodModel(CONTEXT, this, method);
            }
         }
      }
      return null;
   }

   /**
    * Determines whether the class has a method with a particular name.
    *
    * @return True if the class has a method with the given name.
    */
   public boolean hasMethod(String name) {
      return getMethod(name) != null;
   }

   /**
    * Returns all active methods of the class.
    *
    * <p>This is done by probing the annotations of class methods. We also
    * consider inherited annotations from superclasses and interfaces.</p>
    *
    * @return A list of {@link MethodModel} objects.
    */
   public List<MethodModel> getActiveMethods() {
      List<MethodModel> result = new ArrayList<MethodModel>();
      for (MethodModel method : getMethods()) {
         if (isActiveMethod(method.getMethod())) {
            result.add(method);
         }
      }
      return result;
   }

   /**
    * Determines whether the class has active methods.
    *
    * @return True if the class has active methods.
    */
   public boolean hasActiveMethods() {
      return !getActiveMethods().isEmpty();
   }

   /**
    * Returns all data operations of the class.
    *
    * <p>This is done by probing the annotations of class methods. We also
    * consider inherited annotations from superclasses and interfaces.</p>
    *
    * @return A list of {@link MethodModel} objects.
    */
   public List<MethodModel> getDataOperations() {
      List<MethodModel> result = new ArrayList<MethodModel>();
      for (MethodModel method : getMethods()) {
         if (isDataOperation(method.getMethod())) {
            result.add(method);
         }
      }
      return result;
   }

   /**
    * Determines whether the class has data operations.
    *
    * @return True if the class has data operations.
    */
   public boolean hasDataOperations() {
      return !getDataOperations().isEmpty();
   }


   /**
    * Returns all device access methods of the class.
    *
    * <p>This is done by probing the annotations of class methods. We also
    * consider inherited annotations from superclasses and interfaces.</p>
    *
    * @return A list of {@link MethodModel} objects.
    */
   public List<MethodModel> getDeviceMethods() {
      List<MethodModel> result = new ArrayList<MethodModel>();
      for (MethodModel method : getMethods()) {
         if (isDeviceAccess(method.getMethod())) {
            result.add(method);
         }
      }
      return result;
   }

   /**
    * Determines whether the class has device access methods.
    *
    * @return True if the class has device access methods.
    */
   public boolean hasDeviceMethods() {
      return !getActiveMethods().isEmpty();
   }

   /**
    * Determines whether the class is an active class.
    *
    * @return True if the class is annotated as an active class.
    */
   public boolean isActiveClass() {
      return CONTEXT.ANNOTS.isActiveClass(type_element);
   }

   /**
    * Determines whether the class is a passive class.
    *
    * @return True if the class is annotated as a passive class.
    */
   public boolean isPassiveClass() {
      return CONTEXT.ANNOTS.isPassiveClass(type_element);
   }

   /**
    * Determines whether the class is an interaction class.
    *
    * @return True if the class is annotated as an interaction class.
    */
   public boolean isInteractionClass() {
      return CONTEXT.ANNOTS.isInteractionClass(type_element);
   }

   /**
    * Determines whether the class is to be ignored.
    *
    * @return True if the class is annotated to be ignored.
    */
   public boolean isIgnoredClass() {
      return CONTEXT.ANNOTS.isInteractionClass(type_element);
   }

   /**
    * Determines whether a method is an active method.
    *
    * <p>This is the case for methods that are either explicitly annotated
    * with {@code @ActiveMethod} or otherwise call methods that are either
    * annotated as SCJ framework methods (with {@code @FrameworkMethod}) or
    * otherwise annotated to carry out a device access.</p>
    *
    * @param method Method to be tested.
    * @return True for methods that are considered as active.
    */
   protected boolean isActiveMethod(ExecutableElement method) {
      if (CONTEXT.ANNOTS.isActiveMethod(method)) {
         return true;
      }
      if (CONTEXT.ANNOTS.isFrameworkMethod(method) ||
         CONTEXT.ANNOTS.isDeviceAccess(method) ||
         CONTEXT.ANNOTS.isInteractionCode(method) ||
         CONTEXT.ANNOTS.isIgnored(method)) {
         return false;
      }
      @SuppressWarnings("unchecked")
      Relation<ExecutableElement, ExecutableElement> call_dependency =
         (Relation<ExecutableElement, ExecutableElement>)
            CONTEXT.ANALYSIS.get(MethodCallDepTask.KEY);
      Set<ExecutableElement> called_methods = call_dependency.image(method);
      for (ExecutableElement called_method : called_methods) {
         if (CONTEXT.ANNOTS.isFrameworkMethod(called_method) ||
            CONTEXT.ANNOTS.isDeviceAccess(called_method)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Determines whether a method is a data operation.
    *
    * </p>Note that this does not preclude the method from being active
    * too and thereby possessing both, a <i>Circus</i> action and an
    * <OhCircus> method model. Data operations, however, must not call
    * framework methods (annotated with {@code @FrameworkMethod}) or
    * methods that carry out device accesses.</p>
    *
    * @param method Method to be tested.
    * @return True for methods that are considered as data operations.
    */
   protected boolean isDataOperation(ExecutableElement method) {
      if (CONTEXT.ANNOTS.isFrameworkMethod(method) ||
         CONTEXT.ANNOTS.isDeviceAccess(method) ||
         CONTEXT.ANNOTS.isInteractionCode(method) ||
         CONTEXT.ANNOTS.isIgnored(method)) {
         return false;
      }
      @SuppressWarnings("unchecked")
      Relation<ExecutableElement, ExecutableElement> call_dependency =
         (Relation<ExecutableElement, ExecutableElement>)
            CONTEXT.ANALYSIS.get(MethodCallDepTask.KEY);
      Set<ExecutableElement> called_methods = call_dependency.image(method);
      for (ExecutableElement called_method : called_methods) {
         if (CONTEXT.ANNOTS.isFrameworkMethod(called_method) ||
            CONTEXT.ANNOTS.isDeviceAccess(called_method)) {
            return false;
         }
      }
      return true;
   }

   /**
    * Determines whether a method is a device access.
    *
    * @param method Method to be tested.
    * @return True for methods annotated as device accesses.
    */
   protected boolean isDeviceAccess(ExecutableElement method) {
      return CONTEXT.ANNOTS.isDeviceAccess(method);
   }

   /**
    * Determines whether the class is a concrete mission class.
    *
    * @return True for classes derived from the {@code Mission} SCJ class.
    */
   public boolean isMission() {
      TypeMirror mission_type = (TypeMirror)
         CONTEXT.ANALYSIS.get(Hijac.key("MissionType"));
      return MODEL.TYPES.isSubtype(type_element.asType(), mission_type);
   }

   /**
    * Determines whether the class is a concrete handler class.
    *
    * @return True for classes derived from the {@code AperiodicEventHandler},
    * {@code AperiodicLongEventHandler} or {@code PeriodicEventHandler} SCJ
    * classes.
    */
   public boolean isHandler() {
      TypeMirror handler_type1 = (TypeMirror)
         CONTEXT.ANALYSIS.get(Hijac.key("AperiodicEventHandlerType"));
      TypeMirror handler_type2 = (TypeMirror)
         CONTEXT.ANALYSIS.get(Hijac.key("AperiodicLongEventHandlerType"));
      TypeMirror handler_type3 = (TypeMirror)
         CONTEXT.ANALYSIS.get(Hijac.key("PeriodicEventHandlerType"));
      return MODEL.TYPES.isSubtype(type_element.asType(), handler_type1) ||
         MODEL.TYPES.isSubtype(type_element.asType(), handler_type2) ||
         MODEL.TYPES.isSubtype(type_element.asType(), handler_type3);
   }

   /**
    * Determines whether the class is an API class of the JDK or
    * SCJ / RTSJ programming interface.
    *
    * @return True if the class is an API class of the JDK or SCJ / RTSJ API.
    */
   public boolean isAPIClass() {
      String class_name = ModelUtils.getQualifiedName(type_element);
      return class_name.startsWith("java.") ||
         class_name.startsWith("javax.") ||
         class_name.startsWith(Config.getSCJPackagePrefix()) ||
         class_name.startsWith(Config.getRTSJPackagePrefix());
   }

   /* TODO: Review the next three methods. Return a Name object? */

   /**
    * Returns the fully qualified name of the class.
    *
    * @return Fully qualified class name as a string.
    */
   public String getName() {
      if (Config.getPrefixWithPackage()) {
         return ModelUtils.getQualifiedName(type_element);
      }
      else {
         return ModelUtils.getSimpleName(type_element);
      }
   }

   /**
    * Returns the class name converted into a valid Z section name.
    *
    * @return Class name encoded as a Z section name.
    */
   public String getSectionName() {
      String result = getName();
      result = result.replace("_", "");
      result = result.replace(".", "");
      result = result.replace("$", "");
      return result;
   }

   /**
    * Returns the class name converted into a valid <i>Circus</i> process
    * name.
    *
    * @return Class name encoded as a <i>Circus</i> process name.
    */
   public String getProcessName() {
      String result = getName();
      result = result.replace("_", "\\_");
      result = result.replace(".", "\\_");
      result = result.replace("$", "\\$");
      return result;
   }
}
