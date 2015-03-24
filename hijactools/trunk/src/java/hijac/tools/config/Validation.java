package hijac.tools.config;

import hijac.tools.config.Config;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Validation {
   public static boolean VALIDATE = Config.getValidateObjects();

   public static boolean get() {
      return VALIDATE;
   }

   public static boolean set(boolean flag) {
      boolean prev = VALIDATE;
      VALIDATE = flag;
      return prev;
   }

   public static boolean enable() {
      return set(true);
   }

   public static boolean disable() {
      return set(false);
   }
}
