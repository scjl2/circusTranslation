package hijac.tests;

import java.util.Hashtable;
import java.util.Map;

public class HashcodeTest{
   static class DummyObject extends Object { }

   public static void reportClash(DummyObject obj1, DummyObject obj2) {
      System.out.println("obj1.hashCode() = " + obj1.hashCode());
      System.out.println("obj2.hashCode() = " + obj2.hashCode());
      System.out.println("(obj1 == obj2) = " + (obj1 == obj2) + " (!)");   
   }

   public static void main(String[] args) {
      Map<Integer, DummyObject> map = new Hashtable<Integer, DummyObject>();
      for (int count = 1; true; count++) {
         DummyObject obj = new DummyObject();
         if (map.containsKey(obj.hashCode())) {
            System.out.println(
               "Clash found after instantiating " + count + " objects.");
            reportClash(map.get(obj.hashCode()), obj);
            System.exit(0);
         }
         map.put(obj.hashCode(), obj);
      }
   }
}
