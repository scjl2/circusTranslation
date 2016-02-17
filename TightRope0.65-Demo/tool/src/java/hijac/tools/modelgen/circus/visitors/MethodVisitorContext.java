package hijac.tools.modelgen.circus.visitors;

import com.sun.source.tree.Tree;

import hijac.tools.collections.Copyable;
import hijac.tools.modelgen.circus.datamodel.MacroModel;
import hijac.tools.modelgen.circus.utils.Layout;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;

/* Immutable class */

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class MethodVisitorContext implements
      Copyable<MethodVisitorContext> {

   public /*final*/ MacroModel MACRO_MODEL;

   public /*final*/ Layout LAYOUT;

   public boolean insideExpression;

   public Tree switchTree;

   public MethodVisitorContext(int default_indent) {
      MACRO_MODEL = new MacroModel();
      LAYOUT = new Layout(default_indent);
      reset();
   }

   public MethodVisitorContext() {
      this(0);
   }

   public void reset() {
      MACRO_MODEL = new MacroModel();
      LAYOUT.reset();
      insideExpression = false;
      switchTree = null;
   }

   public MethodVisitorContext indent() {
      MethodVisitorContext clone = copy();
      clone.LAYOUT.indent();
      return clone;
   }

   public MethodVisitorContext dedent() {
      MethodVisitorContext clone = copy();
      clone.LAYOUT.dedent();
      return clone;
   }

   public String newline() {
      return LAYOUT.newline();
   }

   public String tabbing() {
      return LAYOUT.tabbing();
   }

   public MethodVisitorContext enterExpression() {
      MethodVisitorContext clone = copy();
      clone.insideExpression = true;
      return clone;
   }

   public boolean isInsideExpression() {
      return insideExpression;
   }

   public MethodVisitorContext setSwitch(Tree switchTree) {
      MethodVisitorContext clone = copy();
      clone.switchTree = switchTree;
      return clone;
   }

   public Tree getSwitch() {
      return switchTree;
   }

   public MethodVisitorContext copy() {
      return (MethodVisitorContext) clone();
   }

   protected Object clone() {
      MethodVisitorContext clone;
      try {
         clone = (MethodVisitorContext) super.clone();
      }
      catch(CloneNotSupportedException e) {
         throw new AssertionError();
      }
      clone.MACRO_MODEL = MACRO_MODEL.copy();
      return clone;
   }
}
