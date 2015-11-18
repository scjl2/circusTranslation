<#-- Templates for translating Methods into Actions -->

<#include "Misc.ftl">

<#macro AMethod method>
\begin{circusaction}
  ${method.uniqueName}Meth ~ \circdef ~ \\
  \t1 ${method.uniqueName}Call<@Target/><@Inputs method/> ~ \circthen ~ \\
  \t1
  \circblockopen
  <#if !method.isVoid>\circvar ret : ${method.retType} \circspot\\</#if>
  <@Body method/> \circseq
  \\
  ${method.uniqueName}Ret<@Target/><@Return method/> ~ \circthen ~ \Skip
  \circblockclose
\end{circusaction}
</#macro>

<#-- Translation of Target -->

<#macro Target>
<@compact>
  <#if class.isMission()>~.~${missionId}</#if>
  <#if class.isHandler()>~.~${handlerId}</#if>
</@compact>
</#macro>

<#-- Translation of Inputs -->

<#macro Inputs method>
<@compact>
<#if method.hasParams>
<#list method.params as param>
  ~?~${param.name}
</#list>
</#if>
</@compact>
</#macro>

<#-- Translation of Return -->

<#macro Return method>
<@compact>
<#if !method.isVoid>
  ~!~ret
</#if>
</@compact>
</#macro>

<#-- Translation of Method Body -->

<#macro Body method>
<@compact>
<#if method.isDataOperation()>
  this~.~${method.name}(<@CallArgs method/>)
<#else>
  ${ACTION(method.body)}
</#if>
</@compact>
</#macro>

<#-- Translation of Call Arguments -->

<#macro CallArgs method>
<@compact>
<#if !method.isVoid>
  ret
</#if>
<#if method.hasParams>
<#list method.params as param>
  ${param.name}<#if param_has_next>, </#if>
</#list>
</#if>
</@compact>
</#macro>
