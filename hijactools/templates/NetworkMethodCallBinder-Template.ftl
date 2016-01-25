\input{MethodCallBindingChannels.tex}

\begin{circus}
\circprocess  MethodCallBinder \circdef \circbegin \\
\end{circus}
%
<#list MethodCallBindings as mcb>
%
\begin{circusaction}
${mcb.Name}\_MethodBinder \circdef \\
	\t1 \circblockopen
	binder\_${mcb.Name}Call\\ \t1 ~?~loc\prefixcolon(loc \in ${mcb.Name}Locs)\\ \t1 ~?~caller\prefixcolon(caller \in ${mcb.Name}Callers) <#list mcb.Parameters?keys as paramName> \cross ${mcb.Parameters[paramName]} </#list> \then \\
	${mcb.Name}Call~.~loc~.~caller <#list mcb.Parameters?keys as paramName> \cross ${mcb.Parameters[paramName]} </#list> \then \\
	${mcb.Name}Ret~.~loc~.~caller<#if mcb.ReturnType != 'null'>~?~ret</#if> \then \\
	binder\_${mcb.Name}Ret~.~loc~.~caller<#if mcb.ReturnType != 'null'>~!~ret</#if>  \then \\
	${mcb.Name}\_MethodBinder
	\circblockclose
\end{circusaction}
%
</#list>
%
\begin{circus}
BinderActions \circdef \\
\circblockopen <#list MethodCallBindings as mcb>
	${mcb.Name}\_MethodBinder
	<#sep>\\ \interleave \\</#sep>
</#list>
\circblockclose
\end{circus}
%
\begin{circus}
\circspot BinderActions \circinterrupt (done\_toplevel\_sequencer \then \Skip)
\end{circus}
%
\begin{circus}
\circend
\end{circus}
%
\begin{circus}
\circprocess ApplicationB \circdef Application \lpar MethodCallBinderSync \rpar MethodCallBinder
\end{circus}
