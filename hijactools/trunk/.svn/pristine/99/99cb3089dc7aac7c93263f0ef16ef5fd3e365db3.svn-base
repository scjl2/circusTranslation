package javax.safetycritical;

class RunMissionsInScope implements Runnable {
    private MissionSequencer sequencer;

    public RunMissionsInScope(MissionSequencer sequencer) {
        this.sequencer = sequencer;
    }

    public void run() {
        Mission mission = sequencer.getNextMission();
        while (mission != null) {
            mission.run();
            if (mission._terminateAll) {
                break;
            }
            //if (!mission._restart)
            mission = sequencer.getNextMission();
        }
    }
}
