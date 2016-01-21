\begin{zsection}
  \SECTION ~ ${ProcessID}MethChan ~ \parents ~ scj\_prelude, GlobalTypes, MissionId, SchedulableId 
\end{zsection}
%
<#list Methods as meth>
\begin{circus}
\t1 \circchannel ${meth.MethodName}Call : <#if IDType!="">${IDType} </#if> ${meth.ExternalAppmeth?then('\cross ', '')}  <#if meth.Parameters?has_content> \cross
<#list meth.Parameters?values as param>
${param}
<#sep>\cross </#sep>
</#list>
</#if>
\\
<#if meth.ReturnType != 'null'>
\t1 \circchannel ${meth.MethodName}Ret : <#if IDType!="">${IDType} \cross </#if> ${meth.ExternalAppmeth?then('Y', '')}  ${meth.ReturnType} \\
<#else>
\t1 \circchannel ${meth.MethodName}Ret <#if IDType!="">: ${IDType} </#if> ${meth.ExternalAppmeth?then('Y', '')} \\
</#if>    
\end{circus}	
%
</#list>
%
<#list SyncMethods as meth>
\begin{circus}
\t1 \circchannel ${meth.MethodName}Call : <#if IDType!="">${IDType} \cross</#if> ThreadID ${meth.ExternalAppmeth?then('Y', '')}  <#if meth.Parameters?has_content> \cross
<#list meth.Parameters?values as param>
${param}
<#sep>\cross </#sep>
</#list>
</#if>
  \\
<#if meth.ReturnType != 'null'>
\t1 \circchannel ${meth.MethodName}Ret : <#if IDType!="">${IDType} \cross </#if> ThreadID ${meth.ExternalAppmeth?then('Y', '')} \cross ${meth.ReturnType} \\
<#else>
\t1 \circchannel ${meth.MethodName}Ret <#if IDType!="">: ${IDType} \cross </#if> ThreadID ${meth.ExternalAppmeth?then('Y', '')} \\
</#if>  
\end{circus}
% 
</#list>
