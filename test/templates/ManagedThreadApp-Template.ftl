\begin{circus}
\circprocess ${SchedulableID}App \circdef \circbegin
\end{circus}

\begin{circusaction}
Methods \circdef \\
	Run \circseq Methods
\end{circusaction}

\begin{circusaction}
Run \circdef \\
\circblockopen
	runCall~.~${SchedulableID} \then \\
		
	runRet~.~${SchedulableID} \then \\
	\Skip
\circblockclose
\end{circusaction}	
	
\begin{circusaction}
\circspot (Methods) \circinterrupt (end\_managedThread\_app~.~${SchedulableID} \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
