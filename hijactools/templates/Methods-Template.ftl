<#list Methods as meth>
\begin{circusaction}
${meth.MethodName}Meth \circdef
<#if meth.ReturnType != 'null'>
\circvar ret : ${meth.ReturnType} \circspot
</#if>
\\
\circblockopen
${meth.MethodName}Call~.~${ProcessID}<#if meth.ExternalAppmeth == true> ~?~caller</#if>
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
${meth.MethodName}Call~.~${ProcessID}<#if meth.ExternalAppmeth == true>~?~caller</#if>~?~thread
<#list meth.Parameters?keys as param>
~?~${param}
</#list> \then \\
\circblockopen
startSyncMeth~.~${ProcObjectID}~.~thread \then \\
lockAcquired~.~${ProcObjectID}~.~thread \then \\
${meth.Body} \circseq  \\
endSyncMeth~.~${ProcObjectID}~.~thread \then  \\

<#if meth.ReturnType != 'null'>
${meth.MethodName}Ret~.~${ProcessID}~.~thread~!~ret \then \\
<#else>
${meth.MethodName}Ret~.~${ProcessID}~.~thread \then \\
</#if>
\Skip
\circblockclose
\circblockclose
\end{circusaction}
</#list>
%
