package hijac.tests;

import hijac.tools.config.Statics;
import hijac.tools.collections.*;

import java.util.*;

public class RelationImplTest {
   public static void closure() {
      Relation<String, String>
         r = RelationFactory.newRelation();
      r.add(new Maplet<String, String>("Frank", "Leo"));
      r.add(new Maplet<String, String>("Leo", "Jim"));
      r.add(new Maplet<String, String>("Jim", "Ana"));
      /*r.add(new Maplet<String, String>("Ana", "Frank"));*/
      System.out.println("Relation: " + r);
      System.out.println("Closure: " + Relations.closure(r));
      try {
         System.out.println("Topological Order: " +
            Relations.topologicalSort(r));
      }
      catch (CyclicException e) { }
   }

   public static void main(String[] args) {
      Statics.kickstart();
      closure();
   }
}
