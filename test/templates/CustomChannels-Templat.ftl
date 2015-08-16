\begin{zsection}
  \SECTION ~ ${ProcessID}MethChan ~ \parents ~ scj\_prelude, GlobalTypes 
\end{zsection}
%
<#list Methods as meth>
\begin{circus}
\t1 \circchannel ${meth.methodName}Call <#if meth.parameters?size != 0>:
<#list meth.parameters?values as param>
${param} <#if para_has_next> \cross </#if>
</#list> </#if> \\
<#if meth.returnType != 'null'>
\t1 \circchannel ${meth.methodName}Ret : ${meth.returnType} \\
<#else>
\t1 \circchannel ${meth.methodName}Ret \\
</#if>    
\end{circus}
%
</#list>
%
<#list SyncMethods as meth>
\begin{circus}
\t1 \circchannel ${meth.methodName}Call <#if meth.parameters?size != 0>:
<#list meth.parameters?values as param>
${param}<#if para_has_next>\cross</#if>
</#list> </#if>\\
  
<#if meth.returnType != 'null'>
\t1 \circchannel ${meth.methodName}Ret : ${meth.returnType} \\
<#else>
\t1 \circchannel ${meth.methodName}Ret \\
</#if>  
\end{circus}
% 
</#list>
