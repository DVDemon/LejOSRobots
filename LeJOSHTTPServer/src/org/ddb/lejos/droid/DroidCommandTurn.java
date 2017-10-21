package org.ddb.lejos.droid;

import lejos.robotics.RegulatedMotor;

public class DroidCommandTurn implements DroidCommand {

	private DroidCallback _callback = null;
	private int _angle;
	
	public DroidCommandTurn(DroidCallback callback,int angle){
		_callback = callback;
		_angle = angle;
	}
	
	@Override
	public String GetName() {
		// TODO Auto-generated method stub
		return "Turn";
	}

	@Override
	public DroidCallback GetCallback() {
		// TODO Auto-generated method stub
		return _callback;
	}

	@Override
	public void ExecuteLejOS(DroidLejOS state)  throws Exception{

		state.ResetGyro();

		state.GetRightMotor().resetTachoCount();
		state.GetLeftMotor().resetTachoCount();

		state.GetRightMotor().setAcceleration(1000);
		state.GetLeftMotor().setAcceleration(1000);
		state.GetRightMotor().setSpeed(300);
		state.GetLeftMotor().setSpeed(300);
	

		state.GetRightMotor().rotate(-_angle*1099/300,true);
		state.GetLeftMotor().rotate(_angle*1099/300,true);
		state.GetRightMotor().waitComplete();
		state.GetLeftMotor().waitComplete();

		
		System.out.println("Start:"+state.GetGyroAngle());
		;
		while (Math.abs(state.GetGyroAngle() - _angle) > 0) {
			
			int speed = (int)Math.abs(state.GetGyroAngle() - _angle)*3;
			if(speed>500) speed=500;
			if(speed<20) speed=20;
			

			state.GetRightMotor().setSpeed(speed);
			state.GetLeftMotor().setSpeed(speed);

			System.out.println("angle="+Math.abs(state.GetGyroAngle() - _angle)+" speed:"+speed);

			state.GetRightMotor().synchronizeWith(new RegulatedMotor[]{state.GetLeftMotor()});
			state.GetRightMotor().startSynchronization();
			
			if (_angle > state.GetGyroAngle()) {
				state.GetRightMotor().backward();
				state.GetLeftMotor().forward();
				state.GetRightMotor().endSynchronization();

				while (state.GetGyroAngle() < _angle); 
					Thread.yield();


			} else {
				state.GetRightMotor().forward();
				state.GetLeftMotor().backward();
				state.GetRightMotor().endSynchronization();

				while (state.GetGyroAngle() > _angle);
					Thread.yield();

			}
			state.GetLeftMotor().stop();
			state.GetRightMotor().stop();
			
			

		}
		
		System.out.println("left="+state.GetLeftMotor().getTachoCount()+", right="+state.GetRightMotor().getTachoCount());
		if(GetCallback()!=null) GetCallback().OnCompleted(new DroidStatus(200,-1));
	}

}
