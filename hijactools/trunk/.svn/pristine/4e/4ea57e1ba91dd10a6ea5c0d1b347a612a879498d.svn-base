package hijac.tools.modelgen.circus;

import hijac.tools.config.Config;
import hijac.tools.modelgen.circus.utils.LaTeX;
import hijac.tools.utils.FileUtils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Utility for post-processing of generated Circus LaTeX files.
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class PostProcessor {
   public static final Pattern IGNORE_TAILING =
      Pattern.compile("%it%[^\\n]*\\n", Pattern.MULTILINE);

   public static final Pattern SUBST_NEWLINE =
      Pattern.compile(Pattern.quote(LaTeX.MACRO_NL), Pattern.MULTILINE);

   public static final Pattern REMOVE_EMPTY_LINES =
      Pattern.compile("\\n(\\s)*\\n", Pattern.MULTILINE);

   public static final Pattern REMOVE_LBRACKET_SPACE =
      Pattern.compile("\\([ ]", Pattern.MULTILINE);

   public static final Pattern REMOVE_RBRACKET_SPACE =
      Pattern.compile("[ ]+\\)", Pattern.MULTILINE);

   protected final File gen_dir;

   public PostProcessor() {
      gen_dir = new File(Config.getModelOutputDir());
   }

   /* The simplification mechanism is still somewhat ad hoc. */

   public void execute() {
      for (File file : gen_dir.listFiles(new GenFileFilter())) {
         String content = FileUtils.readFile(file);
         content = IGNORE_TAILING.matcher(content).replaceAll("");
         content = SUBST_NEWLINE.matcher(content).replaceAll("\n");
         content = REMOVE_EMPTY_LINES.matcher(content).replaceAll("\n");
         FileUtils.writeFile(file, content);
      }
      (new Simplifier()).execute();
      for (File file : gen_dir.listFiles(new GenFileFilter())) {
         String content = FileUtils.readFile(file);
         content = REMOVE_LBRACKET_SPACE.matcher(content).replaceAll("(");
         content = REMOVE_RBRACKET_SPACE.matcher(content).replaceAll(")");
         FileUtils.writeFile(file, content);
      }
   }
}
