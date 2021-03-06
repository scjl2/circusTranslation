package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.application.TightRope;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.generators.NewSCJApplication;
import hijac.tools.tightrope.utils.NewTransUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

/**
 * Visitor for capturing the name, return type, parameters and return value of a
 * method and returning these as a <code>MethodEnv</code>
 * 

 * 
 */

public class MethodVisitor
{
	private static NewSCJApplication application;
	private static MethodBodyVisitor franksMethodVisitor;

	private ObjectEnv object;

	public MethodVisitor(SCJAnalysis analysis, ObjectEnv object)
	{
		// this.analysis = analysis;
		this.object = object;
		MethodVisitor.application = TightRope.getSCJApplication();
		MethodVisitor.franksMethodVisitor = new MethodBodyVisitor(application,
				object);
	}

	// TODO Tuning: have this method accept an empty ArrayList to fill
	public MethodEnv visitMethod(MethodTree mt, Boolean arg1)
	{
		// get name
		Name methodName = mt.getName();

		System.out.println("+++ Method Visitor: " + methodName + " +++");
		MethodEnv m;

		// return values
		ArrayList<Name> returnsValues = mt
				.accept(new ReturnVisitor(null), null);

		Map<Object, Object> parameters = new HashMap<>();

		for (VariableTree vt : mt.getParameters())
		{
			if (!(vt.getType().toString().contains("String")))
			{
				parameters.put(vt.getName().toString(),
						NewTransUtils.encodeType(vt.getType()));
			}
		}

		Tree returnType = mt.getReturnType();

		String body = null;

		if (returnType instanceof PrimitiveTypeTree)
		{
			body = mt.accept(franksMethodVisitor, new MethodVisitorContext());

			m = new MethodEnv(methodName, NewTransUtils.encodeType(returnType),
					returnsValues, parameters, body);
		}
		else
		{
			m = new MethodEnv(methodName, NewTransUtils.encodeType(returnType),
					returnsValues, parameters, "");

			franksMethodVisitor = new MethodBodyVisitor(application, object, m);

			body = mt.accept(franksMethodVisitor, new MethodVisitorContext());

			m.setBody(body);
		}

		return m;
	}
}
