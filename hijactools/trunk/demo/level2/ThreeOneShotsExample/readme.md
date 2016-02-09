ThreeOneShots Example
=======

Matt Luckcuck <ml881@york.ac.uk> 2014
-----------------------------------

###Classes: 6 

### Features:
	* Mission with Multiple Schedulable Objects of the Same Type (`OneShotEventHandler`s)
	* Use of `OneShotEventHansdlers`


This example program has a single `Mission` that contains three `OneShotEventHansdlers`s, each of which will wait for their start time, run and terminate. The last handler will call `requestTermination` on the mission.


