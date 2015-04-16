package hijac.tools.application;

import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

import hijac.tools.analysis.AnalysisError;
import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.analysis.TaskProcessor;
import hijac.tools.analysis.SCJDataKey;
import hijac.tools.analysis.AnalysisTask;
import hijac.tools.analysis.tasks.MethodCallDepTask;
import hijac.tools.collections.RelationImpl;
import hijac.tools.compiler.SCJCompilationConfig;
import hijac.tools.compiler.SCJCompilationException;
import hijac.tools.compiler.SCJCompilationTask;
import hijac.tools.config.Hijac;
import hijac.tools.config.Statics;

import hijac.tools.application.UncaughtExceptionHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import hijac.tools.modelgen.Output;
import hijac.tools.modelgen.Target;
import hijac.tools.modelgen.circus.PostProcessor;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.datamodel.ClassModel;
import hijac.tools.modelgen.circus.datamodel.SafeletModel;
import hijac.tools.modelgen.circus.translators.CircusTranslator;
import hijac.tools.modelgen.circus.translators.SafeletTranslator;
import hijac.tools.modelgen.circus.visitors.AMethodVisitor;
import hijac.tools.modelgen.targets.ClassTarget;
import hijac.tools.scjmsafe.translation.*;
import hijac.tools.scjmsafe.checker.*;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.translators.Level2Translator;
import hijac.tools.tightrope.visitors.ReturnVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import javax.lang.model.element.TypeElement;

import javax.lang.model.type.TypeMirror;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.IntersectionTypeTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;

import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.util.SimpleTreeVisitor;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;

public class TightRopeTest
{
	public static SCJAnalysis ANALYSIS;

	private static Trees trees;
	private static Set<CompilationUnitTree> units;
	private static Set<TypeElement> type_elements;

	private static ProgramEnv programEnv;

	static
	{
		Statics.kickstart();
	}

	// private static HashMap<String, Name> framework = new HashMap<String,
	// Name>();

