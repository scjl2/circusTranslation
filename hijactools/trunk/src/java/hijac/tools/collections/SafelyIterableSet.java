package hijac.tools.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public final class SafelyIterableSet<E> implements Set<E> {
   private final Set<E> set;

   public SafelyIterableSet(Set<E> set) {
      this.set = set;
   }

   public boolean add(E e) {
      return set.add(e);
   }

   public boolean addAll(Collection<? extends E> c) {
      return set.addAll(c);
   }

   public void clear() {
      set.clear();
   }

   public boolean contains(Object o) {
      return set.contains(o);
   }

   public boolean containsAll(Collection<?> c) {
      return set.containsAll(c);
   }

   public boolean equals(Object o) {
      return set.equals(o);
   }

   public int hashCode() {
      return set.hashCode();
   }

   public boolean isEmpty() {
      return isEmpty();
   }

   /* This is the only method whose implementation we alter w.r.t. set. */

   @SuppressWarnings("unchecked")
   public Iterator<E> iterator() {
      return new ArrayIterator((E[]) toArray());
   }

   public boolean remove(Object o) {
      return set.remove(o);
   }

   public boolean removeAll(Collection<?> c) {
      return set.removeAll(c);
   }

   public boolean retainAll(Collection<?> c) {
      return set.retainAll(c);
   }

   public int size() {
      return set.size();
   }

   public Object[] toArray() {
      return set.toArray();
   }

   public <T> T[] toArray(T[] a) {
      return set.toArray(a);
   }

   public String toString() {
      return set.toString();
   }
}
