package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.ProgramEnv;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

public class MissionLevel2Visitor implements ElementVisitor<Name[][], Void>
{
	ProgramEnv programEnv;
	SCJAnalysis analysis;
	
	private  Trees trees;
	private  Set<CompilationUnitTree> units;
	private  Set<TypeElement> type_elements;
	
	public MissionLevel2Visitor(ProgramEnv programEnv, SCJAnalysis analysis)
	{
		this.analysis = analysis;
		this.programEnv = programEnv;
		

		trees = analysis.TREES;
		units = analysis.getCompilationUnits();
		type_elements = analysis.getTypeElements();
	}

		@Override
		public Name[][] visit(Element arg0)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[][] visit(Element arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[][] visitExecutable(ExecutableElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[][] visitPackage(PackageElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[][] visitType(TypeElement arg0, Void arg1)
		{
//			System.out.println(arg0);

			ClassTree ct = trees.getTree(arg0);

			List<Tree> members = (List<Tree>) ct.getMembers();
			Iterator<Tree> i = members.iterator();
			while (i.hasNext())
			{
				System.out.println("Mission Visitor: I itorator");
				
				Tree tlst = i.next();
				
				if (tlst instanceof VariableTree)
				{
					System.out.println("Mission Visitor: Variable Tree Found");
				}
				
//				System.out.println("Mission Visitor: i= " + ((Tree) i).getKind());
				if (tlst instanceof MethodTree)
				{
					MethodTree o = (MethodTree) i.next();
					System.out.println(o.getName());

					if (o.getName().contentEquals("initialize"))
					{
						System.out.println("Mission Visitor: J iterator");
						List<StatementTree> s = (List<StatementTree>) o.getBody().getStatements();

						Iterator<StatementTree> j = s.iterator();

						while (j.hasNext())
						{
							StatementTree st =  j.next();
							
							System.out.println("Mission Visistor: j = "+ st.toString());
							
							if (st instanceof MethodInvocationTree)
							{
								System.out.println("Mission Visitor: Found Method Invocation");
								System.out.println(((MethodInvocationTree) st).getMethodSelect());
							}
							
						}

					}
				}
			}

			return null;
		}

		@Override
		public Name[][] visitTypeParameter(TypeParameterElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[][] visitUnknown(Element arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[][] visitVariable(VariableElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

	}
