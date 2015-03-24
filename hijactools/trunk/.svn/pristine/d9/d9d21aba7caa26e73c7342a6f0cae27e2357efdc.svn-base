package mission;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.Mission;

import javax.safetycritical.annotate.*;

@SCJAllowed(LEVEL_0)
@Scope("MyMission")
class MyMission extends Mission {

    static Immortal instance = new Immortal();

    A a = new A();  //Error - A must reside in "MyMission" memory area

    @SCJRestricted(value=INITIALIZATION, mayAllocate=true, maySelfSuspenc=true)
    public void initialize() {
        handler.B bObj = new handler.B();   //Error - B must reside in "MyHandler" memory area
        B aObj = new B();
        new MyHander();

        @DefineScope(name="MyMissionInit", parent="MyMission")
        ARunnable aRun = new ARunnable();   // Error - Not at LEVEL_0
        ManagedMemory.getCurrentManagedMemory().enterPrivateMemory(1000, aRun);

        @DefineScope(name="MyMissionInit", parent="MyMission")
        BRunnable bRun = new BRunnable();   // Error - Instance of BRunnable must reside in MyHandler
        ManagedMemory.getCurrentManagedMemory().enterPrivateMemory(1000, bRun);

    }

  public void cleanup() {
  }

  public long missionMemorySize() {
    return 131072;
  }
}


@SCJAllowed(LEVEL_1)
@Scope("MyMission")
@RunsIn("MyMissionInit")
class ARunnable implements Runnable {

    public void run() {
        System.out.println("ARunnable running...");
    }

}


@SCJAllowed(LEVEL_0)
@Scope("MyHandler")
@RunsIn("BRunnable")
class BRunnable implements Runnable {

    public void run() {
        System.out.println("BRunnable running...");
    }

}


@SCJAllowed(LEVEL_1)
@Scope("MyMission")
class B {

    A a;
    A a2 = new A();    // Error - Instance of A must reside in "MyMission" memory
    Object o;

    @SCJRestricted(mayAllocate=false)
    void foo(A a) {
        a.bar();    // Error - Illegal invocation: bar() is MARY_ALLOCATE
        o = a;      // Error - Cannot assign variable from "MyMission" scope to a variable in scope "MyHandler"
    }
}
