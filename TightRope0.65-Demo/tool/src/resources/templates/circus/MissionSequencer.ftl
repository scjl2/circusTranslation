<#-- Template for the MissionSequencer Application Process -->
<#include "AMethod.ftl">
\begin{circus}
  \circprocess ~ ${class.processName}App ~ \circdef ~ \circbegin
\end{circus}
%
<#if class.hasFields() || class.hasDataOperations()>
\begin{circusaction}
  \circstate State ~ == ~ [this : \circreftype ${class.processName}Class]
\end{circusaction}
%
</#if>
<#if class.hasFields() || class.hasDataOperations()>
\begin{circusaction}
  Init ~ == ~ [State~' | this' = \circnew ${class.processName}Class]
\end{circusaction}
<#else>
\begin{circusaction}
  Init ~ \circdef ~ \Skip
\end{circusaction}
</#if>
%
<#list class.activeMethods as method>
  <@AMethod method/>
%
</#list>
\begin{circusaction}
  Methods ~ \circdef ~ \\
  \t1
  \circmu X \circspot
  \circblockopen
<#list class.activeMethods as method>
  ${method.uniqueName}Meth<#if method_has_next> ~ \extchoice ~ \\</#if>
</#list>
  \circblockclose
  \circseq X
\end{circusaction}
%
\begin{circusaction}
  \circspot Init \circseq (Methods \circinterrupt end\_sequencer\_app \then \Skip)
\end{circusaction}
%
\begin{circus}
  \circend
\end{circus}
