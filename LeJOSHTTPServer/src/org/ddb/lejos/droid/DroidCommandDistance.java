package org.ddb.lejos.droid;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;

public class DroidCommandDistance implements DroidCommand {

	private DroidCallback _callback = null;
	private int _request_id = 01;
	
	public DroidCommandDistance(DroidCallback callback,int request_id){
		 _callback = callback;
		 _request_id = request_id;
	}
	
	@Override
	public String GetName() {
		// TODO Auto-generated method stub
		return "Distance";
	}
	
	@Override
	public DroidCallback GetCallback(){
		return _callback;
	}

	@Override
	public void ExecuteLejOS(DroidLejOS state)  throws Exception{
		DroidStatusDistance result = new DroidStatusDistance(200,_request_id);
    	result.distance = state.GetDistance();

        if(GetCallback()!=null) GetCallback().OnCompleted(result);

	}

}
