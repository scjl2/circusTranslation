package hijac.tools.scjmsafe.translation;

import hijac.tools.analysis.SCJAnalysis;

import hijac.tools.scjmsafe.language.*;
import hijac.tools.scjmsafe.language.Commands.*;
import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;

// Top level visitor that is instantiated for each class found in the input
class MissionVisitor extends SimpleTreeVisitor<Void, Void> {

        MSafeMission mission;

        public MissionVisitor (MSafeMission mission) {
            this.mission = mission;
        }

        @Override
        public Void visitClass(ClassTree ct, Void p) {
            for (Tree t : ct.getMembers()) {
                t.accept(new MissionHandlerScanner(mission), null);
                t.accept(new MissionComponentVisitor(mission), null);
            }
            return super.visitClass(ct, p);
        }

        @Override
        public Void defaultAction(Tree t, Void p) {
            return super.defaultAction(t, p);
        }

}




