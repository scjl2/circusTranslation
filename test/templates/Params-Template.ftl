t1
<#list Parameters as param>
t2
  ${param.VarName} : ${param.VarType}
<#if param_has_next>
,
</#> 
</#list>
