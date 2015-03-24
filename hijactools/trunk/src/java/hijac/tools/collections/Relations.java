package hijac.tools.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Relations {
   public static <A> Set<A> type(Relation<A, A> r) {
      Set<A> set = new TreeSet<A>(r.domOrder());
      set.addAll(r.domain());
      set.addAll(r.range());
      return set;
   }

   /* Identity Relation */

   public static <A> Relation<A, A> identity(Set<A> set) {
      Relation<A, A> id = RelationFactory.newRelation();
      for (A x : set) {
         id.add(new Maplet<A, A>(x, x));
      }
      return id;
   }

   public static <A> Relation<A, A> identity(SortedSet<A> set) {
      @SuppressWarnings("unchecked")
      Comparator<A> c = (Comparator<A>) set.comparator();
      Relation<A, A> id = RelationFactory.newRelation(c, c);
      for (A x : set) {
         id.add(new Maplet<A, A>(x, x));
      }
      return id;
   }

   public static <A> Relation<A, A> identity(Relation<A, A> r) {
      Relation<A, A> id = RelationFactory.newRelation(r);
      for (A x : r.domain()) {
         id.add(new Maplet<A, A>(x, x));
      }
      for (A x : r.range()) {
         id.add(new Maplet<A, A>(x, x));
      }
      return id;
   }

   /* Relational Iteration */

   public static <A> Relation<A, A> iterate(Relation<A, A> r, int n) {
      r.validate();
      if (n <= 0) {
         throw new IllegalArgumentException(
            "Number of iterations less or equal to 0.");
      }
      if (n == 1) {
         return r.copy();
      }
      else {
         return Relations.iterate(r, n - 1).compose(r);
      }
   }

   /* Transitive Closure */

   public static <A> Relation<A, A> closure(Relation<A, A> r) {
      r.validate();
      Relation<A, A> result = r.copy();
      Relation<A, A> r_iter = r;
      boolean changed = true;
      do {
         r_iter = r_iter.compose(r);
         changed = result.addAll(r_iter);
      } while(changed);
      result.validate();
      return result;
   }

   /* Reflexive-transitive Closure */

   public static <A> Relation<A, A> reflClosure(Relation<A, A> r) {
      r.validate();
      Relation<A, A> result = closure(r);
      result.addAll(identity(r));
      result.validate();
      return result;
   }

   /* Topological Sorting */

   public static <A> List<A> topologicalSort(Relation<A, A> r) throws
         CyclicException {
      r.validate();
      List<A> result = new ArrayList<A>();
      Relation<A, A> r_tmp = r;
      while (!r_tmp.isEmpty()) {
         Set<A> set = new TreeSet<A>(r.domOrder());
         set.addAll(r_tmp.domain());
         set.removeAll(r_tmp.range());
         if (set.isEmpty()) {
            throw new CyclicException(r);
         }
         A x = Sets.Choice(set);
         result.add(x);
         for (A y : r.image(x)) {
            if (!r.domain().contains(y)) {
               result.add(y);
            }
         }
         r_tmp = r_tmp.domSub(Collections.singleton(x));
      }
      return result;
   }

   /* Various Tests for Relations */

   public static <A> boolean isReflexive(Relation<A, A> r) {
      r.validate();
      return r.contains(identity(r));
   }

   public static <A> boolean isTransitive(Relation<A, A> r) {
      r.validate();
      return r.contains(r.compose(r));
   }

   public static <A> boolean isCyclic(Relation<A, A> r) {
      try {
         topologicalSort(r);
      }
      catch(CyclicException e) {
         return false;
      }
      return true;
   }
}
