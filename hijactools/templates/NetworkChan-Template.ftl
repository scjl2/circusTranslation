%+++ Channel Sets +++

% MAKE THIS PARSE

\begin{zsection}
	\SECTION ~ NetworkChan ~ \parents ~ scj\_prelude, MissionId, MissionIds, \\
		\t1 SchedulableId, SchedulableIds, MissionChan, TopLevelMissionSequencerFWChan,\\
		\t1 FrameworkChan, SafeletChan, AperiodicEventHandlerChan, ManagedThreadChan, \\
		\t1 OneShotEventHandlerChan, PeriodicEventHandlerChan, MissionSequencerMethChan
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
	\t1 done\_safeletFW, done\_toplevel\_sequencer \rchanset
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
\circchannelset SafeltAppSync \circdef \\
   \lchanset getSequencerCall, getSequencerRet,initializeApplicationCall, initializeApplicationRet, end\_safelet\_app \rchanset
\end{circus}
%
\begin{circus}
\circchannelset MissionSequencerAppSync == \\ \lchanset getNextMissionCall, getNextMissionRet,end\_sequencer\_app \rchanset
\end{circus}
%
\begin{circus}
\circchannelset MissionAppSync == \\
	\lchanset initializeCall,register, initializeRet,cleanupMissionCall, cleanupMissionRet  \rchanset
\end{circus}
%
\begin{circus}
\circchannelset ~ AppSync == \\
\t1  \bigcup \{SafeltAppSync, MissionSequencerAppSync, MissionAppSync, \\
\t1 MTAppSync, OSEHSync , APEHSync, PEHSync, \\
\t1 \lchanset getSequencer, end\_mission\_app, end\_managedThread\_app, \\
\t1 setCeilingPriority, requestTerminationCall,requestTerminationRet, terminationPendingCall, \\
\t1 terminationPendingRet, handleAsyncEventCall, handleAsyncEventRet \rchanset  \}
\end{circus}
%
\begin{circus}
\circchannelset ~ ThreadSync == \\ \t1  \lchanset raise\_thread\_priority, lower\_thread\_priority, isInterruptedCall, isInterruptedRet, get\_priorityLevel \rchanset
\end{circus}
%
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
