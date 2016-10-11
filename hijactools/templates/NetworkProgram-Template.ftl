
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
<#assign schedulables = cluster.Schedulables.Threads + cluster.Schedulables.Oneshots + cluster.Schedulables.NestedSequencers + cluster.Schedulables.Aperiodics + cluster.Schedulables.Periodics>
\circblockopen
	MissionFW(${cluster.Mission.Name}ID)\\
		\t1 	\lpar MissionSync \rpar \\
		\circblockopen

		<#list 	schedulables as sched>

		<#if sched.SchedType == "MT" >
			ManagedThreadFW(${sched.Name}ID)\\
		<#elseif sched.SchedType == "OSEH" >
			OneShotEventHandlerFW(${sched.Name}ID <#if sched.FWParameters?size != 0>,<#list sched.FWParameters as param>${param} <#sep>,</#sep>  </#list></#if>)\\
		<#elseif sched.SchedType ==  "SMS" >
			SchedulableMissionSequencerFW(${sched.Name}ID)\\
		<#elseif sched.SchedType == "APEH" >
			AperiodicEventHandlerFW(${sched.Name}ID <#if sched.FWParameters?size != 0>,<#list sched.FWParameters as param>${param} <#sep>,</#sep>  </#list></#if>)\\
		<#elseif sched.SchedType == "PEH" >
			PeriodicEventHandlerFW(${sched.Name}ID <#if sched.FWParameters?size != 0>,<#list sched.FWParameters as param>${param} <#sep>,</#sep>  </#list></#if>)\\
		</#if>

		<#sep>	\t1 \lpar SchedulablesSync \rpar\\</#sep>

		</#list>




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
<#list NPE as npe >
${npe.Name}App<#if npe.AppParameters?size != 0> (<#list npe.AppParameters as param> ${param} <#sep>,</#sep>  </#list>) </#if>\\
<#sep>\interleave \\</#sep>
</#list>
\circblockclose
\end{circus}
