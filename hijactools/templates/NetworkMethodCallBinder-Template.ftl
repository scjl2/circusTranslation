\begin{zsection}
	\SECTION ~ NetworkMethodCallBinder ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
		\t1 SchedulableId, SchedulableIds, MethodCallBindingChannels\\  <#list MethodCallBindings as mcb><#if mcb.LocParent?has_content>,</#if>${mcb.LocParent} </#list>
\end{zsection}
%
\begin{circus}
\circprocess  MethodCallBinder \circdef \circbegin \\
\end{circus}
%
<#list MethodCallBindings as mcb>
%
\begin{circusaction}
${mcb.Name}\_MethodBinder \circdef \\
	\t1 \circblockopen
	binder\_${mcb.Name}Call~?~loc\prefixcolon(loc \in ${mcb.Name}Locs)~?~caller\prefixcolon(caller \in ${mcb.Name}Callers)<#if mcb.Sync==true>~?~callingThread </#if> <#list mcb.Parameters?keys as paramName>~?~p${paramName?counter} </#list> \then \\
	${mcb.Name}Call~.~loc~.~caller<#if mcb.Sync==true>~.~callingThread </#if> <#list mcb.Parameters?keys as paramName>~!~p${paramName?counter} </#list> \then \\
	${mcb.Name}Ret~.~loc~.~caller<#if mcb.Sync==true>~.~callingThread </#if><#if mcb.ReturnType != 'null'>~?~ret</#if> \then \\
	binder\_${mcb.Name}Ret~.~loc~.~caller  <#if mcb.Sync==true>~.~callingThread </#if>~ <#if mcb.ReturnType != 'null'>!~ret</#if>  \then \\
	${mcb.Name}\_MethodBinder
	\circblockclose
\end{circusaction}
%
</#list>
%
\begin{circusaction}
BinderActions \circdef \\
\circblockopen <#list MethodCallBindings as mcb>
	${mcb.Name}\_MethodBinder
	<#sep>\\ \interleave \\</#sep>
</#list>
\circblockclose
\end{circusaction}
%
\begin{circusaction}
\circspot BinderActions \circinterrupt (done\_toplevel\_sequencer \then \Skip)
\end{circusaction}
%
\begin{circus}
\circend
\end{circus}
