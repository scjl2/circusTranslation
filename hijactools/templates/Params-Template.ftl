<#if AppParameters?has_content>
\\ \qquad 
<#list AppParameters as param>
  ${param.VarName} : ${param.ProgramType}
<#sep>
, \\ \qquad 
</#sep> 
</#list>
\circspot 
</#if>
