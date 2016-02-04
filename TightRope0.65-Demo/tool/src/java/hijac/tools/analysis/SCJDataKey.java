package hijac.tools.analysis;

/* Previously I pondered whether we should moreover reflect the type of the
 * stored value in the data key. I decided in the end against it because this
 * makes the handling of keys more awkward. In particular, when the value is
 * itself of some generic type. There is not a lot of added benefit from
 * this either, maybe even confusion if keys with similar names coexist. */

/* Immutable class */

/**
 * @author Frank Zeyda
 * @version $Revision: 198 $
 */
public class SCJDataKey implements Comparable<SCJDataKey>, Cloneable {
   public final String namespace;
   public final String name;

   public SCJDataKey(String namespace, String name) {
      assert namespace != null;
      assert name != null;
      this.namespace = namespace;
      this.name = name;
   }

   public int hashCode() {
      return namespace.hashCode() ^ name.hashCode() ;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (obj instanceof SCJDataKey) {
         SCJDataKey key = (SCJDataKey) obj;
         return
            namespace.equals(key.namespace) && name.equals(key.name);
      }
      else {
         return super.equals(obj);
      }
   }

   /* We use a lexicographic order w.r.t. namespace and key name. */

   public int compareTo(SCJDataKey key) {
      int result = namespace.compareTo(key.namespace);
      if (result == 0) {
         result = name.compareTo(key.name);
      }
      return result;
   }

   public String toString() {
      return namespace + "." + name;
   }
}
