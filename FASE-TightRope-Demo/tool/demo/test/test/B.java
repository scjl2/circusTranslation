package test;

class B {
   public int y;

   { y = 2; }
   { y = 3; }

   public B() {
      //wait();
   }

   public void bar() {
      abc();
      try {
         wait();
      }
      catch (InterruptedException e) {
      }
   }

   public static synchronized void abc() {

   }
}
