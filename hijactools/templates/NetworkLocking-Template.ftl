%%%%%%%%%%%%%%%%%%THREADS
%
\begin{circus}
\circprocess Threads \circdef  \\
\circblockopen
<#if Threads?has_content>
<#list Threads?keys as thread>
ThreadFW(${thread}, ${Threads[thread]}) \\
<#sep>\interleave \\</#sep>
</#list>
<#else>
\Skip
</#if>
\circblockclose
\end{circus}
%
%%%%%%%%%%%%%%OBJECTS
%
\begin{circus}
\circprocess Objects \circdef \\
\circblockopen
<#if Objects.Objects?has_content>
<#list Objects.Objects as object>
ObjectFW(${object}) \\
<#sep>\interleave \\</#sep>
</#list>
<#else>
\Skip
</#if>

\circblockclose
\end{circus}
%
%%%%%%%%%%%%%LOCKING
%
\begin{circus}
\circprocess Locking \circdef Threads \lpar ThreadSync \rpar Objects
\end{circus}
