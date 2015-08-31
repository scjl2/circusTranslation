\begin{zsection}
  \SECTION ~ ${ProcessID}MethChan ~ \parents ~ scj\_prelude, GlobalTypes, MissionId, SchedulableId 
\end{zsection}
%
<#list Methods as meth>
\begin{circus}
\t1 \circchannel ${meth.MethodName}Call :<#if IDType!="">${IDType} </#if> <#if meth.Parameters?has_content> \cross
<#list meth.Parameters?values as param>
${param}
<#sep>\cross </#sep>
</#list>
</#if>
\\
<#if meth.ReturnType != 'null'>
\t1 \circchannel ${meth.MethodName}Ret : <#if IDType!="">${IDType} \cross </#if> ${meth.ReturnType} \\
<#else>
\t1 \circchannel ${meth.MethodName}Ret <#if IDType!="">: ${IDType} </#if>\\
</#if>    
\end{circus}
%
</#list>
%
<#list SyncMethods as meth>
\begin{circus}
\t1 \circchannel ${meth.MethodName}Call : <#if IDType!="">${IDType} \cross</#if> ThreadID <#if meth.Parameters?has_content> \cross
<#list meth.Parameters?values as param>
${param}
<#sep>\cross </#sep>
</#list>
</#if>
  \\
<#if meth.ReturnType != 'null'>
\t1 \circchannel ${meth.MethodName}Ret : <#if IDType!="">${IDType} \cross </#if> ThreadID \cross ${meth.ReturnType} \\
<#else>
\t1 \circchannel ${meth.MethodName}Ret <#if IDType!="">: ${IDType} \cross </#if> ThreadID\\
</#if>  
\end{circus}
% 
</#list>
