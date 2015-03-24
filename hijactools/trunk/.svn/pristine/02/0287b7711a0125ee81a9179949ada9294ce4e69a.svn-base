/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

/* This class may still be subject to revision. */

public class DeviceRegister {
  public final long REG_SIZE = 2;
  public final short ENABLE_DEVICE = 01;
  public final short SET_OPERATION = 02000;
  private RawMemoryAccess rawMemory;
  private short shadow;

  public DeviceRegister(long base) {
    try {
      rawMemory =
        new RawMemoryAccess(PhysicalMemoryManager.IO_PAGE, base, REG_SIZE);
    }
    catch (Exception e) {
      throw new IllegalArgumentException();
    }
  }

  public synchronized void enableDevice() {
    try {
      shadow = ENABLE_DEVICE;
      rawMemory.setShort(0, shadow);
    }
    catch (Exception e) { }
  }

  public synchronized void setOperate() {
    try {
      shadow = ENABLE_DEVICE | SET_OPERATION;
      rawMemory.setShort(0, shadow);
    }
    catch (Exception e) { }
  }

  public synchronized void clearOperate() {
    try {
      /* Error in the original RTSJ code? */
      /*shadow = ENABLE_DEVICE | CLEAR_OPERATION;*/
      shadow = ENABLE_DEVICE;
      rawMemory.setShort(0, shadow);
    }
    catch (Exception e) { }
  }
}
