package javax.safetycritical;

import javax.safetycritical.annotate.*;

@SCJAllowed
public class StorageParameters {
  @SCJAllowed
  public StorageParameters(long storeSz, long nativeSz, long javaSz) {
  }

  @SCJAllowed
  public StorageParameters(long storeSZ, int nativeSz, int javaSz,
    int messageLen, int traceLen) {
  }

  @SCJAllowed
  public static long backingStoreConsumed() {
    return 0;
  }

  @SCJAllowed
  public static long backingStoreRemaining() {
    return 0;
  }

  @SCJAllowed
  public static long backingStoreSize() {
    return 0;
  }

  @SCJAllowed
  public static long nativeStackConsumed() {
    return 0;
  }

  @SCJAllowed
  public static long nativeStackRemaining() {
    return 0;
  }

  @SCJAllowed
  public static long nativeStackSize() {
    return 0;
  }

  @SCJAllowed
  public static long javaStackConsumed() {
    return 0;
  }

  @SCJAllowed
  public static long javaStackRemaining() {
    return 0;
  }

  @SCJAllowed
  public static long javaStackSize() {
    return 0;
  }

  @SCJAllowed
  public long getTotalBackingStoreSize() {
    return 0;
  }

  @SCJAllowed
  public long getNativeStackSize() {
    return 0;
  }

  @SCJAllowed
  public long getJavaStackSize() {
    return 0;
  }

  @SCJAllowed
  public int getMessageLength() {
    return 0;
  }

  @SCJAllowed
  public int getStackTraceLength() {
    return 0;
  }
}
