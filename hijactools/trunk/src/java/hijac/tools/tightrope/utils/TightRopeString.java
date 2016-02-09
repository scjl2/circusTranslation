package hijac.tools.tightrope.utils;

public final class TightRopeString
{
	public static class LATEX
	{
		public static final String DOT = "~.~";
		public static final String SHRIEK = "~!~";
		public static final String THEN = "\\then ";
		public static final String SKIP = "\\Skip";
		public static final String NEW_LINE = " \\\\ ";
		public static final String RET_FALSE = "ret := \\false";
		public static final String RET_TRUE = "ret := \\true";
		public static final String ASSIGN = " :=";
		public static final String RPAR = "\\rpar";
		public static final String RCHANSET = " \\rchanset ";
		public static final String LPAR = "\\lpar ";
		public static final String LCHANSET = "\\lchanset ";
		public static final String INTERLEAVE = "\\interleave";
		
	}
	
	public static class Name
	{

		public static final String ID_STR = "ID";
		public static final String MID = "M"+ID_STR;
		public static final String SID = "S"+ID_STR;
		public static final String OBJ_ID = "O"+ID_STR;
		public static final String Thread_ID = "T"+ID_STR;
		public static final String MISSION_IDS = "MissionIds";
		public static final String MISSION_ID = "MissionId";
		public static final String NULL_S_ID = "nullSchedulableId";
		public static final String NULL_M_ID = "nullMissionId";
		public static final String BINDER = "binder\\_";
		
	}
	
	public static class Location
	{

		public static final String TYPE_TEMPLATE = "L2Type.ftl";
		public static final String EXPR_TEMPLATE = "L2Expr.ftl";
		public static final String STMT_TEMPLATE = "L2Stmt.ftl";
		
	}

	
	
}
