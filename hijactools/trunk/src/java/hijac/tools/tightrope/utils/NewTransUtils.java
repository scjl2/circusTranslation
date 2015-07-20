package hijac.tools.tightrope.utils;

import com.sun.source.tree.*;

import hijac.tools.modelgen.circus.utils.TransUtils;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/* There is more to do here. We should really use the templates and also
 * properly handler types that are represented by identifiers... */

/**
 * Utility methods for the low-level translation of Java into Circus.
 * 
 * @author Frank Zeyda
 * @version $Revision$
 */
public class NewTransUtils extends TransUtils
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

	//mine
	public static String encodeType(Tree returnTree)
	{
		String returnString = null;
		
		if(returnTree != null)
		{
		if (returnTree instanceof PrimitiveTypeTree)
		{

			TypeKind returnTypeKind = ((PrimitiveTypeTree) returnTree)
					.getPrimitiveTypeKind();
			
		

			switch (returnTypeKind)
			{
				case BOOLEAN:
					returnString = "\\boolean";
					break;
				case BYTE:
					returnString = "byte";
					break;
				case INT:
					returnString = "int";
					break;
				case LONG:
					returnString = "long";
					break;
				case FLOAT:
					returnString = "float";
					break;
				case DOUBLE:
					returnString = "double";
					break;
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
			} else if (s.contains("MissionSequencer")
					|| s.contains("OneShotEventHandler")
					|| s.contains("AperiodicEventHandler")
					|| s.contains("PeriodicEventHandler")
					|| s.contains("ManagedThread"))
			{
				returnString = "SchedulableID";
			}
			}
			
		}
		return returnString;
	}
	
	public static String encodeType(TypeMirror type)
	{
		/* Primitive Types */
		if (type.getKind().isPrimitive())
		{
			switch (type.getKind())
			{
				case BOOLEAN:
					return "boolean";
				case BYTE:
					return "byte";
				case INT:
					return "int";
				case LONG:
					return "long";
				case FLOAT:
					return "float";
				case DOUBLE:
					return "double";
				case CHAR:
					return "char";
			}
		}
		/* Declared Type */
		if (type.getKind() == TypeKind.DECLARED)
		{
			TypeElement type_element = (TypeElement) ((DeclaredType) type)
					.asElement();
			Name name = type_element.getSimpleName();
			/* Name name = type_element.getQualifiedName(); */
			/* The following is a rather a hack... Revised! */
			if (name.toString().equals("Mission"))
			{
				return "MissionId";
			}
			if (name.toString().equals("AperiodicEventHandler")
					|| name.toString().equals("AperiodicLongEventHandler")
					|| name.toString().equals("PeriodicLongEventHandler"))
			{
				return "HandlerId";
			}
			return encodeName(name) + "Class";
		}
		/* Null Type */
		if (type.getKind() == TypeKind.NULL)
		{
			return "\\universe";
		}
		/* Array Type */
		if (type.getKind() == TypeKind.ARRAY)
		{
			TypeMirror ctype = ((ArrayType) type).getComponentType();
			if (ctype.getKind() == TypeKind.ARRAY)
			{
				return "\\seq \\, (" + encodeType(ctype) + ")";
			} else
			{
				return "\\seq " + encodeType(ctype);
			}
		}
		/* Record a translation error here too. */
		System.out.println("/// failed type");
		return FAILED_RESULT;
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
				System.out.println("/// failed literal");
				return FAILED_RESULT;

			case STRING_LITERAL:
				return encodeString((String) node.getValue());

			case NULL_LITERAL:
				return "\\circnull";

			default:
				throw new AssertionError(
						"Unexpected Tree.Kind in LiteralTree: "
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
		return result.toString();
	}
}
