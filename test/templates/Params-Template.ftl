<#list Parameters as param>
  ${param.VarName} : ${param.VarType}
<#if param_has_next>
,
</#if> 
</#list>
