%+++ Channel Sets +++

\begin{zsection}
	\SECTION ~ NetworkChannels ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
		\t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}
%
\begin{circus}
\circchannelset ~ TerminateSync == \lchanset \rchanset %\\ \t1 \lchanset schedulables\_terminated, schedulables\_stopped, get\_activeSchedulables \rchanset
\end{circus}

\begin{circus}	
\circchannelset ~ SafeletTierSync ==\lchanset \rchanset %\\ \t1 \lchanset start\_toplevel\_sequencer, done\_toplevel\_sequencer, done\_safeletFW \rchanset
\end{circus}

<#list Tiers[0] as cluster>
\begin{circus}
\circchannelset ~ TierSync == \lchanset \rchanset %\\ \t1 \lchanset start\_mission.${cluster.Mission}, done\_mission.${cluster.Mission},\\ \t1 done\_safeletFW, done\_toplevel\_sequencer\rchanset 
\end{circus}
</#list>

\begin{circus}
\circchannelset ~ MissionSync == \lchanset \rchanset %\\ \t1 \lchanset done\_safeletFW, done\_toplevel\_sequencer, register, \\
                  % signalTerminationCall, signalTerminationRet, activate\_schedulables, done\_schedulable, \\
                  % cleanupSchedulableCall, cleanupSchedulableRet  \rchanset  
\end{circus}

\begin{circus}
\circchannelset ~ SchedulablesSync == \lchanset \rchanset %\\ \t1 \lchanset activate\_schedulables, done\_safeletFW, done\_toplevel\_sequencer \rchanset 
\end{circus}

\begin{circus}
\circchannelset ~ ClusterSync == \lchanset \rchanset %\\ \t1 \lchanset done\_toplevel\_sequencer, done\_safeletFW \rchanset 
\end{circus}

\begin{circus}
\circchannelset ~ AppSync == \lchanset \rchanset %\\ \t1  \bigcup \{SafeltAppSync, MissionSequencerAppSync, MissionAppSync, \\ \t1 MTAppSync, OSEHSync , APEHSync,  \\ \t1
		%\lchanset getSequencer, end\_mission\_app, end\_managedThread\_app, \\ \t1 setCeilingPriority, requestTerminationCall,requestTerminationRet, terminationPendingCall, \\ \t1 terminationPendingRet, handleAsyncEventCall, handleAsyncEventRet\rchanset  \}    
\end{circus}

%
<#list Tiers as tier >
<#if tier_has_next>
\begin{circus}
\circchannelset ~ Tier${tier_index}Sync == TierCommonSync \cup 
\lchanset 	
<#list tier as cluster> 
start\_mission.${cluster.Mission}, done\_mission.${cluster.Mission}, initializeRet.${cluster.Mission}, 

<#if tier_index == 0> 
requestTermination.${cluster.Mission}.${TopLevelSequencer}
<#else>
requestTermination.${cluster.Mission}.${Tiers[tier_index -1]}Needs to have paramter for all sequencers in the tier above
</#if>

<#if cluster_has_next>
,
</#if>
</#list>
\rchanset
\end{circus}
</#if>

</#list>


%
%+++ Control Tier +++
%
\begin{zsection}
  \SECTION ~ ControlTier ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}
%
\begin{circus}
\circprocess ControlTier \circdef \circbegin\\
\end{circus}
%
\begin{circusaction}
\circspot
\circblockopen
${SafeletName}FW \\
\t1 \lpar \emptyset | TierSync | \emptyset \rpar \\
${TopLevelSequencer}FW 
\circblockclose
\end{circusaction}
%
\begin{circus}
\circend
\end{circus}

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
%+++ Tiers +++
%
<#list Tiers as tier >
\begin{zsection}
  \SECTION ~ Tier${tier_index} ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}

\begin{circus}
\circprocess Tier${tier_index} \circdef \circbegin\\
\end{circus}

\begin{circusaction}
\circspot
<#list tier as cluster>

\circblockopen
	${cluster.Mission}\\
		\t1 	\lpar \emptyset | MissionSync | \emptyset \rpar \\
		\circblockopen
		<#list cluster.Schedulables as schedulable>
			${schedulable}\\
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

\begin{circus}
\circend
\end{circus}

</#list>



%
%+++ Application +++
%
\begin{zsection}
  \SECTION ~ Application ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}
%
\begin{circus}
\circprocess Application \circdef \\
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

%
%+++ Framework +++
%
\begin{zsection}
  \SECTION ~ Framework ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}
%
\begin{circus}
\circprocess Framework \circdef \circbegin\\
\end{circus}

\begin{circusaction}
\circspot 
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

\begin{circus}
\circend
\end{circus}


%
%+++ Program +++
%
\begin{zsection}
  \SECTION ~ Program ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan
\end{zsection}
%
\begin{circus}
\circprocess Program \circdef \circbegin \\
\end{circus}
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
