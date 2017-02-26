<#list Methods as meth>
\begin{circusaction}
\circ${meth.Access} ~ ${meth.MethodName} ~ \circdef
<#if meth.Parameters?size gt 0>
  \circvar
  <#list meth.Parameters as param, type>
    ${param} : ${type}
    <#sep>, \\</#sep>
    </#list>
<#if meth.ReturnType != 'null'>
 ret : ${meth.ReturnType}
</#if>
  \circspot \\
</#if>
\\
${meth.Body}
\end{circusaction}
</#list>
%
<#list SyncMethods as meth >
\begin{circusaction}
\circ${meth.Access} ~ \circsync ~ ${meth.MethodName} ~ \circdef
<#if meth.Parameters?size gt 0>
  \circvar
  <#list meth.Parameters as param, type>
    ${param} : ${type}
    <#sep>, \\</#sep>
    </#list>
<#if meth.ReturnType != 'null'>
 ret : ${meth.ReturnType}
</#if>
  \circspot \\
</#if>
\\
${meth.Body}

\end{circusaction}
</#list>
%
