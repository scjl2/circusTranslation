package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.application.TightRope;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.VariableEnv;
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
 * @author Matt Luckcuck
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
		MethodVisitor.franksMethodVisitor = new MethodBodyVisitor(application, object);
	}

	public MethodEnv visitMethod(MethodTree mt, Boolean isConstructor)
	{
		// get name
		Name methodName = mt.getName();

		System.out.println("+++ Method Visitor: " + methodName + " +++");
		MethodEnv m;

		// return values
		ArrayList<Name> returnsValues = mt.accept(new ReturnVisitor(null), null);

		Map<Object, Object> parameters = new HashMap<>();

		if (isConstructor)
		{
			getConstructorParameters(mt);

			// VariableVisitor varVis = new
			// VariableVisitor(TightRope.getProgramEnv(), object);
			// Map<Name, Tree> newVarMap = mt.accept(varVis, false);
			//
			// for(Name name : newVarMap.keySet())
			// {
			// object.addVariableInit(name.toString(), newVarMap.get(name));
			// }

			// System.out.println("New Var Map = " + newVarMap.toString());
		}
		else
		{
			for (VariableTree vt : mt.getParameters())
			{
				if (!(vt.getType().toString().contains("String")))
				{
					parameters.put(vt.getName().toString(),
							NewTransUtils.encodeType(vt.getType()));
				}
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

	private void getConstructorParameters(MethodTree mt)
	{
		for (VariableTree vt : mt.getParameters())
		{
			if (!(vt.getType().toString().contains("String")))
			{
				// object.addParameter(vt.getName().toString(),
				// NewTransUtils.encodeType(vt.getType()),
				// NewTransUtils.encodeType(vt.getType()),
				// (vt.getType() instanceof PrimitiveTypeTree),
				// "FromMethVis");
				//
			}
			// else
			// {
			VariableEnv parameter = new VariableEnv();

			parameter.setName(vt.getName().toString());
			parameter.setType(NewTransUtils.encodeType(vt.getType()));
			parameter.setProgramType(NewTransUtils.encodeType(vt.getType()));

			if (parameter.getType().endsWith("Parameters"))
			{

			}
			else
			{
				// System.out.println("Adding Proc Param "
				// + parameter.toString());
				object.addProcParameter(parameter);
			}
			// }
		}
	}
}