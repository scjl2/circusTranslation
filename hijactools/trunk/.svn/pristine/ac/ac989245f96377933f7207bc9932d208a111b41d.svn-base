/* This file has been generated from /home/michal/paparazzi/conf/flight_plans/papabench-test.xml */
/* Please DO NOT EDIT */

#ifndef FLIGHT_PLAN_H
#define FLIGHT_PLAN_H 

#include "std.h"
#define FLIGHT_PLAN_NAME "demo 1"
#define NAV_UTM_EAST0 605530
#define NAV_UTM_NORTH0 5797350
#define NAV_UTM_ZONE0 32
#define QFU 270.0
#define WP_dummy 0
#define WP_HOME 1
#define WP_road_start 2
#define WP_road_end 3
#define WAYPOINTS { \
 {42.0, 42.0, 250},\
 {0.0, 0.0, 0.},\
 {-337.0, 17.0, 60.},\
 {238.0, -30.0, 40.},\
};
#define NB_WAYPOINT 4
#define NB_BLOCK 5
#define GROUND_ALT 0.
#define SECURITY_HEIGHT 25.
#define SECURITY_ALT 25.
#define MAX_DIST_FROM_HOME 500.
#ifdef NAV_C

static inline void auto_nav(void) {
  switch (nav_block) {
    Block(0) // Takeoff
    ; // pre_call
    if ((estimator_z>(ground_alt+25)) && (nav_block != 1)) { GotoBlock(1); return; }
    switch(nav_stage) {
      Stage(0)
        kill_throttle = 0;
        NextStageAndBreak();
        break;
      Stage(1)
        estimator_flight_time = 0;
        NextStageAndBreak();
        break;
      Stage(2)
        launch = 1;
        NextStageAndBreak();
        break;
      Stage(3)
        if (NavApproachingFrom(2,1,CARROT)) NextStageAndBreakFrom(2) else {
          NavGotoWaypoint(2);
          NavVerticalAutoThrottleMode(RadOfDeg(15));
          NavVerticalThrottleMode(9600*(1.000000));
        }
        break;
      Stage(4)
        NextBlock();
        break;
    }
    ; // post_call
    break;

    Block(1) // survey road 1
    ; // pre_call
    switch(nav_stage) {
      Stage(0)
        if (NavApproachingFrom(2,1,CARROT)) NextStageAndBreakFrom(2) else {
          NavGotoWaypoint(2);
          NavVerticalAutoThrottleMode(RadOfDeg(0.000000));
          NavVerticalAltitudeMode(WaypointAlt(2), 0.);
        }
        break;
      Stage(1)
        if (NavApproaching(3,CARROT)) NextStageAndBreakFrom(3) else {
          NavGotoWaypoint(3);
          NavVerticalAutoThrottleMode(RadOfDeg(0.000000));
          NavVerticalAltitudeMode(WaypointAlt(3), 0.);
        }
        break;
      Stage(2)
        NextBlock();
        break;
    }
    ; // post_call
    break;

    Block(2) // survey road 2
    ; // pre_call
    switch(nav_stage) {
      Label(while_1)
      Stage(0)
        if (! (TRUE)) Goto(endwhile_2) else NextStageAndBreak();
        Stage(1)
          if (NavApproachingFrom(2,3,CARROT)) NextStageAndBreakFrom(2) else {
            NavSegment(3, 2);
            NavVerticalAutoThrottleMode(RadOfDeg(0.000000));
            NavVerticalAltitudeMode(WaypointAlt(2), 0.);
          }
          break;
        Stage(2)
          if (NavApproachingFrom(3,2,CARROT)) NextStageAndBreakFrom(3) else {
            NavSegment(2, 3);
            NavVerticalAutoThrottleMode(RadOfDeg(0.000000));
            NavVerticalAltitudeMode(WaypointAlt(3), 0.);
          }
          break;
        Stage(3)
          Goto(while_1)
        Label(endwhile_2)
      Stage(4)
        NextBlock();
        break;
    }
    ; // post_call
    break;

    Block(3) // circle
    ; // pre_call
    switch(nav_stage) {
      Stage(0)
        NavVerticalAutoThrottleMode(RadOfDeg(0.000000));
        NavVerticalAltitudeMode((ground_alt+50), 0.);
        NavCircleWaypoint(3, 75);
        break;
      Stage(1)
        NextBlock();
        break;
    }
    ; // post_call
    break;

    Block(4) // HOME
    ; // pre_call
    switch(nav_stage) {
      Stage(0)
        nav_home();
        break;
      Stage(1)
        NextBlock();
        break;
    }
    ; // post_call
    break;

  }
}
#endif // NAV_C

#endif // FLIGHT_PLAN_H
