\documentclass[10pt,a4paper]{article}
\usepackage[utf8]{inputenc}
\usepackage{amsmath}
\usepackage{amsfonts}
\usepackage{amssymb}
\usepackage[UKenglish]{babel}
\usepackage{xcolor}

\usepackage{circus}
\usepackage{hijac}

\usepackage[top=2cm, bottom=2cm, left=2cm, right=2cm]{geometry}

\title{${ProgramName}}

\author{Tight Rope ${Version}}

\date{\today}

\begin{document}

\maketitle

\section{ID Files}
\subsection{MissionIds}
\input{MissionIds.circus}
\newpage

\subsection{SchedulablesIds}
\input{SchedulableIds.circus}
\newpage

\subsection{Non-Paradigm Objects}
<#list NPE as npe >
\IfFileExists{${npe.Name}App.circus}{
\input{${npe.Name}App.circus}}{}

\IfFileExists{${npe.Name}Class.circus}{\newpage
\input{${npe.Name}Class.circus}}{}
\newpage
\IfFileExists{${Safelet.Name}MethChan.circus}{\newpage
\input{${Safelet.Name}MethChan.circus}}{}

</#list>
\newpage

\subsection{ThreadIds}
\input{ThreadIds.circus}
\newpage

\subsection{ObjectIds}
\input{ObjectIds.circus}
\newpage

\section{Network}
\subsection{Network Channel Sets}
\input{NetworkChan.circus}



\IfFileExists{NetworkMethodCallBinder.circus}{\newpage
\subsection{MethodCallBinder}

\input{MethodCallBindingChannels.circus}

\input{NetworkMethodCallBinder.circus}}
{}



\newpage

\subsection{Locking}
\input{NetworkLocking.circus}

\newpage

\subsection{Program}
\input{NetworkProgram.circus}

\input{Network.circus}
\newpage


\section{Safelet}
\input{${Safelet.Name}App.circus}
\IfFileExists{${Safelet.Name}Class.circus}{\newpage
\input{${Safelet.Name}Class.circus}}{}
\newpage
\IfFileExists{${Safelet.Name}MethChan.circus}{\newpage
\input{${Safelet.Name}MethChan.circus}}{}
\newpage

\section{Top Level Mission Sequencer}
\input{${TopLevelSequencer.Name}App.circus}
\IfFileExists{${TopLevelSequencer.Name}Class.circus}{\newpage
\input{${TopLevelSequencer.Name}Class.circus}}{}
\newpage
\IfFileExists{${TopLevelSequencer.Name}MethChan.circus}{\newpage
\input{${TopLevelSequencer.Name}MethChan.circus}}{}
\newpage

\section{Missions}

<#list Tiers as tier >

<#list tier as cluster>
\subsection{${cluster.Mission.Name}}
\input{${cluster.Mission.Name}App.circus}
\IfFileExists{${cluster.Mission.Name}Class.circus}{\newpage
\input{${cluster.Mission.Name}Class.circus}}{}
\newpage
\IfFileExists{${cluster.Mission.Name}MethChan.circus}{\newpage
\input{${cluster.Mission.Name}MethChan.circus}}{}
\newpage

<#assign schedulables = cluster.Schedulables.Threads + cluster.Schedulables.Oneshots + cluster.Schedulables.NestedSequencers + cluster.Schedulables.Aperiodics + cluster.Schedulables.Periodics>
\subsection{Schedulables of ${cluster.Mission.Name}}
<#list schedulables as schedulable>

\input{${schedulable.Name}App.circus}
\IfFileExists{${schedulable.Name}Class.circus}{\newpage
\input{${schedulable.Name}Class.circus}}{}
\newpage
\IfFileExists{${schedulable.Name}MethChan.circus}{\newpage
\input{${schedulable.Name}MethChan.circus}}{}

			<#if schedulable_has_next>
\newpage
			</#if>
		</#list>
</#list>
</#list>


\end{document}
