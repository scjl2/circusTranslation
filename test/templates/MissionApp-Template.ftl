\begin{zsection}
  \SECTION ~ ${ProcessID}App ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}
%\begin{circus}
%\circchannelset ${ProcessID}AppSync == \\
%	\lchanset initializeCall,register, initializeRet,cleanupMissionCall, cleanupMissionRet  \rchanset
%\end{circus}

%\begin{circus}
%\circchannelset ${ProcessID}AppChanSet == \\
% \lchanset initializeCall, initializeRet, cleanupMissionCall, cleanupMissionRet,

<#list RegisteredSchedulables as SchedulableID >
%    register~.~${SchedulableID}~.~${ProcessID} 
</#list>
%\rchanset
%\end{circus}

\begin{circus}
\circprocess ${ProcessID}App \circdef \circbegin
\end{circus}

<#include "State-Template.ftl">

\begin{circusaction}
InitializePhase \circdef \\
\circblockopen
  initializeCall~.~${ProcessID} \then \\
  
<#list RegisteredSchedulables as SchedulableID >
	register~!~${SchedulableID}~!~${ProcessID} \then   \\
</#list>


  initializeRet~.~${ProcessID} \then \\
  \Skip
\circblockclose 
\end{circusaction}
 
\begin{circusaction}
CleanupPhase \circdef  \\
\circblockopen
 cleanupMissionCall~.~${ProcessID} \then \\

 cleanupMissionRet~.~${ProcessID}~!~\false \then \\
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
\circspot (<#if Variables?size != 0> Init \circseq</#if> Methods) \circinterrupt (end\_mission\_app~.~${ProcessID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
