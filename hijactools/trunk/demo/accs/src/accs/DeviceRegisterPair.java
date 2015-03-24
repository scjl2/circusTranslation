/* REVIEWED */
package accs;

import javax.realtime.*;

import javax.safetycritical.*;

/* This class may still be subject to revision. */

public class DeviceRegisterPair {
  public final long REG_SIZE = 2;
  public final short ENABLE_DEVICE = 01;
  public final short SET_OPERATION = 02000;
  private RawMemoryAccess rawMemoryControl;
  private RawMemoryAccess rawMemoryData;
  private short shadow;

  public DeviceRegisterPair(long controlAddress, long dataAddress) {
    try {
      rawMemoryControl =
        new RawMemoryAccess(PhysicalMemoryManager.IO_PAGE, controlAddress,
          REG_SIZE);
      rawMemoryData =
        new RawMemoryAccess(PhysicalMemoryManager.IO_PAGE, dataAddress,
          REG_SIZE);
    }
    catch (Exception e) {
      throw new IllegalArgumentException();
    }
  }

  public synchronized void enableDevice() {
    try {
      shadow = ENABLE_DEVICE;
      rawMemoryControl.setShort(0, shadow);
    }
    catch (Exception e) { }
  }

  public synchronized void setOperate() {
    try {
      shadow = ENABLE_DEVICE | SET_OPERATION;
      rawMemoryControl.setShort(0, shadow);
    }
    catch (Exception e) { }
  }

  public synchronized void clearOperate() {
    try {
      /* Error in the original RTSJ code? */
      /*shadow = ENABLE_DEVICE | CLEAR_OPERATION;*/
      shadow = ENABLE_DEVICE;
      rawMemoryControl.setShort(0, shadow);
    }
    catch (Exception e) { }
  }

  public synchronized int readDataRegister() {
    try {
      return rawMemoryData.getInt(0);
    }
    catch (Exception e) { return 0; }
  }
}
