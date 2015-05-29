<#-- Template for the ManagedThread Application Process -->

\begin{circus}
\circprocess ${class.processName}App \circdef
\end{circus}
%
<#if class.hasFields() || class.hasDataOperations()>
\begin{circusaction}
  \circstate State ~ == ~ [this : \circreftype ${class.processName}Class]
\end{circusaction}
%
</#if>
<#if class.hasFields() || class.hasDataOperations()>
\begin{circusaction}
  Init ~ == ~ ${class.processName}Init~?~obj \then this := obj
\end{circusaction}
<#else>
\begin{circusaction}
  Init ~ \circdef ~ \Skip
\end{circusaction}
</#if>
%
<#list class.activeMethods as method>
  <@AMethod method/>
%
</#list>
\begin{circusaction}
Methods \circdef \\
  \circblockopen
	Run
<#if class.hasActiveMethods()>
	\extchoice \\
	<#list class.activeMethods as method>
  		${method.uniqueName}Meth <#if method_has_next> ~ \extchoice ~ \\</#if>
	</#list>
</#if>	
  \circblockclose
\circseq Methods
\end{circusaction}
%
\begin{circusaction}
Run \circdef \\
\circblockopen
	runCall~.~${schedulableId} \then \\
	${class.runMethod}	
	runRet~.~${schedulableId} \then \\

	\Skip
\circblockclose
\end{circusaction}	
%	
\begin{circus}
\circspot (Methods) \circinterrupt (end\_managedThread\_app~.~${schedulableId} \then \Skip)
\end{circus}
