\begin{zsection}
  \SECTION ~ ThreadIds ~ \parents ~ scj\_prelude, GlobalTypes
\end{zsection}
\extracircusvspace
%
\begin{axdef}
<#list Threads?keys as thread>
	${thread} : ThreadID \\
</#list>

\where
  distinct \langle SafeletTId, nullThreadId, \\
  <#list Threads?keys as thread>
	${thread}
	<sep>
	<#if thread?counter % 2 == 0>
	,\\
	<#else>
	,
	</#if>
	</#sep>
</#list>
  \rangle
\end{axdef}
