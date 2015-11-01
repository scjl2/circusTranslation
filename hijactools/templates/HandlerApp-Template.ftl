\begin{zsection}
  \SECTION ~ ${ProcessID}App ~ \parents ~ ${ImportName}EventHandlerChan, SchedulableId, SchedulableIds
 \t1  <#include "Parent-Template.ftl">
\end{zsection}

\begin{circus}
\circprocess ${ProcessID}App \circdef <#include "Params-Template.ftl">  \circbegin
\end{circus}

<#include "State-Template.ftl">

\begin{circusaction}
handlerAsyncEvent \circdef \\
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
	handlerAsyncEvent \\
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
