package hijac.tools.collections;

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class Maplet<A, B> {
   public final A fst;
   public final B snd;

   public Maplet(A fst, B snd) {
      this.fst = fst;
      this.snd = snd;
   }

   public boolean isReflexive() {
      return fst.equals(snd);
   }

   public Maplet<B, A> swap() {
      return new Maplet<B, A>(snd, fst);
   }

   @Override
   public int hashCode() {
      return fst.hashCode() ^ snd.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof Maplet) {
         if (obj == this) {
            return true;
         }
         else {
            Maplet maplet = (Maplet) obj;
            return
               maplet.fst.equals(fst) &&
               maplet.snd.equals(snd);
         }
      }
      return false;
   }

   @Override
   public String toString() {
      return fst + " |-> " + snd;
   }
}
