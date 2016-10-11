TODO 
=======


* Fix: the output of getNextMission ids.
* Fix: adds startTime to OSEH apps



* Make the Damn Thing Translate Both Examples!
	- Threads not working (This neends setThreadPriority to be called in the right place)

	- Non paradigm objects need IDs producing (Urg, this needs a template and everything)

* Remove limitation that getNextMission must return ovject of the right type. I.E. returning a `Mission` makes TightRope crash, but returning a `MissionA` works.

* Remove limitation that TightRope crashes if you use the same schedulable twice, even in different missions.
