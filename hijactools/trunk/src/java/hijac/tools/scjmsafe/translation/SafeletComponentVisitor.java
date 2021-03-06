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
public class SafeletComponentVisitor extends SimpleTreeVisitor<Void, Void> {

        MSafeSafelet safelet;

        public SafeletComponentVisitor(MSafeSafelet param) {
            safelet = param;
        }

        // Extracts the fields of the class
        @Override
        public Void visitVariable(VariableTree vt, Void p) {
            Command var = vt.accept(new VariableVisitor(), null);
            if (var instanceof Declaration) {
                safelet.addField((Declaration) var);
            } else if (var instanceof Sequence) {
                safelet.addField((Declaration) ((Sequence) var).getC1());
                safelet.addInit(((Sequence) var).getC2());
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
                    System.out.println("Safelet constructor found");
//                    safelet.addConstr(Util.getBlockCommand(methodBody));
                    Method method = Util.newConstr(safelet.getName(), methodBody, parameters);
                    safelet.addConstr(method);
                    System.out.println();
                    break;

                }
                case "initializeApplication" :
                case "setUp" :
                case "setup" : {
                    System.out.println("Safelet initializeApplication method found");
                    safelet.addInitApp(Util.getBlockCommand(methodBody));
                    System.out.println();
                    break;
                }
                case "tearDown" :
                case "teardown" : {
                    System.out.println("Safelet teardown method found. NOTE - This method is no longer part of the SCJ spec.");
                    //safelet.addTearDown(Util.getBlockCommand(methodBody));
                    //System.out.println();
                    break;
                }
                case "getSequencer" : {
                    System.out.println("Safelet getSequencer method found");
                    safelet.addGetSequencer(Util.getBlockCommand(methodBody));
                    System.out.println();
                    break;
                }
                default : {
                    Method method = Util.newDefaultMethod(methodName, returnType, methodBody, parameters);
                    safelet.addMethod(method);
                }
            }

            return super.visitMethod(mt, p);
        }
/*
        @Override
        public Void visitClass(ClassTree ct, Void p) {
            System.out.println("FOUND AN EMBEDDED CLASS - WHOOP WHOOP");
            String className = ct.getSimpleName().toString();
            Tree classExtends = ct.getExtendsClause();
            MSafeClass newClass2;
            if (classExtends != null) {
                newClass2 = new MSafeClass(className, classExtends.toString());                
            } else {
                newClass2 = new MSafeClass(className);
            }           
            ct.accept(new ClassVisitor(newClass2), null);
            SCJmSafeTranslator.program.addClass(newClass2);
            return super.defaultAction(ct, p);
        }
*/

        @Override
        public Void defaultAction(Tree t, Void p) {
            return super.defaultAction(t, p);
        }

}
