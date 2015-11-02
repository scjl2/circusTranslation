\begin{zsection}
  \SECTION ~ MissionIds ~ \parents ~ scj\_prelude, MissionId
\end{zsection}
\extracircusvspace
%
\begin{axdef}
<#list Missions as mission>
	${mission} : MissionID\\
</#list>
 %eg MainMission : MissionID\\
%  NestedMission : MissionID\\
 
\where
  distinct \langle nullMissionId, 
  <#list Missions as mission>
	${mission}
	<sep>
	<#if mission?counter % 2 == 0>
	,\\
	<#else>
	,
	</#if>
	</#sep>
</#list>
  \rangle
\end{axdef}

