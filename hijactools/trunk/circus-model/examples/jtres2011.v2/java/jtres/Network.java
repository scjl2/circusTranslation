package jtres;

/* This class was added to model the network resource. */

public class Network {
   private NetworkState state;

   public Network() {
      state = NetworkState.DISCONNECTED;
   }

   public synchronized NetworkState getState() {
      return state;
   }

   public synchronized void connect() {
      /* We could simulate a delay here as well. */
      System.out.println("[Network] connect");
      assert state == NetworkState.DISCONNECTED;
      /* TODO: Carry out device access. (Discuss with Andy) */
      state = NetworkState.CONNECTED;
   }

   public synchronized void send(java.util.Set set) {
      /* We could simulate a delay here as well. */
      System.out.println("[Network] sending " + set.toString());
      assert state == NetworkState.CONNECTED;
      /* TODO: Carry out device access. (Discuss with Andy) */
      state = NetworkState.SENT;
   }

   public synchronized void disconnect() {
      /* We could simulate a delay here as well. */
      System.out.println("[Network] disconnect");
      assert state == NetworkState.CONNECTED;
      /* TODO: Carry out device access. (Discuss with Andy) */
      state = NetworkState.DISCONNECTED;
   }
}
