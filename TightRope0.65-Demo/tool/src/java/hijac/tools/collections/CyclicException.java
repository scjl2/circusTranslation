package hijac.tools.collections;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class CyclicException extends Exception {
   public static final String MESSAGE =
      "Relation is cyclic, therefore does not have a topological order.";

   public Relation r;

   public CyclicException(Relation r) {
      super(MESSAGE);
      this.r = r;
   }
}
