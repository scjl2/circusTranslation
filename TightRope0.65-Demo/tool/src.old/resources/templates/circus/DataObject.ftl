<#-- Template for Data Objects -->
<#include "CState.ftl">
<#include "DMethod.ftl">
\begin{circus}
  \circclass ~ ${class.processName}Class ~ \circdef ~ \circbegin
\end{circus}
%
<#if class.hasFields()>
  <@CState class.fields/>
%
</#if>
<#list class.dataOperations as method>
  <@DMethod method/>
%
</#list>
\begin{circus}
  \circend
\end{circus}
