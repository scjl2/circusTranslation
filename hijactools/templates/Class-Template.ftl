
\begin{zsection}
  \SECTION ~ ${ProcessName}Class ~ \parents ~ scj\_prelude, SchedulableId, SchedulableIds, SafeletChan \\
  <#include "CommonImports-Template.ftl">  <#include "Parent-Template.ftl">
\end{zsection}

\begin{circus}
\circclass ${ProcessName}Class \circdef \circbegin
\end{circus}

<#include "Class-Variables-Template.ftl">


<#include "Class-Methods-Template.ftl">

\begin{circusaction}
\circspot ~ \Skip
\end{circusaction}

\begin{circus}
  \circend
\end{circus}
