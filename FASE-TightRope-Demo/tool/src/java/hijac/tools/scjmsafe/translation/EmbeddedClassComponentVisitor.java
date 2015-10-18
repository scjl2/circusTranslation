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

class EmbeddedClassComponentVisitor extends SimpleTreeVisitor<Void, Void> {

        ArrayList<ClassTree> trees;
        ArrayList<String> parents;
        ArrayList<String> embedded;
        ClassTree parent;

        public EmbeddedClassComponentVisitor(ClassTree ct, ArrayList<ClassTree> trees, ArrayList<String> parents, ArrayList<String> embedded) {
            parent = ct;
            this.parents = parents;
            this.embedded = embedded;
            this.trees = trees;
        }

        @Override
        public Void visitClass(ClassTree ct, Void p) {
            System.out.println("Found embedded class " + ct.getSimpleName() + " in class " + parent.getSimpleName());
            parents.add(parent.getSimpleName().toString());
            embedded.add(ct.getSimpleName().toString());
            trees.add(ct);
            ct.accept(new EmbeddedClassVisitor(trees, parents, embedded), null);
            return super.defaultAction(ct, p);
        }

        @Override
        public Void defaultAction(Tree t, Void p) {
            return super.defaultAction(t, p);
        }

}
