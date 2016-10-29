package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.application.TightRope;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;
import hijac.tools.tightrope.builders.NonParadigmBuilder;
import hijac.tools.tightrope.builders.ParadigmBuilder.IDType;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.MissionEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.generators.NewSCJApplication;
import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeTransUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
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
	private String idType;
//  private NonParadigmBuilder nonParadigmBuilder = null;

	public MethodVisitor(SCJAnalysis analysis, ObjectEnv object)
	{
		// this.analysis = analysis;
		this.object = object;
		MethodVisitor.application = TightRope.getSCJApplication();
		MethodVisitor.franksMethodVisitor = new MethodBodyVisitor(application, object);
//		propagateNonPBuilder();
		idType = "blankID";
		
	}
	
//	private void propagateNonPBuilder()
//  {
//    if(nonParadigmBuilder != null )
//    {
////      franksMethodVisitor.setNonPBuilder(nonParadigmBuilder);
//    }    
//  }

  public MethodVisitor(SCJAnalysis analysis, MissionEnv missionEnv, IDType idType)
	{
		this(analysis, missionEnv);
		this.idType = idType.toString();
	}

	public MethodEnv visitMethod(MethodTree mt, Boolean isConstructor)
	{
		// get name
		Name methodName = mt.getName();

		Debugger.log("+++ Method Visitor: " + methodName + " +++");
		MethodEnv m;

		// return values
		ArrayList<Name> returnsValues = mt.accept(new ReturnVisitor(), null);

		Map<Object, Object> parameters = new HashMap<>();

		if (isConstructor)
		{
			getConstructorParameters(mt);			
		}
		else
		{
			for (VariableTree vt : mt.getParameters())
			{
				if (!(vt.getType().toString().contains("String")))
				{
					parameters.put(vt.getName().toString(),
							TightRopeTransUtils.encodeType(vt.getType()));
				}
			}
		}

		Tree returnType = mt.getReturnType();

		String body = null;

		Debugger.log("*** Getting Body ***");
		if (returnType instanceof PrimitiveTypeTree)
		{
			body = mt.accept(franksMethodVisitor, new MethodVisitorContext());

			m = new MethodEnv(methodName, TightRopeTransUtils.encodeType(returnType),
					returnsValues, parameters, body);
		}
		else
		{
			m = new MethodEnv(methodName, TightRopeTransUtils.encodeType(returnType),
					returnsValues, parameters, "");

			franksMethodVisitor = new MethodBodyVisitor(application, object, m);
//			propagateNonPBuilder();

			body = mt.accept(franksMethodVisitor, new MethodVisitorContext());

			m.setBody(body);
		}

		getModifiers(mt, m);
		assert(idType != null && (!idType.equals("")));
		m.setLocationType(idType);


		assert (m.getLocationType() != null);
		// assert(m.getLocationType().equals(idType));
		m.setMethodLocation(object);
			
		TightRope.getProgramEnv().addGlobalMethod(m);

		return m;
	}

	private void getModifiers(MethodTree mt, MethodEnv m)
	{
		ModifiersTree modifiers = mt.getModifiers();
		Set<Modifier> modifierFlags = modifiers.getFlags();

		final boolean isMethodSynchronised = modifierFlags.contains(Modifier.SYNCHRONIZED);
    if (isMethodSynchronised)
		{
			m.setSynchronised(true);
			
			if(object != null)
			{
			  TightRope.getProgramEnv().addObjectIdName(object.getName().toString());
			}
		}
		else
		{
			m.setSynchronised(false);
		}
	}

	//TODO Merge this with SafeletBuilder.extractProcessParameters()
	private void getConstructorParameters(MethodTree mt)
	{
		for (VariableTree vt : mt.getParameters())
		{
			VariableEnv parameter = new VariableEnv();

			parameter.setName(vt.getName().toString());
			parameter.setType(TightRopeTransUtils.encodeType(vt.getType()));
			parameter.setProgramType(TightRopeTransUtils.encodeType(vt.getType()));

			if (! parameter.getType().endsWith("Parameters"))
			{
				object.addProcParameter(parameter);
			}
			
		}
	}

//  public void setNonPBuilder(NonParadigmBuilder nonParadigmBuilder)
//  {
//    this.nonParadigmBuilder = nonParadigmBuilder;
//    
//  }
}