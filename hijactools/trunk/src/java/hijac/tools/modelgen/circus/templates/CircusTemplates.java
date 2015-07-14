package hijac.tools.modelgen.circus.templates;

import hijac.tools.modelgen.circus.SCJApplication;

import freemarker.template.Template;

/**
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class CircusTemplates {
   public final CircusTemplateFactory FACTORY;

   public final Template SAFELET;

   public final Template MISSION_SEQUENCER;

   public final Template MISSION;

   public final Template APERIODIC_HANDLER;

   public final Template PERIODIC_HANDLER;

   public final Template DATA_OBJECT;

   public final Template STMT;

   public final Template EXPR;

   public final Template TYPE;

   
   
   public CircusTemplates(SCJApplication context) {
      FACTORY = new CircusTemplateFactory(context);
      SAFELET = FACTORY.getTemplate("Safelet.ftl");
      MISSION_SEQUENCER = FACTORY.getTemplate("MissionSequencer.ftl");
      MISSION = FACTORY.getTemplate("Mission.ftl");
      APERIODIC_HANDLER = FACTORY.getTemplate("AperiodicHandler.ftl");
      PERIODIC_HANDLER = FACTORY.getTemplate("PeriodicHandler.ftl");
      DATA_OBJECT = FACTORY.getTemplate("DataObject.ftl");
      STMT = FACTORY.getTemplate("Stmt.ftl");
      EXPR = FACTORY.getTemplate("Expr.ftl");
      TYPE = FACTORY.getTemplate("Type.ftl");
   }
}
