<#if Parameters?has_content>
\\ \qquad 
<#list Parameters as param>
  ${param.VarName} : ${param.VarType}
<#sep>
, \\ \qquad 
</#sep> 
</#list>
\circspot 
</#if>
