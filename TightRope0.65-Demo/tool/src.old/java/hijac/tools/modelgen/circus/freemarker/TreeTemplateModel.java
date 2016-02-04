package hijac.tools.modelgen.circus.freemarker;

import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.Tree;

import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.ext.beans.BeanModel;

import hijac.tools.modelgen.circus.templates.CircusObjectWrapper;
import hijac.tools.utils.TreeUtils;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

/**
 * Template model for a syntax {@link Tree} within the JDK Compiler API,
 * exposed within the templates.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class TreeTemplateModel extends BeanModel {
   /**
    * JDK Compiler API node representing the syntax tree.
    */
   protected final Tree tree;

   /**
    * Constructs a data model for a Java AST.
    *
    * @param tree JDK Compiler API node representing the syntax tree.
    */
   public TreeTemplateModel(Tree tree) {
      super(tree, CircusObjectWrapper.INSTANCE);
      assert tree != null;
      this.tree = tree;
   }

   /**
    * Overrides a method of the {@link TemplateHashModelEx} interface
    * implemented through {@link BeanModel}.
    *
    * <p>This method provides the additional keys "string", "type", "kind"
    * and "label". They respectively wrap a {@link String}, {@link TypeMirror},
    * {@link Tree.Kind} and {@link Name} object of the JDK model and tree
    * APIs. Note that the implementation does not alter the behaviour of the
    * remaining interface methods of {@link TemplateHashModelEx}.</p>
    *
    * @param key Key for which to retrieve the template model.
    *
    * @return One of the models for the four special keys or otherwise the
    * model stored under the given key in the underlying {@link BeanModel}.
    */
   @Override
   public TemplateModel get(String key) throws TemplateModelException {
      if (key.equals("string")) {
         String obj = tree.toString();
         return CircusObjectWrapper.doWrap(obj);
      }
      if (key.equals("type")) {
         TypeMirror obj = TreeUtils.getType(tree);
         return CircusObjectWrapper.doWrap(obj);
      }
      if (key.equals("kind")) {
         Tree.Kind obj = tree.getKind();
         return CircusObjectWrapper.doWrap(obj);
      }
      if (key.equals("label")) {
         if (tree.getKind() != Tree.Kind.LABELED_STATEMENT) {
            throw new TemplateModelException(
               "Cannot select label: node is not a LabeledStatementTree.");
         }
         Name obj = ((LabeledStatementTree) tree).getLabel();
         return CircusObjectWrapper.doWrap(obj);
      }
      return super.get(key);
   }
}
