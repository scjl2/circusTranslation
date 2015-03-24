package hijac.tools.scjmsafe.translation;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import java.util.HashSet;
import java.util.ArrayList;

public class PrintUtil {

    public static void printStructure(MSafeProgram program) {
        System.out.println("-------------------------------------------------------------------");
        System.out.println("--------------------  Translated to SCJmSafe  ---------------------");
        System.out.println("-------------------------------------------------------------------");

        System.out.println("  - Safelet: " + program.getSafelet().getName());
        System.out.println("  - Mission Sequencer: " + program.getMissionSeq().getName());
        for(MSafeMission mission : program.getMissions()) {
            System.out.println("  - Mission: " + mission.getName());
        }
        for(MSafeHandler handler : program.getHandlers()) {
            System.out.println("  - Handler: " + handler.getName());
        }
        for(MSafeClass newClass : program.getClasses()) {
            System.out.println("  - Class: " + newClass.getName());
        }
        System.out.println();
        System.out.println("Complete!");
        System.out.println();

    }



    public static void printTextOutput(MSafeProgram program) {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("SCJmSafe translation:");
        System.out.println();
        printProgram(program);
        System.out.println("Complete!");
        System.out.println();
    }


    // Program
    public static void printProgram(MSafeProgram program) {
        System.out.println("program {");
        System.out.println("  static {");
        for (Declaration d : program.getStaticDecs()) {
            d.printCommand(4);
            System.out.println();
        }
        System.out.println("  }");
        System.out.println("  sInit {");
        for (Command c : program.getStaticInits()) {
            c.printCommand(4);
            System.out.println();
        }
        System.out.println("  }");
        System.out.println();
        printSafelet(program.getSafelet());
        System.out.println();
        printMissionSeq(program.getMissionSeq());
        System.out.println();
        printMissions(program.getMissions());
        printHandlers(program.getHandlers());
        printClasses(program.getClasses());
        System.out.println("}");
    }


    // Safelet
    public static void printSafelet(MSafeSafelet safelet) {
        System.out.println("  safelet {");
        printFields(safelet.getFields());
        printInits(safelet.getInit());
        printConstrs(safelet.getConstrs());
        printCommandComponent(safelet.getInitApp(), "initializeApplication");
        printCommandComponent(safelet.getGetSequencer(), "getSequencer");
        printMethods(safelet.getMethods());
        System.out.println("  }");
    }


    // Mission Sequencer
    public static void printMissionSeq(MSafeMissionSeq mSeq) {
        System.out.println("  missionSeq {");
        printFields(mSeq.getFields());
        printInits(mSeq.getInit());
        printConstrs(mSeq.getConstrs());
        printCommandComponent(mSeq.getGetNextMission(), "getNextMission");
        printMethods(mSeq.getMethods());
        System.out.println("  }");
    }
    


    // Missions
    public static void printMissions(HashSet<MSafeMission> missions) {
        for (MSafeMission mission : missions) {
            printMission(mission);
            System.out.println();
        }
    }

    public static void printMission(MSafeMission mission) {
        System.out.println("  mission " + mission.getName() + " {");
        printFields(mission.getFields());
        printInits(mission.getInit());
        System.out.println("    handlers {");
        for (String s : mission.getHandlers()) {
            System.out.println("      \"" + s + "\"");
        }
        System.out.println("    }");
        printConstrs(mission.getConstrs());
        printCommandComponent(mission.getInitialize(), "initialize");
        printCommandComponent(mission.getCleanUp(), "cleanUp");
        printMethods(mission.getMethods());
        System.out.println("  }");
    }



    // Handlers
    public static void printHandlers(HashSet<MSafeHandler> handlers) {
        for (MSafeHandler handler : handlers) {
            printHandler(handler);
            System.out.println();
        }
    }

    public static void printHandler(MSafeHandler handler) {
        System.out.println("  handler " + handler.getName() + " {");
        printFields(handler.getFields());
        printInits(handler.getInit());
        printConstrs(handler.getConstrs());
        printCommandComponent(handler.getHAE(), "handleEvent");
        printMethods(handler.getMethods());
        System.out.println("  }");
    }


    // Classes
    public static void printClasses(HashSet<MSafeClass> classes) {
        for (MSafeClass newClass : classes) {
            printClass(newClass);
            System.out.println();
        }
    }

    public static void printClass(MSafeClass newClass) {
        System.out.println("  class " + newClass.getName() + " {");
        printFields(newClass.getFields());
        printInits(newClass.getInit());
        printConstrs(newClass.getConstrs());
        printMethods(newClass.getMethods());
        System.out.println("  }");
    }


    // Components
    public static void printFields(ArrayList<Declaration> fields) {
        System.out.println("    fields {");
        for (Declaration dec : fields) {
            dec.printCommand(6);
            System.out.println();
        }
        System.out.println("    }");
    }

    public static void printInits(ArrayList<Command> init) {
        System.out.println("    init {");
        for (Command c : init) {
            c.printCommand(6);
            System.out.println();
        }
        System.out.println("    }");
    }

    public static void printCommandComponent(Command command, String component) {
        System.out.println("    " + component + " {");
        printCommands(command, 6);
        System.out.println("    }");
    }


    // Methods
    public static void printMethods(ArrayList<Method> methods) {
        for (Method method : methods) {
            printMethod(method);
            System.out.println();
        }
    }


    public static void printMethod(Method method) {
        System.out.print("    method " + method.getName() + "(");
        for (int i = 0; i < method.getParams().size(); i++) {
            method.getParams().get(i).printExpression();
            if (i+1 < method.getParams().size()) {
                System.out.print(", ");
            }
        }
        System.out.print(") {\n");
        printCommands(method.getBody(), 6);
        System.out.println("    }");
    }


    public static void printConstrs(ArrayList<Method> constrs) {
        for (Method constr : constrs) {
            printConstr(constr);
            System.out.println();
        }
    }

    public static void printConstr(Method constr) {
        System.out.print("    constr (");
        for (int i = 0; i < constr.getParams().size(); i++) {
            constr.getParams().get(i).printExpression();
            if (i+1 < constr.getParams().size()) {
                System.out.print(", ");
            }
        }
        System.out.print(") {\n");
        printCommands(constr.getBody(), 6);
        System.out.println("    }");
    }


    // Commands
    public static void printCommands(Command command, int indent) {
        if (command != null) {
            command.printCommand(indent);
            System.out.print("\n");
        }
    }



    public static void printIndent(int indent) {
        while (indent > 0) {
            System.out.print(" ");
            indent--;
        }
    }
}
