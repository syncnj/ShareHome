package com.sharehome.timers;

import com.backendless.servercode.annotation.BackendlessTimer;
    
/**
* TaskManagerTimer is a timer.
* It is executed according to the schedule defined in Backendless Console. The
* class becomes a timer by extending the TimerExtender class. The information
* about the timer, its name, schedule, expiration date/time is configured in
* the special annotation - BackendlessTimer. The annotation contains a JSON
* object which describes all properties of the timer.
*/
@BackendlessTimer("{'startDate':1492411200000,'frequency':{'schedule':'custom','repeat':{'every':3600}},'timername':'TaskManager'}")
public class TaskManagerTimer extends com.backendless.servercode.extension.TimerExtender
{
    
  @Override
  public void execute( String appVersionId ) throws Exception
  {
    // add your code here
    // duration is the days between each day


  }
    
}
        