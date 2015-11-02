\begin{zsection}
  \SECTION ~ ObjectIds ~ \parents ~ scj\_prelude, GlobalTypes
\end{zsection}
\extracircusvspace
%
\begin{axdef}
<#list Objects as object>
	${object} : ObjectID \\
</#list>
 
\where
  distinct \langle 
   <#list Objects as object>
	${object}
	<sep>
	<#if object?counter % 2 == 0>
	,\\
	<#else>
	,
	</#if>
	</#sep>
</#list>
  \rangle
\end{axdef}

