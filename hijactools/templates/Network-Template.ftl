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
\circchannelset ~ TierSync == \\ \t1 \lchanset start\_mission~.~${cluster.Mission.Name}, done\_mission~.~${cluster.Mission.Name},\\
	\t1 done\_safeletFW, done\_toplevel\_sequencer\rchanset
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
%
%\begin{circus}
%\circchannelset ~ ObjectSync   == \\ \t1    \lchanset	 \rchanset
%\end{circus}

\begin{circus}
\circchannelset ~ ThreadSync == \\ \t1  \lchanset raise\_thread\_priority, lower\_thread\_priority, isInterruptedCall, isInterruptedRet, get\_priorityLevel 	\rchanset
\end{circus}

\begin{circus}
\circchannelset ~ LockingSync == \\ \t1  \lchanset lockAcquired, startSyncMeth, endSyncMeth, waitCall, waitRet, notify, isInterruptedCall, isInterruptedRet, \\
\t1 interruptedCall, interruptedRet, done\_toplevel\_sequencer, get\_priorityLevel  \rchanset
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
\t1 start\_mission~.~${cluster.Mission.Name}, done\_mission~.~${cluster.Mission.Name},\\
\t1 initializeRet~.~${cluster.Mission.Name},
<#if tier_index == 0>
requestTermination~.~${cluster.Mission.Name}~.~${TopLevelSequencer.Name}
<#else>
requestTermination~.~${cluster.Mission.Name}~.~${cluster.Sequencer.Name}
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
  \t1 AperiodicEventHandlerFW, ObjectFW, ThreadFW, \\
  \t1 ${Safelet.Name}App, ${TopLevelSequencer.Name}App,

<#list Tiers as tier >
<#assign count=1>
<#list tier as cluster>
${cluster.Mission.Name}App,
<#assign schedulables = cluster.Schedulables.Threads + cluster.Schedulables.Oneshots + cluster.Schedulables.NestedSequencers + cluster.Schedulables.Aperiodics + cluster.Schedulables.Periodics>

<#list schedulables as schedulable>
${schedulable.Name}App
	<#sep>,</#sep>
<#if count==2>
<#assign count=0>
\\ \t1
<#else>
<#assign count++>
</#if>
</#list>
	<#sep>,</#sep>
