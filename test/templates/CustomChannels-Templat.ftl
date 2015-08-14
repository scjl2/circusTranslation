\begin{zsection}
  \SECTION ~ ${ProcessID}MethChan ~ \parents ~ scj\_prelude, GlobalTypes 
\end{zsection}
%
<#if Methods?size > 0>
\begin{circus}
<#list Methods as meth>
  \circchannel ${meth.methodName}CalL
  <#if meth.parameters?size > 0> 
  :
<#list meth.parameters?values as param>
${param}
<#if para_has_next>
\cross
</#if>
</#list>  
\\
</#if>
  
<#if meth.returnType != 'null'>
  \circchannel ${meth.methodName}Ret : ${meth.returnType} \\
<#else>
  \circchannel ${meth.methodName}Ret \\
</#if>  
  

</#list>
\end{circus}
</#if>
%
<#if SyncMethods?size > 0>
\begin{circus}
<#list SyncMethods as meth>
  \circchannel ${meth.methodName}CalL
  <#if meth.parameters?size > 0> 
  :
<#list meth.parameters?values as param>
${param}
<#if para_has_next>
\cross
</#if>
</#list>  
\\
</#if>
  
<#if meth.returnType != 'null'>
  \circchannel ${meth.methodName}Ret : ${meth.returnType} \\
<#else>
  \circchannel ${meth.methodName}Ret \\
</#if>  
  

</#list>
\end{circus}
</#if>





