package hijac.tools.tightrope.utils;

import hijac.tools.modelgen.circus.utils.LaTeX;

public final class TightRopeString
{
  public static class LATEX
  {
    public static final String DOT = "~.~";
    public static final String SHRIEK = "~!~";
    public static final String THEN = "\\then ";
    public static final String SKIP = "\\Skip";
    public static final String NEW_LINE = LaTeX.NEWLINE;
    public static final String RET_FALSE = "ret := \\false";
    public static final String RET_TRUE = "ret := \\true";
    public static final String ASSIGN = " :=";
    public static final String RPAR = "\\rpar";
    public static final String RCHANSET = " \\rchanset ";
    public static final String LPAR = "\\lpar ";
    public static final String LCHANSET = "\\lchanset ";
    public static final String INTERLEAVE = "\\interleave";
    public static final String UNDERSCORE = "\\_";
    public static final String CIRCNEW = "\\circnew ";
    public static final String CIRCREFTYPE = "\\circreftype ";

  }

  public static class Name
  {

    public static final String ID_STR = "ID";
    public static final String MID = "M" + ID_STR;
    public static final String SID = "S" + ID_STR;
    public static final String OBJ_ID = "O" + ID_STR;
    public static final String Thread_ID = "T" + ID_STR;
    public static final String MISSION_IDS = "MissionIds";
    public static final String MISSION_ID = "MissionId";
    public static final String NULL_S_ID = "nullSchedulableId";
    public static final String NULL_M_ID = "nullMissionId";
    public static final String BINDER = "binder" + LATEX.UNDERSCORE;
    public static final String CALLERS_STR = "Callers";
    public static final String CALLER_TYPE = "CallerType";
    public static final String LOCS = "Locs";
    public static final String LOC_TYPE = "LocType";
    public static final String CHANNEL_NAME = "ChannelName";
    public static final String METHOD_NAME = "MethodName";
    public static final String RETURN_TYPE = "ReturnType";
    public static final String RETURN_VALUE = "ReturnValue";
    public static final String ACCESS = "Access";
    public static final String BODY = "Body";
    public static final String PARAMETERS_STR = "Parameters";
    public static final String EXTERNAL_APPMETH = "ExternalAppmeth";
    public static final String ONE_SHOT_EVENT_HANDLER_QUALIFIED_NAME = "javax.safetycritical.OneShotEventHandler";
    public static final String APERIODIC_EVENT_HANDLER_QUALIFIED_NAME = "javax.safetycritical.AperiodicEventHandler";
    public static final String PERIODIC_EVENT_HANDLER_QUALIFIED_NAME = "javax.safetycritical.PeriodicEventHandler";
    public static final String MISSION_SEQUENCER_QUALIFIED_NAME = "javax.safetycritical.MissionSequencer";
    public static final String MANAGED_THREAD_QUALIFIED_NAME = "javax.safetycritical.ManagedThread";
    public static final String CLASS = "Class";
    public static final String CLASS_BRACKETS = "Class()";
    public static final String THIS = "this";

  }

  public static class Location
  {

    public static final String TYPE_TEMPLATE = "L2Type.ftl";
    public static final String EXPR_TEMPLATE = "L2Expr.ftl";
    public static final String STMT_TEMPLATE = "L2Stmt.ftl";

  }

  public static class ParadigmName
  {
    public static final String SAFELET = "Safelet";
    public static final String MISSION_SEQUENCER = "MissionSequencer";
    public static final String MISSION = "Mission";
    public static final String APERIODIC_EVENT_HANDLER = "AperiodicEventHandler";
    public static final String ONE_SHOT_EVENT_HANDLER = "OneShotEventHandler";
    public static final String PERIODIC_EVENT_HANDLER = "PeriodicEventHandler";
    public static final String MANAGED_THREAD = "ManagedThread";

  }

  public static class ObjectString
  {

    public static final String VAR_INPUT = "VarInput";
    public static final String VAR_INIT = "VarInit";
    public static final String VAR_TYPE = "VarType";
    public static final String VAR_NAME = "VarName";
    public static final String VARIABLES_STR = "Variables";
    public static final String INITED_VARIABLES = "InitedVariables";
    public static final String SYNC_METHODS = "SyncMethods";
    public static final String METHODS = "Methods";
    public static final String PROCESS_ID = "ProcessID";
    public static final String PROCESS_NAME = "ProcessName";
    public static final String IMPORT_NAME = "ImportName";
    public static final String HANDLER_TYPE = "HandlerType";
    public static final String PARENTS_STR = "Parents";
    public static final String ID = "ID";
    public static final String HAS_CLASS = "HasClass";

  }

  public static class ProgramOutput
  {

    public static final String FINDING_PROCESS_PARAMETERS = "+++ Finding Process Parameters +++";
    public static final String BUILDING_TOP_LEVEL_SEQUENCER = "+++ Building Top Level Sequencer +++";
    public static final String BUILD_MISSION = "+++ Build Mission: ";
    public static final String NO_MISSIONS = "+++ No Missions +++";
    public static final String BUILDING_SCHEDULABLE_MISSION_SEQUENCERS = "+++ Building Schedulable Mission Sequencers +++";
    public static final String BUILDING_SCHEDULABLE = "+++ Building Schedulable ";
    public static final String NO_SCHEDULABLES = "Has No Schedulables +++";
    public static final String END_PLUSES = " +++";
    public static final String BUILDING_MISSION = "+++ Building Mission ";

  }
}
