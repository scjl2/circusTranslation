\begin{zsection}
  \SECTION ~ ${ProcessID}App ~ \parents ~ ManagedThreadChan, SchedulableId, SchedulableIds, ${ProcessID}Class
\end{zsection}
%
\begin{circus}
\circprocess ${ProcessID}App \circdef \circbegin
\end{circus}

<#include "State-Template.ftl">

\begin{circusaction}
Run \circdef \\
\circblockopen
	runCall~.~${ProcessID} \then \\
	this~.~run() \circseq \\
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
\circspot (Methods) \circinterrupt (end\_managedThread\_app~.~${ProcessID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}