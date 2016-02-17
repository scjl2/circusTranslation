package hijac.tools.comparators;

import hijac.tools.utils.ObjectIds;

import java.util.Comparator;

/* This class provides a "universal comparator" - that is a comparator which
 * can compare objects of any type T. Importantly, the comparator fulfils the
 * contract of Comparator<T> and therefore is consistent w.r.t. equality.
 * The price to pay is a more expensive comparison operation that has to
 * store and look up objects in a map (see ObjectIds) to establish an ad hoc
 * total order. This is safer than a cop out using hash codes directly.
 * That latter is flawed since the contract of hashCode() does not require
 * the returned integers to be unique w.r.t. the object references. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class UniversalComparator<T> implements Comparator<T> {
   public final static UniversalComparator<Object>
      INSTANCE = new UniversalComparator<Object>();

   public UniversalComparator() { }

   public synchronized int compare(T o1, T o2) {
      if (o1.equals(o2)) {
         return 0;
      }
      else {
         long i1 = ObjectIds.getId(o1);
         long i2 = ObjectIds.getId(o2);
         assert i1 != i2;
         /* A cop out here would be o1.hashCode() <= o2.hashCode() but this is
          * flawed since even the identity hash code would not be unique. */
         return i1 < i2 ? 1 : -1;
      }
   }

   public boolean equals(Object obj) {
      return this instanceof UniversalComparator;
   }

   public static int compareAny(Object o1, Object o2) {
      return INSTANCE.compare(o1, o2);
   }
}