</#list>
	<#sep>,</#sep>
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
	MissionFW(${cluster.Mission.Name}ID)\\
		\t1 	\lpar MissionSync \rpar \\
		\circblockopen



		<#list cluster.Schedulables.Threads as thread>
			ManagedThreadFW(${thread.Name}ID)\\
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
	OneShotEventHandlerFW(${oneshot.Name}ID <#if oneshot.FWParameters?size != 0>,<#list oneshot.FWParameters as param>${param} <#sep>,</#sep>  </#list></#if>  )\\
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
			SchedulableMissionSequencerFW(${sequencer.Name}ID)\\
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
			AperiodicEventHandlerFW(${aperiodic.Name}ID <#if aperiodic.FWParameters?size != 0>,<#list aperiodic.FWParameters as param>${param} <#sep>,</#sep>  </#list></#if>)\\
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
			PeriodicEventHandlerFW(${periodic.Name}ID <#if periodic.FWParameters?size != 0>,<#list periodic.FWParameters as param>${param} <#sep>,</#sep>  </#list></#if>)\\
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
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%Framework +++
%
\begin{circus}
\circprocess Framework \circdef \\
\circblockopen
ControlTier \\
\t1 \lpar TierSync \rpar \\
<#assign brackets = false>
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
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% APPLICATION
%
\begin{circus}
\circprocess  Application \circdef \\
\circblockopen
${Safelet.Name}App<#if Safelet.AppParameters?size != 0> (<#list Safelet.AppParameters as param>${param} <#sep>,</#sep>  </#list>) </#if>\\
${SafeletSync}\\
${TopLevelSequencer.Name}App<#if TopLevelSequencer.AppParameters?size != 0> (<#list TopLevelSequencer.AppParameters as param> ${param} <#sep>,</#sep>  </#list>) </#if>\\
${ControlTierSync} \\
<#list Tiers as tier >
	<#if tier?counter gt 1>
		\interleave \\
	</#if>
	<#list tier as cluster>
		${cluster.Mission.Name}App<#if cluster.Mission.AppParameters?size != 0> (<#list cluster.Mission.AppParameters as param> ${param} <#sep>,</#sep>  </#list>) </#if>\\
		\interleave \\
		<#assign schedulables = cluster.Schedulables.Threads + cluster.Schedulables.Oneshots + cluster.Schedulables.NestedSequencers + cluster.Schedulables.Aperiodics + cluster.Schedulables.Periodics>
		<#list schedulables as schedulable>
			${schedulable.Name}App<#if schedulable.AppParameters?size != 0> (<#list schedulable.AppParameters as param> ${param} <#sep>,</#sep> </#list>) </#if>\\
			<#sep>\interleave \\</#sep>
		</#list>
		<#sep>\interleave \\</#sep>
	</#list>
</#list>
\circblockclose
\end{circus}

\newpage
%
%%%%%%%%%%%%%%%%%%%%MethodCallBinder
%
\begin{circus}
MethodCallBinder \circdef \\
\t1 \circblockopen
<#list MethodCallBindings as mcb>
	${mcb.Name}\_MethodBinder
	<#sep>\\ \interleave \\</#sep>
</#list>
\circblockclose
\end{circus}
%
<#list MethodCallBindings as mcb>
\begin{circus}
\circchannel binder\_${mcb.Name}Call : ${mcb.LocType} \cross ${mcb.CallerType} \\
\circchannel binder\_${mcb.Name}Ret : ${mcb.LocType} \cross ${mcb.CallerType} ${mcb.ReturnType}   \\ \\

${mcb.Name}Locs == \{ <#list mcb.Locations as loc>${loc}<#sep>,</#sep></#list> \}  \\
${mcb.Name}Callers == \{ <#list mcb.Callers as caller>${caller}<#sep>,</#sep></#list> \}  \\
\end{circus}
%
\begin{circus}
${mcb.Name}\_MethodBinder \circdef \\
	\t1 \circblockopen
	binder\_${mcb.Name}Call~?~loc\prefixcolon(loc \in ${mcb.Name}Locs)~?~caller\prefixcolon(caller \in ${mcb.Name}Callers) \then \\
	${mcb.Name}Call~.~loc~.~caller \then \\
	${mcb.Name}Ret~.~loc~.~caller<#if mcb.Return = true>~?~ret</#if> \then \\
	binder\_${mcb.Name}Ret~.~loc~.~caller<#if mcb.Return = true>~!~ret</#if>  \then \\
	\Skip
	\circblockclose
\end{circus}
%
</#list>

\begin{circus}
	ApplicationB \circdef Application \lpar MethodCallBinderSync \rpar MethodCallBinder
\end{circus}

\newpage
%
%%%%%%%%%%%%%%%%%%THREADS
%
\begin{circus}
Threads \circdef  \\
\circblockopen
<#list Threads?keys as thread>
ThreadFW(${thread}, ${Threads[thread]}) \\
<#sep>\interleave \\</#sep>
</#list>
\circblockclose
\end{circus}
%
%%%%%%%%%%%%%%OBJECTS
%
\begin{circus}
Objects \circdef \\
\circblockopen
<#list Objects.Objects as object>
ObjectFW(${object}) \\
<#sep>\interleave \\</#sep>
</#list>
\circblockclose
\end{circus}
%
%%%%%%%%%%%%%LOCKING
%
\begin{circus}
Locking \circdef Threads \lpar ThreadSync \rpar Objects
\end{circus}
%
\begin{circus}
\circprocess Program \circdef \circblockopen Framework \lpar AppSync \rpar ApplicationB \circblockclose \lpar LockingSync \rpar Locking
\end{circus}
