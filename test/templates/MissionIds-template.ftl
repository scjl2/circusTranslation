\begin{zsection}
  \SECTION ~ MissionIds ~ \parents ~ scj\_prelude, MissionId
\end{zsection}
\extracircusvspace
%
\begin{axdef}
  MainMission : MissionID\\
  NestedMission : MissionID\\
 
\where
  distinct \langle nullMissionId, MainMission, NestedMission  \rangle
\end{axdef}

