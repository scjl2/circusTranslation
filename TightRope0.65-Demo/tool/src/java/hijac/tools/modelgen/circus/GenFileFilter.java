package hijac.tools.modelgen.circus;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class GenFileFilter implements FileFilter {
   public boolean accept(File file) {
      return file.isFile() && file.getName().endsWith(".circus");
   }
}
