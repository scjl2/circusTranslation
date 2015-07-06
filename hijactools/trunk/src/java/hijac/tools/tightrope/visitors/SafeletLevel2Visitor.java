package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.ProgramEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

public class SafeletLevel2Visitor implements ElementVisitor<ArrayList<Name>, Void>
{

	ProgramEnv programEnv;
	SCJAnalysis analysis;

	private Trees trees;
	private ReturnVisitor returnVisitor = new ReturnVisitor(null);


	public SafeletLevel2Visitor(ProgramEnv programEnv, SCJAnalysis analysis)
	{
		this.analysis = analysis;
		this.programEnv = programEnv;

		trees = analysis.TREES;
		analysis.getCompilationUnits();
		analysis.getTypeElements();
	}

	@Override
	public ArrayList<Name> visit(Element e)
	{
		// System.out.println(e);
		return null;
	}

	@Override
	public ArrayList<Name> visit(Element e, Void p)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitExecutable(ExecutableElement e, Void p)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitPackage(PackageElement e, Void p)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Name> visitType(TypeElement e, Void p)
	{
		// System.out.println(e);

		ClassTree ct = trees.getTree(e);
	

//		programEnv.getSafelet().setClassTree(ct);


		List<StatementTree> members = (List<StatementTree>) ct.getMembers();
		Iterator<StatementTree> i = members.iterator();
		while (i.hasNext())
		{
			Object obj = i.next();
			
			// wrong visiotr here?
	
			if (obj instanceof MethodTree)
			{
				MethodTree mt = (MethodTree) obj;
				
				Tree returnType = mt.getReturnType();
				TypeKind typeKind = TypeKind.ERROR;

				if (returnType instanceof PrimitiveTypeTree)
				{
					typeKind = ((PrimitiveTypeTree) mt.getReturnType())
							.getPrimitiveTypeKind();

				}
				ArrayList<Name> returns = mt.accept(
						new ReturnVisitor(null), null);
				
				Map paramMap = new HashMap();
				for (VariableTree vt : mt.getParameters())
				{
					paramMap.put(vt.getName().toString(), vt.getType());
				}
				
				if (mt.getName().contentEquals("initializeApplication"))
				{
					programEnv.getSafelet().addMeth(mt.getName(), typeKind, returns,
							paramMap);
				}
				else
				if (mt.getName().contentEquals("getSequencer"))
				{
					
					programEnv.getSafelet().addMeth(mt.getName(), typeKind, returns,
							paramMap);
					List<StatementTree> s = (List<StatementTree>) mt.getBody()
							.getStatements();

					@SuppressWarnings("rawtypes")
					Iterator j = s.iterator();

					while (j.hasNext())
					{
						StatementTree st = (StatementTree) j.next();

						

						if (st instanceof ReturnTree)
						{
							
							return st.accept(returnVisitor, null);
						
						}
					}
				}
				else					
					{						
						{
							// ADD METHOD TO  ENV
							if (mt.getModifiers().getFlags()
									.contains(Modifier.SYNCHRONIZED))
							{


								programEnv.getSafelet().addSyncMeth(mt.accept(new MethodVisitor(), null));
							}
							else if(! (mt.getName().contentEquals("<init>") || mt.getName().contentEquals("getSequencer") ) )
							{

//								programEnv.getSafelet().addMeth(mt.accept(new MethodVisitor(), null));
							}
						}
					}
			}

		}

		return null;
	}

	@Override
	public ArrayList<Name> visitTypeParameter(TypeParameterElement e, Void p)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitUnknown(Element e, Void p)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visitVariable(VariableElement e, Void p)
	{
		// TODO Auto-generated method stub
		return null;
	}

}