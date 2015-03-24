class B {

    A a;
    A a2 = new A();    // Error - Instance of A must reside in "MyMission" memory
    Object o;

    void foo(A a) {
        a.bar();    // Error - Illegal invocation: bar() is MARY_ALLOCATE
        o = a;      // Error - Cannot assign variable from "MyMission" scope to a variable in scope "MyHandler"
    }
}
