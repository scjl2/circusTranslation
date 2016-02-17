package nestedSequencer2;

import javax.safetycritical.LaunchLevel2;
import javax.safetycritical.StorageParameters;
import javax.scj.util.Const;

public class MySCJ {

    public static StorageParameters storageParameters;
    public static StorageParameters storageParameters_Handlers;

    public static void main(String[] args) {
	 storageParameters = new StorageParameters(150 * 1000, new long[] { Const.HANDLER_STACK_SIZE },
	 Const.PRIVATE_MEM_DEFAULT-25*1000, Const.IMMORTAL_MEM_DEFAULT-50*1000, Const.MISSION_MEM_DEFAULT-100*1000);
	
	 storageParameters_Handlers = new StorageParameters(Const.PRIVATE_MEM_DEFAULT-30*1000, new long[] { Const.HANDLER_STACK_SIZE },
	 Const.PRIVATE_MEM_DEFAULT-30*1000, Const.IMMORTAL_MEM_DEFAULT-50*1000, Const.MISSION_MEM_DEFAULT-100*1000);

	new LaunchLevel2(new MyApp());
    }

}
