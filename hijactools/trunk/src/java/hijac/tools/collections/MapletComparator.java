package hijac.tools.collections;

import java.util.Comparator;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class MapletComparator<A, B> implements Comparator<Maplet<A, B>> {
   public final Comparator<A> c1;
   public final Comparator<B> c2;

   public MapletComparator(Comparator<A> c1, Comparator<B> c2) {
      this.c1 = c1;
      this.c2 = c2;
   }

   @SuppressWarnings("unchecked")
   protected final int compareFst(A x, A y) throws ClassCastException {
      if (c1 != null) {
         return c1.compare(x, y);
      }
      else {
         return ((Comparable<A>) x).compareTo(y);
      }
   }

   @SuppressWarnings("unchecked")
   protected final int compareSnd(B x, B y) throws ClassCastException {
      if (c2 != null) {
         return c2.compare(x, y);
      }
      else {
         return ((Comparable<B>) x).compareTo(y);
      }
   }

   public int compare(Maplet<A, B> m1, Maplet<A, B> m2) {
      int result = 0;
      if (result == 0) {
         result = compareFst(m1.fst, m2.fst);
      }
      if (result == 0) {
         result = compareSnd(m1.snd, m2.snd);
      }
      return result;
   }

   public boolean equals(Object obj) {
      if (obj instanceof MapletComparator) {
         MapletComparator mc = (MapletComparator) obj;
         return c1.equals(mc.c1) && c2.equals(mc.c2);
      }
      else {
         return super.equals(obj);
      }
   }
}
