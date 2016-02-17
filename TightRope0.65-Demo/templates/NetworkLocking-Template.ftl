%%%%%%%%%%%%%%%%%%THREADS
%
\begin{circus}
\circprocess Threads \circdef  \\
\circblockopen
<#list Threads?keys as thread>
ThreadFW(${thread}, ${Threads[thread]}) \\
<#sep>\interleave \\</#sep>
</#list>
\circblockclose
\end{circus}
%
%%%%%%%%%%%%%%OBJECTS
%
\begin{circus}
\circprocess Objects \circdef \\
\circblockopen
<#list Objects.Objects as object>
ObjectFW(${object}) \\
<#sep>\interleave \\</#sep>
</#list>
\circblockclose
\end{circus}
%
%%%%%%%%%%%%%LOCKING
%
\begin{circus}
\circprocess Locking \circdef Threads \lpar ThreadSync \rpar Objects
\end{circus}
