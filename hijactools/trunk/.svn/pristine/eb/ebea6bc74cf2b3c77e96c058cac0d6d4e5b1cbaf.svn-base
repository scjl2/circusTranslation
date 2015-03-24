<#-- Template for the Mission Application Process -->
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
  Init ~ == ~ ${class.processName}Init~?~obj \then this := obj
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
<#if class.hasActiveMethods()>
  Methods ~ \circdef ~ \\
  \t1
  \circblockopen
<#list class.activeMethods as method>
  ${method.uniqueName}Meth<#if method_has_next> ~ \extchoice ~ \\</#if>
</#list>
  \circblockclose
<#else>
  Methods ~ \circdef ~ \Stop
</#if>
\end{circusaction}
%
<#if class.hasMethod("handleAsyncEvent")>
<#assign handleAsyncEvent=class.getMethod("handleAsyncEvent")/>
</#if>
\begin{circusaction}
  handleAsyncEvent ~ \circdef ~
  \\
  \t1
  <@Body handleAsyncEvent/>
\end{circusaction}
%
\begin{circusaction}
  Execute ~ \circdef ~ enter\_dispatch~.~${handlerId} \then
  \\
  \t1 (Dispatch \lpar \{this\} | \lchanset release\_handler \rchanset | \emptyset \rpar Release) 
\end{circusaction}
%
\begin{circusaction}
  Dispatch ~ \circdef ~ \\
  \t1
  \circblockopen
    \circmu X \circspot
    \circblockopen
      \circblockopen
        \circblockopen
          Methods ~ ~ \extchoice ~
          \\
          release\_handler~.~${handlerId} ~ \then
          \\
          handleAsyncEvent
        \circblockclose
        \circseq X
      \circblockclose
      \\
      \extchoice
      \\
      leave\_dispatch~.~${handlerId} \then \Skip
    \circblockclose
  \circblockclose
\end{circusaction}
%
\begin{circusaction}
  Release ~ \circdef ~ \\
  \t1
  \circblockopen
    \circmu X \circspot
    \circblockopen
      release\_handler~.~${handlerId} \then \Skip \circseq
      \\
      \circwait this~.~period
    \circblockclose
  \circblockclose
  \\
  \t2 \circinterrupt ~ leave\_dispatch~.~${handlerId} \then \Skip
\end{circusaction}
%
\begin{circusaction}
  \circspot (\circmu X \circspot Init \circseq Execute \circseq X) \circinterrupt end\_mission\_fw \then \Skip
\end{circusaction}
%
\begin{circus}
  \circend
\end{circus}
