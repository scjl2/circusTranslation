import javax.realtime.PeriodicParameters;
import javax.realtime.PeriodicTimer;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.Timer;
import javax.realtime.RelativeTime;
import javax.safetycritical.StorageParameters;
import javax.realtime.*;
import javax.safetycritical.*;

// Implementaion of a periodic event handler

public class DDDR_Handler_MotionSensor extends PeriodicEventHandler {
static boolean RM_flag = false ;
double S_value=0.0;

	public DDDR_Handler_MotionSensor() {
		super(new PriorityParameters(PriorityScheduler.instance()
				.getNormPriority()), new PeriodicParameters(null,
				new RelativeTime(30000, 0)), new StorageParameters(
				32768, 4096, 4096), "DDDR_Handler_MotionSensor");

	}

	public void handleAsyncEvent() {
	  try{

	    if( RM_flag == false  && S_value > 0.5 ){  // different activities have diffrent threshold so for medium we have 0.5 
	      for(int i=1; i< (int)Math.ceil((120-60)/DDDR_PMMission.Slop);i++){
		DDDR_PMMission.PaceInterval =  (int)Math.ceil(60000/(60 + DDDR_PMMission.Slop*i));
		RealtimeThread.sleep(new RelativeTime(DDDR_PMMission.reactionTime/(int)Math.ceil((120-60)/DDDR_PMMission.Slop),0));  //sleep
		System.out.println("Update Pacing Interval :" + DDDR_PMMission.PaceInterval);
	      }
	    RM_flag = true;
	    }else if(RM_flag == true  && S_value <= 0.5){
	      for(int i=1; i< Math.ceil((120-60)/DDDR_PMMission.Slop);i++){
		DDDR_PMMission.PaceInterval =  (int)Math.ceil(60000/(120 - DDDR_PMMission.Slop*i));
		 RealtimeThread.sleep(new RelativeTime((int)Math.ceil(DDDR_PMMission.recoveryTime/Math.ceil((120-60)/DDDR_PMMission.Slop)),0));  //sleep
		 System.out.println("Update Pacing Interval :" + DDDR_PMMission.PaceInterval);
	      }
	      RM_flag = false;
	    }

	  }catch (InterruptedException ie){
	    System.out.println(ie.getMessage());
	   }
}

}// end class








