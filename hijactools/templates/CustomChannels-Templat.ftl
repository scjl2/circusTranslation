\begin{zsection}
  \SECTION ~ ${ProcessName}MethChan ~ \parents ~ scj\_prelude, GlobalTypes, MissionId, SchedulableId 
\end{zsection}
%

<#list Methods as meth>
\begin{circus}
\t1 \circchannel ${meth.MethodName}Call : ${meth.LocType} <#if meth.ExternalAppmeth == true> \cross ${meth.CallerType} </#if> <#if meth.Parameters?has_content> \cross
<#list meth.Parameters?values as param>
${param}
<#sep>\cross </#sep>
</#list>
</#if>
\\
<#if meth.ReturnType != 'null'>
\t1 \circchannel ${meth.MethodName}Ret : ${meth.LocType} <#if meth.ExternalAppmeth == true> \cross ${meth.CallerType} </#if> \cross ${meth.ReturnType} \\
<#else>
\t1 \circchannel ${meth.MethodName}Ret : ${meth.LocType} <#if meth.ExternalAppmeth == true> \cross ${meth.CallerType} </#if> \\
</#if>
\end{circus}
%
</#list>
%
<#list SyncMethods as meth>
\begin{circus}
\t1 \circchannel ${meth.MethodName}Call : ${meth.LocType}  <#if meth.ExternalAppmeth == true> \cross ${meth.CallerType} </#if> \cross ThreadID <#if meth.Parameters?has_content> \cross
<#list meth.Parameters?values as param>
${param}
<#sep>\cross </#sep>
</#list>
</#if>
  \\
<#if meth.ReturnType != 'null'>
\t1 \circchannel ${meth.MethodName}Ret : ${meth.LocType}  <#if meth.ExternalAppmeth == true> \cross ${meth.CallerType} </#if> \cross ThreadID \cross ${meth.ReturnType} \\
<#else>
\t1 \circchannel ${meth.MethodName}Ret : ${meth.LocType}  <#if meth.ExternalAppmeth == true> \cross ${meth.CallerType} </#if> \cross ThreadID \\
</#if>
\end{circus}
%
</#list>
