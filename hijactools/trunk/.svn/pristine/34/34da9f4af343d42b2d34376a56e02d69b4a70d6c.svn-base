package jtres;

/* A simple but not very efficient implementation. */

public class List {
   private int val;
   private List next;
   private boolean empty;

   public List() {
      empty = true;
   }

   public synchronized void insert(int value) {
      List node = this;
      while (!node.empty) {
         node = node.next;
      }
      node.val = value;
      node.next = new List();
      node.empty = false;
   }

   /* I use a java List below to represent the type \power \num. */

   public synchronized
      java.util.Set<Integer> elems() {
      java.util.Set<Integer> ret =
         new java.util.TreeSet<Integer>();
      List node = next;
      while (!node.empty) {
         ret.add(node.val);
         node = node.next;
      }
      return ret;
   }
}
