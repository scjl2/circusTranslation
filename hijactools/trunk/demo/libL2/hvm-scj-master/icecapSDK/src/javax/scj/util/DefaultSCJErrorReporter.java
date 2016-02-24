package javax.scj.util;

import util.StringUtil;
import vm.Memory;

public class DefaultSCJErrorReporter implements SCJErrorReporter {

	@Override
	public void processExecutionError(Throwable t) {
		devices.Console.println("SCJ Process execution error: " + t);
		if (t instanceof java.lang.OutOfMemoryError) {
			Memory.reportMemoryUsage();
		}
	}

	@Override
	public void processOutOfMemoryError(OutOfMemoryError o) {
		devices.Console.println("No more ImmortalMemory to print error");
		devices.Console.println("Please increase ImmortalMemory to see full report");
	}

	@Override
	public void schedulerError(Throwable t) {
		devices.Console.println("Scheduler error: " + t);
	}

	@Override
	public String processOutOfBackingStoreError(int start, int end, int left) {
		StringBuffer buf = StringUtil.constructStringBuffer("Out of backingstore: ", start);
		buf.append(StringUtil.constructStringBuffer(", ", end));
		buf.append(StringUtil.constructStringBuffer(", ", left));
		return buf.toString();
	}

}
