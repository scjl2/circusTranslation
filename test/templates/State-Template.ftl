<#if Variables.size > 0>
\begin{schema}{State}
<#list Variables as var>
  ${var.VarName} : ${VarType}\\ 
</#list>
\end{schema}
% 
\begin{circusaction}
\circstate State
\end{circusaction}
%
\begin{schema}{Init}
  State~'
\where
 <#list Variables as var>
  ${var.VarName}' :=${VarInit}\\ 
</#list>
\end{schema}
</#if>
