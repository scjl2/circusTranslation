<#list Methods as meth>
\begin{circusaction}
${meth.MethodName}Meth \circdef 
<#if meth.ReturnType != 'null'>
\circvar ret : ${meth.ReturnType} \circspot
</#if>
\\
\circblockopen
${meth.MethodName}Call~.~${ProcessID} 
<#list meth.Parameters?keys as param>
~?~${param}
</#list>
\then \\
${meth.Body}  \circseq  \\

<#if meth.ReturnType != 'null'>
${meth.MethodName}Ret~.~${ProcessID}~!~ret \then \\
<#else>
${meth.MethodName}Ret~.~${ProcessID} \then \\
</#if>
\Skip
\circblockclose
\end{circusaction}	
</#list>
%
<#list SyncMethods as meth >
\begin{circusaction}
${meth.MethodName}SyncMeth \circdef 
<#if meth.ReturnType != 'null'>
\circvar ret : ${meth.ReturnType} \circspot
</#if>
\\
\circblockopen
${meth.MethodName}Call~.~${ProcessID}~?~thread 
<#list meth.Parameters?keys as param>
~?~${param}
</#list>
\then \\
startSyncMeth~.~${ProcessID}~.~thread \then \\
lockAcquired~.~${ProcessID}~.~thread \then \\
${meth.Body} \circseq  \\
endSyncMeth~.~${ProcessID}~.~thread \then  \\
<#if meth.ReturnType != 'VOID'>
${meth.MethodName}Ret~.~${ProcessID}~!~ret~!~thread \then \\
<#else>
${meth.MethodName}Ret~.~${ProcessID}~.~thread \then \\
</#if>
\Skip
\circblockclose
\end{circusaction}	
</#list>
%
