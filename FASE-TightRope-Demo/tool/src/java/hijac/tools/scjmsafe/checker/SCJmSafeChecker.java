package hijac.tools.scjmsafe.checker;

import hijac.tools.scjmsafe.checker.analysis.*;

import hijac.tools.scjmsafe.checker.environment.*;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;

import hijac.tools.collections.RelationImpl;


public class SCJmSafeChecker {

    public static MSafeProgram SCJmSafePROGRAM;
    public static ArrayList<String> STATICVARS;
    public static String SAFELETNAME;
    public static String MSEQNAME;
    public static ArrayList<String> MISSIONNAMES = new ArrayList<String>(0);
    public static ArrayList<String> HANDLERNAMES = new ArrayList<String>(0);

    public static Expr MSEQEXPR;
    public static ArrayList<Expr> MISSIONEXPRS = new ArrayList<Expr>(0);
    public static ArrayList<Expr> HANDLEREXPRS = new ArrayList<Expr>(0);

    public enum ExecutionPoint {
        STATICFIELDS,
        SAFELETFIELDS,
        SAFELETINITIALIZEAPPLICATION,
        SAFELETGETSEQUENCER,
        SEQUENCERGETMISSION,
        MISSIONINIT,
        MISSIONCLEANUP,
        HANDLERHANDLEEVENT,
    }

    public static ExecutionPoint EXECUTIONPOINT;
    public static ArrayList<MSafeSuperClass> classesToAnalyse = new ArrayList<MSafeSuperClass>(0);
    public static ArrayList<Expr> exprsOfClasses = new ArrayList<Expr>(0);

    public SCJmSafeChecker(MSafeProgram program, RelationImpl methodDeps) {
        SCJmSafePROGRAM = program;
        System.out.println("--------------------------------------------");
        System.out.println("    CheckMSafe - (c) Chris Marriott 2013    ");
        System.out.println("--------------------------------------------");
        System.out.println();

        buildStaticVars();
        populateParadigmNames();
        System.out.println("-------------------------------------------------------------------");
        System.out.println("------------------  Building method properties  -------------------");
        System.out.println("-------------------------------------------------------------------");
        System.out.println();
        buildMethodProperties(methodDeps);
        System.out.println();
        System.out.println("Complete!");

        System.out.println("-------------------------------------------------------------------");
        System.out.println("-----------------------  Checking program  ------------------------");
        System.out.println("-------------------------------------------------------------------");
        System.out.println();
        checkProgram();
        System.out.println();
        System.out.println("Complete!");
    }

    private static void buildStaticVars() {
        STATICVARS = new ArrayList<String>(0);
        for (Declaration d : SCJmSafePROGRAM.getStaticDecs()) {
            STATICVARS.add(d.getVar().getName());
//            System.out.println("Adding static var : " + d.getVar().getExpressionString());
        }
    }

    private static void populateParadigmNames() {
        SAFELETNAME = SCJmSafePROGRAM.getSafelet().getName();
        MSEQNAME = SCJmSafePROGRAM.getMissionSeq().getName();
        for (MSafeMission mission : SCJmSafePROGRAM.getMissions()) {
            MISSIONNAMES.add(mission.getName());
        }
        for (MSafeHandler handler : SCJmSafePROGRAM.getHandlers()) {
            HANDLERNAMES.add(handler.getName());
        }
    }

    private static void buildMethodProperties(RelationImpl methodDeps) {
        MethodPropertiesBuilder methodPropertiesBuilder = new MethodPropertiesBuilder(SCJmSafePROGRAM, methodDeps);
    }

    private static void checkProgram() {
        Environment env = new Environment();
//        setupEnvironment(env);

        RefCon currentRC;

        // Static
        currentRC = new IMem();
        EXECUTIONPOINT = ExecutionPoint.STATICFIELDS;
        EnvUtil.addDecsToEnv(env, SCJmSafePROGRAM.getStaticDecs(), null, currentRC);
        EnvUtil.analyseCommands(env, SCJmSafePROGRAM.getStaticInits(), null, currentRC);

        // Safelet
        MSafeSafelet safelet = SCJmSafePROGRAM.getSafelet();
        checkSafelet(env, safelet, currentRC);
    }


    private static void checkSafelet(Environment env, MSafeSafelet safelet, RefCon rc) {
        System.out.println("Analysing safelet");
        // Fields & Init
        EXECUTIONPOINT = ExecutionPoint.SAFELETFIELDS;
        EnvUtil.addDecsToEnv(env, safelet.getFields(), null, rc);
        EnvUtil.analyseCommands(env, safelet.getInit(), null, rc);

        // SetUp
        EXECUTIONPOINT = ExecutionPoint.SAFELETINITIALIZEAPPLICATION;
        EnvUtil.analyseCommand(env, safelet.getInitApp(), null, rc);

        // Mission Sequencer
        EXECUTIONPOINT = ExecutionPoint.SAFELETGETSEQUENCER;
        EnvUtil.analyseCommand(env, safelet.getGetSequencer(), null, rc);

        checkMissionSeq(env, rc);

        System.out.println("\nResulting environment after the safelet " + safelet.getName() + " has executed:");
        EnvUtil.printEnv(env);
    }

