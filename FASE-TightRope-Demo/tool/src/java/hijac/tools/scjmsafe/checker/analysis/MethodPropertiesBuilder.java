package hijac.tools.scjmsafe.checker.analysis;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;
import hijac.tools.scjmsafe.checker.environment.Share;
import hijac.tools.scjmsafe.checker.environment.ShareRelation;
import hijac.tools.scjmsafe.checker.SCJmSafeChecker;

import hijac.tools.scjmsafe.checker.environment.EnvUtil;
import hijac.tools.scjmsafe.checker.CheckingUtil;

import hijac.tools.collections.RelationImpl;
import hijac.tools.collections.Maplet;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MethodPropertiesBuilder {

    static ArrayList<MethodDepsContainer> analysedMethods = new ArrayList<MethodDepsContainer>(0);

    public MethodPropertiesBuilder(MSafeProgram program, RelationImpl methodDeps) {

        // Analyse the relation methodDeps, which details which methods are dependant on others
        ArrayList<MethodDep> deps = new ArrayList<MethodDep>(0);
        Iterator iter = methodDeps.iterator();
        while (iter.hasNext()) {
            Maplet maplet = (Maplet) iter.next();
            ExecutableElement first = (ExecutableElement) maplet.fst;
            Element firstParent = first.getEnclosingElement();
            String firstParentName = firstParent.getSimpleName().toString();
            String firstName = maplet.fst.toString();
            MethodDepEntry firstEntry = parseString(firstName, firstParentName);
            ExecutableElement second = (ExecutableElement) maplet.snd;
            Element secondParent = second.getEnclosingElement();
            String secondParentName = secondParent.getSimpleName().toString();
            String secondName = maplet.snd.toString();
            MethodDepEntry secondEntry = parseString(secondName, secondParentName);
            MethodDep dep = new MethodDep(firstEntry, secondEntry);
            if (!secondEntry.getName().equals("Object")) {
                deps.add(dep);
//                dep.print();
            }
        }

        // Adds the dependancies for instantiation of local fields to the constructors of classes
        analyseFieldDeps(program, program.getSafelet(), deps);
        analyseFieldDeps(program, program.getMissionSeq(), deps);
        for (MSafeMission mission : program.getMissions()) {
            analyseFieldDeps(program, mission, deps);
        }
        for (MSafeHandler handler : program.getHandlers()) {
            analyseFieldDeps(program, handler, deps);
        }
        for (MSafeClass c : program.getClasses()) {
            analyseFieldDeps(program, c, deps);
        }

        // Adds the dependencies for run methods from enterpriv and executein etc.
        analyseEnterPrivDeps(program.getSafelet(), deps);
        analyseEnterPrivDeps(program.getMissionSeq(), deps);
        for (MSafeMission mission : program.getMissions()) {
            analyseEnterPrivDeps(mission, deps);
        }
        for (MSafeHandler handler : program.getHandlers()) {
            analyseEnterPrivDeps(handler, deps);
        }
        for (MSafeClass c : program.getClasses()) {
            analyseEnterPrivDeps(c, deps);
        }


        // Create an array of methods to analyse using the MethodDepsContainer class
        System.out.println();
        ArrayList<MethodDepsContainer> methodsToAnalyse = new ArrayList<MethodDepsContainer>(0);
        analyseMethodDeps(program.getSafelet(), methodsToAnalyse, deps);
        analyseMethodDeps(program.getMissionSeq(), methodsToAnalyse, deps);
        for (MSafeMission mission : program.getMissions()) {
            analyseMethodDeps(mission, methodsToAnalyse, deps);
        }
        for (MSafeHandler handler : program.getHandlers()) {
            analyseMethodDeps(handler, methodsToAnalyse, deps);
        }
        for (MSafeClass c : program.getClasses()) {
            analyseMethodDeps(c, methodsToAnalyse, deps);
        }

        cleanMethodDepsToAnalyse(methodsToAnalyse); 

        // Sort the methods based on what is dependant on what, and analyse those that we can analse:
        ArrayList<MethodDepsContainer> remainingMethods = sortMethodsAndAnalyse(methodsToAnalyse);

        remainingMethods = analyseSimpleRecursiveMethods(remainingMethods);
        remainingMethods = sortMethodsAndAnalyse(remainingMethods);

    }


    // Method to parse the strings of the relations in the method dependencies
    private MethodDepEntry parseString(String s, String p) {
        int startParam = s.indexOf("(");
        String methodName = s.substring(0, startParam);
        String params = s.substring(startParam + 1, s.length() - 1);
        String[] paramArray = params.split(",");
        ArrayList<String> newParamArray = new ArrayList<String>(0);
        for (String paramString : paramArray) {
            int lastDot = paramString.lastIndexOf(".");
            String newParamString = paramString.substring(lastDot + 1, paramString.length());
            if (!newParamString.equals("")) {
                newParamArray.add(newParamString);
            }
        }
        return new MethodDepEntry(methodName, newParamArray, p);
    }

    private static void analyseFieldDeps(MSafeProgram program, MSafeSuperClass msafeClass, ArrayList<MethodDep> deps) {
        ArrayList<Command> inits = msafeClass.getInit();
        for (Command c : inits) {
            analyseFieldDepsNewInstance(c, program, msafeClass, deps);
        }
    }

    private static void analyseFieldDepsNewInstance(Command c, MSafeProgram program, MSafeSuperClass msafeClass, ArrayList<MethodDep> deps) {
        if (c instanceof NewInstance) {
            NewInstance com = (NewInstance) c;
            String className = com.getVarType().getTypeName();
            for (Method constr : msafeClass.getConstrs()) {
                ArrayList<String> lhsParams = constr.getParamTypesNoResult();
                MethodDepEntry lhs = new MethodDepEntry(constr.getName(), lhsParams, constr.getName());
                MSafeSuperClass rhsClass = program.getClass(className);
                if (rhsClass != null) {
                    for (Method constr2 : rhsClass.getConstrs()) {
                        ArrayList<String> rhsParams = constr2.getParamTypesNoResult();
                        MethodDepEntry rhs = new MethodDepEntry(className, rhsParams, className);
                        MethodDep dep = new MethodDep(lhs, rhs);
                        deps.add(dep);
                    }
                }
            }
        } else if (c instanceof Sequence) {
            Sequence seq = (Sequence) c;
            analyseFieldDepsNewInstance(seq.getC1(), program, msafeClass, deps);
            analyseFieldDepsNewInstance(seq.getC2(), program, msafeClass, deps);
        }
    }


    private static void analyseEnterPrivDeps(MSafeSuperClass msafeClass, ArrayList<MethodDep> deps) {
        for (Method m : msafeClass.getMethods()) {
            ArrayList<MethodCall> enterPrivMethodCallDeps = getEnterPrivMethodCallDeps(m.getBody());
            for (MethodCall mc : enterPrivMethodCallDeps) {
                for (MethodSig ms : mc.getPossibleMethods()) {
                    MethodDepEntry lhs = new MethodDepEntry(m.getName(), m.getParamTypesNoResult(), msafeClass.getName());
                    MethodDepEntry rhs = new MethodDepEntry(ms.getName(), ms.getParamTypes(), ms.getClassName());
                    MethodDep dep = new MethodDep(lhs, rhs);
                    deps.add(dep);
                }
            }
        }
    }

    private static ArrayList<MethodCall> getEnterPrivMethodCallDeps(Command com) {
        ArrayList<MethodCall> result = new ArrayList<MethodCall>(0);

        if (com instanceof DoWhile) {
            DoWhile doWhile = (DoWhile) com;
            result.addAll(getEnterPrivMethodCallDeps(doWhile.getC1()));

        } else if (com instanceof EnterPrivateMemory) {
            EnterPrivateMemory enterPriv = (EnterPrivateMemory) com;
            result.add(enterPriv.getMethodCall());

        } else if (com instanceof ExecuteInAreaOf) {
            ExecuteInAreaOf executeIn = (ExecuteInAreaOf) com;
            result.add(executeIn.getMethodCall());

        } else if (com instanceof ExecuteInOuterArea) {
            ExecuteInOuterArea executeIn = (ExecuteInOuterArea) com;
            result.add(executeIn.getMethodCall());

        } else if (com instanceof For) {
            For forCom = (For) com;
            result.addAll(getEnterPrivMethodCallDeps(forCom.getC1()));
            result.addAll(getEnterPrivMethodCallDeps(forCom.getC2()));
            result.addAll(getEnterPrivMethodCallDeps(forCom.getC3()));

        } else if (com instanceof If) {
            If ifCom = (If) com;
            result.addAll(getEnterPrivMethodCallDeps(ifCom.getC1()));
            result.addAll(getEnterPrivMethodCallDeps(ifCom.getC2()));

        } else if (com instanceof Scope) {
            Scope scope = (Scope) com;
            result.addAll(getEnterPrivMethodCallDeps(scope.getCommand()));

        } else if (com instanceof Sequence) {
            Sequence seq = (Sequence) com;
            result.addAll(getEnterPrivMethodCallDeps(seq.getC1()));
            result.addAll(getEnterPrivMethodCallDeps(seq.getC2()));

        } else if (com instanceof Switch) {
            Switch switchCom = (Switch) com;
            ArrayList<Command> commands = switchCom.getCommands();
            for (Command c : commands) {
                result.addAll(getEnterPrivMethodCallDeps(c));
            }

        } else if (com instanceof Try) {
            Try tryCom = (Try) com;
            result.addAll(getEnterPrivMethodCallDeps(tryCom.getTryCommand()));
            for (Command c : tryCom.getCatchExprs()) {
                result.addAll(getEnterPrivMethodCallDeps(c));
            }
            for (Command c : tryCom.getCatchComs()) {
                result.addAll(getEnterPrivMethodCallDeps(c));
            }
            result.addAll(getEnterPrivMethodCallDeps(tryCom.getFinallyCommand()));

        } else if (com instanceof While) {
            While whileCom = (While) com;
            result.addAll(getEnterPrivMethodCallDeps(whileCom.getC1()));

        }
        return result;
    }


    // Method to analyse the constrs and methods of a class, and add any dependencies based on the 'deps' array
    private static void analyseMethodDeps(MSafeSuperClass c, ArrayList<MethodDepsContainer> methodsToAnalyse, ArrayList<MethodDep> deps) {
//        System.out.println("Analysing methods of class " + c.getName());
        for (Method m : c.getMethods()) {
            MethodDepsContainer container = new MethodDepsContainer(m, c, false);
            for (MethodDep dep : deps) {
                // If the method name equals that in the lhs of the relation & the parameter size is the same & the container doesn't already include the dependency
                if (c.getName().equals(dep.getFirst().getClassName()) && m.getName().equals(dep.getFirst().getName()) && m.getParamTypesNoResult().size() == dep.getFirst().getParams().size() && !container.containsDep(dep.getSecond())) {
//                    System.out.println("  Found match for " + m.getName() + " with correct name and " + dep.getFirst().getParams().size() + " parameters");
                    boolean match = true;
                    for (int i = 0; i < m.getParamTypesNoResult().size(); i++) {
//                        System.out.println("    - Method param type = " + m.getParamTypesNoResult().get(i) + ", found dependancy method param type = " + dep.getFirst().getParams().get(i));
                        if (!m.getParamTypesNoResult().get(i).equals(dep.getFirst().getParams().get(i))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        container.addDep(dep.getSecond());
//                        System.out.println("  Match confirmed - adding to container for " + m.getName());
                    }
                }
            }
            methodsToAnalyse.add(container);
        }
        for (Method m : c.getConstrs()) {
            MethodDepsContainer container = new MethodDepsContainer(m, c, true);
            for (MethodDep dep : deps) {
                // If the method name equals that in the lhs of the relation & the parameter size is the same & the container doesn't already include the dependency
                if (c.getName().equals(dep.getFirst().getClassName()) && m.getName().equals(dep.getFirst().getName()) && m.getParamTypesNoResult().size() == dep.getFirst().getParams().size() && !container.containsDep(dep.getSecond())) {
//                    System.out.println("  Found match for " + m.getName() + " with correct name and #parameters");
                    boolean match = true;
                    for (int i = 0; i < m.getParamTypesNoResult().size(); i++) {
//                        System.out.println("    - Constr param type = " + m.getParamTypesNoResult().get(i) + ", found dependancy constr param type = " + dep.getFirst().getParams().get(i));
                        if (!m.getParamTypesNoResult().get(i).equals(dep.getFirst().getParams().get(i))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        container.addDep(dep.getSecond());
//                        System.out.println("  Match confirmed - adding to container for " + m.getName());
                    }
                }
            }
            methodsToAnalyse.add(container);
        }
    }



    private static void cleanMethodDepsToAnalyse(ArrayList<MethodDepsContainer> methodsToAnalyse) {

        // Names of the methods we are interested in analysing
        ArrayList<MethodDepsContainer> methodsToAnalyse2 = new ArrayList<MethodDepsContainer>(0);
        for (MethodDepsContainer mdc : methodsToAnalyse) {
            methodsToAnalyse2.add(mdc);
        }
        // Remove all of the method dependencies from each method to analyse if it is not in the set of methods we will be analysing
        Iterator<MethodDepsContainer> mdcIter = methodsToAnalyse.iterator();
        while (mdcIter.hasNext()) {
            MethodDepsContainer mdc = mdcIter.next();
            ArrayList<MethodDepEntry> remove = new ArrayList<MethodDepEntry>(0);
            for (MethodDepEntry entry : mdc.getDeps()) {
                boolean match = true;
                for (MethodDepsContainer mdc2 : methodsToAnalyse2) {
                    if (mdc2.getMethod().getName().equals(entry.getName()) && mdc2.getMSafeClass().getName().equals(entry.getClassName()) && (mdc2.getMethod().getParamTypesNoResult().size() == entry.getParams().size())) {
                        boolean match2 = true;
                        for (int i = 0; i < mdc2.getMethod().getParamTypesNoResult().size(); i++) {
                            if (!mdc2.getMethod().getParamTypes().get(i).equals(entry.getParams().get(i))) {
                                match2 = false;
                                break;
                            }
                        }
                        if (match2) {
                            match = false;
                            break;
                        }
                    }
                }
                if (match) {
                    remove.add(entry);
                }
            }
            for (MethodDepEntry entry : remove) {
                mdc.removeDep(entry);
            }
        }

        // Print out method dependencies
        System.out.println();
        System.out.println("=================================");
        System.out.println("Method dependencies:");
        System.out.println();
        for (MethodDepsContainer mdc : methodsToAnalyse) {
            if (mdc.getDeps().size() > 0) {
                System.out.println("Method " + mdc.getMethod().getName() + " in class " + mdc.getMSafeClass().getName() + " depends on:");
                for (MethodDepEntry entry : mdc.getDeps()) {
                    System.out.print("  - ");
                    entry.print();
                    System.out.println();
                }
                System.out.println();
            }
        }
    }


    private static ArrayList<String> analysed = new ArrayList<String>(0);
    private static ArrayList<String> analysedClass = new ArrayList<String>(0);

    // Method to sort the array of MethodDepsContainers based on the dependencies on each other
    private static ArrayList<MethodDepsContainer> sortMethodsAndAnalyse(ArrayList<MethodDepsContainer> methodsToAnalyse) {

        ArrayList<MethodDepsContainer> sortedMethods = new ArrayList<MethodDepsContainer>(0);
        int analysedCount = 0;
        int infiniteLoopCatch = 0;
        // While there are still methods left to analyse
        while (analysedCount < methodsToAnalyse.size()) {
            infiniteLoopCatch++;
            for (MethodDepsContainer mdc : methodsToAnalyse) {
                // If the method hasn't been analysed
                if (!mdc.beenAnalysed()) {
                    // If the number of dependencies is 0
                    if (mdc.getDeps().size() == 0) {
                        sortedMethods.add(mdc);
                        analysedCount++;
                        System.out.println("Analysing " + analysedCount + " of " + methodsToAnalyse.size() + ". Method " + mdc.getMethod().getName() + " in class " + mdc.getMSafeClass().getName() + " - there are no dependencies");
                        analysed.add(mdc.getMethod().getName());
                        analysedClass.add(mdc.getMSafeClass().getName());
                        mdc.markAsAnalysed();
                        infiniteLoopCatch = 0;
                    // Else the method does have dependencies
                    } else {
                        boolean okayToAnalyse = true;
                        // For all the method dependencies
                        for(MethodDepEntry entry : mdc.getDeps()) {
                            // If the set of analysed methods does not contain the method that this one is dependant on, or this one is dependent on itself
//                            if (!analysed.contains(entry.getName()) && (!entry.getName().equals(mdc.getMethod().getName()))) {
                            // If the set of analysed methods does not contain the method that this one is dependant on
//                            if (!analysed.contains(entry.getName())) {
                            boolean inAnalysed = false;
                            for (int i = 0; i < analysed.size(); i++) {
                                if (analysed.get(i).equals(entry.getName()) && analysedClass.get(i).equals(entry.getClassName())) {
                                    inAnalysed = true;
                                    break;
                                }
                            }
                            if (!inAnalysed) {
                                okayToAnalyse = false;
                                break;
                            }
                        }
                        // If all dependencies have been analysed
                        if (okayToAnalyse) {
                            sortedMethods.add(mdc);
                            analysedCount++;
                            System.out.println("Analysing " + analysedCount + " of " + methodsToAnalyse.size() + ". Method " + mdc.getMethod().getName() + " in class " + mdc.getMSafeClass().getName() + " - all dependencies have been analysed");
                            analysed.add(mdc.getMethod().getName());
                            analysedClass.add(mdc.getMSafeClass().getName());
                            mdc.markAsAnalysed();
                            infiniteLoopCatch = 0;
                        }
                    }
                }
            }
            // If the while loop completes an iteration of the methods without analysing anything
            if (infiniteLoopCatch > methodsToAnalyse.size()) {
                System.out.println();
                System.out.println("[ERROR] - Not all method properties have been generated. Failed to generate " + (methodsToAnalyse.size() - analysedCount) + " method properties:");
                for (MethodDepsContainer mdc : methodsToAnalyse) {
                    if (!mdc.beenAnalysed()) {
                        System.out.print(" - " + mdc.getMethod().getName() + "(");
                        for (int i = 0; i < mdc.getMethod().getParamTypesNoResult().size(); i++) {
                            System.out.print(mdc.getMethod().getParamTypesNoResult().get(i));
                            if (i < mdc.getMethod().getParamTypesNoResult().size() - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.print(") in class " + mdc.getMSafeClass().getName() + ". It depends on the following methods that have not been analysed:\n");
                        for (MethodDepEntry dep : mdc.getDeps()) {
                            if (!analysed.contains(dep.getName())) {
                                System.out.print("    - ");
                                dep.print();
                                System.out.println();
                            }
                        }
                    }
                }
                System.out.println();
                break;
            }
        }

        // Analyse the methods individually
        for (int i = 0; i < sortedMethods.size(); i++) {
            MethodDepsContainer current = sortedMethods.get(i);
            if (current.isConstr() == false) {
                analyseMethod(current.getMethod(), current.getMSafeClass());
            } else {
                analyseConstr(current.getMethod(), current.getMSafeClass());
            }
            analysedMethods.add(current);
        }

        ArrayList<MethodDepsContainer> remainingMethods = new ArrayList<MethodDepsContainer>(0);
        for (MethodDepsContainer mdc : methodsToAnalyse) {
            if (!sortedMethods.contains(mdc)) {
                remainingMethods.add(mdc);
            }
        }

        return remainingMethods;
    }



    private static ArrayList<MethodDepsContainer> analyseSimpleRecursiveMethods(ArrayList<MethodDepsContainer> methods) {   // TODO
        ArrayList<MethodDepsContainer> recursiveMethodsNoParam = new ArrayList<MethodDepsContainer>(0);
        ArrayList<MethodDepsContainer> recursiveMethodsParam = new ArrayList<MethodDepsContainer>(0);
        ArrayList<MethodDepsContainer> remainingMethods = new ArrayList<MethodDepsContainer>(0);
        for (MethodDepsContainer mdc : methods) {
            ArrayList<MethodDepEntry> methodDeps;
            if (!mdc.beenAnalysed()) {
                methodDeps = mdc.getDeps();
                for (MethodDepEntry mde : methodDeps) {
                    if (mde.getName().equals(mdc.getMethod().getName()) && mdc.getMethod().getParamTypesNoResult().size() == 0) {
                        System.out.println("Found method " + mdc.getMethod().getName() + " that calls itself with no parameters");
                        recursiveMethodsNoParam.add(mdc);
                    } else if (mde.getName().equals(mdc.getMethod().getName()) && mdc.getMethod().getParamTypesNoResult().size() > 0) {
                        System.out.println("Found method " + mdc.getMethod().getName() + " that calls itself with " + mdc.getMethod().getParamTypesNoResult().size() + " parameters");
                        recursiveMethodsParam.add(mdc);
                    }
                }
            }
        }
        for (MethodDepsContainer mdc : recursiveMethodsNoParam) {
            analyseRecursiveMethodNoParam(mdc);
            mdc.markAsAnalysed();
            analysed.add(mdc.getMethod().getName());
            analysedClass.add(mdc.getMSafeClass().getName());
        }
        for (MethodDepsContainer mdc : recursiveMethodsParam) {
            analyseRecursiveMethodParam(mdc);
            mdc.markAsAnalysed();
            analysed.add(mdc.getMethod().getName());
            analysedClass.add(mdc.getMSafeClass().getName());
        }
        for (MethodDepsContainer mdc : methods) {
            if (!recursiveMethodsNoParam.contains(mdc) && !recursiveMethodsParam.contains(mdc)) {
                remainingMethods.add(mdc);
            }
        }
        return remainingMethods;
    }


    private static void analyseRecursiveMethodNoParam(MethodDepsContainer mdc) {
        Method m = mdc.getMethod();
        MSafeSuperClass msafeClass = mdc.getMSafeClass();

        System.out.println("=================================");
        System.out.println("Analysing recursive method '" + m.getName() + "' in class " + msafeClass.getName());
        System.out.println();

        // Create a new blank method properties
//        MethodProperties properties = m.getProperties();
        MethodProperties properties = new MethodProperties();
//        MethodProperty blankProperty = new MethodProperty();
//        properties.addProperty(blankProperty);
        Command body = m.getBody();

        MetaRefCon mrc = new Current();
        // Analyse the method body
        calcOutOfScopeDecs(m, body, null);
        analyseCommand(m, properties, body, null, mrc);
//        System.out.println("Need to remove the following expressions after analysis:");
//        for (Expr e : m.getVarsToRemove()) {
//            System.out.println(" - " + e.getExpressionString());
//        }
        removePrimTypes(properties);
        removeEmptyRefs(properties);
//        simplifyMethodProperties(properties);
        removeExprsFromProperties(toRemoveFromMethod, properties);
//        completedPropertys.clear();
        toRemoveFromMethod.clear();

        m.addProperties(properties);
        analysedMethods.add(mdc);

        CheckingUtil.checkMethodPropertiesSafety(properties);

        System.out.println();
        printMethodProperties(properties);

    }


    private static void analyseRecursiveMethodParam(MethodDepsContainer mdc) {
        Method m = mdc.getMethod();
        MSafeSuperClass msafeClass = mdc.getMSafeClass();

        System.out.println("=================================");
        System.out.println("Analysing recursive method '" + m.getName() + "' in class " + msafeClass.getName());
        System.out.println();

        ArrayList<MethodProperties> propertiesList = new ArrayList<MethodProperties>(0);
        Command body = m.getBody();

        boolean fixedPoint = false;
        int iteration = 0;
        while (!fixedPoint) {
            iteration++;

            MethodProperties properties = new MethodProperties();
//            MethodProperty blankProperty = new MethodProperty();
//            properties.addProperty(blankProperty);

            MetaRefCon mrc = new Current();
            analyseCommand(m, properties, body, null, mrc);
//            System.out.println("Need to remove the following expressions after analysis:");
//            for (Expr e : m.getVarsToRemove()) {
//                System.out.println(" - " + e.getExpressionString());
//            }
            removePrimTypes(properties);
            removeEmptyRefs(properties);
//            simplifyMethodProperties(properties);
            removeExprsFromProperties(toRemoveFromMethod, properties);
//            completedPropertys.clear();
            toRemoveFromMethod.clear();

            System.out.println("Properties after " + iteration + " iterations");
            System.out.println();
            printMethodProperties(properties);

            if (propertiesList.size() > 0 && propertiesList.get(propertiesList.size() - 1).equalTo(properties)) {
                System.out.println("Properties are equal after " + iteration + " iterations");
                fixedPoint = true;

                System.out.println();
                printMethodProperties(properties);
            }

            if (iteration == 1) {
                analysedMethods.add(mdc);
            }

//            propertiesList.add(properties);
            propertiesList.add(properties.clone());
            m.addProperties(properties);

            if (iteration >= EnvUtil.LOOPCUTOFF && fixedPoint == false) {
                System.out.println("[ERROR] - No fixed point has been detected after " + iteration + " iterations.");
                fixedPoint = true;
            }
        }

        CheckingUtil.checkMethodPropertiesSafety(m.getProperties());

        calcOutOfScopeDecs(m, body, null);

    }


    // Method to generate the method properties of a constructor
    private static void analyseConstr(Method m, MSafeSuperClass msafeClass) {
        System.out.println("=================================");
        System.out.println("Analysing constr '" + m.getName() + "' of class " + msafeClass.getName());
        System.out.println();

        ArrayList<Declaration> fields = msafeClass.getFields();
        ArrayList<Command> inits = msafeClass.getInit();

        // Create a new blank method properties
//        MethodProperties properties = m.getProperties();
        MethodProperties properties = new MethodProperties();
//        MethodProperty blankProperty = new MethodProperty();
//        properties.addProperty(blankProperty);
        Command body = m.getBody();

        MetaRefCon mrc = new Current();

        // This loop analyses the fields and initialisations of all parent classes
        if (msafeClass instanceof MSafeClass) {
            MSafeClass newClass = (MSafeClass) msafeClass;
            String classExtends = newClass.getExtends();
            while (!classExtends.equals("")) {
                MSafeSuperClass extendsClass = SCJmSafeChecker.SCJmSafePROGRAM.getClass(classExtends);
                if (extendsClass != null) {
                    for (Declaration d : extendsClass.getFields()) {
                        addDecToProperties(properties, d.getVar(), null, mrc);
                    }
                    for (Command c : extendsClass.getInit()) {
                        analyseCommand(m, properties, c, null, mrc);
                    }
                    if (extendsClass instanceof MSafeClass) {
                        MSafeClass temp = (MSafeClass) extendsClass;
                        classExtends = temp.getExtends();
                    } else {
                        classExtends = "";
                    }
                } else {
                    classExtends = "";
                }
            }
        }

        // Add local fields to properties
        for (Declaration d : fields) {
            addDecToProperties(properties, d.getVar(), null, mrc);
        }
        // Apply inits to local fields
        for (Command c : inits) {
            analyseCommand(m, properties, c, null, mrc);
        }
        // Analyse the constructor body
        calcOutOfScopeDecs(m, body, null);
        analyseCommand(m, properties, body, null, mrc);
//        System.out.println("Need to remove the following expressions after analysis:");
//        for (Expr e : m.getVarsToRemove()) {
//            System.out.println(" - " + e.getExpressionString());
//        }
        removePrimTypes(properties);
        removeEmptyRefs(properties);
//        simplifyMethodProperties(properties);
        removeExprsFromProperties(toRemoveFromMethod, properties);
//        completedPropertys.clear();
        toRemoveFromMethod.clear();

        m.addProperties(properties);

        CheckingUtil.checkMethodPropertiesSafety(properties);

        System.out.println();
        printMethodProperties(properties);
    }

    private static ArrayList<Expr> toRemoveFromMethod = new ArrayList<Expr>(0);

    // Method to generate the method properties of a method
    private static void analyseMethod(Method m, MSafeSuperClass msafeClass) {
        System.out.println("=================================");
        System.out.println("Analysing method '" + m.getName() + "' in class " + msafeClass.getName());
        System.out.println();

        // Create a new blank method properties
//        MethodProperties properties = m.getProperties();
        MethodProperties properties = new MethodProperties();
//        MethodProperty blankProperty = new MethodProperty();
//        properties.addProperty(blankProperty);
        Command body = m.getBody();

        MetaRefCon mrc = new Current();
        // Analyse the method body
        calcOutOfScopeDecs(m, body, null);

        analyseCommand(m, properties, body, null, mrc);
//        System.out.println("Need to remove the following expressions after analysis:");
//        for (Expr e : m.getVarsToRemove()) {
//            System.out.println(" - " + e.getExpressionString());
//        }

        removePrimTypes(properties);

        removeEmptyRefs(properties);
//        simplifyMethodProperties(properties);

        removeExprsFromProperties(toRemoveFromMethod, properties);
//        completedPropertys.clear();
        toRemoveFromMethod.clear();

        m.addProperties(properties);

        CheckingUtil.checkMethodPropertiesSafety(properties);

        System.out.println();
        printMethodProperties(properties);
    }

    private static void removeExprsFromProperties(ArrayList<Expr> exprs, MethodProperties properties) {
//        for (MethodProperty property : properties.getProperties()) {
            for (Expr e : exprs) {
                System.out.println("Removing " + e.getExpressionString() + " from properties");
                properties.getMethodShareChange().removeExprFromShares(e);
            }
            for (Expr e : exprs) {
                properties.getMethodRefChange().removeExprFromRefSet(e);
            }
//        }
    }

    ///////////////////////////////////////////////
    ////                Commands               ////
    ///////////////////////////////////////////////

//    private static ArrayList<MethodProperty> completedPropertys = new ArrayList<MethodProperty>(0);

    private static void analyseCommand(Method m, MethodProperties properties, Command com, Expr currentExpr, MetaRefCon mrc) {
        boolean print = false;

        if (com == null) {
            return;
        }

//        for (MethodProperty p : completedPropertys) {
//            properties.addProperty(p);
//        }
//        completedPropertys.clear();

        if (com instanceof Assignment) {
            if (print) {
                System.out.println("--------------------------------- Assignment:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            Assignment ass = (Assignment) com;
            calcPropertiesAss(m, properties, ass, currentExpr, mrc);

            if (print) {
                printMethodProperties(properties);
            }

        } else if (com instanceof Declaration) {
            if (print) {
                System.out.println("--------------------------------- Dec:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            Declaration dec = (Declaration) com;
            Variable var = dec.getVar();
            addDecToProperties(properties, var, currentExpr, mrc);

            if (print) {
                printMethodProperties(properties);
            }

        } else if (com instanceof DoWhile) {
            if (print) {
                System.out.println("--------------------------------- DoWhile:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            DoWhile doWhile = (DoWhile) com;
            //analyseCommand(m, properties, doWhile.getC1(), currentExpr, mrc);
            analyseLoop(m, properties, doWhile.getC1(), currentExpr, mrc);

        } else if (com instanceof EnterPrivateMemory) {
            if (print) {
                System.out.println("--------------------------------- EnterPrivateMemory:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            EnterPrivateMemory enterPrivMem = (EnterPrivateMemory) com;
            MethodCall methodCall = enterPrivMem.getMethodCall();
            ArrayList<MethodSig> possibleMethodsSigs = methodCall.getPossibleMethods();
            ArrayList<Method> possibleMethods = getMethodsFromSignatures(possibleMethodsSigs);
            applyPossibleMethods(m, possibleMethods, methodCall.getArgs(), properties, currentExpr, methodCall.getExpr(), lowerMetaRefCon(mrc));

            if (print) {
                printMethodProperties(properties);
            }

        } else if (com instanceof ExecuteInAreaOf) {
            if (print) {
                System.out.println("--------------------------------- ExecuteInAreaOf:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            ExecuteInAreaOf executeInAreaOf = (ExecuteInAreaOf) com;
            MethodCall methodCall = executeInAreaOf.getMethodCall();
            ArrayList<MethodSig> possibleMethodsSigs = methodCall.getPossibleMethods();
            ArrayList<Method> possibleMethods = getMethodsFromSignatures(possibleMethodsSigs);
            applyPossibleMethods(m, possibleMethods, methodCall.getArgs(), properties, currentExpr, methodCall.getExpr(), executeInAreaOf.getMetaRefCon());

        } else if (com instanceof ExecuteInOuterArea) {
            if (print) {
                System.out.println("--------------------------------- ExecuteInOuterArea:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            ExecuteInOuterArea executeInOuterArea = (ExecuteInOuterArea) com;
            MethodCall methodCall = executeInOuterArea.getMethodCall();
            ArrayList<MethodSig> possibleMethodsSigs = methodCall.getPossibleMethods();
            ArrayList<Method> possibleMethods = getMethodsFromSignatures(possibleMethodsSigs);
            applyPossibleMethods(m, possibleMethods, methodCall.getArgs(), properties, currentExpr, methodCall.getExpr(), raiseMetaRefCon(mrc));

        } else if (com instanceof GetMemoryArea) {
            if (print) {
                System.out.println("--------------------------------- GetMemoryArea:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            GetMemoryArea getMemoryArea = (GetMemoryArea) com;
            analyseGetMemoryArea(m, properties, getMemoryArea, currentExpr);

        } else if (com instanceof For) {
            if (print) {
                System.out.println("--------------------------------- For:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            For forCom = (For) com;
            analyseCommand(m, properties, forCom.getC1(), currentExpr, mrc);

            analyseLoop(m, properties, new Sequence(forCom.getC3(), forCom.getC2()), currentExpr, mrc);

//            MethodProperties clone = properties.clone();
//            analyseCommand(m, clone, forCom.getC3(), currentExpr, mrc);
//            analyseCommand(m, clone, forCom.getC2(), currentExpr, mrc);

//            properties.setProperties(methodPropertiesJoin(clone, properties).getProperties());

        } else if (com instanceof If) {
            if (print) {
                System.out.println("--------------------------------- If:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            If ifCom = (If) com;

            MethodProperties clone1 = properties.clone();
            analyseCommand(m, clone1, ifCom.getC1(), currentExpr, mrc);

            MethodProperties clone2 = properties.clone();
            analyseCommand(m, clone2, ifCom.getC2(), currentExpr, mrc);

            properties.setProperties(methodPropertiesJoin(clone1, clone2));

            if (print) {
                printMethodProperties(properties);
            }

        } else if (com instanceof MethodCall) {
            if (print) {
                System.out.println("--------------------------------- Method Call:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            MethodCall methodCall = (MethodCall) com;
            ArrayList<MethodSig> possibleMethodsSigs = methodCall.getPossibleMethods();
            ArrayList<Method> possibleMethods = getMethodsFromSignatures(possibleMethodsSigs);
            applyPossibleMethods(m, possibleMethods, methodCall.getArgs(), properties, currentExpr, methodCall.getExpr(), mrc);

            if (print) {
                printMethodProperties(properties);
            }

        } else if (com instanceof NewInstance) {
            if (print) {
                System.out.println("--------------------------------- NewInstance:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            NewInstance newInstance = (NewInstance) com;
            calcPropertiesNewInstance(m, properties, newInstance, currentExpr, mrc);

            if (print) {
                printMethodProperties(properties);
            }

        } else if (com instanceof Scope) {
            Scope scope = (Scope) com;
            analyseCommand(m, properties, scope.getCommand(), currentExpr, mrc);

        } else if (com instanceof Sequence) {
            Sequence seq = (Sequence) com;
            analyseCommand(m, properties, seq.getC1(), currentExpr, mrc);
            analyseCommand(m, properties, seq.getC2(), currentExpr, mrc);

        } else if (com instanceof Skip) {
            if (print) {
                System.out.println("--------------------------------- Skip:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

        } else if (com instanceof Switch) {
            if (print) {
                System.out.println("--------------------------------- Switch:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            Switch switchCom = (Switch) com;
            ArrayList<Command> commands = switchCom.getCommands();
            distMethodPropertiesJoin(m, properties, commands, currentExpr, mrc);

        } else if (com instanceof Try) {
            if (print) {
                System.out.println("--------------------------------- Try:");
                com.printCommand(0);
                System.out.println();
                System.out.println();
            }

            Try tryCom = (Try) com;
            Command tryCommand = tryCom.getTryCommand();
            ArrayList<Command> catchComs = tryCom.getCatchComs();
            Command finallyCom = tryCom.getFinallyCommand();

            analyseCommand(m, properties, tryCommand, currentExpr, mrc);
            distMethodPropertiesJoin(m, properties, catchComs, currentExpr, mrc);
            analyseCommand(m, properties, finallyCom, currentExpr, mrc);

        } else if (com instanceof While) {
            System.out.println("--------------------------------- While:");
            com.printCommand(0);
            System.out.println();
            System.out.println();

            While whileCom = (While) com;
            //analyseCommand(m, properties, whileCom.getC1(), currentExpr, mrc);
            analyseLoop(m, properties, whileCom.getC1(), currentExpr, mrc);

        } else {
            System.out.println("[ERROR] - Command in method body cannot be determined");
        }
    }


    public static void analyseLoop(Method m, MethodProperties properties, Command com, Expr currentExpr, MetaRefCon mrc) {
//        MethodProperties clone = properties.clone();

        System.out.println("Analysing loop");
        System.out.println();

        ArrayList<MethodProperties> propertiesList = new ArrayList<MethodProperties>(0);

        boolean fixedPoint = false;
        int iteration = 0;
        while (!fixedPoint) {
            iteration++;

//            analyseCommand(m, clone, com, null, mrc);
            analyseCommand(m, properties, com, null, mrc);

//            removePrimTypes(clone);
            removePrimTypes(properties);
//            simplifyMethodProperties(clone);

            System.out.println("Properties after " + iteration + " iterations");
            System.out.println();
//            printMethodProperties(clone);
            printMethodProperties(properties);

//            if (propertiesList.size() > 0 && propertiesList.get(propertiesList.size() - 1).equalTo(clone)) {
            if (propertiesList.size() > 0 && propertiesList.get(propertiesList.size() - 1).equalTo(properties)) {
                System.out.println("Properties are equal after " + iteration + " iterations");
                fixedPoint = true;

                System.out.println();
//                printMethodProperties(clone);
                printMethodProperties(properties);
            }

//            propertiesList.add(clone);
            propertiesList.add(properties.clone());

            if (iteration >= EnvUtil.LOOPCUTOFF && fixedPoint == false) {
                System.out.println("[ERROR] - No fixed point has been detected after " + iteration + " iterations.");
                fixedPoint = true;
            }
        }

//        properties.setProperties(methodPropertiesJoin(clone, properties));

    }





    ///////////////////////////////////////////////
    ////              Declarations             ////
    ///////////////////////////////////////////////

    private static void addDecToProperties(MethodProperties properties, Variable var, Expr
 currentExpr, MetaRefCon mrc) {
        ShareRelation shareRel = properties.getMethodShareChange();
        Expr newVarExpr = EnvUtil.mergeExprs(currentExpr, var);
        Share share = new Share(newVarExpr, newVarExpr);
        shareRel.addShare(share);
        MethodRefChange refChange = properties.getMethodRefChange();
        HashSet<MetaRefCon> mrcs = getDecMetaRefCon(var.getVarType());
        MethodRefMapping refMapping = new MethodRefMapping(newVarExpr, mrcs);
        refChange.updateRefMapping(refMapping);
    }

    private static HashSet<MetaRefCon> getDecMetaRefCon(VarType varType) {
        HashSet<MetaRefCon> result = new HashSet<MetaRefCon>(0);
        if (varType.isPrimitive()) {
            if (varType.isArray()) {

            } else {
                HashSet<RefCon> temp = new HashSet<RefCon>(0);
                temp.add(new Prim());
                Rcs rcs = new Rcs(temp);
                result.add(rcs);
            }
        } else if (varType.isReference()) {

        } else {
//            System.out.println("[WARNING] - Type of declaration (reference / primitive) has not been detected");
        }
        return result;
    }

    private static void calcOutOfScopeDecs(Method m, Command com, Expr currentExpr) {
        ArrayList<Expr> varsToRemove = getDecs(com, currentExpr);
//        System.out.println("Vars to remove:");
        for (Expr e : varsToRemove) {
//            System.out.println(" - " + e.getExpressionString());
            m.addVarToRemove(e);
        }
    }

    private static void updateOutOfScopeDecs(Method m1, Method m2, Expr currentExpr) {
        ArrayList<Expr> varsToRemove = m2.getVarsToRemove();
        ArrayList<Expr> newToRemoveList = new ArrayList<Expr>(0);
        newToRemoveList.addAll(varsToRemove);
//        System.out.print("Updating out of Scope decs... currentExpr = ");
        if (currentExpr != null) {
//            System.out.print(currentExpr.getExpressionString() + "\n");
        } else {
//            System.out.print("\n");
        }
        for (Expr e : newToRemoveList) {
//            System.out.println(" - Adding " + EnvUtil.mergeExprs(currentExpr, e).getExpressionString());
            m1.addVarToRemove(EnvUtil.mergeExprs(currentExpr, e));
        }
    }

/*
    private static void removeOutOfScopeDecs(Method m, Expr currentExpr, MethodProperties properties) {
        ArrayList<Expr> varsToRemove = m.getVarsToRemove();
        for (MethodProperty property : properties.getProperties()) {
            for (Expr e : varsToRemove) {
                Expr e2 = EnvUtil.mergeExprs(currentExpr, e);
                ShareRelation shareRelation = property.getMethodShareChange();
                shareRelation.removeExprFromShares(e2);
                MethodRefChange refChange = property.getMethodRefChange();
                refChange.removeExprFromRefSet(e2);
            }
        }
        simplifyMethodProperties(properties);
    }
*/

    private static void removePrimTypes(MethodProperties properties) {
        ShareRelation shareRelation = properties.getMethodShareChange();
        MethodRefChange refChange = properties.getMethodRefChange();
        ArrayList<Expr> toRemove = new ArrayList<Expr>(0);
        for (MethodRefMapping mrp : refChange.getMethodRefs()) {
//            if (mrp.getMetaRefCons().size() == 1) {
                for (MetaRefCon mrc : mrp.getMetaRefCons()) {
                    if (mrc instanceof Rcs) {
                        Rcs rcs = (Rcs) mrc;
                        if (rcs.getRefCons().size() == 1) {
                            for (RefCon rc : rcs.getRefCons()) {
                                if (rc instanceof Prim) {
                                    toRemove.add(mrp.getExpr());
                                }
                            }
                        }
                    }
                }
//            }
        }
        for (Expr e : toRemove) {
            shareRelation.removeExprFromShares(e);
            refChange.removeExprFromRefSet(e);
        }
    }

    private static void removeEmptyRefs(MethodProperties properties) {
        MethodRefChange refChange = properties.getMethodRefChange();
        ArrayList<MethodRefMapping> toRemove = new ArrayList<MethodRefMapping>(0);
        for (MethodRefMapping mrp : refChange.getMethodRefs()) {
            if (mrp.getMetaRefCons().size() == 0) {
                toRemove.add(mrp);
            }
        }
        for (MethodRefMapping mrp : toRemove) {
            refChange.removeRefMapping(mrp);
        }
    }



    public static ArrayList<Expr> getDecs(Command com, Expr currentExpr) {
        ArrayList<Expr> result = new ArrayList<Expr>(0);

        if (com instanceof Declaration) {
            Declaration dec = (Declaration) com;
            Variable var = dec.getVar();
            result.add(EnvUtil.mergeExprs(currentExpr, var));

        } else if (com instanceof DoWhile) {
            DoWhile doWhile = (DoWhile) com;
            result.addAll(getDecs(doWhile.getC1(), currentExpr));

        } else if (com instanceof For) {
            For forCom = (For) com;
            result.addAll(getDecs(forCom.getC1(), currentExpr));
            result.addAll(getDecs(forCom.getC2(), currentExpr));
            result.addAll(getDecs(forCom.getC3(), currentExpr));

        } else if (com instanceof If) {
            If ifCom = (If) com;
            result.addAll(getDecs(ifCom.getC1(), currentExpr));
            result.addAll(getDecs(ifCom.getC2(), currentExpr));

        } else if (com instanceof Scope) {
            Scope scope = (Scope) com;
            result.addAll(getDecs(scope.getCommand(), currentExpr));

        } else if (com instanceof Sequence) {
            Sequence seq = (Sequence) com;
            result.addAll(getDecs(seq.getC1(), currentExpr));
            result.addAll(getDecs(seq.getC2(), currentExpr));

        } else if (com instanceof Switch) {
            Switch switchCom = (Switch) com;
            ArrayList<Command> commands = switchCom.getCommands();
            for (Command c : commands) {
                result.addAll(getDecs(c, currentExpr));
            }

        } else if (com instanceof Try) {
            Try tryCom = (Try) com;
            result.addAll(getDecs(tryCom.getTryCommand(), currentExpr));
            for (Command c : tryCom.getCatchExprs()) {
                result.addAll(getDecs(c, currentExpr));
            }
            for (Command c : tryCom.getCatchComs()) {
                result.addAll(getDecs(c, currentExpr));
            }
            result.addAll(getDecs(tryCom.getFinallyCommand(), currentExpr));

        } else if (com instanceof While) {
            While whileCom = (While) com;
            result.addAll(getDecs(whileCom.getC1(), currentExpr));

        }
        return result;
    }

    ///////////////////////////////////////////////
    ////              Assignment               ////
    ///////////////////////////////////////////////

    public static void calcPropertiesAss(Method m, MethodProperties properties, Assignment ass, Expr currentExpr, MetaRefCon mrc) {
        propertiesAssAdd(m, properties, currentExpr, ass);            
    }


    public static void propertiesAssAdd(Method m, MethodProperties properties, Expr currentExpr, Assignment ass) {
        Expr lexpr = ass.getLExpr();
        Expr rexpr = ass.getRExpr();
        // Check to see if the lexpr is being ignored, or should be ignored - this is because assignments to arguments in method calls are not visible once the method returns.
        if (lexpr instanceof Variable) {
            Variable v1 = (Variable) lexpr;
            for (Variable v2 : m.getParams()) {
                if (v1.equalTo(v2) && !v1.getExpressionString().equals("Result")) {
                    toRemoveFromMethod.add(v1);
                }
            }
        } else {
            for (Expr e : toRemoveFromMethod) {
                if (EnvUtil.exprPrefixOfFieldAccess(e, lexpr)) {
                    toRemoveFromMethod.add(lexpr);
                }
            }
        }

        Expr newLExpr = EnvUtil.mergeExprs(currentExpr, lexpr);
        Expr newRExpr;
        if (rexpr instanceof This && currentExpr != null) {
            newRExpr = currentExpr;
        } else if (rexpr instanceof This && currentExpr == null) {
            newRExpr = rexpr;
        } else {
            newRExpr = EnvUtil.mergeExprs(currentExpr, rexpr);
        }

        HashSet<Share> sharesToAdd = new HashSet<Share>(0);
        HashSet<MethodRefMapping> refsToUpdate = new HashSet<MethodRefMapping>(0);

        // Add the new shares
        ShareRelation shareRelation = properties.getMethodShareChange();

        ArrayList<Expr> equalExprs = new ArrayList<Expr>(0);
        if (lexpr instanceof FieldAccess || lexpr instanceof ArrayElement) {
            for (Share s : shareRelation.getShares()) {
                if (newLExpr instanceof FieldAccess) {
                    if (EnvUtil.getFrontOfExpr(newLExpr).equalTo(s.getLExpr()) && !s.getLExpr().equalTo(s.getRExpr())) {
    //                            System.out.println("Assignment - LE : " + newLExpr.getExpressionString() + " is equal to " + s.getRExpr().getExpressionString());
    //                    equalExprs.add(s.getRExpr());
                        addToEqualExprs(equalExprs, s.getRExpr());
                    } else if (EnvUtil.getFrontOfExpr(newLExpr).equalTo(s.getRExpr()) && !s.getLExpr().equalTo(s.getRExpr())) {
    //                            System.out.println("Assignment - LE : " + newLExpr.getExpressionString() + " is equal to " + s.getLExpr().getExpressionString());
    //                    equalExprs.add(s.getLExpr());
                        addToEqualExprs(equalExprs, s.getLExpr());
                    }
                }
            }
        }

//        shareRelation.removeExprFromShares(newLExpr);
        Share newShare;
//                newShare = new Share(newLExpr, newLExpr);   // REMOVED
//                sharesToAdd.add(newShare);
        if (!(rexpr instanceof Val) && !newLExpr.equalTo(newRExpr) && !EnvUtil.exprPrefixOfFieldAccess(newLExpr, newRExpr)) {
            newShare = new Share(newLExpr, newRExpr);
            sharesToAdd.add(newShare);
//                    newShare = new Share(newRExpr, newLExpr);
//                    sharesToAdd.add(newShare);
        }
        for (Expr e : equalExprs) {
//            newShare = new Share(newLExpr, e);
//            sharesToAdd.add(newShare);
//                    newShare = new Share(e, newLExpr);
//                    sharesToAdd.add(newShare);

//            newShare = new Share(e, newRExpr);
//            sharesToAdd.add(newShare);
//                    newShare = new Share(newRExpr, e);
//                    sharesToAdd.add(newShare);

            newShare = new Share(EnvUtil.sortShareAssignmentExpression(e, newRExpr, newLExpr), newRExpr);
            if (!EnvUtil.exprPrefixOfFieldAccess(newLExpr, newRExpr)) {
                sharesToAdd.add(newShare);
            }
        }

        // Add the new ref mappings
        MethodRefChange methodChange = properties.getMethodRefChange();
//        methodChange.removeExprFromRefSet(newLExpr);
        MethodRefMapping lExprRefMapping = methodChange.getMethodRefMapping(newLExpr);
        MethodRefMapping rExprRefMapping = methodChange.getMethodRefMapping(newRExpr);
        HashSet<MetaRefCon> metaRefConSet;
        if (rExprRefMapping == null) {
            if (rexpr instanceof Val) {
                HashSet<RefCon> rexprRcs = new HashSet<RefCon>(0);
                rexprRcs.add(new Prim());
                HashSet<MetaRefCon> rexprMrcs = new HashSet<MetaRefCon>(0);
                rexprMrcs.add(new Rcs(rexprRcs));
                metaRefConSet = rexprMrcs;
            } else if (rexpr instanceof Null) {
                HashSet<MetaRefCon> rexprMrcs = new HashSet<MetaRefCon>(0);
                metaRefConSet = rexprMrcs;
            } else {
                HashSet<MetaRefCon> rexprMrcs = new HashSet<MetaRefCon>(0);
                rexprMrcs.add(new Erc(newRExpr));
                metaRefConSet = rexprMrcs;
            }
        } else {
            metaRefConSet = rExprRefMapping.getMetaRefCons();
        }
        refsToUpdate.add(new MethodRefMapping(newLExpr, metaRefConSet));
        for (Expr e : equalExprs) {
//            methodChange.removeExprFromRefSet(e);
            refsToUpdate.add(new MethodRefMapping(EnvUtil.sortShareAssignmentExpression(e, newRExpr, newLExpr), metaRefConSet));
        }

        // This loop adds in any fields of an object on the rhs to the variable being assigned to on the lhs. For example, a = b would produce a.field if b had a field 'field'.
//        equalExprs.add(newLExpr);
        addToEqualExprs(equalExprs, newLExpr);
        for (Expr e : equalExprs) {
            for (Share s : shareRelation.getShares()) {
                Expr newLhs = EnvUtil.sortShareAssignmentExpression(e, newRExpr, s.getLExpr());
                if (EnvUtil.exprPrefixOfFieldAccess(newRExpr, s.getLExpr()) && newLhs != null && !EnvUtil.exprPrefixOfFieldAccess(newLExpr, newRExpr)) {
                    sharesToAdd.add(new Share(newLhs, s.getRExpr()));
//                            sharesToAdd.add(new Share(s.getRExpr(), newLhs));
                    MethodRefMapping mapping = methodChange.getMethodRefMapping(s.getRExpr());
                    HashSet<MetaRefCon> metaRefCons;
                    if (mapping != null) {
                        metaRefCons = mapping.getMetaRefCons();
//                        newMapping = new MethodRefMapping(newLhs, mapping.getMetaRefCons());
                    } else {
                        HashSet<MetaRefCon> rexprMrcs = new HashSet<MetaRefCon>(0);
                        rexprMrcs.add(new Erc(s.getRExpr()));
                        metaRefCons = rexprMrcs;
//                        newMapping = new MethodRefMapping(newLhs, rexprMrcs);
                    }
                    if (!newLhs.equalTo(e)) {
//                                sharesToAdd.add(new Share(newLhs, newLhs));   // REMOVED
                        refsToUpdate.add(new MethodRefMapping(newLhs, metaRefCons));
                    }
                }
            }
        }



        shareRelation.addShares(sharesToAdd);
        methodChange.updateRefMappings(refsToUpdate);
//        takeTransitiveClosure(properties);

//                for (Share s : shareRelation.getShares()) {
//                    if (!s.getLExpr().equalTo(s.getRExpr())) {
//                        
//                    }
//                }


//                if(newLExpr.getExpressionString().equals("Result")) {
//                    completedPropertys.add(property.clone());
//                    property.complete();
//                }

//        CheckingUtil.checkMethodPropertiesSafety(properties);
    }



    public static void addToEqualExprs(ArrayList<Expr> equalExprs, Expr e) {
        boolean found = false;
        for (Expr exprInEqual : equalExprs) {
            if (exprInEqual.equalTo(e)) {
                found = true;
                break;
            }
        }
        if (!found) {
            equalExprs.add(e);
        }
    }

/*
    public static void takeTransitiveClosure(MethodProperties properties) {
        ShareRelation shareRelation = properties.getMethodShareChange();

        HashSet<Share> shares = new HashSet<Share>(0);
        for (Share s1 : shareRelation.getShares()) {
            HashSet<Share> matchingShares = shareRelation.getAllShares(s1.getRExpr());
            Iterator iter = matchingShares.iterator();
            while (iter.hasNext()) {
                Share s2 = (Share) iter.next();
                if (s1 != s2) {
                    if (s1.getRExpr().equalTo(s2.getLExpr())) {
                        shares.add(new Share(s1.getLExpr(), s2.getRExpr()));
//                        matchingShares.add(s2.getRExpr());
                    } else if (s1.getRExpr().equalTo(s2.getRExpr())) {
                        shares.add(new Share(s1.getLExpr(), s2.getLExpr()));
//                        matchingShares.add(s2.getLExpr());
                    }
                }
            }
        }
        shareRelation.addShares(shares);
    }
*/

    ///////////////////////////////////////////////
    ////             New Instance              ////
    ///////////////////////////////////////////////

    public static void calcPropertiesNewInstance(Method m, MethodProperties properties, NewInstance newInstance, Expr currentExpr, MetaRefCon mrc) {
        Expr lexpr = newInstance.getExpr();
        if (lexpr instanceof Variable) {
            Variable v1 = (Variable) lexpr;
            for (Variable v2 : m.getParams()) {
                if (v1.equalTo(v2) && !v1.getExpressionString().equals("Result")) {
                    toRemoveFromMethod.add(v1);
                }
            }
        } else {
            for (Expr e : toRemoveFromMethod) {
                if (EnvUtil.exprPrefixOfFieldAccess(e, lexpr)) {
                    toRemoveFromMethod.add(lexpr);
                }
            }
        }
        Expr newLExpr = EnvUtil.mergeExprs(currentExpr, lexpr);
        MetaRefCon NImrc = newInstance.getMetaRefCon();
        VarType varType = newInstance.getVarType();
        String className = varType.getTypeName();
        ArrayList<Expr> params = newInstance.getParams();

        ShareRelation shareRel = properties.getMethodShareChange();
//        shareRel.removeExprFromShares(newLExpr);
                shareRel.addShare(new Share(newLExpr, newLExpr));   // REMOVED

        MethodRefChange methodChange = properties.getMethodRefChange();
//        methodChange.removeExprFromRefSet(newLExpr);
        HashSet<MetaRefCon> mrcs = new HashSet<MetaRefCon>(0);
//        mrcs.addAll(analyseMetaRefCon(NImrc, mrc, properties));
        for (MetaRefCon mrc1 : analyseMetaRefCon(NImrc, mrc, properties)) {
            boolean found = false;
            for (MetaRefCon mrc2 : mrcs) {
                if (mrc2.getMetaRefConString().equals(mrc1.getMetaRefConString())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                mrcs.add(mrc1);
            }
        }

//        MethodRefMapping newRefMapping = new MethodRefMapping(newLExpr, mrcs);
//        methodChange.updateRefMapping(newRefMapping);
        methodChange.updateRefMapping(newLExpr, mrcs);



        ArrayList<Expr> equalExprs = new ArrayList<Expr>(0);
        if (lexpr instanceof FieldAccess || lexpr instanceof ArrayElement) {
            for (Share s : shareRel.getShares()) {
                if (newLExpr instanceof FieldAccess) {
                    if (EnvUtil.getFrontOfExpr(newLExpr).equalTo(s.getLExpr()) && !s.getLExpr().equalTo(s.getRExpr())) { 
    //                    System.out.println("New Instance - LE : " + getFrontOfExpr(newLExpr).getExpressionString() + " is equal to " + s.getRExpr().getExpressionString());
                        addToEqualExprs(equalExprs, s.getRExpr());
                    } else if (EnvUtil.getFrontOfExpr(newLExpr).equalTo(s.getRExpr()) && !s.getLExpr().equalTo(s.getRExpr())) {
    //                    System.out.println("New Instance - LE : " + getFrontOfExpr(newLExpr).getExpressionString() + " is equal to " + s.getLExpr().getExpressionString());
                        addToEqualExprs(equalExprs, s.getLExpr());
                    }
                }
            }
        }

        for (Expr e : equalExprs) {
//            System.out.println("NewInstance adding ref mapping : " + sortShareAssignmentExpression(e, getFrontOfExpr(newLExpr), newLExpr).getExpressionString());
            methodChange.updateRefMapping(EnvUtil.sortShareAssignmentExpression(e, EnvUtil.getFrontOfExpr(newLExpr), newLExpr), mrcs);
        }

        addToEqualExprs(equalExprs, newLExpr);


        HashSet<Share> sharesToAdd = new HashSet<Share>(0); 
        for (Expr e : equalExprs) {
            for (Share s : shareRel.getShares()) {
                if (EnvUtil.exprPrefixOfFieldAccess(newLExpr, s.getLExpr()) && s.getLExpr().getLength() > newLExpr.getLength()) {
                    Expr newLhs = EnvUtil.sortShareAssignmentExpression(e, newLExpr, s.getLExpr());
                    if (newLhs != null && !EnvUtil.exprPrefixOfFieldAccess(newLhs, s.getRExpr())) {
                        sharesToAdd.add(new Share(newLhs, s.getRExpr()));
                        MethodRefMapping mapping = methodChange.getMethodRefMapping(s.getRExpr());
                        HashSet<MetaRefCon> metarefCons;
                        if (mapping != null) {
                            metarefCons = mapping.getMetaRefCons();
                        } else {
                            HashSet<MetaRefCon> rexprMrcs = new HashSet<MetaRefCon>(0);
                            metarefCons = rexprMrcs;
                        }
                        if (!newLhs.equalTo(newLExpr)) {
                            sharesToAdd.add(new Share(newLhs, newLhs));
                            methodChange.updateRefMapping(newLhs, metarefCons);
                        }
                    }
                } else if (EnvUtil.exprPrefixOfFieldAccess(newLExpr, s.getRExpr()) && s.getRExpr().getLength() > newLExpr.getLength()) {
                    Expr newLhs = EnvUtil.sortShareAssignmentExpression(e, newLExpr, s.getRExpr());
                    if (newLhs != null && !EnvUtil.exprPrefixOfFieldAccess(newLhs, s.getLExpr())) {
                        sharesToAdd.add(new Share(newLhs, s.getLExpr()));
                        MethodRefMapping mapping = methodChange.getMethodRefMapping(s.getLExpr());
                        HashSet<MetaRefCon> metarefCons;
                        if (mapping != null) {
                            metarefCons = mapping.getMetaRefCons();
                        } else {
                            HashSet<MetaRefCon> rexprMrcs = new HashSet<MetaRefCon>(0);
                            metarefCons = rexprMrcs;
                        }
                        if (!newLhs.equalTo(newLExpr)) {
                            sharesToAdd.add(new Share(newLhs, newLhs));
                            methodChange.updateRefMapping(newLhs, metarefCons);
                        }
                    }
                }
            }
        }
        shareRel.addShares(sharesToAdd);





        MSafeSuperClass newClass = SCJmSafeChecker.SCJmSafePROGRAM.getClass(className);
        if (newClass != null) {
            ArrayList<Method> newClassConstrs = newClass.getConstr(params);
            if (newClassConstrs.size() > 0) {
                applyPossibleMethods(m, newClassConstrs, params, properties, currentExpr, lexpr, mrc);
            } else {
//                System.out.println("[WARNING] - No constructor found for instantiation of new object");
            }
        } else {
//            System.out.println("[WARNING] - Cannot find associated class with new instantiation");
        }

//        for (MethodProperty property : properties.getProperties()) {
//            if(!property.isComplete() && newLExpr.getExpressionString().equals("Result")) {
//                completedPropertys.add(property.clone());
//                property.complete();
//            }
//        }

//        CheckingUtil.checkMethodPropertiesSafety(properties);

    }



    ///////////////////////////////////////////////
    ////              MethodCall               ////
    ///////////////////////////////////////////////

    public static ArrayList<Method> getMethodsFromSignatures(ArrayList<MethodSig> signatures) {
        ArrayList<Method> result = new ArrayList<Method>(0);
        for (MethodSig sig : signatures) {
            for (MethodDepsContainer mdc : analysedMethods) {
                if (sig.getName().equals(mdc.getMethod().getName()) && sig.getClassName().equals(mdc.getMSafeClass().getName()) && sig.getParamTypes().size() == mdc.getMethod().getParamTypesNoResult().size()) {
                    boolean flag = true;
                    for (int i =0; i < sig.getParamTypes().size(); i++) {
                        if (!sig.getParamTypes().get(i).equals(mdc.getMethod().getParamTypes().get(i))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        result.add(mdc.getMethod());
/*
                        System.out.print("Found possible method associated with MethodSig - " + sig.getName() + "(");
                        for (int i = 0; i < mdc.getMethod().getParamTypes().size(); i++) {
                            System.out.print(mdc.getMethod().getParamTypes().get(i));
                            if (i < mdc.getMethod().getParamTypes().size() - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.print(")" + " in class " + sig.getClassName() + " with properties:\n");
                        printMethodProperties(mdc.getMethod().getProperties());
*/
                    }
                }
            }
        }
        return result;
    }

    public static void applyPossibleMethods(Method method, ArrayList<Method> methods, ArrayList<Expr> args, MethodProperties properties, Expr currentExpr, Expr lexpr, MetaRefCon mrc) {
        Expr newCurrentExpr = EnvUtil.mergeExprs(currentExpr, lexpr);
        ArrayList<Expr> vars = new ArrayList<Expr>(0);
        if (methods.size() > 0) {
//            ArrayList<MethodProperties> propertiesList = new ArrayList<MethodProperties>(0);
            for (Method m : methods) {
//                MethodProperties clone = properties.clone();
                MethodProperties updatedMethodProperties = m.getProperties().clone();
//                System.out.println("Method properties to apply (before arg/param swap):");
//                printMethodProperties(updatedMethodProperties);
                swapParamsWithArgs(m, args, updatedMethodProperties);
//                System.out.println("Method properties to apply (after arg/param swap):");
//                printMethodProperties(updatedMethodProperties);
                ArrayList<Expr> fields = m.getVisibleFields();
                updateOutOfScopeDecs(method, m, newCurrentExpr);
                applyMethodProperties(method, updatedMethodProperties, properties, args, fields, currentExpr, lexpr, mrc);
//                propertiesList.add(clone);
                vars.addAll(m.getVarsToRemove());
//                CheckingUtil.checkMethodPropertiesSafetyMethod(properties, m, mrc, newCurrentExpr);
            }
            CheckingUtil.checkMethodPropertiesSafetyMethod(properties, vars, mrc, newCurrentExpr);
//            properties.setProperties(propertiesList.get(0).getProperties());
//            for (int i = 1; i < propertiesList.size(); i++) {
//                for (MethodProperty p : propertiesList.get(i).getProperties()) {
//                    properties.addProperty(p);
//                }
//            }
//            simplifyMethodProperties(properties);
//            CheckingUtil.checkMethodPropertySafety(properties);
        }
    }

    public static void swapParamsWithArgs(Method m, ArrayList<Expr> args, MethodProperties properties) {
        for (int i = 0; i < args.size(); i++) {
            Expr arg = args.get(i);
            Variable param = m.getParams().get(i);
//            for (MethodProperty property : properties.getProperties()) {
                ShareRelation shareRelation = properties.getMethodShareChange();
                MethodRefChange refChange = properties.getMethodRefChange();
                for (Share s : shareRelation.getShares()) {
                    s.setLExpr(updateParamWithArg(s.getLExpr(), param, arg));
                    s.setRExpr(updateParamWithArg(s.getRExpr(), param, arg));
                }
                for (MethodRefMapping map : refChange.getMethodRefs()) {
                    map.setExpr(updateParamWithArg(map.getExpr(), param, arg));
                    for (MetaRefCon mrc : map.getMetaRefCons()) {
                        if (mrc instanceof Erc) {
                            Erc erc = (Erc) mrc;
                            erc.setExpr(updateParamWithArg(erc.getExpr(), param, arg));
                        }
                    }
                }
//            }
        }
    }


    public static Expr updateParamWithArg(Expr compare, Variable param, Expr arg) {
        if (compare instanceof Variable && compare.equalTo(param)) {
//            System.out.println("Swapped parameter " + param.getExpressionString() + " with argument " + arg.getExpressionString() + " in expression " + compare.getExpressionString() + " (1)");
            return arg;
        } else if (compare instanceof FieldAccess) {
            FieldAccess fa = (FieldAccess) compare;
            if (fa.getFirst().equalTo(param)) {
                if (arg instanceof Identifier) {
                    Identifier iden = (Identifier) arg;
                    FieldAccess newFA = new FieldAccess();
                    for (int i = 0; i < fa.getElements().size(); i++) {
                        newFA.addElement(fa.getElements().get(i));
                    }
                    newFA.setElement(iden, 0);
//                    System.out.println("Swapped parameter " + param.getExpressionString() + " with argument " + arg.getExpressionString() + " in expression " + compare.getExpressionString() + " (2)");
                    return newFA;
                } else if (arg instanceof FieldAccess) {
                    FieldAccess argFA = (FieldAccess) arg;
                    FieldAccess newFA = new FieldAccess();
                    for (int i = 0; i < argFA.getElements().size(); i++) {
                        newFA.addElement(argFA.getElements().get(i));
                    }
                    for (int i = 1; i < fa.getElements().size(); i++) {
                        newFA.addElement(fa.getElements().get(i));
                    }
//                    System.out.println("Swapped parameter " + param.getExpressionString() + " with argument " + arg.getExpressionString() + " in expression " + compare.getExpressionString() + " (3)");
                    return newFA;
                } else {
//                    System.out.println("Unable to swap param " + param.getExpressionString() + " with arg " + arg.getExpressionString() + " in expression " + compare.getExpressionString());
                    return compare;
                }
            } else {
//                System.out.println("Unable to swap param " + param.getExpressionString() + " with arg " + arg.getExpressionString() + " in expression " + compare.getExpressionString());
                return compare;
            }
        } else {
//            System.out.println("Unable to swap param " + param.getExpressionString() + " with arg " + arg.getExpressionString() + " in expression " + compare.getExpressionString());
            return compare;
        }
    }

/*
    public static void applyMethodProperties(Method m, MethodProperties toApply, MethodProperties properties, ArrayList<Expr> args, ArrayList<Expr> fields, Expr currentExpr, Expr lexpr, MetaRefCon mrc) {
        ArrayList<MethodProperty> propertyList = new ArrayList<MethodProperty>(0);
        for (MethodProperty existingP : properties.getProperties()) {
            if (!existingP.isComplete()) {
                for (MethodProperty mp : toApply.getProperties()) {
                    MethodProperty clone = existingP.clone();
                    MethodProperty mpClone = mp.clone();
                    applyPropertyToProperty(m, mpClone, clone, args, fields, currentExpr, lexpr, mrc);
                    propertyList.add(clone);
                }
            } else {
                propertyList.add(existingP);
            }
        }
        properties.setProperties(propertyList);
    }
*/

    public static void applyMethodProperties(Method m, MethodProperties toApply, MethodProperties properties, ArrayList<Expr> args, ArrayList<Expr> fields, Expr currentExpr, Expr lexpr, MetaRefCon mrc) {
//        ArrayList<MethodProperty> propertyList = new ArrayList<MethodProperty>(0);
//        for (MethodProperty mp : toApply.getProperties()) {
//            MethodProperty mpClone = mp.clone();
            MethodProperties toApplyClone = toApply.clone();
            ArrayList<Expr> equalExpr = updateToApplyProperties(toApplyClone, args, fields, currentExpr, lexpr);
//            for (MethodProperty existingP : properties.getProperties()) {
//                if (!existingP.isComplete()) {
//                    MethodProperty clone = existingP.clone();
                    applyPropertiesToProperties(m, toApplyClone, properties, args, fields, currentExpr, lexpr, mrc, equalExpr);
//                    propertyList.add(clone);
//                } else {
//                    propertyList.add(existingP);
//                }
//            }
        }
//        properties.setProperties(propertyList);
//    }

    public static ArrayList<Expr> updateToApplyProperties(MethodProperties toApply, ArrayList<Expr> args, ArrayList<Expr> fields, Expr currentExpr, Expr lexpr) {

        boolean print = false;

        ShareRelation newShareRel = toApply.getMethodShareChange();
        MethodRefChange newRefChange = toApply.getMethodRefChange();

        ArrayList<Expr> equalExpr = new ArrayList<Expr>(0);

        if (print) {
            System.out.println("To Apply:");
            printMethodProperties(toApply);
            if (currentExpr != null) {
                System.out.println("currentExpr = " + currentExpr.getExpressionString());
            }
            if (lexpr != null) {
                System.out.println("lexpr = " + lexpr.getExpressionString());
            }
        }

        for (Share newS : newShareRel.getShares()) {
 
            boolean found = false;
            for (Expr e : args) {
//                if (EnvUtil.getFirstOfExpr(newS.getLExpr()).equalTo(e)) { // TODO - FirstOfExpr or prefix of?
                if (EnvUtil.exprPrefixOfFieldAccess(e, newS.getLExpr())) {
                    found = true;
                    break;
                }
            }
            for (Expr e : fields) {
                if (EnvUtil.getFirstOfExpr(newS.getLExpr()).equalTo(e)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                newS.setLExpr(EnvUtil.mergeExprs(EnvUtil.mergeExprs(currentExpr, lexpr), newS.getLExpr()));
            } else {
                newS.setLExpr(EnvUtil.mergeExprs(EnvUtil.mergeExprs(currentExpr, EnvUtil.getFrontOfExpr(lexpr)), newS.getLExpr()));
            }
            found = false;
            for (Expr e : args) {
//                if (EnvUtil.getFirstOfExpr(newS.getRExpr()).equalTo(e)) {
                if (EnvUtil.exprPrefixOfFieldAccess(e, newS.getRExpr())) {
                    found = true;
                    break;
                }
            }
            for (Expr e : fields) {
                if (EnvUtil.getFirstOfExpr(newS.getRExpr()).equalTo(e)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                newS.setRExpr(EnvUtil.mergeExprs(EnvUtil.mergeExprs(currentExpr, lexpr), newS.getRExpr()));
            } else {
                newS.setRExpr(EnvUtil.mergeExprs(EnvUtil.mergeExprs(currentExpr, EnvUtil.getFrontOfExpr(lexpr)), newS.getRExpr()));
            }

           boolean analyseEqual = false;
            if (!newS.getLExpr().equalTo(newS.getRExpr())) {
                if (newS.getLExpr() instanceof FieldAccess || newS.getLExpr() instanceof ArrayElement) {
                    analyseEqual = true;
                }
            }

            if (analyseEqual) {
                equalExpr.add(newS.getLExpr());
            }
        }

        for (MethodRefMapping mrp : newRefChange.getMethodRefs()) {
            boolean found = false;
            for (Expr e : args) {
//                if (EnvUtil.getFirstOfExpr(mrp.getExpr()).equalTo(e)) {
                if (EnvUtil.exprPrefixOfFieldAccess(e, mrp.getExpr())) {
                    found = true;
                    break;
                }
            }
            for (Expr e : fields) {
//                if (EnvUtil.getFirstOfExpr(mrp.getExpr()).equalTo(e)) {
                if (EnvUtil.exprPrefixOfFieldAccess(e, mrp.getExpr())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                mrp.setExpr(EnvUtil.mergeExprs(EnvUtil.mergeExprs(currentExpr, lexpr), mrp.getExpr()));
            } else {
                mrp.setExpr(EnvUtil.mergeExprs(EnvUtil.mergeExprs(currentExpr, EnvUtil.getFrontOfExpr(lexpr)), mrp.getExpr()));
            }
        }

        for (MethodRefMapping mrp : newRefChange.getMethodRefs()) {
            for (MetaRefCon mrc : mrp.getMetaRefCons()) {
                if (mrc instanceof Erc) {
                    Erc erc = (Erc) mrc;
                    boolean found = false;
                    for (Expr e : args) {
//                        if (EnvUtil.getFirstOfExpr(erc.getExpr()).equalTo(e)) {
                        if (EnvUtil.exprPrefixOfFieldAccess(e, erc.getExpr())) {
                            found = true;
                            break;
                        }
                    }
                    for (Expr e : fields) {
//                        if (EnvUtil.getFirstOfExpr(erc.getExpr()).equalTo(e)) {
                        if (EnvUtil.exprPrefixOfFieldAccess(e, erc.getExpr())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found && erc.getExpr() instanceof This) {
                        if (currentExpr != null || lexpr != null) {
                            erc.setExpr(EnvUtil.mergeExprs(currentExpr, lexpr));
                        }
                    } else if (!found) {
                        erc.setExpr(EnvUtil.mergeExprs(EnvUtil.mergeExprs(currentExpr, lexpr), erc.getExpr()));
                    } else {
                        erc.setExpr(EnvUtil.mergeExprs(EnvUtil.mergeExprs(currentExpr, EnvUtil.getFrontOfExpr(lexpr)), erc.getExpr()));
                    }
                }
            }
        }

        if (print) {
            System.out.println("To Apply Updated:");
            printMethodProperties(toApply);
        }

        return equalExpr;
    }

    public static void applyPropertiesToProperties(Method m, MethodProperties toApply, MethodProperties properties, ArrayList<Expr> args, ArrayList<Expr> fields, Expr currentExpr, Expr lexpr, MetaRefCon currentMrc, ArrayList<Expr> equalExpr) {

        boolean print = false;

        if (print) {
            System.out.println("Properties:");
            printMethodProperties(properties);
        }

        ShareRelation newShareRel = toApply.getMethodShareChange();
        MethodRefChange newRefChange = toApply.getMethodRefChange();

        ShareRelation existingShareRel = properties.getMethodShareChange();
        MethodRefChange existingRefChange = properties.getMethodRefChange();

//        ArrayList<Expr> equalExpr = new ArrayList<Expr>(0);
        ArrayList<ArrayList<Expr>> equalExprs = new ArrayList<ArrayList<Expr>>(0);

        for (Expr e : equalExpr) {
            ArrayList<Expr> temp = new ArrayList<Expr>(0);                
            for (Share existingS : existingShareRel.getShares()) {
                if (e instanceof FieldAccess) {
                    if (EnvUtil.getFrontOfExpr(e).equalTo(existingS.getLExpr()) && !existingS.getLExpr().equalTo(existingS.getRExpr())) {
    //                        System.out.println("MethodCall - LE : " + newS.getLExpr().getExpressionString() + " is equal to " + existingS.getRExpr().getExpressionString());
    //                    temp.add(existingS.getRExpr());
                        addToEqualExprs(temp, existingS.getRExpr());
                    } else if (EnvUtil.getFrontOfExpr(e).equalTo(existingS.getRExpr()) && !existingS.getLExpr().equalTo(existingS.getRExpr())) {
    //                        System.out.println("MethodCall - LE : " + newS.getLExpr().getExpressionString() + " is equal to " + existingS.getLExpr().getExpressionString());
    //                    temp.add(existingS.getLExpr());
                        addToEqualExprs(temp, existingS.getLExpr());
                    }
                }
            }
            equalExprs.add(temp);
        }

//        for (Share newS : newShareRel.getShares()) {
//            if (newS.getLExpr() != null) {
//                existingShareRel.removeExprFromShares(newS.getLExpr());
//            }
//        }

        for (Share newS : newShareRel.getShares()) {
            if (newS.getLExpr() != null && newS.getRExpr() != null) {
                Share updatedShare = new Share(newS.getLExpr(), newS.getRExpr());
                existingShareRel.addShare(updatedShare);
            }
            for (int i = 0; i < equalExpr.size(); i++) {
                if (equalExpr.get(i).equalTo(newS.getLExpr())) {
                    for (Expr e : equalExprs.get(i)) {
                        Share newShare = new Share(newS.getLExpr(), e);
                        existingShareRel.addShare(newShare);
//                        newShare = new Share(e, newS.getRExpr());
//                        existingShareRel.addShare(newShare);
                        newShare = new Share(EnvUtil.sortShareAssignmentExpression(e, newS.getRExpr(), newS.getLExpr()), newS.getRExpr());
                        existingShareRel.addShare(newShare);
                    }
                }
            }
        }

//        for (MethodRefMapping mrp : newRefChange.getMethodRefs()) {
//            if (mrp.getExpr() != null) {
//                existingRefChange.removeExprFromRefSet(mrp.getExpr());
//            }
//        }

        for (MethodRefMapping mrp : newRefChange.getMethodRefs()) {
            if (mrp.getExpr() != null) {
                HashSet<MetaRefCon> updatedMetaRefCons = new HashSet<MetaRefCon>(0);
                for (MetaRefCon mrc : mrp.getMetaRefCons()) {
//                    updatedMetaRefCons.addAll(analyseMetaRefCon(mrc, currentMrc, properties));
                    for (MetaRefCon mrc1 : analyseMetaRefCon(mrc, currentMrc, properties)) {
                        boolean found = false;
                        for (MetaRefCon mrc2 : updatedMetaRefCons) {
                            if (mrc2.getMetaRefConString().equals(mrc1.getMetaRefConString())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            updatedMetaRefCons.add(mrc1);
                        }
                    }
                }
//                MethodRefMapping updatedRefMapping = new MethodRefMapping(mrp.getExpr(), updatedMetaRefCons);
//                existingRefChange.updateRefMapping(updatedRefMapping);
                existingRefChange.updateRefMapping(mrp.getExpr(), updatedMetaRefCons);
                for (int i = 0; i < equalExpr.size(); i++) {
                    if (equalExpr.get(i).equalTo(mrp.getExpr())) {
                        for (Expr e : equalExprs.get(i)) {
//                            existingRefChange.removeExprFromRefSet(e);
//                            existingRefChange.updateRefMapping(new MethodRefMapping(e, updatedMetaRefCons));
                            existingRefChange.updateRefMapping(e, updatedMetaRefCons);
                        }
                    }
                }
            }
        }

/*
        // Add an empty entry to the refset for each share component if one doesn't exist.
        for (Share s : newShareRel.getShares()) {
            Expr e = s.getLExpr();
            boolean found = false;
            for (MethodRefMapping mrp : existingRefChange.getMethodRefs()) {
                if (mrp.getExpr().equalTo(e)) {
                    found = true;
                }
            }
            if (!found) {
                HashSet<MetaRefCon> metaRefCons = new HashSet<MetaRefCon>(0);
                MethodRefMapping newRefMapping = new MethodRefMapping(e, metaRefCons);
                existingRefChange.updateRefMapping(newRefMapping);
            }

            e = s.getRExpr();
            found = false;
            for (MethodRefMapping mrp : existingRefChange.getMethodRefs()) {
                if (mrp.getExpr().equalTo(e)) {
                    found = true;
                }
            }
            if (!found) {
                HashSet<MetaRefCon> metaRefCons = new HashSet<MetaRefCon>(0);
                MethodRefMapping newRefMapping = new MethodRefMapping(e, metaRefCons);
                existingRefChange.updateRefMapping(newRefMapping);
            }
        }
*/
        // TODO sharesToAdd needs a function to add elements... could be growing rapidly...?

        HashSet<Share> sharesToAdd = new HashSet<Share>(0);
        HashSet<MethodRefMapping> refsToUpdate = new HashSet<MethodRefMapping>(0);
        for (Share s1 : newShareRel.getShares()) {
//            System.out.println("Analysing new share " + s1.getShareString());
            // If the lhs and rhs of the share are different:
            if (!s1.getLExpr().equalTo(s1.getRExpr())) {
                // Find the corresponding expr in the equalExpr list:
                for (int i = 0; i < equalExpr.size(); i++) {
                    if (equalExpr.get(i).equalTo(s1.getLExpr())) {
                        // If found, add it to the list of expressions that it is equal to
//                        equalExprs.get(i).add(s1.getLExpr());
                        addToEqualExprs(equalExprs.get(i), s1.getLExpr());
                        // Iterate over all the expressions that reference the same object as the lhs of the new share:
                        for (Expr e : equalExprs.get(i)) {
//                            System.out.println("  - Analysing for expr " + e.getExpressionString() + ", which is equal to " + s1.getLExpr().getExpressionString());
                            // Analyse all other existing shares:
                            for (Share s2 : existingShareRel.getShares()) {
//                                System.out.println("    - Analysing existing share " + s2.getShareString());
                                // If the existing share is a prefix of the new share:
                                if (EnvUtil.exprPrefixOfFieldAccess(s1.getRExpr(), s2.getLExpr())) {
                                    // Get the new left expression:
                                    Expr newLhs = EnvUtil.sortShareAssignmentExpression(e, s1.getRExpr(), s2.getLExpr());
                                    if (newLhs != null) {
//                                        System.out.println("      - Existing is a prefix of new, newLhs = " + newLhs.getExpressionString());
//                                        sharesToAdd.add(new Share(newLhs, s2.getRExpr()));
                                        addToShareSet(sharesToAdd, new Share(newLhs, s2.getRExpr()));
//                                        sharesToAdd.add(new Share(s2.getRExpr(), newLhs));
                                        MethodRefMapping mapping = existingRefChange.getMethodRefMapping(s2.getRExpr());
                                        MethodRefMapping newMapping;
                                        if (mapping != null) {
                                            newMapping = new MethodRefMapping(newLhs, mapping.getMetaRefCons());
                                        } else {
                                            HashSet<MetaRefCon> rexprMrcs = new HashSet<MetaRefCon>(0);
                                            rexprMrcs.add(new Erc(s2.getRExpr()));
                                            newMapping = new MethodRefMapping(newLhs, rexprMrcs);
                                        }
                                        if (!newLhs.equalTo(s1.getLExpr())) {
//                                            sharesToAdd.add(new Share(newLhs, newLhs));   // REMOVED
                                            refsToUpdate.add(newMapping);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            } else {
                // NewShare is the same on both sides (ie. an instantiation or declaration)
                // TODO - Also check that when updating the method properties with exprs that are to be removed that we don't remove information that was there before hand - we don't handle this properly in the implementation, but this is fine, because in the coding practice it states that you shouldn't reassign parameters in methods.
                for (Share s2 : existingShareRel.getShares()) {
//                    System.out.println("    - Analysing existing share " + s2.getShareString());
                    // If the existing share is a prefix of the new share:
                    Expr newField = null;
                    if (EnvUtil.exprPrefixOfFieldAccess(s2.getLExpr(), s1.getLExpr())) {
//                        System.out.println("      - Existing share lhs " + s2.getLExpr().getExpressionString() + " is a prefix of new " + s1.getLExpr().getExpressionString());
                        newField = EnvUtil.sortShareAssignmentExpression(s2.getRExpr(), s2.getLExpr(), s1.getLExpr());
//                        System.out.println("        - newExpr = " + newField.getExpressionString());
                    } else if (EnvUtil.exprPrefixOfFieldAccess(s2.getRExpr(), s1.getLExpr())) {
//                        System.out.println("      - Existing share rhs " + s2.getRExpr().getExpressionString() + " is a prefix of new " + s1.getLExpr().getExpressionString());
                        newField = EnvUtil.sortShareAssignmentExpression(s2.getLExpr(), s2.getRExpr(), s1.getLExpr());
//                        System.out.println("        - newExpr = " + newField.getExpressionString());
                    }
                    if (newField != null) {

//                        sharesToAdd.add(new Share(newField, newField));   // REMOVED
//                        sharesToAdd.add(new Share(s1.getLExpr(), newField));
                        addToShareSet(sharesToAdd, new Share(s1.getLExpr(), newField));

                        MethodRefMapping mapping = existingRefChange.getMethodRefMapping(s1.getLExpr());
                        MethodRefMapping newMapping;
                        if (mapping != null) {
                            newMapping = new MethodRefMapping(newField, mapping.getMetaRefCons());
                            refsToUpdate.add(newMapping);
                        } else {
                            HashSet<MetaRefCon> rexprMrcs = new HashSet<MetaRefCon>(0);
                            rexprMrcs.add(new Erc(s1.getLExpr()));
                            newMapping = new MethodRefMapping(newField, rexprMrcs);
                            refsToUpdate.add(newMapping);
                        }
//                        if (!newField.equalTo(s1.getLExpr())) {
//                            sharesToAdd.add(new Share(newField, newField));
//                            refsToUpdate.add(newMapping);
//                        }
                    }
                }
            }
        }

        existingShareRel.addShares(sharesToAdd);
        existingRefChange.updateRefMappings(refsToUpdate);

        if (print) {
            System.out.println("Properties Updated:");
            printMethodProperties(properties);
        }
    }

    public static void addToShareSet(HashSet<Share> shares, Share s) {
        boolean found = false;
        for (Share s1 : shares) {
            if (s.equalTo(s1)) {
                found = true;
                break;
            }
        }
        if (!found) {
            shares.add(s);
        }
    }

    public static void analyseGetMemoryArea(Method m, MethodProperties properties, GetMemoryArea getMemoryArea, Expr currentExpr) {
        Expr ref = getMemoryArea.getRef();
        if (ref instanceof Variable) {
            Variable v1 = (Variable) ref;
            for (Variable v2 : m.getParams()) {
                if (v1.equalTo(v2) && !v1.getExpressionString().equals("Result")) {
                    toRemoveFromMethod.add(v1);
                }
            }
        } else {
            for (Expr e : toRemoveFromMethod) {
                if (EnvUtil.exprPrefixOfFieldAccess(e, ref)) {
                    toRemoveFromMethod.add(ref);
                }
            }
        }
        if (ref != null) {
            Expr expr = getMemoryArea.getExpr();
            Expr newRef = EnvUtil.mergeExprs(currentExpr, ref);
            Expr newExpr = EnvUtil.mergeExprs(currentExpr, expr);
            Erc erc = new Erc(newExpr);
//            for (MethodProperty property : properties.getProperties()) {
//                ShareRelation shareRel = property.getMethodShareChange();
//                shareRel.removeExprFromShares(newRef);
//                shareRel.addShare(new Share(newRef, newRef));   // REMOVED

                MethodRefChange refChange = properties.getMethodRefChange();
//                refChange.removeExprFromRefSet(newRef);
                HashSet<MetaRefCon> metaRefCons = new HashSet<MetaRefCon>(0);
                metaRefCons.add(erc);
//                MethodRefMapping refMapping = new MethodRefMapping(newRef, metaRefCons);
//                refChange.updateRefMapping(refMapping);
                refChange.updateRefMapping(newRef, metaRefCons);
//            }
        }
    }




    ///////////////////////////////////////////////
    ////           Add / Remove / Join         ////
    ///////////////////////////////////////////////


    public static void propertiesRemove(MethodProperties properties, Expr expr) {
        propertiesShareRelationRemove(properties, expr);
        propertiesRefSetRemove(properties, expr);
    }

    public static void propertiesShareRelationRemove(MethodProperties properties, Expr expr) {
//        for (MethodProperty property : properties.getProperties()) {
//            if (!property.isComplete()) {
                ShareRelation shareRelation = properties.getMethodShareChange();
                HashSet<Expr> matchingExprs = shareRelation.matchingExprsInShareRelation(expr);
                for (Expr e : matchingExprs) {
                    shareRelation.removeExprFromShares(e);
                }
//            }
//        }
    }

    public static void propertiesRefSetRemove(MethodProperties properties, Expr expr) {
//        for (MethodProperty property : properties.getProperties()) {
//            if (!property.isComplete()) {
                MethodRefChange refChange = properties.getMethodRefChange();
                HashSet<Expr> matchingExprs = refChange.matchingExprsInRefSet(expr);
                for (Expr e : matchingExprs) {
                    refChange.removeExprFromRefSet(e);
                }
//            }
//        }
    }


    private static void distMethodPropertiesJoin(Method m, MethodProperties properties, ArrayList<Command> coms, Expr currentExpr, MetaRefCon mrc) {
//        ArrayList<MethodProperties> propertiesList = new ArrayList<MethodProperties>(0);
        for (Command c : coms) {
//            MethodProperties clone = properties.clone();
            analyseCommand(m, properties, c, currentExpr, mrc);
//            propertiesList.add(clone);
        }
//        MethodProperties result = propertiesList.get(0);
//        for (int i = 1; i < propertiesList.size(); i++) {
//            result = methodPropertiesJoin(result, propertiesList.get(i));
//        }
//        properties.setProperties(result.getProperties());
    }

    private static MethodProperties methodPropertiesJoin(MethodProperties properties1, MethodProperties properties2) {
        MethodProperties result = properties1;
        result.getMethodShareChange().addShares(properties2.getMethodShareChange().getShares());
        result.getMethodRefChange().updateRefMappings(properties2.getMethodRefChange().getMethodRefs());
        return result;
    }


    public static MethodRefChange distMethodRefChangeJoin(ArrayList<MethodRefChange> refChanges) {
        MethodRefChange temp = null;;
        if (refChanges.size() > 0) {
            temp = refChanges.get(0);
            for (int i = 1; i < refChanges.size(); i++) {
                temp = methodRefChangeJoin(temp, refChanges.get(i));
            }
        } else {
            System.out.println("[ERROR] - No MethodRefChanges to join");
        }
        return temp;
    }

    public static MethodRefChange methodRefChangeJoin(MethodRefChange mrc1, MethodRefChange mrc2) {
        HashSet<Expr> allExprs = mrc1.getExprs();
        allExprs.addAll(mrc2.getExprs());
        MethodRefChange result = new MethodRefChange();
        for (Expr e : allExprs) {
            HashSet<MetaRefCon> mrcs1 = new HashSet<MetaRefCon>(0);
            if (mrc1.getMethodRefMapping(e) != null) {
                mrcs1 = mrc1.getMethodRefMapping(e).getMetaRefCons();
            }
            HashSet<MetaRefCon> mrcs2 = new HashSet<MetaRefCon>(0);
            if (mrc2.getMethodRefMapping(e) != null) {
                mrcs2 = mrc2.getMethodRefMapping(e).getMetaRefCons();
            }
            HashSet<MetaRefCon> mergedMetaRefCons = mergeMetaRefCons(mrcs1, mrcs2);
            MethodRefMapping newMapping = new MethodRefMapping(e, mergedMetaRefCons);
            result.updateRefMapping(newMapping);
        }
        return result;
    }

    public static HashSet<MetaRefCon> mergeMetaRefCons(HashSet<MetaRefCon> mrcs1, HashSet<MetaRefCon> mrcs2) {
        HashSet<MetaRefCon> result = new HashSet<MetaRefCon>(0);
        for (MetaRefCon mrc1 : mrcs1) {
            boolean match = false;
            for (MetaRefCon mrc2 : mrcs2) {
                if (mrc1.equalTo(mrc2)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                result.add(mrc1);
            }
        }
        result.addAll(mrcs2);
        return result;
    }


    ///////////////////////////////////////////////
    ////              MetaRefCon               ////
    ///////////////////////////////////////////////

    public static MetaRefCon lowerMetaRefCon(MetaRefCon currentMrc) {

        MetaRefCon result;

        if (currentMrc instanceof CurrentPlus) {
            CurrentPlus currentPlus = (CurrentPlus) currentMrc;
            int depth = currentPlus.getDepth();
            if (depth > 0) {
                result = new CurrentPlus(depth - 1);
            } else {
                result = new Current();
            }

        } else if (currentMrc instanceof Current) {
            result = new CurrentPrivate(0);

        } else if (currentMrc instanceof CurrentPrivate) {
            CurrentPrivate currentPrivate = (CurrentPrivate) currentMrc;
            int depth = currentPrivate.getDepth();
            result = new CurrentPrivate(depth + 1);

        } else {
            System.out.println("[ERROR] - Unable to determine lower MetaRefCon");
            result = null;
        }
        return result;
    }

    public static MetaRefCon raiseMetaRefCon(MetaRefCon currentMrc) {

        MetaRefCon result;

        if (currentMrc instanceof CurrentPlus) {
            CurrentPlus currentPlus = (CurrentPlus) currentMrc;
            int depth = currentPlus.getDepth();
            result = new CurrentPlus(depth + 1);

        } else if (currentMrc instanceof Current) {
            result = new CurrentPlus(0);

        } else if (currentMrc instanceof CurrentPrivate) {
            CurrentPrivate currentPrivate = (CurrentPrivate) currentMrc;
            int depth = currentPrivate.getDepth();
            if (depth > 0) {
                result = new CurrentPrivate(depth - 1);
            } else {
                result = new Current();
            }

        } else {
            System.out.println("[ERROR] - Unable to determine higher MetaRefCon");
            result = null;
        }
        return result;
    }

    public static HashSet<MetaRefCon> analyseMetaRefCon(MetaRefCon NImrc, MetaRefCon currentMrc, MethodProperties properties) {
        HashSet<MetaRefCon> result = new HashSet<MetaRefCon>(0);
        if (NImrc instanceof CurrentPlus) {
            CurrentPlus currentPlus = (CurrentPlus) NImrc;
            int depth = currentPlus.getDepth();
            if (currentMrc instanceof CurrentPlus) {
                CurrentPlus currentPlus2 = (CurrentPlus) currentMrc;
                result.add(new CurrentPlus(depth + currentPlus2.getDepth()));
            } else if (currentMrc instanceof Current) {
                result.add(new CurrentPrivate(depth));
            } else if (currentMrc instanceof CurrentPrivate) {
                CurrentPrivate currentPrivate = (CurrentPrivate) currentMrc;
                int temp = depth - currentPrivate.getDepth();
                if (temp > 0) {
                    result.add(new CurrentPlus(temp));
                } else if (temp < 0) {
                    result.add(new CurrentPrivate(temp));
                } else {
                    result.add(new Current());
                }
            } else {
                System.out.println("[ERROR] - Unable to determine the appropriate MetaRefCon for the NewInstance command");
            }

        } else if (NImrc instanceof Current) {
            result.add(currentMrc);

        } else if (NImrc instanceof CurrentPrivate) {
            CurrentPrivate currentPrivate = (CurrentPrivate) NImrc;
            int depth = currentPrivate.getDepth();
            if (currentMrc instanceof CurrentPlus) {
                CurrentPlus currentPlus = (CurrentPlus) currentMrc;
                int temp = currentPlus.getDepth() - depth;
                if (temp > 0) {
                    result.add(new CurrentPlus(temp));
                } else if (temp < 0) {
                    result.add(new CurrentPrivate(temp));
                } else {
                    result.add(new Current());
                }
            } else if (currentMrc instanceof Current) {
                result.add(new CurrentPrivate(depth));
            } else if (currentMrc instanceof CurrentPrivate) {
                CurrentPrivate currentPrivate2 = (CurrentPrivate) currentMrc;
                result.add(new CurrentPrivate(currentPrivate2.getDepth() - depth));
            } else {
                System.out.println("[ERROR] - Unable to determine the appropriate MetaRefCon for the NewInstance command");
            }

        } else if (NImrc instanceof Erc) {
            Erc erc = (Erc) NImrc;
            Expr e = erc.getExpr();
            MethodRefChange refChange = properties.getMethodRefChange();
            MethodRefMapping mapping = refChange.getMethodRefMapping(e);
            if (mapping != null) {
                result = mapping.getMetaRefCons();
            } else {
                result.add(NImrc);
            }

        }
        return result;
    }


    ///////////////////////////////////////////////
    ////               Printing                ////
    ///////////////////////////////////////////////

    public static void printMethodProperties(MethodProperties properties) {
        System.out.println("Method properties print-out....");
        ShareRelation methodShareChange = properties.getMethodShareChange();
        MethodRefChange methodRefChange = properties.getMethodRefChange();
        methodShareChange.printShareRel();
        System.out.println("      -> ");
        methodRefChange.printMethodRefSet();
        System.out.println();
    }




}
