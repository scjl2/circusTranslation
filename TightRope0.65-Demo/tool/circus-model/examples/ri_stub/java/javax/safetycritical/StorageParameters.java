package javax.safetycritical;

import javax.safetycritical.annotate.*;
import static javax.safetycritical.annotate.Level.*;
import static javax.safetycritical.annotate.Restrict.*;

@SCJAllowed
public class StorageParameters {
   private final long totalBackingStore;
   private final long nativeStack;
   private final long javaStack;
   private final int messageLength;
   private final int stackTraceLength;

   @SCJAllowed
   public StorageParameters(
         long totalBackingStore,
         long nativeStack,
         long javaStack) {
      this.totalBackingStore = totalBackingStore;
      this.nativeStack = nativeStack;
      this.javaStack = javaStack;
      this.messageLength = 0;
      this.stackTraceLength = 0;
   }

   @SCJAllowed
   public StorageParameters(
         long totalBackingStore,
         long nativeStack,
         long javaStack,
         int messageLength,
         int stackTraceLength) {
      this.totalBackingStore = totalBackingStore;
      this.nativeStack = nativeStack;
      this.javaStack = javaStack;
      this.messageLength = messageLength;
      this.stackTraceLength = stackTraceLength;
   }

   @SCJAllowed
   public long getJavaStackSize() {
      return javaStack;
   }

   @SCJAllowed
   public int getMessageLength() {
      return messageLength;
   }

   @SCJAllowed
   public long getNativeStackSize() {
      return nativeStack;
   }

   @SCJAllowed
   public int getStackTraceLength() {
      return stackTraceLength;
   }

   @SCJAllowed
   public long getTotalBackingStoreSize() {
      return totalBackingStore;
   }
}
