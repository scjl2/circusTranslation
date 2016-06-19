package hijac.tools.config;

import hijac.tools.application.ApplicationError;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

/**
 * @author Frank Zeyda
 * @version $Revision: 206 $
 * 
 * This class seems to handle parsing the properties file and holding the result
 * 
 * @updated 23/03/2015
 * @updateAuthor Matt Luckcuck
 */
public class Config {
   public static final String PROPERTIES_FILENAME =
      "hijac.properties";

   public static final String DEFAULTS_FILENAME =
      "src/resources/default.properties";
//		   	"default.properties"   ;
   
   private static final Properties HIJAC_PROPS, DEFAULT_PROPS;

   static {
      DEFAULT_PROPS = new Properties();
      try {
         DEFAULT_PROPS.load(new FileInputStream(DEFAULTS_FILENAME));
      }
      catch (IOException e) {
         throw new ApplicationError("Failed to load " + DEFAULTS_FILENAME);
      }
      HIJAC_PROPS = new Properties(DEFAULT_PROPS);
      try {
         HIJAC_PROPS.load(new FileInputStream(PROPERTIES_FILENAME));
      }
      catch (IOException e) {
         throw new ApplicationError("Failed to load " + PROPERTIES_FILENAME);
      }
   }

   /*public*/ protected static String getProperty(String key) {
      String value = HIJAC_PROPS.getProperty(key);
      return value != null ? value.trim() : value;
   }

   /* Utility Functions */

   protected static boolean toBoolean(String value) {
      assert value != null;
      value = value.trim().toLowerCase();
      return value.equals("true");
   }

   protected static String[] toStringArray(String value) {
      assert value != null;
      value = value.trim();
      return value.split("\\s");
   }

   /* General Configuration */

   public static String getSCJPackagePrefix() {
      return getProperty("SCJ_PACKAGE_PREFIX");
   }

   public static String getRTSJPackagePrefix() {
      return getProperty("RTSJ_PACKAGE_PREFIX");
   }

   public static boolean isSCJClass(String name) {
      return
         name.startsWith(getSCJPackagePrefix()) ||
         name.startsWith(getRTSJPackagePrefix());
   }

   public static boolean getValidateObjects() {
      return toBoolean(getProperty("VALIDATE_OBJECTS"));
   }

   public static boolean getUseAnsiColours() {
      return toBoolean(getProperty("USE_ANSI_COLOURS"));
   }

   /* Configuration of the SCJ & RTSJ Libraries */

   public static String getSCJLib() {
      return getProperty("SCJ_LIB");
   }

   public static String getRTSJLib() {
      return getProperty("RTSJ_LIB");
   }

   /* Configuration of SCJ Application */

   public static String[] getSCJSrc() {
      return toStringArray(getProperty("SCJ_SRC"));
   }

   /* Configuration of the Analyser */

   public static boolean getTypesReport() {
      return toBoolean(getProperty("Analyser.TYPES_REPORT"));
   }

   public static boolean getMethodsReport() {
      return toBoolean(getProperty("Analyser.METHODS_REPORT"));
   }

   public static boolean getMethodDepReport() {
      return toBoolean(getProperty("Analyser.METHOD_DEP_REPORT"));
   }

   /* Configuration of the Model Generator */

   public static String getModelOutputDir() {
      return getProperty("ModelGen.MODEL_OUTPUT_DIR");
   }

   public static boolean getPrefixWithPackage() {
      return toBoolean(getProperty("ModelGen.PREFIX_WITH_PACKAGE"));
   }
}
