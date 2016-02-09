Nested Missions Sequencer Example (NestedSequencer2)
=======

Matt Luckcuck 15th of December 2014
-----------------------------------

###Classes : 14

### Features:
	* Mulitple Nested Mission Sequencers
	* `PeriodicEventHandler` class
	* `requestTermination` method

This example shows one `Mission` which holds three nested `MissionSequencer`s. Each nested mission sequencer has a single `Mission` that has a single `PeriodicEventHandler`. Each periodic event handler is fired a number of times (10 in the program but fewer in the model, for simplicity) and then calls `requestTermination` on its controlling mission.
