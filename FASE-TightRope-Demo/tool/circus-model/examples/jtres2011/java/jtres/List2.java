package jtres;

/* Alternative implementation of List as a bit set over characters. */

/* Note that the storage requirements depend on the bit size of a character. */

public class List2 {
   private final boolean[] map;
   private int count;

   public List2() {
      map = new boolean[Character.MAX_VALUE - Character.MIN_VALUE + 1];
      clear();
   }

   /* Public Methods (Specification) */

   /* The following method must execute within the deadline for Handler1. */

   public synchronized void insert(char c) {
      if (count == 50) {
         clear();
      }
      if (!contains(c)) {
         include(c);
         count++;
      }
   }

   /* The following method must execute within the deadline for Handler2. */

   public synchronized int size() {
      return count;
   }

   /* Private Methods (Internal) */

   private synchronized boolean contains(char c) {
      return map[getIndex(c)];
   }

   private synchronized void include(char c) {
      map[getIndex(c)] = true;
   }

   private synchronized void clear() {
      for (char c = Character.MIN_VALUE; c <= Character.MAX_VALUE; c++) {
         map[getIndex(c)] = false;
      }
      count = 0;
   }

   private static int getIndex(char c) {
      return c - Character.MIN_VALUE;
   }
}
