
\begin{circus}
\circprocess HandlerApp \circdef \circbegin
\end{circus}


\begin{circusaction}
Methods \circdef \\
	handlerAsyncEvent \circseq Methods
\end{circusaction}

\begin{circusaction}
handlerAsyncEvent \circdef \\
\circblockopen
	handleAsyncEventCall~.~[Handler] \then \\

    handleAsyncEventRet~.~[Handler] \then \\
    \Skip
\circblockclose
\end{circusaction}
    
\begin{circusaction}
\circspot (Methods) \circinterrupt (end\_[handlerType]EventHandler\_app~.~[Handler] \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
