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
%
\where
  distinct \langle nullSequencerId, nullSchedulableId, ${toplevelSequencer}, \\
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
