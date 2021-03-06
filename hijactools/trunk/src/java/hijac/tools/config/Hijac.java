package hijac.tools.config;

import hijac.tools.analysis.SCJDataKey;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Hijac {
   public static final String NAMESPACE = "hijac";

   public static SCJDataKey key(String name) {
      return new SCJDataKey(NAMESPACE, name);
   }

   public static SCJDataKey[] keys(String[] names) {
      SCJDataKey[] keys = new SCJDataKey[names.length];
      for (int i = 0; i < names.length; i++) {
         keys[i] = key(names[i]);
      }
      return keys;
   }
}
