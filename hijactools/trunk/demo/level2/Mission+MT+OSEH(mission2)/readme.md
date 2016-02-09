Mission+MT+OSEH Example (Mission2)
=======

Matt Luckcuck <ml881@york.ac.uk> 2014
-----------------------------------

###Classes: 5

### Features:
	* Mission with Multiple Schedulable Objects
	* Schedulable Object calilng `requestTermination` on its `Mission`
	* Use of `ManagedThread`
	* Use of `OneShotEventHandler`
	* Triggers use of MethodCallBinder in model

This example program has a single `Mission` that contains a `ManagedThread` and a `OneShotEventHandler`. The `ManagedThread` runs, performs some systemActions, calling a method in the `Mission', then terminates. The `OneShotEventHandler` waits for its start time offset, is released, and calls `requestTermination` on its mission.


