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
  distinct \langle SafeletTId, nullThreadId
<#if Threads?has_content>
  , \\
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
</#if>
  \rangle
\end{axdef}
