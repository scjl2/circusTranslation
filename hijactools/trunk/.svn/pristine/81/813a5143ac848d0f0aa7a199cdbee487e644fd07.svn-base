package cdx;

import collision.Vector3d;

import javax.safetycritical.Mission;

import javacp.util.LinkedList;
import javacp.util.List;

/* Class added by Frank Zeyda */

/* Utility class to record a list of Motions in mission memory. */

/* There is a reason for copying the List into an array instead of simply
 * copying its reference i.e. into a field of type List. We recall that
 * run() executes in immortal memory, and hence statements such as, for
 * instance, Iterator iter = list.iterator(); and Motion motion = (Motion)
 * list.next(); produce downward references resulting in assignment errors.
 */

/* A fundamental problem with the duplication of data in mission memory
 * is related to the fact that such memory is not reclaimed until the
 * mission terminates. Hence, we should really abandon the below strategy
 * and use mutable data instead. The revision is pending work for Kun (?)
 */
public class RecordMotionsInMission implements Runnable {
    public final CDxMission mission;
    public final Motion[] motions;

    public RecordMotionsInMission(List list) {
        /* Need to be assigned here while we are still in scoped memory. */
        mission = (CDxMission) Mission.getCurrentMission();
        /* Copy list elements into an array. */
        motions = (Motion[]) list.toArray(new Motion[list.size()]);
    }

    public void run() {
        List duplicate = new LinkedList();
        for (int i = 0; i < motions.length; i++) {
            int callsign_length = motions[i].getAircraft().getCallsign().length;
            byte[] callsign = new byte[callsign_length];
            for (int index = 0; index < callsign_length; index++) {
                callsign[index] = motions[i].getAircraft().getCallsign()[index];
            }
            Aircraft aircraft = new Aircraft(callsign);
            Vector3d pos_one = new Vector3d(
                motions[i].getFirstPosition().x,
                motions[i].getFirstPosition().y,
                motions[i].getFirstPosition().z);
            Vector3d pos_two = new Vector3d(
                motions[i].getSecondPosition().x,
                motions[i].getSecondPosition().y,
                motions[i].getSecondPosition().z);
            Motion new_motion = new Motion(aircraft, pos_one, pos_two);
            duplicate.add(new_motion);
        }
        mission.getWork().recordVoxelMotions(duplicate);
    }
}
