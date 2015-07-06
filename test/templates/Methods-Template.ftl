<#list Methods as meth>
\begin{circusaction}
${meth.methodName}Meth \circdef \\
\circblockopen
${meth.methodName}Call~.~${ProcessID} 
<#list meth.parameters?keys as param>
~?~${param}
</#list>
\then \\
~\\
<#if meth.returnType != 'VOID'>
${meth.methodName}Ret~.~${ProcessID}~.~${meth.returnType} \then \\
<#else>
${meth.methodName}Ret~.~${ProcessID} \then \\
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
${meth.methodName}Call~.~${ProcessID}~?~thread 
<#list meth.parameters?keys as param>
~?~${param}
</#list>
\then \\
startSyncMeth~.~${ProcessID}~.~thread \then \\
lockAcquired~.~${ProcessID}~.~thread \then \\
~\\
endSyncMeth~.~${ProcessID}~.~thread \then  \\
<#if meth.returnType != 'VOID'>
${meth.methodName}Ret~.~${ProcessID}~.~${meth.returnValue}~.~thread \then \\
<#else>
${meth.methodName}Ret~.~${ProcessID}~.~thread \then \\
</#if>
\Skip
\circblockclose
\end{circusaction}	
</#list>
%
