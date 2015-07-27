package hijac.tools.tightrope.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Name;
import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.generators.NewSCJApplication;
import hijac.tools.tightrope.utils.NewTransUtils;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
/**
 * Visitor for capturing the name, return type, parameters and return value
 * of a method and returning these as a <code>MethodEnv</code>
 * @author Matt Luckcuck
 *
 */

//TODO This may need some more context, i.e. the Env that it will reside in
public class MethodVisitor 
{
	MethodEnv methodEnv;
	MethodBodyVisitor franksMethodVisitor;
	SCJAnalysis analysis;
	ObjectEnv object;

	public MethodVisitor(SCJAnalysis analysis, ObjectEnv object)
	{
		this.analysis = analysis;
		this.object = object;
		this.franksMethodVisitor = new MethodBodyVisitor(new NewSCJApplication(
				analysis), object);
	}
	
	

	
	//This should be the entry and exit point, since I'm translating a method
	@SuppressWarnings("unchecked")
	public MethodEnv visitMethod(MethodTree mt, Boolean arg1)
	{
		System.out.println("+++ Method Visitor: Method +++");
		MethodEnv m;
		
		//get name
		Name methodName = mt.getName();
		
		//return values
				ArrayList<Name> returnsValues = mt.accept(
						new ReturnVisitor(null), null);
				
				
				Map<Object, Object> parameters = new HashMap<>();
						
				for (VariableTree vt : mt.getParameters())
				{
					parameters.put(vt.getName().toString(), vt.getType());
				}
		
		
		Tree returnType = mt.getReturnType();
		String returnString = null;
		String body =null;
		
		if (returnType instanceof PrimitiveTypeTree)
		{

//			TypeKind returnTypeKind = ((PrimitiveTypeTree) mt.getReturnType())
//					.getPrimitiveTypeKind();
//			
//		
//
//			switch (returnTypeKind)
//			{
//				case BOOLEAN:
//					returnString = "\\boolean";
//					break;
//				case BYTE:
//					returnString = "byte";
//					break;
//				case INT:
//					returnString = "int";
//					break;
//				case LONG:
//					returnString = "long";
//					break;
//				case FLOAT:
//					returnString = "float";
//					break;
//				case DOUBLE:
//					returnString = "double";
//					break;
//				case CHAR:
//					returnString = "char";
//					break;
//				default:
//					break;
//			}

			body = mt.accept(franksMethodVisitor,
					new MethodVisitorContext());

			System.out.println("*** Body ***");
			System.out.println(body);

			m = new MethodEnv(methodName, NewTransUtils.encodeType(returnType),
					returnsValues, parameters, body);
			
			

		} else
		{
//			String s = "null";
//			if(mt.getReturnType() != null)
//			{
//				s= mt.getReturnType().toString();
//			
//			if (s.contains("Mission"))
//			{
//				returnString = "MissionID";
//			} else if (s.contains("MissionSequencer")
//					|| s.contains("OneShotEventHandler")
//					|| s.contains("AperiodicEventHandler")
//					|| s.contains("PeriodicEventHandler")
//					|| s.contains("ManagedThread"))
//			{
//				returnString = "SchedulableID";
//			}
//			}
			

			m = new MethodEnv(methodName, NewTransUtils.encodeType(returnType),							
					returnsValues, parameters, "");

			franksMethodVisitor = new MethodBodyVisitor(
					new NewSCJApplication(analysis), object, m);

			body = mt.accept(franksMethodVisitor,
					new MethodVisitorContext());

			System.out.println("*** Body ***");
			System.out.println(body);
			m.setBody(body);
		}

		
		
		
		
		//get body
//		Object body = mt.getBody().accept(this, arg1);
		
		
//		return new MethodEnv(methodName, returnString, returnsValues, parameters, body);
		return m;
	}




}