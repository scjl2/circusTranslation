package hijac.tools.tightrope.utils;

import hijac.tools.modelgen.circus.utils.TransUtils;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;

import com.sun.source.doctree.IdentifierTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;

/* There is more to do here. We should really use the templates and also
 * properly handler types that are represented by identifiers... */

/**
 * Utility methods for the low-level translation of Java into Circus. Extends
 * <code>hijac.tools.modelgen.circus.utils.TransUtils</code>
 * 
 * @author Matt Luckcuck
 * @version $Revision$
 */
public class TightRopeTransUtils extends TransUtils
{
	public static final String FAILED_RESULT = "\\invalid";
	
	

	public static String encodeName(String name)
	{
		String result = name;
		result = result.replace("_", "\\_");
		result = result.replace(".", "\\_");
		result = result.replace("$", "\\$");
		return result;
	}

	public static String encodeName(Name name)
	{
		return encodeName(name.toString());
	}

	// mine
	public static String encodeType(Tree returnTree)
	{
		String returnString = null;

		if (returnTree != null)
		{
			if (returnTree.toString().equalsIgnoreCase("double"))
			{
				returnString = "\\power \\arithmos";
			}
			else if (returnTree instanceof PrimitiveTypeTree)
			{

				TypeKind returnTypeKind = ((PrimitiveTypeTree) returnTree)
						.getPrimitiveTypeKind();

				switch (returnTypeKind)
				{
					case BOOLEAN:
						returnString = "\\boolean";
						break;
					// I changed this, rather a coarse coverage of these three.
					// Will not produce good model checking results
					case BYTE:
					case INT:
					case LONG:
						returnString = "\\num";
						break;
					case FLOAT:
						returnString = "\\real";
						break;
					case DOUBLE:
						// returnString = "\\mathbb{R}";
						returnString = " \\real";
						break;
						//TODO URM....
					case CHAR:
						returnString = "char";
						break;

					default:
						break;
				}
			}
			else
			{
				String s = returnTree.toString();

				if (s.contains("Mission"))
				{
					returnString = "MissionID";
				}
				else if (s.contains("MissionSequencer")
						|| s.contains("OneShotEventHandler")
						|| s.contains("AperiodicEventHandler")
						|| s.contains("PeriodicEventHandler")
						|| s.contains("ManagedThread"))
				{
					returnString = "SchedulableID";
				}
				else
				{
					returnString = returnTree.toString();

				}

			}

		}
		return returnString;
	}

	public static String encodeLiteral(LiteralTree node)
	{
		switch (node.getKind())
		{
			case BOOLEAN_LITERAL:
				return ((Boolean) node.getValue()) ? "\\true" : "\\false";

			case CHAR_LITERAL:
			case INT_LITERAL:
			case LONG_LITERAL:
				return node.getValue().toString();

			case FLOAT_LITERAL:
			case DOUBLE_LITERAL:
				return "\\power \\arithmos";

			case STRING_LITERAL:
				return encodeString((String) node.getValue());

			case NULL_LITERAL:
				return "\\circnull";

			default:
				throw new AssertionError("Unexpected Tree.Kind in LiteralTree: "
						+ node.getKind());
		}
	}

	public static String encodeString(String str)
	{
		StringBuilder result = new StringBuilder();
		result.append("\\langle");
		result.append(" ");
		for (int index = 0; index < str.length(); index++)
		{
			result.append((int) str.charAt(index));
			if (index != str.length() - 1)
			{
				result.append(", ");
			}
		}
		result.append(" ");
		result.append("\\rangle");
		// return result.toString();
		return "";
	}

	
}
