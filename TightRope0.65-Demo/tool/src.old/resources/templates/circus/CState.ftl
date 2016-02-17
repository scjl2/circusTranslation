<#-- Template for generation the Class State -->
<#include "Modifiers.ftl">

<#macro CState fields>
\begin{schema}{\circstateignore ~ State}
<#list fields as field>
  <@FieldModifiers field/>${field} : ${field.type}<#if field_has_next>\\</#if>
</#list>
\end{schema}
%
\begin{comment}
\begin{circusaction}
  \circstate ~ State
\end{circusaction}
\end{comment}
</#macro>
