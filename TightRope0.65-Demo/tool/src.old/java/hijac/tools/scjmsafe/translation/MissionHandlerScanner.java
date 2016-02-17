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
class MissionHandlerScanner extends SimpleTreeVisitor<Void, Void> {

        MSafeMission mission;

        public MissionHandlerScanner(MSafeMission param) {
            mission = param;
        }

        @Override
        public Void visitMethod(MethodTree mt, Void p) {
            String methodName = mt.getName().toString();
            BlockTree methodBody = mt.getBody();
            
            switch(methodName) {
                case "<init>" :
                case "initialize" : {
                    Util.scanForHandlers(mission, methodBody);
                    break;
                }
                default : { }
            }

            return super.visitMethod(mt, p);
        }


        @Override
        public Void defaultAction(Tree t, Void p) {
            return super.defaultAction(t, p);
        }

}
