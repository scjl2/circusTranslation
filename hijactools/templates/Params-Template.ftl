<#if Parameters?has_content>
\\ \qquad 
<#list AppParameters as param>
  ${param.VarName} : ${param.VarType}
<#sep>
, \\ \qquad 
</#sep> 
</#list>
\circspot 
</#if>
