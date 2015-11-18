package hijac.tools.modelgen.circus;

import hijac.tools.config.Config;
import hijac.tools.utils.FileUtils;

import java.io.File;

import java.util.regex.Pattern;

/**
 * Utility for simplification of the generated Circus models.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class Simplifier {
   /* Patterns used in simplification steps. */
   public static final Pattern SIMPLIFY_JTRUE1 =
      Pattern.compile("\\\\IF ~ true ~ \\\\THEN ~ jtrue ~ \\\\ELSE ~ jfalse",
      Pattern.MULTILINE);

   public static final Pattern SIMPLIFY_JTRUE2 =
      Pattern.compile("\\\\IF ~ true ~ \\\\THEN ~ jfalse ~ \\\\ELSE ~ jtrue",
      Pattern.MULTILINE);

   public static final Pattern SIMPLIFY_JFALSE1 =
      Pattern.compile("\\\\IF ~ false ~ \\\\THEN ~ jtrue ~ \\\\ELSE ~ jfalse",
      Pattern.MULTILINE);

   public static final Pattern SIMPLIFY_JFALSE2 =
      Pattern.compile("\\\\IF ~ false ~ \\\\THEN ~ jfalse ~ \\\\ELSE ~ jtrue",
      Pattern.MULTILINE);

   protected final File gen_dir;

   public Simplifier() {
      gen_dir = new File(Config.getModelOutputDir());
   }

   /* Simplification of surplus nested parentheses such as ((...)). */

   private static void removeSurplusNestingBrackets(
         StringBuilder s, int fromIndex, int toIndex) {
      assert 0 <= fromIndex && toIndex <= s.length() - 1;
      assert fromIndex <= toIndex;
      /* Move sequentially through the string from fromIndex to toIndex. */
      do {
         /* Find index of the next open bracket after fromIndex. */
         int openIndex = s.indexOf("(", fromIndex);
         if (openIndex > toIndex || openIndex == -1) {
            /* No open bracket found between fromIndex and toIndex. */
            return ;
         }
         /* Find index of the matching closing bracket. */
         int closeIndex = findMatchingBracket(s, openIndex);
         assert closeIndex <= toIndex;
         assert s.charAt(closeIndex) == ')';
         /* Remember the new starting point. */
         int newfromIndex = closeIndex + 1;
         /* Remove suplus inner nesting brackets. */
         openIndex++;
         closeIndex--;
         while (openIndex < closeIndex) {
            if (s.charAt(openIndex) == '(' &&
                  findMatchingBracket(s, openIndex) == closeIndex) {
               s.setCharAt(openIndex, ' ');
               s.setCharAt(closeIndex, ' ');
               openIndex++;
               closeIndex--;
            }
            else { break; }
         }
         if (openIndex < closeIndex) {
            /* Recursive application on the nested content. */
            removeSurplusNestingBrackets(s, openIndex, closeIndex);
         }
         fromIndex = newfromIndex;
      } while (fromIndex <= toIndex);
   }

   private static int findMatchingBracket(StringBuilder s, int openIndex) {
      assert openIndex <= s.length() - 1;
      assert s.charAt(openIndex) == '(';
      int closeIndex = openIndex;
      int openCount = 0;
      while (!(s.charAt(closeIndex) == ')' && openCount == 1)) {
         if (s.charAt(closeIndex) == '(') { openCount++; }
         if (s.charAt(closeIndex) == ')') { openCount--; }
         closeIndex++;
         if (closeIndex == s.length()) {
            throw new AssertionError(
               "Unbalanced parentheses encountered during post-processing.");
         }
      }
      return closeIndex;
   }

   public static String removeSurplusNestingBrackets(String str) {
      StringBuilder builder = new StringBuilder(str);
      removeSurplusNestingBrackets(builder, 0, builder.length() - 1);
      return builder.toString();
   }

   /* The simplification mechanism is still somewhat ad hoc. */

   public void execute() {
      for (File file : gen_dir.listFiles(new GenFileFilter())) {
         String content = FileUtils.readFile(file);
         content = removeSurplusNestingBrackets(content);
         content = SIMPLIFY_JTRUE1.matcher(content).replaceAll("jtrue");
         content = SIMPLIFY_JTRUE2.matcher(content).replaceAll("jfalse");
         content = SIMPLIFY_JFALSE1.matcher(content).replaceAll("jfalse");
         content = SIMPLIFY_JFALSE2.matcher(content).replaceAll("jtrue");
         FileUtils.writeFile(file, content);
      }
   }
}
