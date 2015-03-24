package hijac.tools.utils;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

import javax.lang.model.type.TypeMirror;

/**
 *
 * @author Frank Zeyda
 * @version $Revision$
 */
public class TreeUtils {
   /* The following is slightly dodgy since we make assumptions beyond what
    * is guaranteed by the JDK Compiler API. There seems, however, no other
    * way to obtain a type element for a Tree node, unless we know as well
    * the compilation unit of the node. The latter we do not always have. */
   public static TypeMirror getType(Tree tree) {
      return ((JCTree) tree).type;
   }
}
