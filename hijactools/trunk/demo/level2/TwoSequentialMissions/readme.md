Two Sequential Missions Example
 =======

Matt Luckcuck <ml881@york.ac.uk> 2014
-----------------------------------

### Classes: 8 

### Features:
	* MissionSequencer with two `Mission`s
	* Use of `ManagedThread`
	* Reuse of schedulable objects in another mission


This example program has a two `Mission`s , each containing two `ManagedThread`s. The second mission reuses the managed threads from the first mission. Becasue this reuse occurs in a different mission, both instances of each `ManagedThread` gets a Framework/Application pair.