	public static class SafeletLevel2Visitor implements
			ElementVisitor<Name[], Void>
	{

		@Override
		public Name[] visit(Element e)
		{
			// System.out.println(e);
			return null;
		}

		@Override
		public Name[] visit(Element e, Void p)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitExecutable(ExecutableElement e, Void p)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitPackage(PackageElement e, Void p)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitType(TypeElement e, Void p)
		{
			// System.out.println(e);

			ClassTree ct = trees.getTree(e);
			
			programEnv.getSafelet().setClassTree(ct);

			List<StatementTree> members = (List<StatementTree>) ct.getMembers();
			Iterator<StatementTree> i = members.iterator();
			while (i.hasNext())
			{
				MethodTree o = (MethodTree) i.next();
				// System.out.println(o.getName());

				if (o.getName().contentEquals("initializeApplication"))
				{
					programEnv.getSafelet().addMeth(o);
				}

				if (o.getName().contentEquals("getSequencer"))
				{
					// System.out.println("in iterator");
					programEnv.getSafelet().addMeth(o);
					List<StatementTree> s = (List<StatementTree>) o.getBody()
							.getStatements();

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
							return st.accept(new ReturnVisitor(), null);
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

			return null;
		}

		@Override
		public Name[] visitTypeParameter(TypeParameterElement e, Void p)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitUnknown(Element e, Void p)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Name[] visitVariable(VariableElement e, Void p)
		{
			// TODO Auto-generated method stub
			return null;
		}

	}

	public static class MissionSequencerLevel2Visitor implements
			ElementVisitor<Name[], Void>
	{

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
				System.out.println("MS Visitor i=" + i);

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
						for (AnnotationTree at : o.getModifiers().getAnnotations())
						{
							System.out.println("+++Mission Annotation Arguments");
							System.out.println(at.getArguments().get(0));
						}
						
						System.out.println("Release the Visitor!");
						o.accept(new ReturnVisitor(), null);

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

	public static class MissionLevel2Visitor implements
			ElementVisitor<Name[][], Void>
	{

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
			System.out.println(arg0);

			ClassTree ct = trees.getTree(arg0);

			List<StatementTree> members = (List<StatementTree>) ct.getMembers();
			Iterator<StatementTree> i = members.iterator();
			while (i.hasNext())
			{
				i.next();
				if (i instanceof MethodTree)
				{
					MethodTree o = (MethodTree) i.next();
					System.out.println(o.getName());

					if (o.getName().contentEquals("initialize"))
					{
						System.out.println("in iterator");
						List<StatementTree> s = (List<StatementTree>) o
								.getBody().getStatements();

						Iterator j = s.iterator();

						while (j.hasNext())
						{
							System.out.println(j.next());
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

	protected static void setUncaughtExceptionHandler()
	{
		Thread.currentThread().setUncaughtExceptionHandler(
				new UncaughtExceptionHandler());
	}

	/* Main Program */

	public static void main(String[] args) throws IOException
	{
		setUncaughtExceptionHandler();

		SCJCompilationTask compiler = new SCJCompilationTask(
				SCJCompilationConfig.getDefault());

		System.out.println("Compiling SCJ sources...");

		try
		{
			ANALYSIS = SCJAnalysis.create(compiler);
		} catch (SCJCompilationException e)
		{
			System.out.println("Failed to compile...");
			e.getDiagnostics().print(System.out);
			System.exit(-1);
		}

		trees = ANALYSIS.TREES;
		units = ANALYSIS.getCompilationUnits();
		type_elements = ANALYSIS.getTypeElements();
		programEnv = new ProgramEnv(ANALYSIS);

		Name[] names = null;
		String packagePrefix = null;

		// for all the types in the program
		for (TypeElement elem : type_elements)
		{
			// System.out.println(elem.toString());
			String elemID = elem.toString();
			// if the type we have is the safelet

			TypeMirror safelet_type = (TypeMirror) ANALYSIS.get(Hijac
					.key("SafeletType"));

			// System.out.println( safelet_type.toString() );

			// Set<TypeElement> supers = ANALYSIS.getAllSuperclasses(elem);
			// System.out.println(elem.getInterfaces().toString());
			// if( ANALYSIS.TYPES.isSubtype( elem.asType(), safelet_type) )

			if (elem.getInterfaces().toString().contains("Safelet"))
			// if (elemID.equalsIgnoreCase("accs.ACCSafelet") )
			{
				System.out.println("Found Safelet");

				packagePrefix = elem.getQualifiedName().toString();
				int firstIndex = packagePrefix.indexOf(elem.getSimpleName()
						.toString());
				packagePrefix = packagePrefix.substring(0, firstIndex);

				// System.out.println("PACKAGE PREFIX: " +packagePrefix);

				// framework.put("Safelet", elem.getSimpleName());
				programEnv.addSafelet(elem.getSimpleName());

				// add methods etc here
				programEnv.getSafelet();

				names = elem.accept(new SafeletLevel2Visitor(), null);
				
				programEnv.getSafelet().setTLMSNames(names);

				for (int i = 0; i < names.length; i++)
				{
					// framework.put("TopLevelMissionSequencer", names[i]);
					programEnv.addTopLevelMissionSequencer(names[i]);
				}

				System.out.println(names == null);

				// SafeletModel sm = new SafeletModel(new
				// SCJApplication(ANALYSIS), new ClassModel(new
				// SCJApplication(ANALYSIS), elem));
				//
				// SafeletTranslator st = new SafeletTranslator();
				// st.setContext(new SCJApplication(ANALYSIS));
				// st.setTarget(new ClassTarget(elem));
				// st.setOutput(new Output());
				//
				// st.execute();

				// System.out.println("printing safelet class tree");
				// //get the class tree of the safelet
				// ClassTree tree = trees.getTree(elem);
				// System.out.println(tree.toString());
				//
				//
				//
				// System.out.println("Printing members class tree");
				// //this is a sub set of above
				// List<? extends Tree> members = tree.getMembers();
				//
				// System.out.println(members.toString());
				//
				//
				//
				// System.out.println("Iterator");
				// Iterator<? extends Tree> i = members.iterator();
				//
				// while (i.hasNext())
				// {
				// MethodTree o = (MethodTree) i.next();
				// System.out.println(o.getName());
				//
				// if (o.getName().contentEquals("getSequencer"))
				// {
				// System.out.println("in iterator");
				// List<? extends StatementTree> s =
				// o.getBody().getStatements();
				//
				// Iterator j = s.iterator();
				//
				// while(j.hasNext())
				// {
				// StatementTree st = (StatementTree) j.next();
				//
				// if(st instanceof ReturnTree )
				// {
				// System.out.println(((ReturnTree) st).getExpression() );
				// }
				// }
				//
				// }
				//
				// }
				//

				// SafeletTranslator st = new SafeletTranslator();
				// // st.setTarget(elem);
				// st.invoke();

				// return super.visitClass(tree, p);

				//
			}

			//
		}

		Name[] missionNames = null;

		if (names != null)
		{
			System.out.println("Mission Sequencer Visiting");
			for (int i = 0; i < names.length; i++)
			{
				// System.out.println(packagePrefix +names[i]);
				TypeElement elem = ANALYSIS.getTypeElement(packagePrefix
						+ names[i]);

				System.out.println("Visiting: " + elem);
				missionNames = elem.accept(new MissionSequencerLevel2Visitor(),
						null);
			}
		}

		Name[][] clusters = null;

		System.out.println(missionNames == null);

		if (missionNames != null)
		{
			System.out.println("Mission Visiting");
			for (int i = 0; i < missionNames.length; i++)
			{
				TypeElement elem = ANALYSIS.getTypeElement(packagePrefix
						+ missionNames[i]);
				System.out.println("Visiting: " + elem);

				clusters = elem.accept(new MissionLevel2Visitor(), null);
			}
		}

		System.out.println("Framework Printing");
		programEnv.output();
		
		
		programEnv.getMethod("getSequencer");
		
		
		  /* ------------------------------------------------------------------------ */    
        /* You should do this ONLY ONCE in the whole application life-cycle:        */    
    
        /* Create and adjust the configuration singleton */
        freemarker.template.Configuration cfg = new freemarker.template.Configuration();
//        cfg.setDirectoryForTemplateLoading(new File("/home/matt/Uni/Translation/test/templates"));
        cfg.setDirectoryForTemplateLoading(new File("/home/matt/Documents/Translation/test/templates"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        /* ------------------------------------------------------------------------ */    
        

        /* Create a data-model */
        Map root = programEnv.getSafelet().toMap();
//        root.put("author", "Matt");      
//        root.put("title", "Matt's Awesome PDF");
        

        /* Get the template (uses cache internally) */
        freemarker.template.Template temp = cfg.getTemplate("SafeletApp-Template.ftl");

        
        
        /* Variables for writing output to a file */
        File file = new File("/home/matt/Documents/Translation/test/output/SafeletApp.circus");
        FileOutputStream fop = new FileOutputStream(file);
		
			
        
        /* Merge data-model with template */
        Writer out = new OutputStreamWriter(fop);
        try
		{
			temp.process(root, out);
		} catch (TemplateException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        // Note: Depending on what `out` is, you may need to call `out.close()`.
        // This is usually the case for file output, but not for servlet output.
        
        /* Execute pdflatex on the output */
        try {
        String s = null;
        
        
        /* This executes pdflatex with the output directory of our translation on the latex file we've just written */
        System.out.println("Generating PDF...");
//        Process p = Runtime.getRuntime().exec("/usr/bin/pdflatex -output-directory /home/matt/Uni/Translation/test/output/ /home/matt/Uni/Translation/test/output/latexTest.tex");
        Process p = Runtime.getRuntime().exec("/usr/bin/pdflatex -output-directory /home/matt/Documents/Translation/test/output/ /home/matt/Documents/Translation/test/output/SafeletApp.tex");

        
        BufferedReader stdInput = new BufferedReader(new
        InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
        InputStreamReader(p.getErrorStream()));

        // read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        while ((s = stdInput.readLine()) != null) {
        System.out.println(s);
        }

        // read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) 
        {
        	System.out.println(s);
        }

   

		
	
		
		// Level2Translator circus_trans = new Level2Translator();
		//
		// circus_trans.setTarget((Target)t);
		//
		//
		//
		//
		// circus_trans.setOutput(new Output());
		//
		// circus_trans.execute(new SCJApplication(ANALYSIS));
		//
		// PostProcessor post = new PostProcessor();
		//
		// post.execute();

		System.exit(0);
		
        } catch (Exception e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
	}
}
