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
	scedulable
	<#if mission_has_next>
	,
	</#if>
</#list>
  \rangle
\end{axdef}

