import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.AperiodicLongEvent;
import javax.safetycritical.Mission;
import javax.safetycritical.ManagedMemory;

import javax.safetycritical.annotate.*;

class MyMission extends Mission {

    A a = new A();  //Error - A must reside in "MyMission" memory area

    public void initialize() {
        B bObj = new B();   //Error - B must reside in "MyHandler" memory area
        B aObj = new B();
        MyHandler handler = new MyHandler(new AperiodicEvent());

        ARunnable aRun = new ARunnable();   // Error - Not at LEVEL_0
        ManagedMemory.enterPrivateMemory(1000, aRun);

        BRunnable bRun = new BRunnable();   // Error - Instance of BRunnable must reside in MyHandler
        ManagedMemory.enterPrivateMemory(1000, bRun);

    }

  public void cleanup() {
  }

  public long missionMemorySize() {
    return 131072;
  }
}


class ARunnable implements Runnable {

    public void run() {
        System.out.println("ARunnable running...");
    }

}


class BRunnable implements Runnable {

    public void run() {
        System.out.println("BRunnable running...");
    }

}
