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
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

	public class MissionSequencerLevel2Visitor implements ElementVisitor<Name[], Void>
	{
		ProgramEnv programEnv;
		SCJAnalysis analysis;
		
		private  Trees trees;
		private  Set<CompilationUnitTree> units;
		private  Set<TypeElement> type_elements;
		
		
		public MissionSequencerLevel2Visitor(ProgramEnv programEnv, SCJAnalysis analysis)
		{
			this.analysis = analysis;
			this.programEnv = programEnv;
			

			trees = analysis.TREES;
			units = analysis.getCompilationUnits();
			type_elements = analysis.getTypeElements();
		}

		@Override
		public Name[] visit(Element arg0)
		{
			// TODO Auto-generated method stub
			return null;

		}

		@Override
		public Name[] visit(Element arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitExecutable(ExecutableElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitPackage(PackageElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitType(TypeElement arg0, Void arg1)
		{

			System.out.println("In MS Visitor for " + arg0);

			ClassTree ct = trees.getTree(arg0);
			System.out.println("MS Visitor class tree: " + ct);

			List<StatementTree> members = (List<StatementTree>) ct.getMembers();
			System.out.println("MS Visitor members: " + members);

			Iterator<StatementTree> i = members.iterator();
			while (i.hasNext())
			{
				Tree tlst = i.next();
//				System.out.println("MS Visitor i=" + ((Tree) i).getKind());

				if (tlst instanceof VariableTree)
				{
					System.out.println("MS VIsitor: Variable Tree Found");
				}

				if (tlst instanceof MethodTree)
				{
					MethodTree o = (MethodTree) tlst;
					System.out.println("MS Visitor Method Tree = "
							+ o.getName());

					if (o.getName().contentEquals("getNextMission"))
					{
						
						System.out.println("Release the Visitor!");
						return o.accept(new ReturnVisitor(), null);

						// System.out.println("in iterator");
						// List<StatementTree> s = (List<StatementTree>)
						// o.getBody().getStatements();
						//
						// Iterator j = s.iterator();
						//
						// while(j.hasNext())
						// {
						// StatementTree st = (StatementTree) j.next();
						// System.out.println("MS Visitor: " + st);
						//
						// if(st instanceof ReturnTree )
						// {
						// System.out.println("Mission Sequencer Return Tree FOUND: "+
						// ((ReturnTree) st).getExpression() );
						//
						// // Name id = ((IdentifierTree) ((NewClassTree)
						// ((ReturnTree)
						// st).getExpression()).getIdentifier()).getName() ;
						//
						// return st.accept(new ReturnVisitor(), null);
						// // System.out.println("Kind: " + id );
						// // ((ReturnTree) st).getExpression()
						//
						// // st.accept(new ReturnVisitor(), null);
						// // return new Name[] {id};
						// //Now use this name to get to the next thing I need
						// to explore?
						//
						// }
						// }
						//
					}
				}
			}

			return null;
		}

		@Override
		public Name[] visitTypeParameter(TypeParameterElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitUnknown(Element arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitVariable(VariableElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

	}