\begin{zsection}
  \SECTION ~ ThreadIds ~ \parents ~ scj\_prelude, GlobalTypes
\end{zsection}
\extracircusvspace
%
\begin{axdef}
<#list Threads as thread>
	${thread}Thread : ThreadID \\
</#list>
 
\where
  distinct \langle SafeletThreadId, 
  nullThreadId, \\ 
  <#list Threads as thread>
	${thread}Thread
	<#if thread_has_next>
	,\\
	</#if>
</#list>
  \rangle
\end{axdef}

