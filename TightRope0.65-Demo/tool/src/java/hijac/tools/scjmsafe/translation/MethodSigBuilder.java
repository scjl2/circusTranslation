package hijac.tools.scjmsafe.translation;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Method.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;

// Specific visitor for classes that analyses each part of a class
class MethodSigBuilder extends SimpleTreeVisitor<Void, Void> {

        ClassTree classTree;
        String className;
        String classExtends;

        public MethodSigBuilder(ClassTree ct) {
            classTree = ct;
            className = classTree.getSimpleName().toString();
            if (ct.getExtendsClause() != null) {
                classExtends = ct.getExtendsClause().toString();
            } else {
                classExtends = "";
            }
        }

        // Extracts the methods of the class
        @Override
        public Void visitMethod(MethodTree mt, Void p) {
            String methodName = mt.getName().toString();
            Tree returnType = mt.getReturnType();
            @SuppressWarnings("unchecked")
            List<VariableTree> params = (List<VariableTree>) mt.getParameters();
            ArrayList<String> paramTypes = new ArrayList<String>(0);
            for (VariableTree param : params) {
                Tree type = param.getType();
                paramTypes.add(Util.getTypeString(type));
            }

            Util.addMethodToDatabase(methodName, className, classExtends, returnType, paramTypes);            

            System.out.print("Found method '" + mt.getName() + "' with return type '");
            if (returnType != null) {
                System.out.print(returnType.toString() + "'");
            } else {
                System.out.print("void'");
            }
            System.out.print(" and parameters: (");
            for (int i = 0; i < paramTypes.size(); i++) {
                if (i < paramTypes.size() - 1) {
                    System.out.print(paramTypes.get(i) + ", ");
                } else {
                    System.out.print(paramTypes.get(i));
                }
            }
            System.out.print(") \n");

            return super.visitMethod(mt, p);
        }


        @Override
        public Void defaultAction(Tree t, Void p) {
            return super.defaultAction(t, p);
        }

}
