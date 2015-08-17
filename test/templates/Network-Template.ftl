%+++ Channel Sets +++

% MAKE THIS PARSE

\begin{zsection}
	\SECTION ~ NetworkChannels ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
		\t1 SchedulableId, SchedulableIds, MissionChan, SchedulableChan, TopLevelMissionSequencerFWChan,\\
		\t1 FrameworkChan, SafeletChan
\end{zsection}
%
\begin{circus}
\circchannelset ~ TerminateSync == \\ \t1 \lchanset schedulables\_terminated, schedulables\_stopped, get\_activeSchedulables \rchanset
\end{circus}
%
\begin{circus}	
\circchannelset ~ ControlTierSync ==\\ \t1 \lchanset start\_toplevel\_sequencer, done\_toplevel\_sequencer, done\_safeletFW \rchanset
\end{circus}
% IDs wont type check again   Start Mission and Done Mission wont parse?
<#list Tiers[0] as cluster>
\begin{circus}
\circchannelset ~ TierSync == \\ \t1 \lchanset start\_mission~.~${cluster.Mission}, done\_mission~.~${cluster.Mission},\\ \t1 done\_safeletFW, done\_toplevel\_sequencer\rchanset 
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
\begin{circus}
\circchannelset ~ AppSync == \\ \t1  \bigcup \{SafeltAppSync, MissionSequencerAppSync, MissionAppSync, \\ \t1 MTAppSync, OSEHSync , APEHSync,  \\ \t1
	\lchanset getSequencer, end\_mission\_app, end\_managedThread\_app, \\ \t1 setCeilingPriority, requestTerminationCall,requestTerminationRet, terminationPendingCall, \\ \t1 terminationPendingRet, handleAsyncEventCall, handleAsyncEventRet\rchanset  \}    
\end{circus}

%IDs wont type check
<#list Tiers as tier >
<#if tier_has_next>
\begin{circus}
\circchannelset ~ Tier${tier_index}Sync == \\ 
\t1 \lchanset 	
done\_toplevel\_sequencer, done\_safeletFW, \\
<#assign next=tier_index+1>
<#list Tiers[next] as cluster>
start\_mission~.~${cluster.Mission}, done\_mission~.~${cluster.Mission},\\ 
\t1 initializeRet~.~${cluster.Mission}, 
<#if tier_index == 0> 
requestTermination~.~${cluster.Mission}~.~${TopLevelSequencer}
<#else>
requestTermination~.~${cluster.Mission}~.~${cluster.Sequencer}
</#if>

<#if cluster_has_next>
, \\
</#if>
</#list>

\rchanset
\end{circus}
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
  \t1 SchedulableId, SchedulableIds, MissionChan, SchedulableMethChan, MissionFW,\\
  \t1 SafeletFW, TopLevelMissionSequencerFW, NetworkChannels, ManagedThreadFW, \\
  \t1 SchedulableMissionSequencerFW, PeriodicEventHandlerFW, OneShotEventHandlerFW,\\
  \t1 AperiodicEventHandlerFW, ${SafeletName}App, ${TopLevelSequencer}App, \\
  \t1 
<#list Tiers as tier >
<#list tier as cluster>


${cluster.Mission}App, 

<#assign schedulables = cluster.Schedulables.Threads + cluster.Schedulables.Oneshots + cluster.Schedulables.NestedSequencers + cluster.Schedulables.Aperiodics + cluster.Schedulables.Periodics>

<#list schedulables as schedulable>
${schedulable}App 
	<#if schedulable_has_next>
,
	</#if>
</#list>
	<#if cluster_has_next>
,
	</#if>
</#list>
	<#if tier_has_next>
,
	</#if>
</#list>
\end{zsection}
%
\begin{circus}
\circprocess ControlTier \circdef \\
\circblockopen
SafeletFW \\
\t1 \lpar ControlTierSync \rpar \\
TopLevelMissionSequencerFW(${TopLevelSequencer.Name})
\circblockclose
\end{circus}
%

<#function zebra index>
  <#if (index % 2) == 0>
    <#return "white" />
  <#else>
    <#return "#efefef" />
  </#if>
</#function>

<#assign syncNeeded=false>

<#list Tiers as tier >
\begin{circus}
\circprocess Tier${tier_index} \circdef \\
<#list tier as cluster>

\circblockopen
	MissionFW(${cluster.Mission.Name})\\
		\t1 	\lpar MissionSync \rpar \\
		\circblockopen



		<#list cluster.Schedulables.Threads as thread>			
			ManagedThreadFW(${thread.Name})\\
			<#if thread_has_next>
			<#if thread?counter % 2 == 0>
			\circblockclosed
			\circblockopen
			</#if>
			\t1 \lpar SchedulablesSync \rpar\\
			</#if>
		</#list>	

<#assign syncNeeded=false>




<#if cluster.Schedulables.Oneshots?size gte 1>

<#if syncNeeded == true>
\t1 \lpar SchedulablesSync \rpar\\
</#if>


<#if cluster.Schedulables.Oneshots?size gte 2>
\circblockopen 
</#if>

