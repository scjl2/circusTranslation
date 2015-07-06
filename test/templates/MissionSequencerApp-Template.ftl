\begin{zsection}
  \SECTION ~ ${ProcessID}App ~ \parents ~ TopLevelMissionSequencerChan,\\
  \t1 MissionId, MissionIds, SchedulableId 
\end{zsection}
%\begin{circus}
%\circchannelset ${ProcessID}AppSync == \\ \lchanset getNextMissionCall, getNextMissionRet,end\_sequencer\_app \rchanset
%\end{circus}

%\begin{circus}
%\circchannelset ${ProcessID}AppChanSet == ${ProcessID}AppSync
%\end{circus}

\begin{circus}
\circprocess ${ProcessID}App \circdef \circbegin\\ 
\end{circus}
   
<#include "State-Template.ftl">

\begin{circusaction}
GetNextMission \circdef \\
\circblockopen
    getNextMissionCall~.~${ProcessID} \then \\
	
	getNextMissionRet~.~${ProcessID}~!~${MissionID}  \then \\
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
\circspot (Methods) %\circhide MissionSequencerAppStateSync
\circinterrupt (end\_sequencer\_app~.~${ProcessID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
