\begin{zsection}
  \SECTION ~ SchedulableIds ~ \parents ~ scj\_prelude, SchedulableId
\end{zsection}
\extracircusvspace
%
\begin{axdef}
${toplevelSequencer}ID : SchedulableID\\
<#list Schedulables as schedulable>
	${schedulable} : SchedulableID\\
</#list>
 %eg MainMissionSequencer : SchedulableID\\
 % NestedMissionSequencer : SchedulableID\\
 % NestedOneShotEventHandler : SchedulableID\\   
  
\where
  distinct \langle nullSequencerId, nullSchedulableId, ${toplevelSequencer}ID, \\
  <#list Schedulables as schedulable>
	${schedulable}
	<sep>
	<#if schedulable?counter % 2 == 0>
	,\\
	<#else>
	,
	</#if>
	</#sep>
</#list>
  \rangle
\end{axdef}