<#list cluster.Schedulables.Oneshots as oneshot>			
	OneShotEventHandlerFW(${oneshot})\\
	<#if oneshot_has_next>
		\t1 \lpar SchedulablesSync \rpar\\
	</#if>

</#list>

<#assign syncNeeded=false>

<#if cluster.Schedulables.Oneshots?size gte 2>
\circblockclose \\
</#if>

<#if cluster.Schedulables.Oneshots?size gte 1>
\t1 \lpar SchedulablesSync \rpar\\
</#if>

</#if>

<#if cluster.Schedulables.NestedSequencers?size gte 1>
<#if syncNeeded == true>
\t1 \lpar SchedulablesSync \rpar\\
</#if>
<#if cluster.Schedulables.NestedSequencers?size gte 2>
\circblockopen 
</#if>
		<#list cluster.Schedulables.NestedSequencers as sequencer>			
			SchedulableMissionSequencerFW(${sequencer})\\
			<#if sequencer_has_next>
			\t1 \lpar SchedulablesSync \rpar\\
			</#if>
		</#list>
<#assign syncNeeded=false>
<#if cluster.Schedulables.NestedSequencers?size gte 2>
\circblockclose \\
</#if>
<#if cluster.Schedulables.NestedSequencers?size gte 1>
\t1 \lpar SchedulablesSync \rpar\\
</#if>

</#if>

<#if cluster.Schedulables.Aperiodics?size gte 1>
<#if syncNeeded == true>
\t1 \lpar SchedulablesSync \rpar\\
</#if>
<#if cluster.Schedulables.Aperiodics?size gte 2>
\circblockopen
</#if>
		<#list cluster.Schedulables.Aperiodics as aperiodic>			
			AperiodicEventHandlerFW(${aperiodic})\\
			<#if aperiodic_has_next>
			\t1 \lpar SchedulablesSync \rpar\\
			</#if>
		</#list>
<#assign syncNeeded=false>
<#if cluster.Schedulables.Aperiodics?size gte 2>
\circblockclose \\

</#if>

<#if cluster.Schedulables.Aperiodics?size gte 1>
\t1 \lpar SchedulablesSync \rpar\\
</#if>


</#if>

<#if cluster.Schedulables.Periodics?size gte 1>
<#if syncNeeded == true>
\t1 \lpar SchedulablesSync \rpar\\
</#if>
<#if cluster.Schedulables.Periodics?size gte 2>
\circblockopen
</#if>
		<#list cluster.Schedulables.Periodics as periodic>			
			PeriodicEventHandlerFW(${periodic})\\
			<#if periodic_has_next>
			\t1 \lpar SchedulablesSync \rpar\\
			</#if>
		</#list>
<#assign syncNeeded=false>
<#if cluster.Schedulables.Periodics?size gte 2>
\circblockclose \\ 

</#if>
</#if>
		\circblockclose
\circblockclose
	<#if cluster_has_next>
	\\ \t1 \lpar ClusterSync \rpar \\
	</#if>
</#list>
\end{circus}
%
</#list>
%
%+++ Framework +++ 
%
\begin{circus}
\circprocess Framework \circdef \\
\circblockopen
ControlTier \\
\t1 \lpar TierSync \rpar \\
<#assign brakcets = false>
 \circblockopen
<#list Tiers as tier >
Tier${tier_index}
<#if tier_has_next>
\\ \t1 \lpar Tier${tier_index}Sync \rpar \\
</#if>
</#list>
\circblockclose
\circblockclose
\end{circus}
%
\begin{circus}
\circprocess  Application \circdef \\
\circblockopen

${Safelet.Name}App<#if Safelet.Parameters?size != 0> (<#list Safelet.Parameters as param> ?param? <#if param_has_next>,</#if> </#list>) </#if>\\
\interleave \\
${TopLevelSequencer.Name}App<#if TopLevelSequencer.Parameters?size != 0> (<#list TopLevelSequencer.Parameters as param> ?param? <#if param_has_next>,</#if> </#list>) </#if>\\
\interleave \\
<#list Tiers as tier >
<#list tier as cluster>
${cluster.Mission.Name}App<#if cluster.Mission.Parameters?size != 0> (<#list cluster.Mission.Parameters as param> ?param? <#if param_has_next>,</#if> </#list>) </#if>\\
\interleave \\
<#assign schedulables = cluster.Schedulables.Threads + cluster.Schedulables.Oneshots + cluster.Schedulables.NestedSequencers + cluster.Schedulables.Aperiodics + cluster.Schedulables.Periodics>

<#list schedulables as schedulable>
${schedulable.Name}App<#if schedulable.Parameters?size != 0> (<#list schedulable.Parameters as param> ?param? <#if param_has_next>,</#if> </#list>) </#if>\\
			<#if schedulable_has_next>
\interleave \\
			</#if>
		</#list>
<#if cluster_has_next>
\interleave \\
			</#if>
</#list>
<#if tier_has_next>
\interleave \\
			</#if>
</#list>
\circblockclose
\end{circus}
%
\begin{circus}
\circprocess Program \circdef Framework \lpar AppSync \rpar Application
\end{circus}

