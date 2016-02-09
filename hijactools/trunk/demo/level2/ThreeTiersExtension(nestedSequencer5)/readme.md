ThreeTiersExtension Example (NestedSequencer5)
==============================================

Matt Luckcuck <ml881@york.ac.uk> 2014
-------------------------------------

### Classes: 15

### Features:
	* Three tiers
	* Multiple schedulable objects at each tier
	* `requestTermination` method
	* Use of all types of schedulable object


This example program is an extension of the ThreeTires example and has three tiers. The first contains a `Mission`, a `ManagedThread`, and a nested `MissionSequencer`, the managed thread simple runs and terminates.

The second tier contains the nested mission sequencers's single mission and its two nested mission sequencers. 

The third tier contains two clusters, each having the single mission of one of the nested mission sequencers from the tier above. MissionA has a `ManagedThread` and a `OneShotEventHandler`, the managed thread runs and releases the one shot event handler, which terminates MissionA. MisisonB has a `PeriodicEventHandler` and an `AperiodicEventHandler`, the periodic event handler releases a number of times and then releases the aperiodic event handler, whcih terminates MissionB. 


