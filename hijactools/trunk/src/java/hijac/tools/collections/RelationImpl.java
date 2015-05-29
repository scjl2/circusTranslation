package hijac.tools.collections;

import hijac.tools.config.Validation;

import hijac.tools.utils.ValidationError;

import java.util.*;

import static java.util.Map.Entry;

/* Efficient (?) implementation of relations based on two sorted maps. */

/* An important invariant of the data model is that the two maps encoding the
 * relation do not contain null or empty sets. Because of this we can equate
 * their key sets directly with the domain and range of the relation. */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class RelationImpl<A, B> extends AbstractSet<Maplet<A, B>>
      implements Relation<A, B>, Cloneable {
   /* Use flyweight objects for maplets? */
   public static boolean USE_FLYWEIGHT = false;

   /* Comparators used to ordering domain and range elements. */
   protected final Comparator<A> c1;
   protected final Comparator<B> c2;

   /* Pre-constructed immutable empty sets (for efficiency). */
   protected final Set<A> EMPTY_A;
   protected final Set<B> EMPTY_B;

   /* Two 'power set maps' to encoded the relation as functions. */
   protected /*final*/ Map<A, Set<B>> map1;
   protected /*final*/ Map<B, Set<A>> map2;

   /* Convenience fields initialised to the maps' key sets. */
   protected /*final*/ Set<A> domain;
   protected /*final*/ Set<B> range;

   /* The number of maplets is tracked rather than computed. */
   protected int size;

   /* Constructor that uses the natural order of A and B. */

   public RelationImpl() {
      this(null, null);
   }

   /* Constructor that uses the order for A and B of a given relation. */

   public RelationImpl(RelationImpl<A, B> r) {
      this(r.c1, r.c2);
   }

   /* Constructor that supports the use of explicit orders for A and B. */

   public RelationImpl(Comparator<A> c1, Comparator<B> c2) {
      this.c1 = c1;
      this.c2 = c2;
      EMPTY_A = java.util.Collections.unmodifiableSet(newSet(c1));
      EMPTY_B = java.util.Collections.unmodifiableSet(newSet(c2));
      map1 = newMap(c1);
      map2 = newMap(c2);
      domain = map1.keySet();
      range = map2.keySet();
      size = 0;
   }

   /* Methods for instantiating concrete collections. */

   protected static <T> Set<T> newSet(Comparator<T> c) {
      return new TreeSet<T>(c);
   }

   protected static <K, V> Map<K, V> newMap(Comparator<K> c) {
      return new TreeMap<K, V>(c);
   }

   protected static <A, B> Maplet<A, B> newMaplet(A x, B y) {
      if (USE_FLYWEIGHT) {
         return FlyweightMaplet.create(x, y);
      }
      else {
         return new Maplet<A, B>(x, y);
      }
   }

   @SuppressWarnings("unchecked")
   protected static <T> Set<T> cloneSet(Set<T> set) {
      return (Set<T>) ((TreeSet<T>) set).clone();
   }

   @SuppressWarnings("unchecked")
   protected static <K, V> Map<K, V> cloneMap(Map<K, V> map) {
      return (Map<K, V>) ((TreeMap<K,V>) map).clone();
   }

   /********************/
   /* Internal Methods */
   /********************/

   /* The get methods return null if the element is not in the domain. */

   protected Set<B> get1(A value) {
      return map1.get(value);
   }

   protected Set<A> get2(B value) {
      return map2.get(value);
   }

   /* The return value of the insert methods indicates if the object was
    * already present in the maps. */

   protected boolean insert1(Maplet<A, B> maplet) {
      if (!domain.contains(maplet.fst)) {
         map1.put(maplet.fst, newSet(c2));
      }
      return get1(maplet.fst).add(maplet.snd);
   }

   protected boolean insert2(Maplet<A, B> maplet) {
      if (!range.contains(maplet.snd)) {
         map2.put(maplet.snd, newSet(c1));
      }
      return get2(maplet.snd).add(maplet.fst);
   }

   /* The return value of the delete methods indicates if the object was
    * present in the maps, i.e. deletion has caused the maps to change. */

   protected boolean delete1(Maplet<A, B> maplet) {
      boolean modified = false;
      if (domain.contains(maplet.fst)) {
         Set<B> set = get1(maplet.fst);
         set.remove(maplet.snd);
         if (set.isEmpty()) {
            map1.remove(maplet.fst);
         }
         modified = true;
      }
      return modified;
   }

   protected boolean delete2(Maplet<A, B> maplet) {
      boolean modified = false;
      if (range.contains(maplet.snd)) {
         Set<A> set = get2(maplet.snd);
         set.remove(maplet.fst);
         if (set.isEmpty()) {
            map2.remove(maplet.snd);
         }
         modified = true;
      }
      return modified;
   }

   /* The following methods modify both maps simultaneously. */

   protected boolean insert(Maplet<A, B> maplet) {
      boolean modified = false;
      modified |= insert1(maplet);
      modified |= insert2(maplet);
      if (modified) size++;
      return modified;
   }

   protected boolean delete(Maplet<A, B> maplet) {
      boolean modified = false;
      modified |= delete1(maplet);
      modified |= delete2(maplet);
      if (modified) size--;
      return modified;
   }

   /* The following two methods are rather inefficient since they create new
    * sets of maplets from scratch. I added them mostly for validation. */

   protected Set<Maplet<A, B>> toMapletSet1() {
      Set<Maplet<A, B>> result =
         newSet(new MapletComparator<A, B>(c1, c2));
      for (Entry<A, Set<B>> entry : map1.entrySet()) {
         A fst = entry.getKey();
         for (B snd : entry.getValue()) {
            result.add(newMaplet(fst, snd));
         }
      }
      return result;
   }

   /* Equivalent to toMapletSet2(); primarily for validation purposes. */

   protected Set<Maplet<A, B>> toMapletSet2() {
      Set<Maplet<A, B>> result =
         newSet(new MapletComparator<A, B>(c1, c2));
      for (Entry<B, Set<A>> entry : map2.entrySet()) {
         B snd = entry.getKey();
         for (A fst : entry.getValue()) {
            result.add(newMaplet(fst, snd));
         }
      }
      return result;
   }

   /* Shall the following method be exposed? I do not see a need for now. */

   protected Set<Maplet<A, B>> toMapletSet() {
      return toMapletSet1();
   }

   /*******************************************************************/
   /* Implementation of the AbstractSet<Maplet<A, B>> abstract class. */
   /*******************************************************************/

   @Override
   public boolean add(Maplet<A, B> maplet) {
      validate();
      boolean result;
      result = insert(maplet);
      validate();
      return result;
   }

   @Override
   public void clear() {
      validate();
      map1.clear();
      map2.clear();
      size = 0;
      validate();
   }

   @Override
   public boolean contains(Object o) {
      validate();
      @SuppressWarnings("unchecked")
      Maplet<A, B> maplet = (Maplet<A, B>) o;
      Set<B> image = get1(maplet.fst);
      return image != null && image.contains(maplet.snd);
   }

   @Override
   public boolean equals(Object o) {
      if (o instanceof RelationImpl) {
         if (o == this) {
            return true;
         }
         else {
            RelationImpl r = (RelationImpl) o;
            return map1.equals(r.map1);
         }
      }
      else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return map1.hashCode();
   }

   @Override
   public Iterator<Maplet<A, B>> iterator() {
      return new MapletIterator();
   }

   @Override
   @SuppressWarnings("unchecked")
   public boolean remove(Object maplet) throws ClassCastException {
      validate();
      return delete((Maplet<A, B>) maplet);
   }

   @Override
   public int size() {
      validate();
      return size;
   }

   /***************************************************/
   /* Implementation of the Relation<A, B> interface. */
   /***************************************************/

   public Comparator<A> domOrder() {
      return c1;
   }

   public Comparator<B> ranOrder() {
      return c2;
   }

   public B apply(A value) {
      validate();
      Set<B> image = get1(value);
      if (image == null) {
         throw new IllegalArgumentException(
            "Application outside the relation's domain.");
      }
      if (image.size() > 1) {
         throw new IllegalArgumentException(
            "Relation is not functional at this point.");
      }
      return image.iterator().next();
   }

   public Set<B> image(A value) {
      validate();
      Set<B> image = get1(value);
      /*return image == null ? EMPTY_B : cloneSet(image);*/
      return image == null ? EMPTY_B : new SafelyIterableSet<B>(image);
   }

   public Set<B> image(Set<A> values) {
      validate();
      Set<B> result = newSet(c2);
      for(A value : values) {
         result.addAll(image(value));
      }
      return result;
   }

   public Set<A> domain() {
      validate();
      return new SafelyIterableSet<A>(domain);
   }

   public Set<B> range() {
      validate();
      return new SafelyIterableSet<B>(range);
   }

   /* Some of the following methods depend on the RelationImpl implementation
    * rather than the Relation interface. It may be worth to try and minimise
    * this dependency although there is also an efficiency trade-off here. */

   public Relation<A, B> domRes(Set<A> set) {
      validate();
      @SuppressWarnings("unchecked")
      RelationImpl<A, B> result = (RelationImpl<A, B>) clone();
      for (A fst : domain) {
         if (!set.contains(fst)) {
            for (B snd : get1(fst)) {
               result.delete(newMaplet(fst, snd));
            }
         }
      }
      result.validate();
      return result;
   }

   public Relation<A, B> domSub(Set<A> set) {
      validate();
      @SuppressWarnings("unchecked")
      RelationImpl<A, B> result = (RelationImpl<A, B>) clone();
      for (A fst : domain) {
         if (set.contains(fst)) {
            for (B snd : get1(fst)) {
               result.delete(newMaplet(fst, snd));
            }
         }
      }
      result.validate();
      return result;
   }

   public Relation<A, B> ranRes(Set<B> set) {
      validate();
      @SuppressWarnings("unchecked")
      RelationImpl<A, B> result = (RelationImpl<A, B>) clone();
      for (B snd : range) {
         if (!set.contains(snd)) {
            for (A fst : get2(snd)) {
               result.delete(newMaplet(fst, snd));
            }
         }
      }
      result.validate();
      return result;
   }

   public Relation<A, B> ranSub(Set<B> set) {
      validate();
      @SuppressWarnings("unchecked")
      RelationImpl<A, B> result = (RelationImpl<A, B>) clone();
      for (B snd : range) {
         if (set.contains(snd)) {
            for (A fst : get2(snd)) {
               result.delete(newMaplet(fst, snd));
            }
         }
      }
      result.validate();
      return result;
   }

   public Relation<B, A> inverse() {
      validate();
      @SuppressWarnings("unchecked")
      RelationImpl<A, B> clone = (RelationImpl<A, B>) clone();
      RelationImpl<B, A> result = new RelationImpl<B, A>(c2, c1);
      /* Simply carry out a symmetric exchange of member fields! */
      result.map1 = clone.map2;
      result.map2 = clone.map1;
      result.domain = clone.range;
      result.range = clone.domain;
      result.size = clone.size;
      result.validate();
      return result;
   }

   public <C> Relation<A, C> compose(Relation<B, C> r) {
      validate();
      @SuppressWarnings("unchecked")
      RelationImpl<A, C> result =
         new RelationImpl<A, C>(c1, ((RelationImpl<B, C>) r).c2);
      RelationImpl<B, C> r1 = (RelationImpl<B, C>) r;
      for (B y : range) {
         /*assert get2(y) != null;*/
         for (A x : get2(y)) {
            Set<C> r1_image = r1.get1(y);
            if (r1_image != null) {
               for (C z : r1_image) {
                  result.add(newMaplet(x, z));
               }
            }
         }
      }
      result.validate();
      return result;
   }

   /*************************************************************/
   /* Implementation of the Copyable<Relation<A, B>> interface. */
   /*************************************************************/

   @SuppressWarnings("unchecked")
   public Relation<A, B> copy() {
      return (Relation<A, B>) clone();
   }

   /* The following method checks consistency of the object. */

   public void validate() {
      if (!Validation.VALIDATE) {
         return;
      }
      if (map1.values().contains(null)) {
         throw new ValidationError(
            "Validation failed due to map1 containing null.");
      }
      if (map2.values().contains(null)) {
         throw new ValidationError(
            "Validation failed due to map1 containing null.");
      }
      if (map1.values().contains(EMPTY_A)) {
         throw new ValidationError(
            "Validation failed due to map1 containing empty set(s).");
      }
      if (map1.values().contains(EMPTY_B)) {
         throw new AssertionError(
            "Validation failed due to map2 containing empty set(s).");
      }
      if (!toMapletSet1().equals(toMapletSet2())) {
         throw new ValidationError(
            "Validation failed due to inconsistency between maps.");
      }
      if (toMapletSet().size() != size) {
         throw new ValidationError(
            "Validation failed due to inconsistent size member.");
      }
   }

   /* Creates a shallow copy of this objects. */

   @Override
   @SuppressWarnings("unchecked")
   public Object clone() {
      validate();
      try {
         RelationImpl<A, B> copy = (RelationImpl<A, B>) super.clone();
         /* Create deep copies of the maps. */
         copy.map1 = cloneMap(map1);
         copy.map2 = cloneMap(map2);
         /* Create deep copies of the values in each maps. */
         for (Entry<A, Set<B>> entry : map1.entrySet()) {
            entry.setValue(cloneSet(entry.getValue()));
         }
         for (Entry<B, Set<A>> entry : map2.entrySet()) {
            entry.setValue(cloneSet(entry.getValue()));
         }
         /* Reinitialise domain and range convenience fields. */
         copy.domain = copy.map1.keySet();
         copy.range = copy.map2.keySet();
         copy.validate();
         return copy;
      }
      catch(CloneNotSupportedException e) {
         /* Should never happen since we implement Cloneable. */
         throw new AssertionError(e);
      }
   }

   @Override
   public String toString() {
      validate();
      StringBuilder result = new StringBuilder();
      result.append("{");
      Iterator<Maplet<A, B>> iter = iterator();
      while (iter.hasNext()) {
         Maplet<A, B> maplet = iter.next();
         result.append(maplet.toString());
         if (iter.hasNext()) {
            result.append(", ");
         }
      }
      result.append("}");
      return result.toString();
   }
   
 //Removes Weird Duplicate methods inherited named SPliterator error: Matt 29 May 2015
   @Override
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public Spliterator spliterator()
{
	return null;
}

   /**********************************************/
   /* Inner Class for the Maplet<A, B> iterator. */
   /**********************************************/

   final class MapletIterator implements Iterator<Maplet<A, B>> {
      private Iterator<Map.Entry<A, Set<B>>> iter1;
      private Iterator<B> iter2;
      private Map.Entry<A, Set<B>> entry;
      private Maplet<A, B> maplet;

      public MapletIterator() {
         /* A more defensive solution wraps the set into SafelyIterableSet. */
         /*Set<Map.Entry<A, Set<B>>> safe =
            new SafelyIterableSet<Map.Entry<A, Set<B>>>(map1.entrySet());*/
         iter1 = map1.entrySet().iterator();
         if (iter1.hasNext()) {
            advanceIter1();
         }
      }

      private void advanceIter1() {
         entry = iter1.next();
         iter2 = entry.getValue().iterator();
         assert iter2.hasNext();
      }

      public boolean hasNext() {
         return (iter2 != null && iter2.hasNext()) || iter1.hasNext();
      }

      public Maplet<A, B> next() {
         if (iter2 != null && iter2.hasNext()) {
            maplet = newMaplet(entry.getKey(), iter2.next());
            return maplet;
         }
         else if (iter1.hasNext()) {
            advanceIter1();
            return next();
         }
         else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         RelationImpl.this.remove(maplet);
      }
   }
}
