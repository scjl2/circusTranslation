package hijac.tools.tightrope.visitors;

import hijac.tools.analysis.SCJAnalysis;
import hijac.tools.tightrope.environments.MethodEnv;
import hijac.tools.tightrope.environments.ProgramEnv;
import hijac.tools.tightrope.environments.SafeletEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

public class SafeletLevel2Visitor 
{

	ProgramEnv programEnv;
	SCJAnalysis analysis;

	private Trees trees;
	private ReturnVisitor returnVisitor = new ReturnVisitor(null);
	private SafeletEnv safeletEnv;
	

	public SafeletLevel2Visitor(ProgramEnv programEnv, SCJAnalysis analysis)
	{
		this.analysis = analysis;
		this.programEnv = programEnv;

		trees = analysis.TREES;
		analysis.getCompilationUnits();
		analysis.getTypeElements();
		
		safeletEnv = programEnv.getSafelet();
	}


	@SuppressWarnings("unchecked")
	public ArrayList<Name> visitType(TypeElement e, Void p)
	{
		// System.out.println(e);

		ClassTree ct = trees.getTree(e);

		// programEnv.getSafelet().setClassTree(ct);

		List<StatementTree> members = (List<StatementTree>) ct.getMembers();
		Iterator<StatementTree> i = members.iterator();
		while (i.hasNext())
		{
			Object obj = i.next();

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
				ArrayList<Name> returns = mt.accept(new ReturnVisitor(null),
						null);

				@SuppressWarnings("rawtypes")
				Map paramMap = new HashMap();
				for (VariableTree vt : mt.getParameters())
				{
					paramMap.put(vt.getName().toString(), vt.getType());
				}

				if (mt.getName().contentEquals("initializeApplication"))
				{
					safeletEnv.addMeth(mt.getName(), typeKind,
							returns, paramMap);
				}
				else
				{
					{
						// ADD METHOD TO ENV
						MethodVisitor methodVisitor = new MethodVisitor(analysis, safeletEnv);
						if (mt.getModifiers().getFlags()
								.contains(Modifier.SYNCHRONIZED))
						{
							MethodEnv m= methodVisitor.visitMethod(mt, null);
							setMethodAccess( m,  mt);
							safeletEnv.addSyncMeth(m);
						} 
						else if( !(mt.getName().contentEquals("getSequencer") || mt.getName().contentEquals("<init>") || mt.getName().contentEquals("getLevel")    ) ) 
						{
							MethodEnv m = methodVisitor.visitMethod(mt, null);
							setMethodAccess( m,  mt);
							safeletEnv.addMeth(m);
						}
					}
				}
				if (mt.getName().contentEquals("getSequencer"))
				{
					
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
				
			}

		}

		return null;
	}
	
	private void setMethodAccess(MethodEnv m, MethodTree o)
	{
		ModifiersTree modTree = o.getModifiers();
		Set<Modifier> flags = modTree.getFlags();
		
//				m.setSynchronised(flags.contains(Modifier.SYNCHRONIZED));
		
		if(flags.contains(Modifier.PUBLIC))
		{
			m.setAccess(MethodEnv.AccessMod.PUBLIC);
		}
		else if (flags.contains(Modifier.PRIVATE))
		{
			m.setAccess(MethodEnv.AccessMod.PRIVATE);
		}
		else if (flags.contains(Modifier.PROTECTED))
		{
			m.setAccess(MethodEnv.AccessMod.PROTECTED);
		}
	}
}