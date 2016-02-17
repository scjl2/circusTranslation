\begin{zsection}
  \SECTION ~ ${ProcessName}App ~ \parents ~ ManagedThreadChan, SchedulableId, SchedulableIds  <#include "CommonImports-Template.ftl"> \\
  \t1 <#include "Parent-Template.ftl">
\end{zsection}
%
\begin{circus}
\circprocess ${ProcessName}App \circdef <#include "Params-Template.ftl"> \circbegin
\end{circus}

<#include "State-Template.ftl">

\begin{circusaction}
Run \circdef \\
\circblockopen
	runCall~.~${ProcessID} \then \\
	${Run.Body} \circseq \\
	runRet~.~${ProcessID} \then \\
	\Skip
\circblockclose
\end{circusaction}

<#include "Methods-Template.ftl">

\begin{circusaction}
Methods \circdef \\
\circblockopen
	Run \\
<#include "MethodsAction-Template.ftl">
\circblockclose
	 \circseq Methods
\end{circusaction}

\begin{circusaction}
<#include "MainAction-Template.ftl">  \circinterrupt (end\_managedThread\_app~.~${ProcessID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
