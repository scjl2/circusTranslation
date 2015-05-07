\begin{circus}
\circprocess ${SchedulableID}App \circdef
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
	
\begin{circus}
\circspot (Methods) \circinterrupt (end\_managedThread\_app~.~${SchedulableID} \then \Skip)
\end{circus}
