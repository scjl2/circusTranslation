package hijac.tools.scjmsafe.translation;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;


public class SCJmSafeTranslator {

    private Trees trees;
    private Set<CompilationUnitTree> units;
    private Set<TypeElement> type_elements;

    MSafeProgram program = new MSafeProgram();

    public static ArrayList<String> translatedClasses = new ArrayList<String>(0);
    public static ArrayList<String> ignoredClasses = new ArrayList<String>(0);

    public SCJmSafeTranslator(SCJAnalysis analysis) {
        trees = analysis.TREES;
        units = analysis.getCompilationUnits();
        type_elements = analysis.getTypeElements();

        System.out.println("--------------------------------------------");
        System.out.println("    TransMSafe - (c) Chris Marriott 2013    ");
        System.out.println("--------------------------------------------");
        System.out.println();

        // -----------------------------------------------------------------
        // First iteration determines which classes to translate:
        System.out.println("-------------------------------------------------------------------");
        System.out.println("---------------  Analysing classes to translate  ------------------");
        System.out.println("-------------------------------------------------------------------");

        ArrayList<ClassTree> analyseList = new ArrayList<ClassTree>(0);

        // For all elements in the SCJ program tree:
        for(TypeElement elem : type_elements) {
            String elemID = elem.toString();
            ClassTree tree = trees.getTree(elem);
            if(tree instanceof ClassTree) {
                analyseList.add((ClassTree) tree);
                String className = tree.getSimpleName().toString();
                translatedClasses.add(className);
                System.out.print(" - " + className);
                if (elemID.contains("javacp") || elemID.contains("javax")) {
                    ignoredClasses.add(className);
                    System.out.print(" - Infrastructure");
                }
//                } else if (tree.getModifiers().getFlags().contains(Modifier.ABSTRACT)) {
//                    ignoredClasses.add(className);
//                    System.out.print(" - Abstract");
//                }
                System.out.print("\n");
//                @SuppressWarnings("unchecked")
//                List<Tree> classImplements = (List<Tree>) tree.getImplementsClause();
//                if(classImplements.size() > 0) {
//                    ArrayList<String> implementsList = new ArrayList<String>(0);
//                    for(Tree var : classImplements) {
//                        ignoredClasses.add(var.toString());
//                    }
//                }
            }
        }

        ArrayList<String> parents = new ArrayList<String>(0);
        ArrayList<String> embedded = new ArrayList<String>(0);

        // Search for embedded classes:
        ArrayList<ClassTree> embeddedClasses = new ArrayList<ClassTree>(0);
        for(ClassTree tree : analyseList) {
            tree.accept(new EmbeddedClassVisitor(embeddedClasses, parents, embedded), null);
        }
        analyseList.addAll(embeddedClasses);
        System.out.println();

        System.out.println();
        System.out.println("Complete!");
        System.out.println();

        // -----------------------------------------------------------------
        // Second iteration analyses all class methods and establishes return types:
        System.out.println("-------------------------------------------------------------------");
        System.out.println("------------------  Analysing class methods  ----------------------");
        System.out.println("-------------------------------------------------------------------");

        
        // Generate method signatures for all methods in all classes:
        for(ClassTree tree : analyseList) {
            String className = tree.getSimpleName().toString();
            if (!ignoredClasses.contains(className)) {
                System.out.println("Visiting " + className);
                tree.accept(new MethodSigVisitor(), null);
            }
        }
        System.out.println();
        System.out.println("Complete!");
        System.out.println();

        // Analyse descendants:
        for(MethodSig ms : Util.methodDatabase) {
            ArrayList<String> descendants = new ArrayList<String>(0);
            LinkedList<ClassTree> queue = new LinkedList<ClassTree>();
            String className;
            for (ClassTree tree : analyseList) {
                className = tree.getSimpleName().toString();
                if (className.equals(ms.getClassName())) {
                    queue.add(tree);
                }
            }
            while (!queue.isEmpty()) {
                ClassTree element = queue.remove();
                String classString = element.getSimpleName().toString();
                for (ClassTree tree : analyseList) {
                    className = tree.getSimpleName().toString();

                    Tree classExtends = tree.getExtendsClause();
                    if(classExtends != null) {
                        if (classExtends.toString().equals(classString)) {
                            queue.add(tree);
                            descendants.add(className);
                        }
                    }

                    @SuppressWarnings("unchecked")
                    List<Tree> classImplements = (List<Tree>) tree.getImplementsClause();
                    if(classImplements.size() > 0) {
                        for (Tree t : classImplements) {
                            if (t.toString().equals(classString)) {
                                queue.add(tree);
                                descendants.add(className);
                            }
                        }
                    }
                }
            }
            ms.addDescendants(descendants); 
        }

//        for(MethodSig ms : Util.methodDatabase) {
//            if (ms.getDescendants().size() > 0)
//                System.out.println("Method : " + ms.getName() + " in class " + ms.getClassName() + ", which has descendants:");
//                for (String s : ms.getDescendants()) {
//                    System.out.println(" - " + s);
//                }
//        }


        // -----------------------------------------------------------------
        // Third iteration identifies the handlers within the program:
        System.out.println("-------------------------------------------------------------------");
        System.out.println("--------------------  Identifying Handlers  -----------------------");
        System.out.println("-------------------------------------------------------------------");

        for(ClassTree tree : analyseList) {
            String className = tree.getSimpleName().toString();
            if (!ignoredClasses.contains(className)) {
                Tree classExtends = tree.getExtendsClause();
                if(classExtends != null) { // The class extends another class

                    switch(classExtends.toString()) {
                        case "AperiodicLongEventHandler": {
                            System.out.println("Found Aperiodic handler: " + className);
                            Util.addHandler(className);
                            break;
                        }
                        case "AperiodicEventHandler": {
                            System.out.println("Found Aperiodic handler: " + className);
                            Util.addHandler(className);
                            break;
                        }
                        case "PeriodicEventHandler": {
                            System.out.println("Found Periodic handler: " + className);
                            Util.addHandler(className);
                            break;
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.println("Complete!");
        System.out.println();


        // -----------------------------------------------------------------
        // Fourth iteration translates the program:
        System.out.println("-------------------------------------------------------------------");
        System.out.println("--------------------  Translating classes  ------------------------");
        System.out.println("-------------------------------------------------------------------");

        Util.populateIgnoredMethods();
        for(ClassTree tree : analyseList) {
            String className = tree.getSimpleName().toString();
            if (ignoredClasses.contains(className)) {}
            else {

                String classEmbeddedParent = "";
                for (int i = 0; i < embedded.size(); i++) {
                    if (embedded.get(i).equals(className)) {
                        classEmbeddedParent = parents.get(i);
                    }
                }

                Util.resetVariables();

                Tree classExtends = tree.getExtendsClause();

                @SuppressWarnings("unchecked")  // Casting tree.getImplementsClause, which is of type "? extends List<Tree>", to List<Tree> presents a warning at compile time
                List<Tree> classImplements = (List<Tree>) tree.getImplementsClause();

                if(classExtends != null) { // The class extends another class

                    // Extends and implements...
                    ArrayList<String> implementsList = new ArrayList<String>(0);
                    for(Tree var : classImplements) {
                        implementsList.add(var.toString());
                    }
                    if (implementsList.contains("Safelet")) {
                        System.out.println("Found Safelet: " + className);
                        System.out.println();
                        MSafeSafelet safelet = new MSafeSafelet(className);
                        tree.accept(new SafeletVisitor(safelet), null);
                        program.addSafelet(safelet);
                    }

                    // Only extends...
                    switch(classExtends.toString()) {
                        case "MissionSequencer":
                        case "SingleMissionSequencer": {
                            System.out.println("Found Mission Sequencer: " + className);
                            System.out.println();
                            MSafeMissionSeq missionSeq = new MSafeMissionSeq(className);                
                            tree.accept(new MissionSeqVisitor(missionSeq), null);
                            program.addMissionSeq(missionSeq);
                            break;
                        }                            
                        case "Mission": {
                            System.out.println("Found Mission: " + className);
                            System.out.println();
                            MSafeMission mission = new MSafeMission(className);
                            tree.accept(new MissionVisitor(mission), null);
                            program.addMission(mission);
                            break;
                        }
                        case "AperiodicLongEventHandler": {
                            System.out.println("Found Aperiodic handler: " + className);
                            System.out.println();
                            MSafeHandler handler = new MSafeHandler(className);
                            tree.accept(new HandlerVisitor(handler), null);
                            program.addHandler(handler);
                            break;
                        }
                        case "AperiodicEventHandler": {
                            System.out.println("Found Aperiodic handler: " + className);
                            System.out.println();
                            MSafeHandler handler = new MSafeHandler(className);
                            tree.accept(new HandlerVisitor(handler), null);
                            program.addHandler(handler);
                            break;
                        }
                        case "PeriodicEventHandler": {
                            System.out.println("Found Periodic handler: " + className);
                            System.out.println();
                            MSafeHandler handler = new MSafeHandler(className);
                            tree.accept(new HandlerVisitor(handler), null);
                            program.addHandler(handler);
                            break;
                        }
                        default: {
                            System.out.println("Found Class: " + className + " extends " + classExtends.toString());
                            System.out.println();
                            MSafeClass newClass = new MSafeClass(className, classExtends.toString(), classEmbeddedParent);                
                            tree.accept(new ClassVisitor(newClass), null);
                            program.addClass(newClass);
                        }
                    }
                } else if(classImplements.size() > 0) { // The class implements another class
                    
                    ArrayList<String> implementsList = new ArrayList<String>(0);
                    for(Tree var : classImplements) {
                        implementsList.add(var.toString());
                    }
                    if (implementsList.contains("Safelet")) {
                        System.out.println("Found Safelet: " + className);
                        System.out.println();
                        MSafeSafelet safelet = new MSafeSafelet(className);
                        tree.accept(new SafeletVisitor(safelet), null);
                        program.addSafelet(safelet);
                    } else if (implementsList.contains("Runnable")) {
                        System.out.println("Found Class: " + className + " implements Runnable");
                        System.out.println();
                        MSafeClass newClass = new MSafeClass(className, classEmbeddedParent);                
                        tree.accept(new ClassVisitor(newClass), null);
                        program.addClass(newClass);
                    } else {
                        System.out.print("Found Class: " + className + " implements ");
                        for(Tree var : classImplements) {
                            System.out.print(var.toString() + ", ");
                        }
                        MSafeClass newClass = new MSafeClass(className, classEmbeddedParent);                
                        tree.accept(new ClassVisitor(newClass), null);
                        program.addClass(newClass);
                    }

                } else {
                    System.out.println("Found Class: " + className);
                    System.out.println();
                    MSafeClass newClass = new MSafeClass(className, classEmbeddedParent);
                    tree.accept(new ClassVisitor(newClass), null);
                    program.addClass(newClass);
                }
            System.out.println("-----------------------------------------------------------------");
            System.out.println();

            }
            
        }

        // TODO - Need to traverse the analysed classes and ensure that fields from parents are included in each child.

        System.out.println();
        System.out.println("Complete!");
        System.out.println();

//        if (program.getMissionSeq() == null) {
//            MSafeMissionSeq missionSeq = new MSafeMissionSeq("Mission Sequencer");                
//            program.addMissionSeq(missionSeq);
//        }

        for (MSafeClass msafeClass : program.getClasses()) {
            boolean match = false;
            String parentName = "";
            for (int i = 0; i < embedded.size(); i++) {
                if (embedded.get(i).equals(msafeClass.getName())) {
                    match = true;
                    parentName = parents.get(i);
                }
            }
            ArrayList<Declaration> fields = msafeClass.getFields();
            for (Method m : msafeClass.getMethods()) {
//                for (Declaration d : fields) {
//                    m.addVisibleField(d.getVar());
//                }
                if (match) {
                    System.out.println("Adding visible fields to method " + m.getName() + " in class " + msafeClass.getName());
                    MSafeSuperClass parentClass = program.getClass(parentName);
                    for (Declaration d : parentClass.getFields()) {
                        System.out.println(" - " + d.getVar().getName());
                        m.addVisibleField(d.getVar());
                    }
                }
            }
        }


        ArrayList<String> ignoreArray = sortStringSet(ignoredClasses);
        System.out.println("-------------------------------------------------------------------");
        System.out.println("---------------  Infrastructure / library classes  ----------------");
        System.out.println("-------------------------------------------------------------------");
        for (String s : ignoreArray) {
            System.out.println(" - " + s);
        }

        if (verifyStructure()) {
            PrintUtil.printStructure(program);
            PrintUtil.printTextOutput(program);
        }

        System.out.println();
        System.out.println("Finished.");
        System.out.println();

    }

    public MSafeProgram getTranslatedProgram() {
        return program;
    }


    private ArrayList<String> sortStringSet(ArrayList<String> ignoredClasses) {
        String[] temp = ignoredClasses.toArray(new String[ignoredClasses.size()]);
        ArrayList<String> ignoreArray = new ArrayList<String>(0);
        for (String s : temp) {
            ignoreArray.add(s);
        }
        ArrayList<String> result = new ArrayList<String>(0);
        while (result.size() < ignoredClasses.size()) {
            String low = ignoreArray.get(0);
            for (int i = 1; i < ignoreArray.size(); i++) {
                String s = ignoreArray.get(i);
                if (low.compareTo(s) > 0) {
                    low = s;
                }
            }
            result.add(low);
            ignoreArray.remove(low);
        }
        return result;
    }


    private boolean verifyStructure() {
        boolean result = true;
        System.out.println("-------------------------------------------------------------------");
        System.out.println("-----------------  Verifying SCJmSafe structure  ------------------");
        System.out.println("-------------------------------------------------------------------");

        if(program.getSafelet() == null) {
            System.out.println("  - [ERROR] - No safelet created");
            result = false;
        }
        if(program.getMissionSeq() == null) {
            System.out.println("  - [ERROR] - No mission sequencer created");
            result = false;
        }
        boolean missions = program.getMissions().isEmpty();
        boolean handlers = program.getHandlers().isEmpty();
        if(missions && handlers) {
            System.out.println("  - [WARNING] - No missions or handlers created");
        } else if (missions && !handlers) {
            System.out.println("  - [ERROR] - Handlers created, but no missions created");
            result = false;
        } else if (!missions && handlers) {
            System.out.println("  - [WARNING] - Missions created, but no handlers created");
        }
        System.out.println();
        System.out.println("Complete!");
        System.out.println();
        return result;
    }




    private void addCustomIgnores(ArrayList<String> ignoredClasses) {
        ignoredClasses.add("Comparator");
        ignoredClasses.add("HashTable");
    }


    String[] frameworkClasses = {
        "AbstractAsyncEventHandler",
        "AsyncEventHandler",
        "AsyncLongEventHandler",
        "BoundAsyncEventHandler",
        "BoundAsyncLongEventHandler",
        "HighResolutionTime",
        "PeriodicParameters",
        "PriorityParameters",
        "PriorityScheduler",
        "RelativeTime",
        "ReleaseParameters",
        "Schedulable",
        "SchedulingParameters",
        "ActiveClass",
        "ActiveMethod",
        "BoundEvent",
        "BoundEvents",
        "DeviceAccess",
        "FrameworkMethod",
        "HandlerId",
        "Ignore",
        "InteractionClass",
        "InteractionCode",
        "Level",
        "MissionId",
        "PassiveClass",
        "Restrict",
        "SCJAllowed",
        "SCJRestricted",
        "AbstractAsyncEvent",
        "AperiodicEvent",
        "AperiodicEventHandler",
        "AperiodicLongEvent",
        "AperiodicLongEventHandler",
        "AperiodicParameters",
        "AsyncEvent",
        "AsyncLongEvent",
        "InterruptServiceRoutine",
        "ManagedEventHandler",
        "ManagedLongEventHandler",
        "ManagedSchedulable",
        "Mission",
        "MissionSequencer",
        "PeriodicEventHandler",
        "PriorityScheduler",
        "RawIntegralAccess",
        "RawIntegralAccessFactory",
        "RawMemory",
        "RawMemoryName",
        "Safelet",
        "StorageParameters"
    };


}



