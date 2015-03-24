package hijac.tools.scjmsafe.language;

import java.util.ArrayList;
import java.util.HashSet;

import hijac.tools.scjmsafe.language.Commands.*;

public class MSafeProgram {

    public static ArrayList<Declaration> staticDecs;
    public static ArrayList<Command> staticInits;
    private MSafeSafelet safelet;
    private MSafeMissionSeq mSeq;
    private HashSet<MSafeMission> missions;
    private HashSet<MSafeHandler> handlers;
    private HashSet<MSafeClass> classes;

    public MSafeProgram() {
        staticDecs = new ArrayList<Declaration>(0);
        staticInits = new ArrayList<Command>(0);
        missions = new HashSet<MSafeMission>(0);
        handlers = new HashSet<MSafeHandler>(0);
        classes = new HashSet<MSafeClass>(0);
    }

    public static void addStaticDec(Declaration dec) {
        staticDecs.add(dec);
    }

    public ArrayList<Declaration> getStaticDecs() {
        return staticDecs;
    }

    public static void addStaticInit(Command com) {
        staticInits.add(com);
    }

    public ArrayList<Command> getStaticInits() {
        return staticInits;
    }

    public void addSafelet(MSafeSafelet safelet) {
        this.safelet = safelet;
    }

    public MSafeSafelet getSafelet() {
        return safelet;
    }

    public void addMissionSeq(MSafeMissionSeq mSeq) {
        this.mSeq = mSeq;
    }

    public MSafeMissionSeq getMissionSeq() {
        return mSeq;
    }

    public void addMission(MSafeMission mission) {
        missions.add(mission);
    }

    public HashSet<MSafeMission> getMissions() {
        return missions;
    }

    public void addHandler(MSafeHandler handler) {
        handlers.add(handler);
    }

    public HashSet<MSafeHandler> getHandlers() {
        return handlers;
    }

    public void addClass(MSafeClass c) {
        classes.add(c);
    }

    public HashSet<MSafeClass> getClasses() {
        return classes;
    }

    public MSafeSuperClass getClass(String name) {
        MSafeSuperClass result = null;
        if (name.equals(safelet.getName())) {
            result = safelet;
        }
        if (name.equals(mSeq.getName())) {
            result = mSeq;
        }
        for (MSafeMission c : missions) {
            if (c.getName().equals(name)) {
                result = c;
            }
        }
        for (MSafeHandler c : handlers) {
            if (c.getName().equals(name)) {
                result = c;
            }
        }
        for (MSafeClass c : classes) {
            if (c.getName().equals(name)) {
                result = c;
            }
        }
        return result;
    }

}
