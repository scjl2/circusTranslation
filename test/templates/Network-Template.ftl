\begin{zsection}
  \SECTION ~ Netwrok ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}


\begin{circus}
Program \circdef \\
Framework \lpar ns_1 | sync_set | ns_2 \rpar Application
\end{circus}
%
\begin{circus}
Framework \circdef \\
ControlTier \\
\lpar ns_1 | sync_set | ns_2 \rpar \\
\Interleave Tier
\end{circus}
%
\begin{circus}
ControlTier \circdef \\
SafeletFW \\
\lpar ns_1 | sync_set | ns_2 \rpar \\
TopLevelMissionSequencersFW \\
\end{circus}
%
\begin{circus}
Tier \circdef \\
\Interleave Cluster
\end{circus}
%
\begin{circus}
Schedulables \circdef \\
\Interleave ???
\end{circus}
%
\begin{circus}
Cluster \circdef \\
Mission \\
\lpar ns_1 | sync_set | ns_2 \rpar \\
Schedulables
\end{circus}
%
\begin{circus}
Application \circdef \\
SafeletApp
\Interleave 
TopLevelMissionSequencerApps
\Interleave 
Mission_1App
\Interleave 
Schedulable_1Apps
\Interleave 

Mission_2App
\Interleave 
Schedulable_2Apps

\Interleave 
Mission_3App
\Interleave 
Schedulable_3Apps

\end{circus}
