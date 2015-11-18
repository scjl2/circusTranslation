package jtres;

/* A simple implementation of List. */

public class List {
   private int val;
   private List next;
   private boolean empty;

   /* Invariant: !node.empty => node.next != null. */

   /* However note that it is not an equivalence! */

   public List() {
      next = null;
      empty = true;
   }

   /* Public Methods (Specification) */

   /* The following method must execute within the deadline for Handler1. */

   public synchronized void insert(int value) {
      if (size() == 50) {
         clear();
      }
      if (!contains(value)) {
         append(value);
      }
   }

   /* The following method must execute within the deadline for Handler2. */

   public synchronized int size() {
      int size = 0;
      List node = this;
      while (!node.empty) {
         node = node.next;
         size++;
      }
      return size;
   }

   /* Private Methods (Internal) */

   private synchronized boolean contains(int value) {
      boolean contains = false;
      List node = this;
      while (!node.empty) {
         if (node.val == value) {
            contains = true;
         }
         node = node.next;
      }
      return contains;
   }

   private synchronized void append(int value) {
      List node = this;
      while (!node.empty) {
         node = node.next;
      }
      node.val = value;
      /* We cannot just overwrite node.next as memory is not reclaimed. */
      if (node.next == null) {
         node.next = new List();
      }
      else {
         /* Bug pointed out by Andy. We need to ignore the former list. */
         node.next.empty = true;
      }
      node.empty = false;
   }

   private synchronized void clear() {
      empty = true;
   }
}
