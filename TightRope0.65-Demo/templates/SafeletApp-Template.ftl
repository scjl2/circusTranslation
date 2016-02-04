\begin{zsection}
  \SECTION ~ ${ProcessID}App ~ \parents ~ scj\_prelude, SchedulableId, SchedulableIds, SafeletChan  <#include "CommonImports-Template.ftl">
  \t1 <#include "Parent-Template.ftl">
\end{zsection}
%\begin{circus}
%\circchannelset ${ProcessID}AppSync \circdef \\
%   \lchanset getSequencerCall, getSequencerRet,initializeApplicationCall, initializeApplicationRet, end\_safelet\_app \rchanset
%\end{circus}

%\begin{circus}
%\circchannelset ${ProcessID}ChanSet \circdef \\
%  \lchanset getSequencerCall, getSequencerRet,initializeApplicationCall, initializeApplicationRet, end\_safelet\_app \rchanset
%\end{circus}

\begin{circus}
\circprocess ${ProcessID}App  \circdef <#include "Params-Template.ftl"> \circbegin
\end{circus}

<#include "State-Template.ftl">

\begin{circusaction}
InitializeApplication \circdef \\
\circblockopen
     initializeApplicationCall \then \\

	   ${initializeApplicationMethod.body}	

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

<#include "Methods-Template.ftl">

\begin{circusaction}
Methods \circdef \\
\circblockopen
	GetSequencer \\
	\extchoice  \\
	InitializeApplication  \\
<#include "MethodsAction-Template.ftl">
\circblockclose
\circseq Methods
\end{circusaction}

\begin{circusaction}
<#include "MainAction-Template.ftl"> \circinterrupt (end\_safelet\_app \then \Skip)
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
