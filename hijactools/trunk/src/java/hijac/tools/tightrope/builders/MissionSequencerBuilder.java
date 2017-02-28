package hijac.tools.tightrope.builders;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.ClassEnv;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.MissionSequencerEnv;
import hijac.tools.tightrope.environments.ObjectEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.VariableEnv;
import hijac.tools.tightrope.utils.Debugger;
import hijac.tools.tightrope.utils.TightRopeTransUtils;
import hijac.tools.tightrope.visitors.MethodVisitor;
import hijac.tools.tightrope.visitors.ReturnVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

public class MissionSequencerBuilder extends ParadigmBuilder
{

	private Trees trees;
	private ReturnVisitor returnVisitor;
	private MissionSequencerEnv sequencerEnv;
	private HashMap<Name, Tree> varMap;
	private MethodVisitor methodVisitor;

	public MissionSequencerBuilder(ProgramEnv programEnv,
			MissionSequencerEnv sequencerEnv, SCJAnalysis analysis,
			EnvironmentBuilder environmentBuilder)
	{
		super(analysis, programEnv, environmentBuilder);

		ParadigmBuilder.analysis = analysis;
		ParadigmBuilder.programEnv = programEnv;
		this.sequencerEnv = sequencerEnv;

		trees = analysis.TREES;

		methodVisitor = new MethodVisitor(analysis, sequencerEnv);
	}
	
	// TODO Tuning: have this method accept an empty ArrayList to fill
	@SuppressWarnings("unchecked")
	public ArrayList<Name> build(TypeElement arg0)
	{
		Debugger.log("");
		Debugger.log("+++ Mission Sequencer Variables +++");
		Debugger.log("");

		assert (sequencerEnv != null);
		
		addParents();
		

		varMap = getVariables(arg0, sequencerEnv);

//		for (Name n : varMap.keySet())
//		{
//			Debugger.log("+++ Variable " + n + " = " + varMap.get(n));
//		}

		ArrayList<Name> missions = new ArrayList<Name>();

		ClassTree ct = trees.getTree(arg0);

		@SuppressWarnings("unchecked")
		List<StatementTree> members = (List<StatementTree>) ct.getMembers();

		Iterator<StatementTree> i = members.iterator();
//		assert(varMap != null);
		returnVisitor = new ReturnVisitor(varMap);
		MethodEnv m;
		while (i.hasNext())
		{
			m = null;
			Tree tlst = i.next();

			if (tlst instanceof VariableTree)
			{
				Debugger.log("MS VIsitor: Variable Tree Found");

				VariableTree vt = (VariableTree) tlst;

				Debugger.log("-> " + vt.toString());
				Debugger.log("-> Name:" + vt.getName());
				Debugger.log("-> Type: " + vt.getType());

			}

			if (tlst instanceof MethodTree)
			{

				MethodTree o = (MethodTree) tlst;

				Name methodName = o.getName();
				Debugger.log("MS Visitor Method Tree = " + methodName);

				List<StatementTree> methodStatements = (List<StatementTree>) o.getBody()
						.getStatements();

				addDeferredParameters(methodStatements, varMap, (ObjectEnv) sequencerEnv);

				if (methodName.contentEquals("<init>"))
				{
					//methodVisitor.visitMethod(o, true);
				//	o.accept(returnVisitor, false);
				  extractProcessParameters(o, sequencerEnv);
				}
				else
				{

					final boolean isGetNextMissionMethod = methodName
							.contentEquals("getNextMission");

					final boolean notIgnoredMethod = !(methodName.contentEquals("<init>"));

					if (isGetNextMissionMethod)
					{
						ArrayList<Name> getNextReturns = null;

						getNextReturns = o.accept(returnVisitor, false);

						if (getNextReturns != null)
						{
							missions.addAll(getNextReturns);
						}

						m = methodVisitor.visitMethod(o, false, varMap);
						setMethodAccess(m, o);

						
						sequencerEnv.getClassEnv().addMeth(m);
						
					}
					else
					{// ADD METHOD TO MISSION ENV
						sequencerEnv.setObjectId(sequencerEnv.getName().toString());
						m = methodVisitor.visitMethod(o, false, varMap);
						setMethodAccess(m, o);

						final boolean isSyncMethod = o.getModifiers().getFlags()
								.contains(Modifier.SYNCHRONIZED);
						if (isSyncMethod)
						{
							sequencerEnv.addSyncMeth(m);
						}
						else
						{
							if (notIgnoredMethod)
							{
								sequencerEnv.addMeth(m);
							}
						}
					}
				}
			}
		}

		return missions;
	}
	
	public void addParents()
	{
		ClassEnv msClassEnv = sequencerEnv.getClassEnv();
		msClassEnv.addParent(hijac.tools.tightrope.utils.TightRopeString.Name.MISSION_ID);
		msClassEnv.addParent(hijac.tools.tightrope.utils.TightRopeString.Name.MISSION_IDS);
	}

  protected void extractProcessParameters(MethodTree methodTree, ObjectEnv object)
  {
  	for (VariableTree vt : methodTree.getParameters())
  	{
  
  		VariableEnv parameter = new VariableEnv();
  
  		parameter.setName(vt.getName().toString());
  		parameter.setType(TightRopeTransUtils.encodeType(vt.getType()));
   		parameter.setProgramType(TightRopeTransUtils.encodeType(vt.getType()));
  
  		final boolean ignoredParameter = 
  		    parameter.getType().endsWith("Parameters")
  				|| parameter.getType().contains("String");
  
  		if (!ignoredParameter)
  		{
  			object.addProcParameter(parameter);
  			
  			if(object.getVariable(parameter.getName()) != null)
        {
          object.removeVariable(parameter.getName());
        }
  		}  
  	}
  }
}