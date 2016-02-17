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
class MethodSigVisitor extends SimpleTreeVisitor<Void, Void> {


        public MethodSigVisitor() { }

        @Override
        public Void visitClass(ClassTree ct, Void p) {
            for (Tree t : ct.getMembers()) {
                t.accept(new MethodSigBuilder(ct), null);
            }
            System.out.println();
            return super.visitClass(ct, p);
        }


        @Override
        public Void defaultAction(Tree t, Void p) {
            return super.defaultAction(t, p);
        }

}
