<#list Methods as meth>
\begin{circusaction}
${meth.methodName}Meth \circdef 
<#if meth.returnType != 'null'>
\circvar ret : ${meth.returnType} \circspot
</#if>
\\
\circblockopen
${meth.methodName}Call~.~${ProcessID} 
<#list meth.Parameters?keys as param>
~?~${param}
</#list>
\then \\
${meth.body}  \circseq  \\

<#if meth.returnType != 'null'>
${meth.methodName}Ret~.~${ProcessID}~!~ret \then \\
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
${meth.methodName}SyncMeth \circdef 
<#if meth.returnType != 'null'>
\circvar ret : ${meth.returnType} \circspot
</#if>
\\
\circblockopen
${meth.methodName}Call~.~${ProcessID}~?~thread 
<#list meth.Parameters?keys as param>
~?~${param}
</#list>
\then \\
startSyncMeth~.~${ProcessID}~.~thread \then \\
lockAcquired~.~${ProcessID}~.~thread \then \\
${meth.body} \circseq  \\
endSyncMeth~.~${ProcessID}~.~thread \then  \\
<#if meth.returnType != 'VOID'>
${meth.methodName}Ret~.~${ProcessID}~!~ret~!~thread \then \\
<#else>
${meth.methodName}Ret~.~${ProcessID}~.~thread \then \\
</#if>
\Skip
\circblockclose
\end{circusaction}	
</#list>
%
