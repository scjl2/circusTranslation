package hijac.tools.modelgen;

import hijac.tools.application.ApplicationError;
import hijac.tools.config.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.Map;

/* Utility class to handle multiple file output of model generation. */

/**
 * @author Frank Zeyda
 * @version $Revision$
 */
public class Output {
   protected final File output_dir;
   protected final Map<String, PrintWriter> writers;

   public Output(File output_dir) {
      this.output_dir = output_dir;
      writers = new HashMap<String, PrintWriter>();
   }

   public Output() {
      this(new File(Config.getModelOutputDir()));
   }

   public PrintWriter getWriter(String filename) {
      if (!writers.containsKey(filename)) {
         File newfile = new File(output_dir, filename);
         if (newfile.exists()) {
            if (!newfile.delete()) {
               throw new ApplicationError("Could not delete file " + newfile);
            }
         }
         try {
            if (!newfile.createNewFile()) {
               throw new ApplicationError("Could not create file " + newfile);
            }
         }
         catch (IOException e) {
            throw new ApplicationError(e);
         }
         try {
            writers.put(filename, new PrintWriter(newfile));
         }
         catch (FileNotFoundException e) {
            throw new AssertionError(e);
         }
      }
      return writers.get(filename);
   }

   public void close() {
      for (PrintWriter writer : writers.values()) {
         writer.close();
      }
      writers.clear();
   }
}
