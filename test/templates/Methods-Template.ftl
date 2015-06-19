<#list Methods as meth>
\begin{circusaction}
${meth}Meth \circdef \\
\circblockopen
${meth}Call~.~${MissionID} \then \\
~\\
<#if meth.returnType != 'VOID'>
${meth}Ret~.~${MissionID}~.~${meth.returnType} \then \\
<#else>
${meth}Ret~.~${MissionID} \then \\
</#if>
\Skip
\circblockclose
\end{circusaction}	
</#list>
%
<#list SyncMethods as meth >
\begin{circusaction}
${meth.methodName}SyncMeth \circdef \\
\circblockopen
${meth.methodName}Call~.~${MissionID}~?~thread \then \\
startSyncMeth~.~${MissionID}~.~thread \then \\
lockAcquired~.~${MissionID}~.~thread \then \\
~\\
endSyncMeth~.~${MissionID}~.~thread \then  \\
<#if meth.returnType != 'VOID'>
${meth.methodName}Ret~.~${MissionID}~.~${meth.returnType}~.~thread \then \\
<#else>
${meth.methodName}Ret~.~${MissionID}~.~thread \then \\
</#if>
\Skip
\circblockclose
\end{circusaction}	
</#list>
%
