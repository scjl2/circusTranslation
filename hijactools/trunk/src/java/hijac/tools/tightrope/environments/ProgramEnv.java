package hijac.tools.tightrope.environments;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.modelgen.circus.SCJApplication;
import hijac.tools.modelgen.circus.visitors.AMethodVisitor;
import hijac.tools.modelgen.circus.visitors.MethodVisitorContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.TreeVisitor;

public class ProgramEnv
{
	FrameworkEnv frameworkEnv;
	List<NonParadigmEnv> nonParadigmObjectEnvs;
	SCJAnalysis context;

	public ProgramEnv(SCJAnalysis context)
	{
		this.frameworkEnv = new FrameworkEnv();
		this.nonParadigmObjectEnvs = new ArrayList<NonParadigmEnv>();
		this.context = context;
	}

	public FrameworkEnv getFrameworkEnv()
	{
		return frameworkEnv;
	}

	public void setFrameworkEnv(FrameworkEnv frameworkEnv)
	{
		this.frameworkEnv = frameworkEnv;
	}

	public List<NonParadigmEnv> getNonParadigmObjectEnvs()
	{
		return nonParadigmObjectEnvs;
	}

	public void setNonParadigmObjectEnvs(
			List<NonParadigmEnv> nonParadigmObjectEnvs)
	{
		this.nonParadigmObjectEnvs = nonParadigmObjectEnvs;
	}

	

	public void output()
	{
		System.out.println("*******************");
		System.out.println("Program Environment");
		System.out.println("-------------------");
		System.out.println("Framework Environment");
		System.out.println("_____________________");
		System.out.println(frameworkEnv.toString());
		System.out.println("----------------------");
		System.out.println("Non-Framework Environments");
		System.out.println("___________________________");
		System.out.println(nonParadigmObjectEnvs.toString());
		System.out.println("--------------------------------");

	}

	public void addSafelet(Name safelet)
	{
		frameworkEnv.addSafelet(safelet);
	}

	public void addTopLevelMissionSequencer(Name topLevelMissionSequencer)
	{
		frameworkEnv.addTopLevelMissionSequencer(topLevelMissionSequencer);
	}

	public ParadigmEnv getSafelet()
	{
		return frameworkEnv.getControlTier().getSafeletEnv();
	}
	
	public void getMethod(String methodName)
	{
		
		
//		Name n = getSafelet().getmethodName(;
//		if (n != null)
//		{
		
		TypeElement te = null;
		
		for (TypeElement e : context.getTypeElements())
		{
			if (e.getSimpleName().contentEquals("FlatBuffer"))
			{
				te = e;
			}
		}
				
			
			
			ClassTree ct = context.TREES.getTree(te);
			
			List<StatementTree> members = (List<StatementTree>) ct.getMembers();
			Iterator<StatementTree> i = members.iterator();
			while (i.hasNext())
			{
				MethodTree o = (MethodTree) i.next();
				if (o.getName().contentEquals("getSequencer"))
				{
//					o.accept(new AMethodVisitor(new SCJApplication(context)) , null);
					
				}
			}
			
			
//		}
	}

}
