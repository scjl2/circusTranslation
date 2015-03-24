package hijac.tools.modelgen.circus.templates;

import com.sun.source.tree.Tree;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import hijac.tools.modelgen.circus.freemarker.NameTemplateModel;
import hijac.tools.modelgen.circus.freemarker.TreeTemplateModel;
import hijac.tools.modelgen.circus.freemarker.TypeMirrorTemplateModel;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

/**
 * Object wrapper that is used in conjunction with <i>Circus</i> FreeMarker
 * templates. The propose of this class is to wrap objects into suitable
 * template models when they are inserted into data models of the templates.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class CircusObjectWrapper extends DefaultObjectWrapper {
   /**
    * Shared instance of this class to avoid unnecessary object creation.
    */
   public static final CircusObjectWrapper
      INSTANCE = new CircusObjectWrapper();

   /**
    * Constructs a FreeMarker object wrapper.
    */
   public CircusObjectWrapper() { }

   /**
    * Overrides a method of the {@link DefaultObjectWrapper} class to extend
    * the handling of unknown types. In particular, this wraps objects of
    * the JDK model and compiler API, namely {@link Name}, {@link TypeMirror}
    * and {@link Tree}.
    */
   @Override
   protected TemplateModel handleUnknownType(Object obj) throws
         TemplateModelException {
      if (obj instanceof Name) {
         return new NameTemplateModel((Name) obj);
      }
      if (obj instanceof TypeMirror) {
         return new TypeMirrorTemplateModel((TypeMirror) obj);
      }
      if (obj instanceof Tree) {
         return new TreeTemplateModel((Tree) obj);
      }
      return super.handleUnknownType(obj);
   }

   /**
    * Static method to facilitate wrapping of objects. It uses the default
    * instance of this class. Unfortunately, we cannot call it {@code wrap()}
    * due to Java grumbling.
    *
    * @param obj Object to be wrapped.
    *
    * @return Template model of the wrapped object.
    */
   public static TemplateModel doWrap(Object obj)
         throws TemplateModelException {
      return INSTANCE.wrap(obj);
   }

   /**
    * Yields the default instance of this class. This avoids unnecessary
    * object creation.
    *
    * @return Default instance of this class.
    */
   public static CircusObjectWrapper getInstance() {
      return INSTANCE;
   }
}
