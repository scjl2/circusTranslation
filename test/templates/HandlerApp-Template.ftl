\begin{zsection}
  \SECTION ~ ${ProcessID}App ~ \parents ~ ${importName}EventHandlerChan, SchedulableId, SchedulableIds
\end{zsection}

\begin{circus}
\circprocess ${ProcessID}App \circdef \circbegin
\end{circus}

<#include "State-Template.ftl">



\begin{circusaction}
handlerAsyncEvent \circdef \\
\circblockopen
	handleAsyncEventCall~.~${ProcessID} \then \\

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
\circspot (Methods) \circinterrupt (end\_${handlerType}\_app~.~${ProcessID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
