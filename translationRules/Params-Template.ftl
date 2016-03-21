<#if ProcParameters?has_content>
\\ \t1
<#list ProcParameters as param>
  ${param.VarName} : ${param.ProgramType}
<#sep>,\\</#sep>
</#list>
\circspot
</#if>
