package icecaptools.debugging.variables;

import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.Method;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;

public class HVMCharVariable extends HVMNumberVariable {

    public HVMCharVariable(IDebugTarget iDebugTarget, LocalVariable localVariable, Method method) {
        super(iDebugTarget, localVariable, method);
    }

    @Override
    protected IValue getNumberValue(IDebugTarget target, long ival) {
        return new CharValue(target, ival);
    }

    @Override
    protected byte[] getValueBuffer() {
        return new byte[1];
    }
}
