package hijac.tools.collections;

import hijac.tools.utils.Validatable;

import java.util.Comparator;
import java.util.Set;
import java.util.Spliterator;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public interface Relation<A, B> extends
      Set<Maplet<A, B>>, Copyable<Relation<A, B>>, Validatable {
   public Comparator<A> domOrder();
   public Comparator<B> ranOrder();
   public B apply(A value);
   public Set<B> image(A value);
   public Set<B> image(Set<A> values);
   public Set<A> domain();
   public Set<B> range();
   public Relation<A, B> domRes(Set<A> a);
   public Relation<A, B> domSub(Set<A> a);
   public Relation<A, B> ranRes(Set<B> b);
   public Relation<A, B> ranSub(Set<B> b);
   public Relation<B, A> inverse();
   public <C> Relation<A, C> compose(Relation<B, C> r);
   //Removes Weird Duplicate methods inherited named SPliterator error: Matt 29 May 2015
   @Override
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public Spliterator spliterator() ;
}
