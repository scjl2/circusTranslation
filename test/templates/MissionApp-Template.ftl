
%\begin{circus}
%\circchannelset MissionAppSync == \\
%	\lchanset initializeCall,register, initializeRet,cleanupMissionCall, cleanupMissionRet  \rchanset
%\end{circus}

%\begin{circus}
%\circchannelset MissionAppChanSet == \\
% \lchanset initializeCall, initializeRet, cleanupMissionCall, cleanupMissionRet,

<#list RegisteredSchedulables as SchedulableID >
%    register~.~${SchedulableID}~.~${MissionID} 
</#list>
%\rchanset
%\end{circus}

\begin{circus}
\circprocess MainMissionApp \circdef
\end{circus}

\begin{circusaction}
InitializePhase \circdef \\
\circblockopen
  initializeCall~.~${MissionID} \then \\
  
<#list RegisteredSchedulables as SchedulableID >
	register~!~${SchedulableID}~!~${MissionID} \then   \\
</#list>


  initializeRet~.~${MissionID} \then \\
  \Skip
\circblockclose 
\end{circusaction}
 
\begin{circusaction}
CleanupPhase \circdef  \\
\circblockopen
 cleanupMissionCall~.~${MissionID} \then \\
 cleanupMissionRet~.~${MissionID}~?~false \then \\
 \Skip
\circblockclose
\end{circusaction}

\begin{circusaction}
Methods \circdef \\
\circblockopen
	InitializePhase \\
	\extchoice \\
	CleanupPhase
\circblockclose 
\circseq Methods
\end{circusaction}

\begin{circus}
\circspot (Methods) \circinterrupt (end\_mission\_app~.~${MissionID} \then \Skip)

\end{circus}
