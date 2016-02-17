package hijac.tools.scjmsafe.translation;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import hijac.tools.scjmsafe.translation.SCJmSafeTranslator;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;

// Specific visitor for classes that analyses each part of a class
class MissionComponentVisitor extends SimpleTreeVisitor<Void, Void> {

        MSafeMission mission;

        public MissionComponentVisitor(MSafeMission param) {
            mission = param;
        }

        // Extracts the fields of the class
        @Override
        public Void visitVariable(VariableTree vt, Void p) {
            VariableVisitor visitor = new VariableVisitor();
            Command var = vt.accept(visitor, null);
            if (var instanceof Declaration) {
                mission.addField((Declaration) var);
            } else if (var instanceof Sequence) {
                mission.addField((Declaration) ((Sequence) var).getC1());
                mission.addInit(((Sequence) var).getC2());
            }
            return super.visitVariable(vt, null);
        }

        // Extracts the methods of the class
        @Override
        public Void visitMethod(MethodTree mt, Void p) {
            String methodName = mt.getName().toString();
            BlockTree methodBody = mt.getBody();
            Tree returnType = mt.getReturnType();

            @SuppressWarnings("unchecked")  // Casting mt.getParameters(), which is of type "? extends List<VariableTree>", to List<VariableTree> presents a warning at compile time
            List<VariableTree> parameters = (List<VariableTree>) mt.getParameters();
            
            switch(methodName) {
                case "<init>" : { // Constructor
                    System.out.println("Mission constructor found");
//                    mission.addConstr(Util.getBlockCommand(methodBody));
                    Method method = Util.newConstr(mission.getName(), methodBody, parameters);
                    mission.addConstr(method);
                    System.out.println(); 
                    break;
                }
                case "initialize" : {
                    System.out.println("Mission initialize method found");
                    mission.addInitialize(Util.getBlockCommand(methodBody));
                    System.out.println();
                    break;
                }
                case "cleanUp" :
                case "cleanup" : {
                    System.out.println("Mission cleanUp method found");
                    mission.addCleanUp(Util.getBlockCommand(methodBody));
                    System.out.println();
                    break;
                }
                default : {
                    Method method = Util.newDefaultMethod(methodName, returnType, methodBody, parameters);
                    mission.addMethod(method);
                }
            }

            return super.visitMethod(mt, p);
        }

        @Override
        public Void defaultAction(Tree t, Void p) {
            return super.defaultAction(t, p);
        }

}
