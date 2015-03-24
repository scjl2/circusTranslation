package handler;

import javax.realtime.PriorityParameters;

import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.AperiodicParameters;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import javax.safetycritical.annotate.*;

@SCJAllowed(value=LEVEL_0, members=true)
@Scope("MyMission")
@RunsIn("MyHandler")
public class MyHandler extends AperiodicLongEventHandler {


  public MyHandler(@Ignore AperiodicLongEvent event) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new AperiodicParameters(null, null),
      new StorageParameters(32768, 4096, 4096),
      event, "MainHandler");
  }

    @SCJRestricted(mayAllocate=true, maySelfSuspend=true)
    public void handleAsyncLongEvent(long val) {
        A aObj = new A();   // Error - instance of A must reside in "MyMission" memory
        B bObj = new B();

        @DefineScope(name="MyHandler", parent="MyMission")
        ManagedMemory mem = ManagedMemory.getCurrentManagedMemory();

        @DefineScope(name="MyMissionInit", parent="MyHandler")
        ARunnable aRun = new ARunnable();   // Error - "MyMissionInit" is not child of "MyHandler"
        mem.enterPrivateMemory(1000, aRun);

        @DefineScope(name="BRunnable", parent="MyHandler")
        BRunnable BRun = new BRunnable();   // Error - "MyMissionInit" is not child of "MyHandler"
        mem.enterPrivateMemory(1000, bRun);
    }

}

@SCJAllowed(LEVEL_0)
@Scope("MyHandler")
class A {

    @SCJRestricted(mayAllocate=true)
    void bar() {}

}

@SCJAllowed(LEVEL_1)
@Scope("MyHandler")
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
