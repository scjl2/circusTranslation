package jtres;

import javax.realtime.RawMemory;
import javax.realtime.RawInt;

/* This class was added to model the network resource. The methods carry out
 * the device access for the enable, send and disable actions. We assume that
 * they are all asynchronous and instantaneous. */

public class Network {
   /* Device Control Commands */
   public static final int ENABLE_CMD = 0x1;
   public static final int SEND_CMD = 0x2;
   public static final int DISABLE_CMD = 0x4;

   /* Device Registers */
   public final static long NETWORK_CTRL_REGISTER_ADDRESS = 1;
   public final static long NETWORK_DATA_REGISTER_ADDRESS = 2;

   private final RawInt network_ctrl_register =
      RawMemory.createRawIntAccessInstance(
      RawMemory.IO_MEM_MAPPED, NETWORK_CTRL_REGISTER_ADDRESS);

   private final RawInt network_data_register =
      RawMemory.createRawIntAccessInstance(
      RawMemory.IO_MEM_MAPPED, NETWORK_DATA_REGISTER_ADDRESS);

   public Network() { }

   public synchronized void enable() {
      network_ctrl_register.put(ENABLE_CMD);
      System.out.println("[Network] enabled");
   }

   public synchronized void send(int size) {
      network_data_register.put(size);
      network_ctrl_register.put(SEND_CMD);
      System.out.println("[Network] sending " + size);
   }

   public synchronized void disable() {
      network_ctrl_register.put(DISABLE_CMD);
      System.out.println("[Network] disasble");
   }
}
