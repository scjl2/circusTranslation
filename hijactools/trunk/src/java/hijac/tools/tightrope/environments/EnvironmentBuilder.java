package hijac.tools.tightrope.environments;

import java.util.Set;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.application.TightRopeTest.MissionLevel2Visitor;
import hijac.tools.application.TightRopeTest.MissionSequencerLevel2Visitor;
import hijac.tools.application.TightRopeTest.SafeletLevel2Visitor;
import hijac.tools.config.Hijac;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;

public class EnvironmentBuilder
{
	public  SCJAnalysis analysis;

	private  Trees trees;
	private  Set<CompilationUnitTree> units;
	private  Set<TypeElement> type_elements;

	private final ProgramEnv programEnv;

	public EnvironmentBuilder(SCJAnalysis analysis, ProgramEnv programEnv)
	{
		this.analysis = analysis;
		this.programEnv = programEnv;
		
		trees = analysis.TREES;
		units = analysis.getCompilationUnits();
		type_elements = analysis.getTypeElements();
	}
	
	public void build()
	{
	
	
	Name[] names = null;
	String packagePrefix = null;

	// for all the types in the program
	for (TypeElement elem : type_elements)
	{
		// System.out.println(elem.toString());
		String elemID = elem.toString();
		// if the type we have is the safelet

		TypeMirror safelet_type = (TypeMirror) analysis.get(Hijac
				.key("SafeletType"));

		// System.out.println( safelet_type.toString() );

		// Set<TypeElement> supers = ANALYSIS.getAllSuperclasses(elem);
		// System.out.println(elem.getInterfaces().toString());
		// if( ANALYSIS.TYPES.isSubtype( elem.asType(), safelet_type) )

		if (elem.getInterfaces().toString().contains("Safelet"))
		// if (elemID.equalsIgnoreCase("accs.ACCSafelet") )
		{
			System.out.println("Found Safelet");

			packagePrefix = findPackagePrefix(elem);

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

	missionNames = buildMissionSequencers(names, packagePrefix, missionNames);

	Name[][] clusters = null;

	

	buildMissions(packagePrefix, missionNames);
	
	
	}

	private String findPackagePrefix(TypeElement elem)
	{
		String packagePrefix;
		packagePrefix = elem.getQualifiedName().toString();
		int firstIndex = packagePrefix.indexOf(elem.getSimpleName()
				.toString());
		packagePrefix = packagePrefix.substring(0, firstIndex);
		return packagePrefix;
	}

	private Name[] buildMissionSequencers(Name[] names, String packagePrefix,
			Name[] missionNames)
	{
		if (names != null)
		{
			System.out.println("Mission Sequencer Visiting");
			for (int i = 0; i < names.length; i++)
			{
				programEnv.addTopLevelMissionSequencer(names[i]);
				// System.out.println(packagePrefix +names[i]);
				TypeElement elem = analysis.getTypeElement(packagePrefix
						+ names[i]);

				System.out.println("Visiting: " + elem);
				missionNames = elem.accept(new MissionSequencerLevel2Visitor(),
						null);
			}
		}
		return missionNames;
	}

	private void buildMissions(String packagePrefix, Name[] missionNames)
	{
		Name[][] clusters;
		if (missionNames != null)
		{
			System.out.println("Mission Visiting");
			for (int i = 0; i < missionNames.length; i++)
			{
				
				TypeElement elem = analysis.getTypeElement(packagePrefix
						+ missionNames[i]);
				System.out.println("Visiting: " + elem);

				clusters = elem.accept(new MissionLevel2Visitor(), null);
			}
		}
	}
}