package hijac.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class FileUtils {
   public static final FileFilter JAVA_FILES =
      new FileFilter() {
         public boolean accept(File file) {
            return file.getName().endsWith(".java");
         }
      };

   public static Collection<File> scanFolder(File file, FileFilter filter) {
      Collection<File> files = new ArrayList<File>();
      if (file.isDirectory()) {
         for (File child : file.listFiles()) {
            files.addAll(scanFolder(child, filter));
         }
      }
      else {
         assert file.isFile();
         if (filter.accept(file)) {
            files.add(file);
         }
      }
      return files;
   }

   public static String fileListToString(Collection<File> list, String sep) {
      StringBuilder builder = new StringBuilder();
      Iterator<File> iter = list.iterator();
      while (iter.hasNext()) {
         File file = iter.next();
         builder.append(file.getAbsolutePath());
         if (iter.hasNext()) {
            builder.append(sep);
         }
      }
      return builder.toString();
   }
   
   public static String readFile(File file) {
      try {
         FileInputStream fis = new FileInputStream(file);
         byte[] buffer = new byte[(int) file.length()];
         fis.read(buffer);
         fis.close();
         return new String(buffer);
      }
      catch (IOException e) {
         System.out.println(e.toString());
         System.exit(-1);
      }
      assert false;
      return null; // never reached
   }

   public static void writeFile(File file, String text) {
      try {
         FileWriter writer = new FileWriter(file);
         writer.write(text);
         writer.close();
      }
      catch(IOException e) {
         System.out.println(e.toString());
         System.exit(-1);
      }
   }
}
