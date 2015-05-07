%\begin{circus}
%\circchannelset ${ProcessName}AppSync \circdef \\
%   \lchanset getSequencerCall, getSequencerRet,initializeApplicationCall, initializeApplicationRet, end\_safelet\_app \rchanset
%\end{circus}    
    
%\begin{circus}
%\circchannelset ${ProcessName}ChanSet \circdef \\
%  \lchanset getSequencerCall, getSequencerRet,initializeApplicationCall, initializeApplicationRet, end\_safelet\_app \rchanset
%\end{circus}     
  
\begin{circus}
\circprocess ${ProcessName}App  \circdef \circbegin
\end{circus}


\begin{circusaction}
InitializeApplication \circdef \\
\circblockopen
     initializeApplicationCall \then \\

	 ${initializeApplicationMethod}	

     initializeApplicationRet \then \\
     \Skip
\circblockclose
\end{circusaction}

\begin{circusaction}
GetSequencer \circdef \\
\circblockopen
	getSequencerCall \then \\
	getSequencerRet~!~${SchedulableID}  \then \\
	\Skip 
\circblockclose
\end{circusaction}

\begin{circusaction}
Methods \circdef \\
\circblockopen
	GetSequencer \\
	\extchoice  \\
	InitializeApplication  \\
\circblockclose
\circseq Methods
\end{circusaction}
 
 \begin{circusaction}
\circspot (Methods) \circinterrupt (end\_safelet\_app \then \Skip) 
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
