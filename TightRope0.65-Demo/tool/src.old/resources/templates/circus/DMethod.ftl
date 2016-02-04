<#-- Templates for translating Methods into Actions -->

<#macro DMethod method>
\begin{circusaction}
  <@MethodModifiers method/>${method.name} ~ \circdef ~
  <@Params method/>
  <@Return method/>
  \\
  \t1
  ${DATAOP(method.body)}<#lt/>
\end{circusaction}
</#macro>

<#-- Translation of Parameters -->

<#macro Params method>
<#if method.hasParams>
  \circval ~ <@it/>
<#list method.params as param>
  ${param.name} : ${param.type}<#if param_has_next>; </#if><#lt><@it/>
</#list> \circspot
</#if>
</#macro>

<#-- Translation of Return -->

<#macro Return method>
<#if !method.isVoid>
  \circres ~ ret : ${method.retType} \circspot
</#if>
</#macro>
