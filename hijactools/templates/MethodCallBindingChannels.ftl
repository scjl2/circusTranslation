\begin{zsection}
  \SECTION ~ MethodCallBindingChannels ~ \parents ~ scj\_prelude, GlobalTypes, MissionId, SchedulableId, ThreadId
\end{zsection}

<#list MethodCallBindings as mcb>
\begin{circus}

\circchannel binder\_${mcb.Name}Call :  ${mcb.LocType}  \cross ${mcb.CallerType} <#if mcb.Sync==true> \cross ThreadId </#if> <#if mcb.Parameters?has_content> \cross
<#list mcb.Parameters?values as param>
${param}
<#sep>\cross </#sep>
</#list>
</#if>
\\
<#if mcb.ReturnType != 'null'>
\circchannel binder\_${mcb.Name}Ret : ${mcb.LocType} \cross ${mcb.CallerType} <#if mcb.Sync==true> \cross ThreadId </#if> \cross ${mcb.ReturnType} \\
<#else>
\circchannel binder\_${mcb.Name}Ret : ${mcb.LocType}  \cross ${mcb.CallerType} <#if mcb.Sync==true> \cross ThreadId </#if>  \\
</#if>
\end{circus}
%
\begin{zed}
${mcb.Name}Locs == \{ <#list mcb.Locations as loc>${loc}<#sep>,</#sep></#list> \}  \\
${mcb.Name}Callers == \{ <#list mcb.Callers as caller>${caller}<#sep>,</#sep></#list> \}
\end{zed}
%
</#list>

\begin{circus}
\circchannelset MethodCallBinderSync == \lchanset done\_toplevel\_sequencer, \\
<#list MethodCallBindings as mcb>
binder\_${mcb.Name}Call, binder\_${mcb.Name}Ret
<#sep>,\\</#sep>
</#list>
\rchanset
\end{circus}
