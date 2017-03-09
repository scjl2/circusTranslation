\begin{zsection}
  \SECTION ~ Network ~ \parents ~ NetworkProgram, MethodCallBindingChannels,  NetworkMethodCallBinder, NetworkChan, NetworkLocking
\end{zsection}

<#if MethodCallBindings?has_content>
\begin{circus}
\circprocess Bound\_Application \circdef  Application \lpar MethodCallBinderSync \rpar MethodCallBinder \\

\circprocess Program \circdef \circblockopen Framework \lpar AppSync \rpar Bound\_Application \circblockclose \lpar LockingSync \rpar Locking
\end{circus}
<#else>
\begin{circus}
\circprocess Program \circdef \circblockopen Framework \lpar AppSync \rpar Application \circblockclose \lpar LockingSync \rpar Locking
\end{circus}
</#if>
%
