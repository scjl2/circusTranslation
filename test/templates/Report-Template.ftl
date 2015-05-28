\documentclass[10pt,a4paper]{article}
\usepackage[utf8]{inputenc}
\usepackage{amsmath}
\usepackage{amsfonts}
\usepackage{amssymb}

\usepackage{xcolor}

\usepackage{circus}
\usepackage{hijac}

\usepackage[top=2cm, bottom=2cm, left=2cm, right=2cm]{geometry}

\begin{document}
\section{Network}
\input{Network.circus}
\newpage

\section{Safelet}
\input{${SafeletName}App.circus}
\newpage

\section{Top Level Mission Sequencer}
\input{${TopLevelSequencer}App.circus}
\newpage

\section{Missions}

<#list Tiers as tier >

<#list tier as cluster>
\subsection{${cluster.Mission}}
\input{${cluster.Mission}App.circus}
\newpage

<#assign schedulables = cluster.Schedulables.Threads + cluster.Schedulables.Oneshots + cluster.Schedulables.NestedSequencers + cluster.Schedulables.Aperiodics + cluster.Schedulables.Periodics>
\subsection{Schedulables of ${cluster.Mission}}
<#list schedulables as schedulable>

\input{${schedulable}App.circus}
			<#if schedulable_has_next>
\newpage
			</#if>
		</#list>
</#list>
</#list>


\end{document}