\begin{zsection}
  \SECTION ~ Network ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}

%+++ Channel Sets +++

<#list Tiers as tier >

<#if tier_has_next>
\begin{circus}
Tier${tier_index}Sync \circdef TierCommonSync \cup 
\lchanset 	start_mission.MissionIn${tier_index+1}, done_mission.MissionIn${tier_index+1}, 
			initializeRet.MissionIn${tier_index+1}, requestTermination.MissionIn${tier_index+1}.MissionSequencerAbove
\rchanset
\end{circus}
</#if>
</#list>

%
%+++ Program +++
%
\begin{circus}
Program \circdef \\
\circblockopen
Framework \lpar ns_1 | sync_set | ns_2 \rpar Application
\circblockclose
\end{circus}
%
%+++ Framework +++
%
\begin{circus}
Framework \circdef \\
\circblockopen
ControlTier \\
\t1 \lpar ns_1 | sync_set | ns_2 \rpar \\
\circblockopen
<#list Tiers as tier >

Tier${tier_index}
<#if tier_has_next>
\t1 \lpar ns | Tier${tier_index}Sync | ns \rpar
</#if>

</#list>
\circblockclose
\circblockclose
\end{circus}
%
%+++ Control Tier +++
%
\begin{circus}
ControlTier \circdef \\
\circblockopen
${SafeletName}FW \\
\t1 \lpar ns_1 | sync_set | ns_2 \rpar \\
${TopLevelSequencer}FW 
\circblockclose
\end{circus}
%
%+++ Tiers +++
%
<#list Tiers as tier >

\begin{circus}
Tier${tier_index} \circdef \\

<#list tier as cluster>
\circblockopen
	${cluster.Mission}\\
		\t1 	\lpar ns_1 | mission_sync | ns_2 \rpar \\
		\circblockopen
		<#list cluster.Schedulables as schedulable>
			${schedulable}\\
			<#if schedulable_has_next>
			\t1 \lpar ns | schedulable_sync | ns \rpar\\
			</#if>
		</#list>
		\circblockclose
\circblockclose
	<#if cluster_has_next>
	\t1 \lpar ns | \lchanset done_toplevel_sequencer, done_safeletFW \rchanset | ns  \rpar
	</#if>
</#list>

\end{circus}

</#list>

%
%+++ Clusters
%
<#list Tiers as tier >
%\begin{circus}
<#list tier as cluster>
%	Cluster${tier_index}${cluster_index} \circdef \\
%		${cluster.Mission}\\
%			\lpar ns_1 | mission_sync | ns_2 \rpar \\
%		\circblockopen
		<#list cluster.Schedulables as schedulable>
%			${schedulable}\\
			<#if schedulable_has_next>
%			\lpar ns | schedulable_sync | ns \rpar\\
			</#if>
		</#list>
%		\circblockclose
%</#list>
%\end{circus}
</#list>
%
%+++ Application +++
%
\begin{circus}
Application \circdef \\
\circblockopen
${SafeletName}App\\
\interleave \\
${TopLevelSequencer}App\\
\interleave \\
<#list Tiers as tier >
<#list tier as cluster>
${cluster.Mission}App\\
\interleave \\
<#list cluster.Schedulables as schedulable>
${schedulable}App\\
			<#if schedulable_has_next>
\interleave \\
			</#if>
		</#list>
</#list>
</#list>
\circblockclose
\end{circus}
