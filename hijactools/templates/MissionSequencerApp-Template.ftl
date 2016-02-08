\begin{zsection}
  \SECTION ~ ${ProcessName}App ~ \parents ~ TopLevelMissionSequencerChan,\\
  \t1 MissionId, MissionIds, SchedulableId, SchedulableIds  <#include "CommonImports-Template.ftl">
  \t1 <#include "Parent-Template.ftl">
\end{zsection}
%\begin{circus}
%\circchannelset ${ProcessName}AppSync == \\ \lchanset getNextMissionCall, getNextMissionRet,end\_sequencer\_app \rchanset
%\end{circus}

%\begin{circus}
%\circchannelset ${ProcessName}AppChanSet == ${ProcessID}AppSync
%\end{circus}

\begin{circus}
\circprocess ${ProcessName}App \circdef <#include "Params-Template.ftl"> \circbegin\\
\end{circus}

<#include "State-Template.ftl">

\begin{circusaction}
GetNextMission \circdef \circvar ret : MissionID \circspot \\
\circblockopen
    getNextMissionCall~.~${ProcessID} \then \\
	   ret := this~.~getNextMission() \circseq \\
    getNextMissionRet~.~${ProcessID}~!~ret  \then \\
\Skip
\circblockclose
\end{circusaction}

<#include "Methods-Template.ftl">

\begin{circusaction}
Methods \circdef  \\
\circblockopen
	GetNextMission \\
<#include "MethodsAction-Template.ftl">
\circblockclose
\circseq Methods
\end{circusaction}

\begin{circusaction}
<#include "MainAction-Template.ftl">  %\circhide MissionSequencerAppStateSync
\circinterrupt (end\_sequencer\_app~.~${ProcessID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
