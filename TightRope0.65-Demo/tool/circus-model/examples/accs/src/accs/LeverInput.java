package accs;

public class LeverInput {
   private LeverPosition input;

   public LeverInput() { }

   public synchronized void setLeverInput(LeverPosition value) {
      input = value;
   }

   public synchronized LeverPosition getLeverInput() {
      return input;
   }
}
