package hijac.tools.tightrope.generators;

import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.freemarker.VisitorMethodModel;
import hijac.tools.modelgen.circus.templates.CircusTemplateFactory;
import hijac.tools.modelgen.circus.templates.CircusTemplates;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.visitors.MethodBodyVisitor;

import freemarker.template.Template;
import freemarker.template.TemplateModelException;

/**
 * @author Frank Zeyda
 * @version $Revision: 213 $
 */
public class NewCircusTemplates extends CircusTemplates
{
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

	private NewSCJApplication context;

	class NewDataOpMethodModel extends VisitorMethodModel<MethodVisitorContext>
	{

		public NewDataOpMethodModel(NewSCJApplication context, ObjectEnv object)
		{
			super("DATAOP", new MethodBodyVisitor(context, object),
					new MethodVisitorContext());
		}
	}

	public NewCircusTemplates(SCJApplication context)
	{
		super(context);
		this.context = (NewSCJApplication) context;
		FACTORY = new CircusTemplateFactory(context);
		// SAFELET = FACTORY.getTemplate("Safelet.ftl");
		// MISSION_SEQUENCER = FACTORY.getTemplate("MissionSequencer.ftl");
		// MISSION = FACTORY.getTemplate("Mission.ftl");
		// APERIODIC_HANDLER = FACTORY.getTemplate("AperiodicHandler.ftl");
		// PERIODIC_HANDLER = FACTORY.getTemplate("PeriodicHandler.ftl");
		// DATA_OBJECT = FACTORY.getTemplate("DataObject.ftl");
		SAFELET = null;
		MISSION_SEQUENCER = null;
		MISSION = null;
		APERIODIC_HANDLER = null;
		PERIODIC_HANDLER = null;
		DATA_OBJECT = null;

		STMT = FACTORY.getTemplate("L2Stmt.ftl");
		EXPR = FACTORY.getTemplate("L2Expr.ftl");
		TYPE = FACTORY.getTemplate("L2Type.ftl");

		try
		{
			if (context instanceof NewSCJApplication)
			{
				NewSCJApplication newSCJApp = (NewSCJApplication) context;
				
				FACTORY.CIRCUS_CONFIG.setSharedVariable("ACTION",
						new MethodBodyVisitor(newSCJApp, null));
				FACTORY.CIRCUS_CONFIG.setSharedVariable("DATAOP",
						new NewDataOpMethodModel(newSCJApp, null));
			}
		}
		catch (TemplateModelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setObjectEnv(ObjectEnv object)
	{
		try
		{
			FACTORY.CIRCUS_CONFIG.setSharedVariable("ACTION",
					new MethodBodyVisitor(context, object));
			FACTORY.CIRCUS_CONFIG.setSharedVariable("DATAOP",
					new NewDataOpMethodModel(context, object));
		}
		catch (TemplateModelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