    private static void checkMissionSeq(Environment env, RefCon rc) {
        ArrayList<MSafeSuperClass> classClone = new ArrayList<MSafeSuperClass>(0);
        for (MSafeSuperClass c : classesToAnalyse) {
            classClone.add(c);
        }
        ArrayList<Expr> exprClone = new ArrayList<Expr>(0);
        for (Expr e : exprsOfClasses) {
            exprClone.add(e);
        }
        if (classClone.size() == 1 && classClone.get(0) instanceof MSafeMissionSeq) {
            System.out.println("Analysing mission sequencer");

            classesToAnalyse.clear();
            exprsOfClasses.clear();

            MSafeMissionSeq mSeq = (MSafeMissionSeq) classClone.get(0);
            Expr expr = exprClone.get(0);

            // GetNextMission
            rc = new MMem();
            EXECUTIONPOINT = ExecutionPoint.SEQUENCERGETMISSION;
            EnvUtil.analyseCommand(env, mSeq.getGetNextMission(), expr, rc);

            checkMissions(env, rc);

            System.out.println("\nResulting environment after the mission sequencer " + mSeq.getName() + " has executed:");
            EnvUtil.printEnv(env);

            ArrayList<Expr> outOfScopeDecs = new ArrayList<Expr>(0);
            EnvUtil.calcOutOfScopeDecs(outOfScopeDecs, mSeq.getGetNextMission(), expr);
            EnvUtil.removeOutOfScopeDecs(outOfScopeDecs, expr, env);
            EnvUtil.removeResultExprs(env);

        } else {
            System.out.println("[ERROR] - Unable to find instantiation of mission sequencer");
            System.exit(-1);
        }
    }


    private static void checkMissions(Environment env, RefCon rc) {
        ArrayList<MSafeSuperClass> classClone = new ArrayList<MSafeSuperClass>(0);
        ArrayList<Environment> envClones = new ArrayList<Environment>(0);
        for (MSafeSuperClass c : classesToAnalyse) {
            classClone.add(c);
            envClones.add(env.clone());
        }
        ArrayList<Expr> exprClone = new ArrayList<Expr>(0);
        for (Expr e : exprsOfClasses) {
            exprClone.add(e);
        }
        for (int i = 0; i < classClone.size(); i++) {
            System.out.println("Analysing mission");
            classesToAnalyse.clear();
            exprsOfClasses.clear();
            if (classClone.get(i) instanceof MSafeMission) {

                MSafeMission mission = (MSafeMission) classClone.get(i);
                Expr expr = exprClone.get(i);

                // Initialize
                EXECUTIONPOINT = ExecutionPoint.MISSIONINIT;
                EnvUtil.analyseCommand(envClones.get(i), mission.getInitialize(), expr, rc);

                checkHandlers(envClones.get(i), rc);

                System.out.println("\nResulting environment after the mission " + mission.getName() + " has executed the handlers:");
                EnvUtil.printEnv(envClones.get(i));

                ArrayList<Expr> outOfScopeDecs = new ArrayList<Expr>(0);
                EnvUtil.calcOutOfScopeDecs(outOfScopeDecs, mission.getInitialize(), expr);
                EnvUtil.removeOutOfScopeDecs(outOfScopeDecs, expr, envClones.get(i));

                // Cleanup
                EXECUTIONPOINT = ExecutionPoint.MISSIONCLEANUP;
                EnvUtil.analyseCommand(envClones.get(i), mission.getCleanUp(), expr, rc);

                System.out.println("\nResulting environment after the mission " + mission.getName() + " has executed:");
                EnvUtil.printEnv(envClones.get(i));

                outOfScopeDecs.clear();
                EnvUtil.calcOutOfScopeDecs(outOfScopeDecs, mission.getCleanUp(), expr);
                EnvUtil.removeOutOfScopeDecs(outOfScopeDecs, expr, envClones.get(i));
                EnvUtil.removeResultExprs(env);

            } else {
                System.out.println("[ERROR] - Class found is not a mission");
                System.exit(-1);
            }
        }
    }

    public static ArrayList<Expr> HANDLERFIELDS  = new ArrayList<Expr>(0);

    private static void checkHandlers(Environment env, RefCon rc) {
        for (int i = 0; i < classesToAnalyse.size(); i++) {
            System.out.println("Analysing handler");
            if (classesToAnalyse.get(i) instanceof MSafeHandler) {

                MSafeHandler handler = (MSafeHandler) classesToAnalyse.get(i);
                Expr expr = exprsOfClasses.get(i);

                rc = new PRMem(handler);

                // HandleEvent
                EXECUTIONPOINT = ExecutionPoint.HANDLERHANDLEEVENT;
                EnvUtil.analyseCommand(env, handler.getHAE(), expr, rc);

                System.out.println("\nResulting environment after the handler " + handler.getName() + " has executed:");
                EnvUtil.printEnv(env);

            } else {
                System.out.println("[ERROR] - Class found is not a handler");
                System.exit(-1);
            }
        }
        for (int i = 0; i < classesToAnalyse.size(); i++) {
            if (classesToAnalyse.get(i) instanceof MSafeHandler) {
                MSafeHandler handler = (MSafeHandler) classesToAnalyse.get(i);
                Expr expr = exprsOfClasses.get(i);

                ArrayList<Expr> outOfScopeDecs = new ArrayList<Expr>(0);
                EnvUtil.calcOutOfScopeDecs(outOfScopeDecs, handler.getHAE(), expr);
                EnvUtil.removeOutOfScopeDecs(outOfScopeDecs, expr, env);
                EnvUtil.removeResultExprs(env);
            }
        }
        HANDLERFIELDS.clear();
        classesToAnalyse.clear();
        exprsOfClasses.clear();
    }


}



