\begin{zsection}
  \SECTION ~ SchedulableIds ~ \parents ~ scj\_prelude, SchedulableId
\end{zsection}
\extracircusvspace
%
\begin{axdef}
${toplevelSequencer} : SchedulableID\\
<#list Schedulables as schedulable>
	${schedulable} : SchedulableID\\
</#list>
 %eg MainMissionSequencer : SchedulableID\\
 % NestedMissionSequencer : SchedulableID\\
 % NestedOneShotEventHandler : SchedulableID\\   
  
\where
  distinct \langle nullSequencerId, nullSchedulableId,
  <#list Schedulables as schedulable>
	${schedulable}
	<#if schedulable_has_next>
	,\\
	</#if>
</#list>
  \rangle
\end{axdef}

