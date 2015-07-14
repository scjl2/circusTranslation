package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.modelgen.circus.templates.CircusTemplates;
import hijac.tools.modelgen.circus.visitors.AMethodVisitor;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ParadigmEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.generators.NewCircusTemplates;
import hijac.tools.tightrope.generators.NewSCJApplication;

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

import checkers.formatter.quals.ReturnsFormat;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

	public class MissionSequencerLevel2Visitor implements ElementVisitor<ArrayList<Name>, Void>
	{
		ProgramEnv programEnv;
		SCJAnalysis analysis;
		
		private  Trees trees;
//		private  Set<CompilationUnitTree> units;
//		private  Set<TypeElement> type_elements;
		private ReturnVisitor returnVisitor ;
		ParadigmEnv sequencerEnv;
		
		MethodBodyVisitor franksMethodVisitor; 

		
		private HashMap<Name, Tree> varMap = new HashMap<Name, Tree>();
		
				
		public MissionSequencerLevel2Visitor(ProgramEnv programEnv, ParadigmEnv sequencerEnv, SCJAnalysis analysis)
		{
			this.analysis = analysis;
			this.programEnv = programEnv;
			this.sequencerEnv = sequencerEnv;

			trees = analysis.TREES;
//			units = analysis.getCompilationUnits();
//			type_elements = analysis.getTypeElements();

//			returnVisitor = new ReturnVisitor(programEnv);
			
	
			this.franksMethodVisitor = new MethodBodyVisitor(new NewSCJApplication(analysis));
		}

		@Override
		public ArrayList<Name> visit(Element arg0)
		{
			// TODO Auto-generated method stub
			return null;

		}

		@Override
		public ArrayList<Name> visit(Element arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ArrayList<Name> visitExecutable(ExecutableElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ArrayList<Name> visitPackage(PackageElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ArrayList<Name> visitType(TypeElement arg0, Void arg1)
		{
//			getVariables(arg0);	

			System.out.println();

			System.out.println("+++ Mission Sequencer Variables +++");
			System.out.println();
			
			for (Name n : varMap.keySet())
			{
				System.out.println("+++ Variable " + n + " = " + varMap.get(n));
			}
			
			
			ArrayList<Name> missions = new ArrayList<Name>();

			System.out.println("In MS Visitor for " + arg0);

			ClassTree ct = trees.getTree(arg0);
			System.out.println("MS Visitor class tree: " + ct);
			
//			ReturnVisitor rv = new ReturnVisitor(ct);
//			System.out.println("Retrun Visitor says... " +rv.getReturns());

			List<StatementTree> members = (List<StatementTree>) ct.getMembers();
			System.out.println("MS Visitor members: " + members);

			Iterator<StatementTree> i = members.iterator();

			returnVisitor = new ReturnVisitor(varMap);

			
			while (i.hasNext())
			{
				Tree tlst = i.next();
//				System.out.println("MS Visitor i=" + ((Tree) i).getKind());

				if (tlst instanceof VariableTree)
				{
					System.out.println("MS VIsitor: Variable Tree Found");
					
//					VariableTree vt = (VariableTree) tlst;
//			
//					System.out.println("-> " + vt.toString());
//					System.out.println("-> Name:" + vt.getName());
//					System.out.println("-> Type: " + vt.getType());
//
//					programEnv.addVariable(vt.getName(), vt.getType());
				}

				if (tlst instanceof MethodTree)
				{
					
					MethodTree o = (MethodTree) tlst;
					System.out.println("MS Visitor Method Tree = "
							+ o.getName());

					
					if (o.getName().contentEquals("<init>"))
					{
//						ArrayList<Name> constructorReturns = null;
						System.out.println("Release the Visitor!");
						
//						constructorReturns = 
								o.accept(returnVisitor, false);
//						if(constructorReturns != null)
//						{
//							missions.addAll(constructorReturns);
//						}

					}
					else	
					if (o.getName().contentEquals("getNextMission"))
					{
						ArrayList<Name> getNextReturns = null;
						System.out.println("Release the Visitor!");
						
						getNextReturns = o.accept(returnVisitor, false);
						
						if(getNextReturns != null)
						{
							missions.addAll(getNextReturns);
						}
						

						System.out.println("*** TRYING FRANK'S METHOD VISITOR ***");
						Tree returnType = o.getReturnType();
						
						TypeKind returnTypeKind = TypeKind.ERROR;

						if (returnType instanceof PrimitiveTypeTree)
						{
							returnTypeKind = ((PrimitiveTypeTree) o.getReturnType())
									.getPrimitiveTypeKind();
						}
						
						//return values
						ArrayList<Name> returnsValues = o.accept(
								new ReturnVisitor(null), null);
						
						

						@SuppressWarnings("rawtypes")
						Map parameters = new HashMap();
						for (VariableTree vt : o.getParameters())
						{
							parameters.put(vt.getName().toString(), vt.getType());
						}
						
						String body = o.accept(franksMethodVisitor, new MethodVisitorContext());
						
						System.out.println("*** Body ***");
						System.out.println(body);
						
						sequencerEnv.addMeth(
							new MethodEnv(
									o.getName(), returnTypeKind, returnsValues, parameters, body 
									)
							);
						

						
					}
					else
					{// ADD METHOD TO MISSION ENV
						if (o.getModifiers().getFlags()
								.contains(Modifier.SYNCHRONIZED))
						{

							sequencerEnv.addSyncMeth(o.accept(new MethodVisitor(), null));
						}
						else if(! (o.getName().contentEquals("<init>")  ))
						{
//				
							sequencerEnv.addMeth(o.accept(new MethodVisitor(), null));
						}
						
					}

				}
			}

			System.out.println(" +++ MissionSequencerVissitor has " + missions.size() + " missions +++");
			return missions;
		}

//		private void getVariables(TypeElement arg0)
//		{
//			VariableVisitor varVisitor = new VariableVisitor(programEnv);
//			
//			ClassTree ct = trees.getTree(arg0);
//			List<? extends Tree> members =  ct.getMembers();
//			Iterator<? extends Tree> i = members.iterator();
//			
//			
//			while(i.hasNext())
//			{
//				Tree s = i.next();
////				System.out.println(s);
//				 HashMap<Name, Tree> m = (HashMap<Name, Tree>) s.accept(varVisitor, false) ;
//				 
////				 System.out.println("+++ m == null : " + m == null + " +++" );
//				 
//				 if (m == null)
//				 {
//					 System.out.println("+++ Variable Visitor Returned Null +++");
//						
//				 }
//				 else
//				 {
//					 System.out.println("+++ Variable Visitor Returned " + m);
//					 varMap.putAll(m);	
//				 }
//					
//			}
//			
//		}

		@Override
		public ArrayList<Name> visitTypeParameter(TypeParameterElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ArrayList<Name> visitUnknown(Element arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ArrayList<Name> visitVariable(VariableElement arg0, Void arg1)
		{
			// TODO Auto-generated method stub
			return null;
		}

	}