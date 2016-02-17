package hijac.tools.tightrope.generators;

import hijac.tools.modelgen.circus.freemarker.VisitorMethodModel;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.visitors.MethodBodyVisitor;


public class NewActionMethodModel extends
	VisitorMethodModel<MethodVisitorContext> {

	public NewActionMethodModel(NewSCJApplication context, ObjectEnv object) {
	   super("ACTION",
	      new MethodBodyVisitor(context, object),
	      new MethodVisitorContext());
	}

	public NewActionMethodModel(NewSCJApplication context, ObjectEnv object, MethodEnv methodEnv)
	{
		super("ACTION",
			      new MethodBodyVisitor(context, object, methodEnv),
			      new MethodVisitorContext());
	}
}