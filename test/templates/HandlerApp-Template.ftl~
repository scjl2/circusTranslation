\begin{zsection}
  \SECTION ~ ${SchedulableID}App ~ \parents ~ ${handlerType}EventHandlerChan, SchedulableId, SchedulableIds
\end{zsection}

\begin{circus}
\circprocess ${SchedulableID}App \circdef \circbegin
\end{circus}


\begin{circusaction}
Methods \circdef \\
	handlerAsyncEvent \circseq Methods
\end{circusaction}

\begin{circusaction}
handlerAsyncEvent \circdef \\
\circblockopen
	handleAsyncEventCall~.~${SchedulableID} \then \\

    handleAsyncEventRet~.~${SchedulableID} \then \\
    \Skip
\circblockclose
\end{circusaction}
    
\begin{circusaction}
\circspot (Methods) \circinterrupt (end\_${handlerType}EventHandler\_app~.~${SchedulableID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
