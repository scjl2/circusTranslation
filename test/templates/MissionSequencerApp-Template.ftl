\begin{zsection}
  \SECTION ~ ${MissionSequencerID}App ~ \parents ~ TopLevelMissionSequencerChan,\\
  \t1 MissionId, MissionIds, SchedulableId 
\end{zsection}
%\begin{circus}
%\circchannelset MissionSequencerAppSync == \\ \lchanset getNextMissionCall, getNextMissionRet,end\_sequencer\_app \rchanset
%\end{circus}

%\begin{circus}
%\circchannelset MySequencerAppChanSet == MissionSequencerAppSync
%\end{circus}

\begin{circus}
\circprocess ${MissionSequencerID}App \circdef \circbegin\\ 
\end{circus}
   
<#include "State-Template.ftl">

\begin{circusaction}
GetNextMission \circdef \\
\circblockopen
    getNextMissionCall~.~${MissionSequencerID} \then \\
	
	getNextMissionRet~.~${MissionSequencerID}~!~${MissionID}  \then \\
	\Skip
\circblockclose	
\end{circusaction}

\begin{circusaction}
Methods \circdef  \\
\circblockopen
	GetNextMission \\
\circblockclose 
\circseq Methods
\end{circusaction}

\begin{circusaction}
\circspot (Methods) %\circhide MissionSequencerAppStateSync
\circinterrupt (end\_sequencer\_app~.~${MissionSequencerID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
