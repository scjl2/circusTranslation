\begin{zsection}
  \SECTION ~ ${ProcessName}App ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan, ${ProcessName}MethChan \\
 <#include "CommonImports-Template.ftl"> <#include "Parent-Template.ftl">
\end{zsection}




\begin{circus}
\circprocess ${ProcessName}App \circdef <#include "Params-Template.ftl"> \circbegin
\end{circus}

<#include "State-Template.ftl">

\begin{circusaction}
InitializePhase \circdef \\
\circblockopen
  initializeCall~.~${ProcessID} \then \\
<#list RegisteredSchedulables as SchedulableID >
	register~!~${SchedulableID}~!~${ProcessID} \then \\
</#list>
  initializeRet~.~${ProcessID} \then \\
  \Skip
\circblockclose
\end{circusaction}

\begin{circusaction}
CleanupPhase \circdef
<#if cleanupBody??>
\circvar ret : \boolean   \circspot \\
<#else>
\\
</#if>
\circblockopen
 cleanupMissionCall~.~${ProcessID} \then \\
<#if cleanupBody??>
 ${cleanupBody} \circseq \\
 cleanupMissionRet~.~${ProcessID}~!~ret \then \\
<#else>
 cleanupMissionRet~.~${ProcessID}~!~\true \then \\
</#if>
 \Skip
\circblockclose
\end{circusaction}

<#include "Methods-Template.ftl">

\begin{circusaction}
Methods \circdef
\circblockopen
	InitializePhase \\
	\extchoice \\
	CleanupPhase \\
<#include "MethodsAction-Template.ftl">
\circblockclose
\circseq Methods
\end{circusaction}

\begin{circusaction}
<#include "MainAction-Template.ftl"> \circinterrupt (end\_mission\_app~.~${ProcessID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
