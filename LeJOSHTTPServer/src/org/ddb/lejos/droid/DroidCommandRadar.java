package org.ddb.lejos.droid;

import lejos.hardware.motor.EV3MediumRegulatedMotor;

public class DroidCommandRadar implements DroidCommand {

	private DroidCallback _callback = null;
	private int 		  _request_id=-1;
	public DroidCommandRadar(DroidCallback callback,int request_id){
		_callback = callback;
		_request_id = request_id;
	}
	
	@Override
	public String GetName() {
		// TODO Auto-generated method stub
		return "Radar";
	}

	@Override
	public DroidCallback GetCallback() {
		// TODO Auto-generated method stub
		return _callback;
	}

	@Override
	public void ExecuteLejOS(DroidLejOS state)  throws Exception{
		// TODO Auto-generated method stub
		DroidStatusRadar result = new DroidStatusRadar(200,_request_id);
		EV3MediumRegulatedMotor radar = state.GetRadarMotor();
    	
    	

    	result.values = new DroidStatusRadar.RadarValue[19];
    	int j = 0;
    	radar.resetTachoCount();
    	radar.setAcceleration(3000);
    	
    	for(int i=-45;i<=45;i+=5) {
    		radar.rotateTo(i,false);
    		int   tacho = radar.getTachoCount();
    		float distance = 0;
    		distance=state.GetDistance()*100;
    		
    		if(distance>=1600) distance=0;
    		
    		System.out.println(""+tacho+","+distance);
    		result.values[j++] = result. new RadarValue(tacho,distance);
    	}
    	radar.rotateTo(0,false);
    	
    	


        if(GetCallback()!=null) GetCallback().OnCompleted(result);

	}

}
