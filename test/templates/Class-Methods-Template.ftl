<#list Methods as meth>
\begin{circusaction}
\circ${meth.access} ~ ${meth.methodName} ~ \circdef 
<#if meth.returnType != 'null'>
\circvar ret : ${meth.returnType} \circspot
</#if>
\\ 

${meth.body} 

\end{circusaction}	
</#list>
%
<#list SyncMethods as meth >
\begin{circusaction}
\circ${meth.access} ~ \circsync ~ ${meth.methodName} ~ \circdef 
<#if meth.returnType != 'null'>
\circvar ret : ${meth.returnType} \circspot 
</#if>
\\
${meth.body} 

\end{circusaction}	
</#list>
%
