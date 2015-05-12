%+++ Channel Sets +++

% MAKE THIS PARSE

\begin{zsection}
	\SECTION ~ NetworkChannels ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
		\t1 SchedulableId, SchedulableIds, MissionFWChan, SchedulableChan, TopLevelMissionSequencerFWChan,\\
		\t1 FrameworkChan, SafeletMethChan
\end{zsection}
%
\begin{circus}
\circchannelset ~ TerminateSync == \\ \t1 \lchanset schedulables\_terminated, schedulables\_stopped, get\_activeSchedulables \rchanset
\end{circus}
%
\begin{circus}	
\circchannelset ~ SafeletTierSync ==\\ \t1 \lchanset start\_toplevel\_sequencer, done\_toplevel\_sequencer, done\_safeletFW \rchanset
\end{circus}
%
<#list Tiers[0] as cluster>
\begin{circus}
\circchannelset ~ TierSync == \\ \t1 \lchanset start\_mission.${cluster.Mission}, done\_mission.${cluster.Mission},\\ \t1 done\_safeletFW, done\_toplevel\_sequencer\rchanset 
\end{circus}
</#list>
%
\begin{circus}
\circchannelset ~ MissionSync == \\ \t1 \lchanset done\_safeletFW, done\_toplevel\_sequencer, register, \\
                 signalTerminationCall, signalTerminationRet, activate\_schedulables, done\_schedulable, \\
                 cleanupSchedulableCall, cleanupSchedulableRet  \rchanset  
\end{circus}
%
\begin{circus}
\circchannelset ~ SchedulablesSync == \\ \t1 \lchanset activate\_schedulables, done\_safeletFW, done\_toplevel\_sequencer \rchanset 
\end{circus}
%
\begin{circus}
\circchannelset ~ ClusterSync == \\ \t1 \lchanset done\_toplevel\_sequencer, done\_safeletFW \rchanset 
\end{circus}
%
%\begin{circus}
%\circchannelset ~ AppSync == \\ \t1  \bigcup \{SafeltAppSync, MissionSequencerAppSync, MissionAppSync, \\ \t1 MTAppSync, OSEHSync , APEHSync,  \\ \t1
%	\lchanset getSequencer, end\_mission\_app, end\_managedThread\_app, \\ \t1 setCeilingPriority, requestTerminationCall,requestTerminationRet, terminationPendingCall, \\ \t1 terminationPendingRet, handleAsyncEventCall, handleAsyncEventRet\rchanset  \}    
%\end{circus}

%
<#list Tiers as tier >
<#if tier_has_next>
%\begin{circus}
%\circchannelset ~ Tier${tier_index}Sync == TierCommonSync \cup 
%\lchanset 	
<#list tier as cluster> 
%start\_mission.${cluster.Mission}, done\_mission.${cluster.Mission}, initializeRet.${cluster.Mission}, 

<#if tier_index == 0> 
%requestTermination.${cluster.Mission}.${TopLevelSequencer}
<#else>
%requestTermination.${cluster.Mission}.${Tiers[tier_index -1]}Needs to have paramter for all sequencers in the tier above
</#if>
%
<#if cluster_has_next>
%,
</#if>
</#list>
%\rchanset
%\end{circus}
</#if>
%
</#list>
%
%
\newpage

%
%+++ Program +++
%
\begin{zsection}
  \SECTION ~ Program ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}
%
\begin{circus}
\circprocess Program \circdef \circbegin
\end{circus}
%
\begin{circusaction}
ControlTier \circdef \\
\circblockopen
${SafeletName}FW \\
\t1 \lpar \emptyset | TierSync | \emptyset \rpar \\
${TopLevelSequencer}FW 
\circblockclose
\end{circusaction}
%
<#list Tiers as tier >
\begin{circusaction}
Tier${tier_index} \circdef \\
<#list tier as cluster>

\circblockopen
	${cluster.Mission}FW\\
		\t1 	\lpar \emptyset | MissionSync | \emptyset \rpar \\
		\circblockopen
		<#list cluster.Schedulables as schedulable>
			${schedulable}FW\\
			<#if schedulable_has_next>
			\t1 \lpar \emptyset | SchedulablesSync | \emptyset \rpar\\
			</#if>
		</#list>
		\circblockclose
\circblockclose
	<#if cluster_has_next>
	\t1 \lpar \emptyset | ClusterSync | \emptyset  \rpar
	</#if>
</#list>
\end{circusaction}
%
</#list>
%
\begin{circusaction}
Framework \circdef \\
\circblockopen
ControlTier \\
\t1 \lpar \emptyset | TierSync | \emptyset \rpar \\
\circblockopen
<#list Tiers as tier >

Tier${tier_index}
<#if tier_has_next>
\t1 \lpar \emptyset | Tier${tier_index}Sync | \emptyset \rpar
</#if>

</#list>
\circblockclose
\circblockclose
\end{circusaction}
%
\begin{circusaction}
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
\end{circusaction}
%
\begin{circusaction}
\circspot 
\circblockopen
Framework \lpar \emptyset | AppSync | \emptyset \rpar Application
\circblockclose
\end{circusaction}
%
\begin{circus}
\circend
\end{circus}
