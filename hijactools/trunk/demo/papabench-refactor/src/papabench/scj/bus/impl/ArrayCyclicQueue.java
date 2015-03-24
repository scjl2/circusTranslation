/**
 * 
 */
package papabench.scj.bus.impl;

/**
 * Simple cyclic queue.
 * 
 * @author Michal Malohlava
 *
 */
public class ArrayCyclicQueue /* implements Queue */ {
	
	private Object[] queue;
	// the head of queue
	private int queueStart;
	// the last item of queue
	private int queueEnd;
	private int queueSize;
	
	private int maxSize;
	
	public ArrayCyclicQueue(int maxSizeArg) {
		maxSize = maxSizeArg;
		queue = new Object[maxSize];
		queueEnd = 0; // tail - first empty slot
		queueStart = -1; // first element
		queueSize = 0;
	}
	
	public synchronized void offer(Object o) {
		queue[queueEnd] = o;		
		queueSize++;
		if (queueSize == 1) {
			queueStart = queueEnd;
		}
		queueEnd = (queueEnd + 1) % maxSize;
	}
	
	public synchronized Object poll() {
		if (queueSize == 0) {
			return null;
		} else {
			Object o = queue[queueStart];
			queue[queueStart] = null;
			queueStart = (queueStart + 1) % maxSize;
			queueSize--;
			
			return o;
		}
	}
	
	public synchronized Object peek() {
		if (queueSize == 0) {
			return null;
		} else {
			return queue[queueStart];
		}
	}
	
	public synchronized boolean isEmpty() {
		return queueSize == 0;
	}
}
