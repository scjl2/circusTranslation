\begin{zsection}
  \SECTION ~ ObjectIds ~ \parents ~ scj\_prelude, GlobalTypes
\end{zsection}
\extracircusvspace
%
\begin{axdef}
<#list Objects as object>
	${object}Object : ObjectID \\
</#list>
 
\where
  distinct \langle 
   <#list Objects as object>
	${object}Object
	<#sep>
	,\\
	</#sep>
</#list>
  \rangle
\end{axdef}

