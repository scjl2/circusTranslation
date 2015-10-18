import javax.realtime.PriorityParameters;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.AperiodicParameters;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.ManagedMemory;

import javax.safetycritical.annotate.*;

public class MyHandler extends AperiodicEventHandler {


  public MyHandler(AperiodicEvent event) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getNormPriority()),
      new AperiodicParameters(null, null),
      new StorageParameters(32768, 4096, 4096),
      event, "MainHandler");
  }

    public void handleAsyncEvent() {
        A aObj = new A();   // Error - instance of A must reside in "MyMission" memory
        B bObj = new B();

        ARunnable aRun = new ARunnable();   // Error - "MyMissionInit" is not child of "MyHandler"
        ManagedMemory.enterPrivateMemory(1000, aRun);

        BRunnable bRun = new BRunnable();   // Error - "MyMissionInit" is not child of "MyHandler"
        ManagedMemory.enterPrivateMemory(1000, bRun);
    }

}
