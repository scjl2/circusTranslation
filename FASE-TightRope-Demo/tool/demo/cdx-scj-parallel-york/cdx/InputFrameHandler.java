/**
 * @author Frank Zeyda, Kun Wei
 */
package cdx;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;

import javax.safetycritical.AperiodicEvent;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.PriorityScheduler;
import javax.safetycritical.StorageParameters;

import java.util.Iterator;
import java.util.Set;

/**
 * InputFrameHandler is a periodic handler that reads and stores radar frames
 * as they arrive. It also updates the shared variable "state" for previous
 * aircraft positions.
 */
public class InputFrameHandler extends PeriodicEventHandler {
  public final CDxMission mission;
  public final AperiodicEvent reduce;

  public InputFrameHandler(CDxMission missionArg, AperiodicEvent event) {
    super(
      new PriorityParameters(
        PriorityScheduler.instance().getMaxPriority()),
      new PeriodicParameters(null,
        new RelativeTime(Constants.DETECTOR_PERIOD, 0)),
      new StorageParameters(32768, 4096, 4096), "InputFrameHandler");
    mission = missionArg;
    reduce = event;
  }

  public void handleAsyncEvent() {
    /* In a real system perform hardware access to read the next frame. */
    RawFrame frame = null;

    /* Store the frame and update previous positions. */
    StoreFrame(frame);

    /* Release ReducerHandler to perform voxel hashing. */
    reduce.fire();
  }

  /* This method correspond to the StoreFrame action in the S anchor. */

  public void StoreFrame(RawFrame frame) {
      /* Update shared data that holds previous aircraft positions. */
      updateState();

      /* Read the frame into the shared variable. */
      mission.getFrame().copy(frame);
  }

  /**
   * This method records the current positions of all aircrafts in the
   * shared state variable of type StateTable.
   */
  public void updateState() {
    RawFrame frame = mission.getFrame();

    /* Used to determine disappeared aircrafts in the state table. */
    Set vanished = mission.getState().getCallSigns();

    for (int i = 0, pos = 0; i < frame.planeCnt; i++) {
      /* Get the current position of the next aircraft. */
      final float x = frame.positions[3 * i];
      final float y = frame.positions[3 * i + 1];
      final float z = frame.positions[3 * i + 2];

      /* Get the call sign of the next aircraft. */
      final byte[] cs = new byte[Constants.LENGTH_OF_CALLSIGN];
      for (int j = 0; j < cs.length; j++) {
        cs[j] = frame.callsigns[pos + j];
      }

      /* Advance index for call sign. */
      pos += cs.length;

      /* Create a new call sign. */
      CallSign callsign = new CallSign(cs);

      /* The aircrafts is in view; remove from the vanished set. */
      vanished.remove(callsign);

      /* Get old position from the state */
      final Vector3d old_pos = mission.getState().get(new CallSign(cs));

      if (old_pos == null) {
        /* We have detected a new aircraft. */
        /* Note that the callsign object is not actually inserted into the
         * StateTable but duplicated by the logic of the put method below. */
        mission.getState().put(callsign, x, y, z);
      }
      else {
        /* If the aircraft is already recorded in the stable table simply
         * update its position. */
        old_pos.set(x, y, z);
      }
    }

    StateTable state = mission.getState();

    /* Finally remove all aircrafts from the state table that have disappeared
     * from the radar in the current frame. This is important in order to give
     * them a zero velocity when they re-enter, a requirement of our model. */
    for (Iterator iter = vanished.iterator(); iter.hasNext(); ) {
      state.remove((CallSign) iter.next());
    }
  }
}
