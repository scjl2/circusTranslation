package hijac.tools.collections;

import java.util.Comparator;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class RelationFactory {
   public static <A, B> Relation<A, B> newRelation() {
      return new RelationImpl<A, B>();
   }

   public static <A, B> Relation<A, B> newRelation(Relation<A, B> r) {
      return new RelationImpl<A, B>((RelationImpl<A, B>) r);
   }

   public static <A, B> Relation<A, B> newRelation(
         Comparator<A> c1, Comparator<B> c2) {
      return new RelationImpl<A, B>(c1, c2);
   }

   public static <A> Relation<A, A> newRelation(Comparator<A> c) {
      return new RelationImpl<A, A>(c, c);
   }
}
