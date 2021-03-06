package hijac.tools.utils;

import hijac.tools.config.Config;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class ShellUtils {
   public static boolean USE_ANSI_COLOURS = Config.getUseAnsiColours();

   public static String intoRed(String str) {
      return USE_ANSI_COLOURS ? "\033[31m" + str + "\033[30m" : str;
   }

   public static String intoGreen(String str) {
      return USE_ANSI_COLOURS ? "\033[32m" + str + "\033[30m" : str;
   }

   public static String intoBlue(String str) {
      return USE_ANSI_COLOURS ? "\033[34m" + str + "\033[30m" : str;
   }

   public static void hline() {
      for (int i = 1; i <= 80; i++) {
         System.out.print("-");
      }
      System.out.println();
   }
}
