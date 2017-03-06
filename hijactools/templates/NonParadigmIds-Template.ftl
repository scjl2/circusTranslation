\begin{zsection}
  \SECTION ~ NonParadigmIds ~ \parents ~ scj\_prelude, GlobalTypes
\end{zsection}
\extracircusvspace
%
\begin{axdef}
<#list NonP?keys as nonP>
	${nonP} : NonParadigmID \\
</#list>

\where
  distinct \langle
<#if NonP?has_content>
  , \\
  <#list NonP?keys as nonP>
	${nonP}
	<sep>
	<#if NonP?counter % 2 == 0>
	,\\
	<#else>
	,
	</#if>
	</#sep>
</#list>
</#if>
  \rangle
\end{axdef}
