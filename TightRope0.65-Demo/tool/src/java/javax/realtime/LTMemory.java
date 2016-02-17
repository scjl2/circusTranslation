package javax.realtime;

import javax.safetycritical.annotate.*;

@SCJAllowed
public class LTMemory extends ScopedMemory {

    public long memoryConsumed() {
        return 0;
    }

    public long memoryRemaining() {
        return 0;
    }

    public Object newArray(Class type, int number) {
        return null;
    }

    public Object newInstance(Class type) {
        return null;
    }

    public long size() {
        return 0;
    }

}
