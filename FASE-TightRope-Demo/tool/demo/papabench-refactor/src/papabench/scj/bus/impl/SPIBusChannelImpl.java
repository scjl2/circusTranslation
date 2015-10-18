/**
 * 
 */
package papabench.scj.bus.impl;

import papabench.scj.bus.SPIBus;
import papabench.scj.bus.SPIBusChannel;
import papabench.scj.commons.data.InterMCUMsg;
import papabench.scj.utils.LogUtils;

/**
 * SPI Bus abstraction. Each bus contains two ends - slave and master.
 * Each end is represented by {@link SPIBus} interface. 
 * 
 * The channel handles messages exchange between both ends.
 * 
 * @see SPIBus
 * 
 * @author Michal Malohlava
 *
 */
public class SPIBusChannelImpl implements SPIBusChannel {
	
	private static final int MAX_MSG_IN_POOL = 20;
	
	private SPIBusImpl masterEnd;
	private SPIBusImpl slaveEnd;
	
	private ArrayCyclicQueue messagePool;
	
	public SPIBus getMasterEnd() {
		return masterEnd;
	}

	public SPIBus getSlaveEnd() {
		return slaveEnd;
	}

	public void init() {
		// create pool
		messagePool = new ArrayCyclicQueue(MAX_MSG_IN_POOL);		
		for (int i = 0; i < MAX_MSG_IN_POOL; i++) {
			messagePool.offer(new InterMCUMsg(true));
		}
		
		masterEnd = new SPIBusImpl(this);
		slaveEnd = new SPIBusImpl(this);
		
		masterEnd.setSPIBusEnd(slaveEnd);
		slaveEnd.setSPIBusEnd(masterEnd);
		
		masterEnd.init();
		slaveEnd.init();
	}

	public void reset() {
		masterEnd.reset();
		slaveEnd.reset();
	}
	
	protected void freeMessage(InterMCUMsg msg) {
		messagePool.offer(msg);		
	}
	
	protected InterMCUMsg getFreeMessage() {
		return (InterMCUMsg) messagePool.poll();	
	}
	
	private class SPIBusImpl implements SPIBus {
		
		private SPIBusImpl spiBusEnd;
		private SPIBusChannelImpl spiBusChannel;
		
		private ArrayCyclicQueue msgQueue;;  
		
		SPIBusImpl(SPIBusChannelImpl spiBusChannelArg) {
			spiBusChannel = spiBusChannelArg;			
		}

		public boolean getMessage(InterMCUMsg msg) {
			if (!msgQueue.isEmpty()
				&& ((InterMCUMsg) msgQueue.peek()).isValid() 
				) {
				InterMCUMsg internalSPIMsg = (InterMCUMsg) msgQueue.poll();
				
				// copy values from internal SPI bus message
				msg.fillFrom(internalSPIMsg);
				
				// put message back to message poll
				freeMessage(internalSPIMsg);							
				
				return true;
				
			} else {
				msg.setValid(false);
				
				return false;
			}
		}

		public void sendMessage(InterMCUMsg msg) {
			// get message from pool
			LogUtils.log(this, "get free message");
			InterMCUMsg internalSPIMsg = getFreeMessage();
			
			// copy given message to pool message
			LogUtils.log(this, "fill from");
			internalSPIMsg.fillFrom(msg);
			
			// put message into the other SPI's end queue
			LogUtils.log(this, "put message into queu");
			putMessageIntoTheOppositeQueue(internalSPIMsg);									
		}

		public void init() {	
			if (spiBusEnd == null || spiBusChannel == null) {
				throw new IllegalArgumentException("SPIBus has wrong configuration!");
			}
			
			msgQueue = new ArrayCyclicQueue(MAX_MSG_IN_POOL);
		}

		public void reset() {
		}
		
		private void freeMessage(InterMCUMsg msg) {
			spiBusChannel.freeMessage(msg);			
		}
		
		private InterMCUMsg getFreeMessage() {
			return spiBusChannel.getFreeMessage();
		}
		
		private void putMessageIntoTheOppositeQueue(InterMCUMsg msg) {
			LogUtils.log(this, "putting message into queu");
			spiBusEnd.msgQueue.offer(msg);						
		}
		
		private void setSPIBusEnd(SPIBusImpl end) {
			spiBusEnd = end;
		}
	}
}
