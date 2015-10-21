package javax.safetycritical;

class MemoryInfo {

	ManagedMemory privateMemory;
	ManagedMemory currentMemory;
	ManagedMemory topMemory;
	
	private MemoryInfo()
	{
		;
	}
	
	public MemoryInfo(PrivateMemory mem) {
		this();
		this.privateMemory = mem;
		this.currentMemory = mem;
		this.topMemory = mem;
	}
}
