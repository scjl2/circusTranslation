package hijac.tools.modelgen.circus.utils;

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class LaTeX {
   public static final String MACRO_NL = "\\macronl";

   public static final String NEWLINE = "\\\\";

   public static String TABBING(int n) {
      assert n >= 1;
      return "\\t" + n;
   }
}
