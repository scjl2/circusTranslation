package hijac.tools.modelgen.circus.datamodel;

import com.sun.source.tree.Tree;

import hijac.tools.modelgen.Target;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.analysis.UniqueMethodNamesTask;
import hijac.tools.modelgen.circus.freemarker.NameTemplateModel;
import hijac.tools.modelgen.circus.utils.TransUtils;
import hijac.tools.modelgen.circus.visitors.AMethodVisitor;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;

import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Data model for a method exposed within the templates. Methods act also as
 * translation targets, implementing the {@code @Target} marker interface.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class MethodModel extends DataModel /* implements Target */ {
   /**
    * Data model of the class that includes the method.
    */
   public final ClassModel class_model;

   /**
    * JDK model element representing the method in the data model.
    */
   public final ExecutableElement method;

   /**
    * Data model for the parameters of the method.
    */
   public /*final*/ ParameterModel[] parameters;

   /**
    * Constructs a data model for a class method.
    *
    * @param context SCJ application context of the data model.
    * @param method JDK model element representing the method.
    */
   public MethodModel(SCJApplication context, ClassModel class_model,
         ExecutableElement method) {
      super(context);
      assert method != null;
      this.class_model = class_model;
      this.method = method;
      createParameters();
   }

   /**
    * Utility method that creates the data model for the parameters.
    */
   protected void createParameters() {
      int params = 0;
      for (VariableElement parameter : method.getParameters()) {
         if (!CONTEXT.ANNOTS.isInteractionCode(parameter) &&
            !CONTEXT.ANNOTS.isIgnored(parameter)) {
            params++;
         }
      }
      this.parameters = new ParameterModel[params];
      int index = 0;
      for (VariableElement parameter : method.getParameters()) {
         if (!CONTEXT.ANNOTS.isInteractionCode(parameter) &&
            !CONTEXT.ANNOTS.isIgnored(parameter)) {
            parameters[index++] = new ParameterModel(CONTEXT, parameter);
         }
      }
   }

   /**
    * Return the respective JDK model element for the method.
    *
    * @return Model element for the method of type {@link ExecutableElement}.
    */
   public ExecutableElement getMethod() {
      return method;
   }

   /**
    * <p>Return an array of data models for the parameters of the method.</p>
    *
    * <p>Note that we are only considering parameters here that are not
    * nor annotated as interaction code and neither ignored.</p>
    *
    * @return Array of data models for the parameters of the method.
    */
   public ParameterModel[] getParams() {
      return parameters;
   }

   /**
    * <p>Determines whether the methods has parameters.</p>
    *
    * <p>Note that we are only considering parameters here that are not
    * nor annotated as interaction code and neither ignored.</p>
    *
    * @return True if the method has parameters that are not ignored.
    */
   public boolean getHasParams() {
      return parameters.length != 0;
   }

   /**
    * Returns the simple name of the method.
    *
    * @return Simple name of the method as a {@link Name} object.
    */
   public Name getName() {
      return method.getSimpleName();
   }

   /**
    * Returns a unique name of the method in the presence of overloading.
    *
    * @return Unique name of the method as a {@link Name} object.
    */
   public Name getUniqueName() {
      Map<ExecutableElement, Name> unique_names =
         CONTEXT.ANALYSIS.get(UniqueMethodNamesTask.KEY);
      assert unique_names.containsKey(method);
      return unique_names.get(method);
   }

   /**
    * Returns the method body as a JDK AST.
    *
    * @return Method body as a JDK AST of type {@link Tree}.
    */
   public Tree getBody() {
      return CONTEXT.ANALYSIS.TREES.getTree(method);
      /* The following code is obsolete now, replace. */
      /*Tree tree = CONTEXT.ANALYSIS.TREES.getTree(method);
      AMethodVisitor visitor = new AMethodVisitor(CONTEXT, this);
      String result = visitor.visit(tree,
         new MethodVisitorContext(default_indent));
      return result == null ? TransUtils.FAILED : result;*/
   }

   /**
    * Determines if the method is {@code void}.
    *
    * @return True if the method is {@code void}.
    */
   public boolean getIsVoid() {
      return method.getReturnType().getKind() == TypeKind.VOID;
   }

   /**
    * Returns the type of the method.
    *
    * @return Type of the method as a {@link TypeMirror} object.
    */
   public TypeMirror getRetType() {
      return method.getReturnType();
   }

   /**
    * Returns the modifiers of the method.
    *
    * @return Modifiers of the method as a {@link ModifiersModel} data model.
    */
   public ModifiersModel getModifiers() {
      return new ModifiersModel(CONTEXT, method.getModifiers());
   }

   public boolean isActiveMethod() {
      return class_model.isActiveMethod(method);
   }

   public boolean isDataOperation() {
      return class_model.isDataOperation(method);
   }

   public boolean isDeviceAccess() {
      return class_model.isDeviceAccess(method);
   }

   /**
    * <p>Returns a string encoding of the method, escaping symbols as
    * necessary to ensure compatibility with Z LaTeX mark-up.</p>
    *
    * <p>This method suitably escapes symbols such as underscores so
    * that they can be parsed by the <i>Circus</i> parser of CZT.</p>
    *
    * @return String representation of the unique method name that can be
    * used directly in the generated <i>Circus</i> model.
    */
   public String toString() {
      return new NameTemplateModel(getName()).getAsString();
   }
}
