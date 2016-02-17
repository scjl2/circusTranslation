\subsection{Network Channel Sets}
\input{NetworkChan.circus}

\newpage

\subsection{MethodCallBinder}
\input{NetworkMethodCallBinder.circus}

\newpage

\subsection{Locking}
\input{NetworkLocking.circus}

\newpage

\subsection{Program}
\input{NetworkProgram.circus}
%
\begin{circus}
\circprocess Program \circdef \circblockopen Framework \lpar AppSync \rpar ApplicationB \circblockclose \lpar LockingSync \rpar Locking
\end{circus}
