
%\begin{circus}
%\circchannelset MissionSequencerAppSync == \\ \lchanset getNextMissionCall, getNextMissionRet,end\_sequencer\_app \rchanset
%\end{circus}

%\begin{circus}
%\circchannelset MySequencerAppChanSet == MissionSequencerAppSync
%\end{circus}

\begin{circus}
\circprocess ${MissionSequencerID}App \circdef \\ 
\end{circus}
   
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
 
\begin{circus}
\circspot (Methods) %\circhide MissionSequencerAppStateSync
\circinterrupt (end\_sequencer\_app~.~${MissionSequencerID} \then \Skip)
\end{circus}
