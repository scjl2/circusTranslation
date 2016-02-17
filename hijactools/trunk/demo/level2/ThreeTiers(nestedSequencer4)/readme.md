ThreeTiers Example (NestedSequncer4)
=======

Matt Luckcuck <ml881@york.ac.uk> 2014
-----------------------------------

### Classes: 12


### Features:
	* Three tiers
	* Multiple schedulable objects at each tier
	* Use of `ManagedThread`


This example program has three tiers. The first contains a `Mission`, a `ManagedThread`, and a nested `MissionSequencer`. 

The second tier contains the nested mission sequencers's single mission and its two nested mission sequencers.

The third tier contains two clusters, each having the single mission of one of the nested mission sequencers from the tier above and a `ManagedThread`. The execution is simple but this example is for testing the structure. 


