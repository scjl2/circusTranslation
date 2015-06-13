<#list Methods as meth>
\begin{circusaction}
${meth}Meth \circdef \\
\circblockopen
${meth}Call~.~${MissionID} \then \\
~\\
${meth}Ret~.~${MissionID} \then \\
\Skip
\circblockclose
\end{circusaction}	
</#list>
%
<#list SyncMethods as meth>
\begin{circusaction}
${meth}SyncMeth \circdef \\
\circblockopen
${meth}Call~.~${MissionID}~?~thread \then \\
startSyncMeth~.~${MissionID}~.~thread \then \\
lockAcquired~.~${MissionID}~.~thread \then \\
~\\
endSyncMeth~.~${MissionID}~.~thread \then  \\
${meth}Ret~.~${MissionID}~.~thread \then \\
\Skip
\circblockclose
\end{circusaction}	
</#list>
%
