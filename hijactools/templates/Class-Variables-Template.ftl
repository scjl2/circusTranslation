<#if Variables?size != 0>
\begin{schema}{\circstateignore State}
<#list Variables as var>
  ${var.VarName} : ${var.VarType}\\ 
</#list>
\end{schema}
% 
\begin{circusaction}
\circstate State
\end{circusaction}
%
\begin{schema}{\circinitial Init}
  State~'
\where
<#list InitedVariables as var>
  ${var.VarName}' = ${var.VarInit}\\ 
</#list>
\end{schema}
</#if>
