import javax.realtime.*;

import javax.safetycritical.*;
import javax.safetycritical.annotate.*;

class Main {
  public static void main(final String[] args) {
    SafeletExecuter.run(new MainSafelet());
  }
}
