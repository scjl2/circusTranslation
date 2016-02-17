\begin{zsection}
  \SECTION ~ ${ProcessName}App ~ \parents ~ ${ImportName}EventHandlerChan, SchedulableId, SchedulableIds <#include "CommonImports-Template.ftl"> \\
 \t1  <#include "Parent-Template.ftl">
\end{zsection}

\begin{circus}
\circprocess ${ProcessName}App \circdef <#include "Params-Template.ftl">  \circbegin
\end{circus}

<#include "State-Template.ftl">

\begin{circusaction}
handleAsyncEvent \circdef \\
\circblockopen
	handleAsyncEventCall~.~${ProcessID} \then \\
	${HandleAsync.Body} \circseq \\
    handleAsyncEventRet~.~${ProcessID} \then \\
    \Skip
\circblockclose
\end{circusaction}

<#include "Methods-Template.ftl">

\begin{circusaction}
Methods \circdef \\
\circblockopen
	handleAsyncEvent \\
<#include "MethodsAction-Template.ftl">
\circblockclose
	 \circseq Methods
\end{circusaction}

\begin{circusaction}
<#include "MainAction-Template.ftl">  \circinterrupt (end\_${HandlerType}\_app~.~${ProcessID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
