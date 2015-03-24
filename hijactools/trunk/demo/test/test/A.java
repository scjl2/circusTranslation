package test;

class A {
   public int x;

   class C {
      public int y;

      class D {
         public int z;
      }
   }

   static class X {
      public static int abc;
   }

   static {
      System.out.println("Hello World");
   }
   
   public A() {
      x = 1;
   }

   public void foo() {
      B b = new B() { public void test() { } } ;
      synchronized (this) {
         x = 2;
      }
   }
}
