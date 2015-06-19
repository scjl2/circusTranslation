package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import cfjapa.parser.ast.type.PrimitiveType;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

public class MissionLevel2Visitor implements
		ElementVisitor<ArrayList<Name>, Void>
{
	ProgramEnv programEnv;
	SCJAnalysis analysis;

	private Trees trees;
	private ParadigmEnv missionEnv;

	private RegistersVisitor registersVisitor;

	public MissionLevel2Visitor(ProgramEnv programEnv, ParadigmEnv missionEnv,  SCJAnalysis analysis)
	{
		this.analysis = analysis;
		this.programEnv = programEnv;
		this.missionEnv = missionEnv;

		trees = analysis.TREES;
		analysis.getCompilationUnits();
		analysis.getTypeElements();
		

		registersVisitor = new RegistersVisitor(programEnv, analysis);
		
	}

	@Override
	public ArrayList<Name> visitType(TypeElement arg0, Void arg1)
	{
		// System.out.println(arg0);

		ArrayList<Name> schedulables = new ArrayList<Name>();
		ClassTree ct = trees.getTree(arg0);
			
		List<Tree> members = (List<Tree>) ct.getMembers();
		Iterator<Tree> i = members.iterator();
		while (i.hasNext())
		{
			System.out.println("Mission Visitor: I Iterator");

			Tree tlst = i.next();
			System.out.println("Mission Visistor: tlst = " + tlst.getKind());
			
			// Name name = tlst.accept(registersVisitor,null);
			//
			// if (name != null)
			// {
			// schedulables.add(name);
			// }
			//

			if (tlst instanceof VariableTree)
			{
				VariableTree vt = (VariableTree) tlst;
				System.out.println("Mission Visitor: Variable Tree Found");
				System.out.println("-> " + vt.toString());
				System.out.println("-> Name:" + vt.getName());
				System.out.println("-> Type: " + vt.getType());

				//TODO refactor this. Variables should be kept in the relevent env
				programEnv.addVariable(vt.getName(), vt.getType());

			}

			if (tlst instanceof MethodTree)
			{
				MethodTree mt = (MethodTree) tlst;

				if(mt.getModifiers().getFlags()
										.contains(Modifier.SYNCHRONIZED))
				{
					
					Map paramMap = new HashMap();
					for(VariableTree vt : mt.getParameters())
					{
						paramMap.put(vt.getName().toString(), vt.getType());
					}
					//TODO This needs to now actually figure out WHAT is returned and return it too
					Tree returnType = mt.getReturnType();
					TypeKind typeKind = TypeKind.ERROR;
					
					if(returnType instanceof PrimitiveTypeTree)
					{
						 typeKind = ((PrimitiveTypeTree) mt.getReturnType()). getPrimitiveTypeKind();
						
					}
				
					
					missionEnv.addSyncMeth(mt.getName(),typeKind , paramMap );
				}
				
				if (mt.getName().contentEquals("initialize"))
				{
					System.out.println("Mission Visitor: J iterator");
					List<StatementTree> s = (List<StatementTree>) mt.getBody()
							.getStatements();

					Iterator<StatementTree> j = s.iterator();

					// iterate through the statements in the Init method
					while (j.hasNext())
					{
						StatementTree st = j.next();

						System.out.println("Mission Visistor: j = "
								+ st.getKind());

						// ArrayList<Name> name =
						Name name = st.accept(registersVisitor, null);

						if (name != null)
						{
							schedulables.add(name);
						}
					}
				}
			}
		}
		return schedulables;

	}

	@Override
	public ArrayList<Name> visit(Element e, Void p)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Name> visit(Element e)
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

	@Override
	public ArrayList<Name> visitVariable(VariableElement e, Void p)
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

}
