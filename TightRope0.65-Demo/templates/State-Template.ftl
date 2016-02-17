<#if Variables?size != 0>
\begin{schema}{State}
<#list Variables as var>
  ${var.VarName} : ${var.VarType}\\ 
</#list>
\end{schema}
% 
\begin{circusaction}
\circstate State
\end{circusaction}
%
\begin{schema}{Init}
  State~' \\
<#list Variables as var>
<#if var.VarInput>
${var.VarInit} : ${var.VarType}
</#if>	
</#list>
\where
 <#list Variables as var>
  ${var.VarName}' = ${var.VarInit}\\ 
</#list>
\end{schema}
</#if>
