package javax.realtime;

import javax.safetycritical.annotate.*;

@SCJAllowed
public interface AllocationContext {

    public long memoryConsumed();

    public long memoryRemaining();

    public Object newArray(Class type, int number);

    public Object newInstance(Class type);

    public long size();

}
