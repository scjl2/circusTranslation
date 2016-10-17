\begin{zsection}
  \SECTION ~ ${ProcessName}App ~ \parents ~ scj\_prelude, SchedulableId, SchedulableIds, SafeletChan\\
  \t1 <#include "CommonImports-Template.ftl">
  \t1 <#include "Parent-Template.ftl">
\end{zsection}

%\begin{circus}
%\circchannelset ${ProcessName}ChanSet \circdef \\
%  \lchanset getSequencerCall, getSequencerRet,initializeApplicationCall, initializeApplicationRet, end\_safelet\_app \rchanset
%\end{circus}

\begin{axdef}
	${ProcessName}ID : NonParadigmID \\
\end{axdef}

\begin{circus}
\circprocess ${ProcessName}App  \circdef <#include "Params-Template.ftl"> \circbegin
\end{circus}

<#include "State-Template.ftl">

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
