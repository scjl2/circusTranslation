<#list Methods as meth>
\begin{circusaction}
\circ${meth.Access} ~ ${meth.MethodName} ~ \circdef
<#if meth.ReturnType != 'null'>
\circvar ret : ${meth.ReturnType} \circspot
</#if>
\\

${meth.Body} 

\end{circusaction}
</#list>
%
<#list SyncMethods as meth >
\begin{circusaction}
\circ${meth.Access} ~ \circsync ~ ${meth.MethodName} ~ \circdef
<#if meth.ReturnType != 'null'>
\circvar ret : ${meth.ReturnType} \circspot
</#if>
\\
${meth.Body}

\end{circusaction}
</#list>
%
