\begin{zsection}
  \SECTION ~ ${ProcessID}App ~ \parents ~ TopLevelMissionSequencerChan,\\
  \t1 MissionId, MissionIds, SchedulableId  <#include "CommonImports-Template.ftl">
\end{zsection}
%\begin{circus}
%\circchannelset ${ProcessID}AppSync == \\ \lchanset getNextMissionCall, getNextMissionRet,end\_sequencer\_app \rchanset
%\end{circus}

%\begin{circus}
%\circchannelset ${ProcessID}AppChanSet == ${ProcessID}AppSync
%\end{circus}

\begin{circus}
\circprocess ${ProcessID}App \circdef <#include "Params-Template.ftl"> \circbegin\\ 
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
