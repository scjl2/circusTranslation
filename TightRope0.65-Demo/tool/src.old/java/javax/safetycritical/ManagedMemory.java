package javax.safetycritical;

import javax.realtime.LTMemory;

import javax.safetycritical.annotate.*;

@SCJAllowed
public abstract class ManagedMemory extends LTMemory {

    public static void enterPrivateMemory(long size, Runnable logic) {
    }

    public static void executeInAreaOf(Object obj, Runnable logic) {
    }

    public static void executeInOuterArea(Runnable logic) {
    }

    public long getRemainingBackingStore() {
        return 0;
    }

}
