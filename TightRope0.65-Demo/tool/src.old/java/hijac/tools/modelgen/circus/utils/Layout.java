package hijac.tools.modelgen.circus.utils;

import hijac.tools.modelgen.circus.utils.LaTeX;

/**
 * Utility object that used by the visitor contexts to keep track of layout
 * information.
 *
 * A the moment this class only deals with indentation. It may be conceivable
 * in the future to elaborate it to support some Oppen style pretty-printing,
 * as well as intelligent support for setting parentheses.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class Layout implements Cloneable {
   protected final int default_level;
   protected int level;

   public Layout(int default_level) {
      this.default_level = default_level;
      reset();
   }

   public Layout() {
      this(0);
      reset();
   }

   public void reset() {
      level = default_level;
   }

   public void indent() {
      level++;
   }

   public void dedent() {
      level--;
      if (level < default_level) {
         throw new IllegalStateException(
            "Dedentation beyond the default level.");
      }
   }

   public String newline() {
      return LaTeX.NEWLINE + LaTeX.MACRO_NL + tabbing();
   }

   public String tabbing() {
      return LaTeX.TABBING(level) + " ";
   }

   public Object clone() {
      try {
         return super.clone();
      }
      catch(CloneNotSupportedException e) {
         throw new AssertionError();
      }
   }
}
