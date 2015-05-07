
\begin{circus}
\circprocess HandlerApp \circdef
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
    
\begin{circus}
\circspot (Methods) \circinterrupt (end\_[handlerType]EventHandler\_app~.~[Handler] \then \Skip)
\end{circus}