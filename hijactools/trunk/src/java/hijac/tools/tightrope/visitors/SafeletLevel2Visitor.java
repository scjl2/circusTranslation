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
				MethodTree o = (MethodTree) obj;
				// System.out.println(o.getName());

				Tree returnType = o.getReturnType();
				TypeKind typeKind = TypeKind.ERROR;

				if (returnType instanceof PrimitiveTypeTree)
				{
					typeKind = ((PrimitiveTypeTree) o.getReturnType())
							.getPrimitiveTypeKind();

				}
				ArrayList<Name> returns = o.accept(
						new ReturnVisitor(null), null);
				
				Map paramMap = new HashMap();
				for (VariableTree vt : o.getParameters())
				{
					paramMap.put(vt.getName().toString(), vt.getType());
				}
				
				if (o.getName().contentEquals("initializeApplication"))
				{
					programEnv.getSafelet().addMeth(o.getName(), typeKind, returns,
							paramMap);
				}

				if (o.getName().contentEquals("getSequencer"))
				{
					// System.out.println("in iterator");
					programEnv.getSafelet().addMeth(o.getName(), typeKind, returns,
							paramMap);
					List<StatementTree> s = (List<StatementTree>) o.getBody()
							.getStatements();

					@SuppressWarnings("rawtypes")
					Iterator j = s.iterator();

					while (j.hasNext())
					{
						StatementTree st = (StatementTree) j.next();

						// System.out.println("Satement: "+st + " Kind: " +
						// st.getKind());
						// if (st instanceof VariableTree)
						// {
						// System.out.println("Variable: " + ((IdentifierTree)
						// ((NewClassTree) ((VariableTree)
						// st).getInitializer()).getIdentifier()).getName());
						// return new Name[]{ ((IdentifierTree) ((NewClassTree)
						// ((VariableTree)
						// st).getInitializer()).getIdentifier()).getName()};
						// }
						//
						// if (st instanceof AssignmentTree)
						// {
						// System.out.println("Assignment: " + ((AssignmentTree)
						// st).getExpression());
						// }
						//
						// if (st instanceof NewClassTree)
						// {
						// System.out.println("New Class: " + ((NewClassTree)
						// st).getIdentifier());
						// }
						// // {
						// Name id = ((IdentifierTree) ((NewClassTree)
						// st).getIdentifier()).getName() ;
						// System.out.println("Kind: " + id );
						//
						// return new Name[] {id};
						// }

						if (st instanceof ReturnTree)
						{
							// System.out.println("Return Tree Found");
							return st.accept(returnVisitor, null);
							// System.out.println( ((NewClassTree) ((ReturnTree)
							// st).getExpression()).getIdentifier() );
						}
						// {
						// System.out.println(((ReturnTree) st).getExpression()
						// );
						//
						// if (((ReturnTree) st).getExpression().getKind() ==
						// Tree.Kind.NEW_CLASS )
						// {
						//
						// Name id = ((IdentifierTree) ((NewClassTree)
						// ((ReturnTree)
						// st).getExpression()).getIdentifier()).getName() ;
						//
						//
						//
						// System.out.println("Kind: " + id );
						// // ((ReturnTree) st).getExpression()
						//
						// // st.accept(new ReturnVisitor(), null);
						// return new Name[] {id};
						// // return null;
						// //Now use this name to get to the next thing I need
						// to explore?
						// }
						// else
						// {
						// System.out.println("Nope, not a New Class");
						//
						// }
						//
						// }

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