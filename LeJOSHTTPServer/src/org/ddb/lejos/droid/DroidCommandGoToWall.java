package org.ddb.lejos.droid;

import org.ddb.lejos.droid.DroidStatusRadar.RadarValue;

import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class DroidCommandGoToWall implements DroidCommand {

	protected String _host;
	
	public DroidCommandGoToWall(String host){
	 _host = host;	
	}
	
	@Override
	public String GetName() {
		// TODO Auto-generated method stub
		return "GoToWall";
	}

	@Override
	public DroidCallback GetCallback() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ExecuteLejOS(DroidLejOS state)  throws Exception{
		// TODO Auto-generated method stub

		while (state.GetDistance() == -1) {
			state.GetRightMotor().setSpeed(800);
			state.GetLeftMotor().setSpeed(800);

			state.GetRightMotor().synchronizeWith(new RegulatedMotor[] { state.GetLeftMotor() });
			state.GetRightMotor().forward();
			state.GetLeftMotor().forward();

			state.GetRightMotor().endSynchronization();
			while (state.GetDistance() == -1)
				Thread.yield();

			state.GetLeftMotor().stop();
			state.GetRightMotor().stop();
		}

	}
	
	protected DroidStatusUltimate GetStatus(DroidLejOS state){
		DroidStatusUltimate result = new DroidStatusUltimate(200,0);
		EV3MediumRegulatedMotor radar = state.GetRadarMotor();
    	result.values = new DroidStatusRadar.RadarValue[19];
    	int j = 0;
    	radar.resetTachoCount();
    	for(int i=-45;i<=45;i+=5) {
    		radar.rotateTo(i);
    		while(radar.getTachoCount()!=i) Thread.yield();
    		result.values[j++] = result. new RadarValue(radar.getTachoCount(),state.GetDistance());
    	}
    	radar.rotateTo(0);
    	while(radar.getTachoCount()!=0) Thread.yield();
    	radar.stop(); 	
    	return result;
	}

}
