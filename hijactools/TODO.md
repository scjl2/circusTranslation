TODO 
=======


* Fix: the output of getNextMission ids.


* Make the Damn Thing Translate Both Examples!
	

	- Non paradigm objects are wrong. Weird no-name class...
	- `Buffer' in MCB should be BufferId, and in Consumer/Producer (eg)
           So, it's broadly that the Non-P variables are sometimes not needed, and when they are they're not the right type.

	

* Remove limitation that getNextMission must return ovject of the right type. I.E. returning a `Mission` makes TightRope crash, but returning a `MissionA` works.

* Remove limitation that TightRope crashes if you use the same schedulable twice, even in different missions.
