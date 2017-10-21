package org.ddb.lejos.droid;



import lejos.robotics.RegulatedMotor;

public class DroidCommandMove implements DroidCommand {

	private int _distance= 0;
	private int _request_id = 0;
	private DroidCallback _callback = null;
	
	public DroidCommandMove(DroidCallback callback,int distance,int request_id) {
		_callback = callback;
		_distance = distance;
		_request_id = request_id;
	}

	@Override
	public String GetName() {
		// TODO Auto-generated method stub
		return "Move";
	}

	@Override
	public DroidCallback GetCallback() {
		// TODO Auto-generated method stub
		return _callback;
	}

	@Override
	public void ExecuteLejOS(DroidLejOS state)  throws Exception{
		
		
		// TODO Auto-generated method stub
		DroidStatus result = new DroidStatus(200,_request_id);
		RegulatedMotor left  = state.GetLeftMotor();
		RegulatedMotor right = state.GetRightMotor();

		left.setSpeed(800);
		right.setSpeed(800);
		left.setAcceleration(500);
		right.setAcceleration(500);
		boolean error = false;
		double angle = ((double)_distance)*3648.0/1000.0;
		
		right.resetTachoCount();
		left.resetTachoCount();
		
		right.synchronizeWith(new RegulatedMotor[]{left});
		right.startSynchronization();
		right.rotate((int)angle,true);
		left.rotate((int)angle,true);
		right.endSynchronization();

		while((right.getTachoCount()+left.getTachoCount()<((int)angle*2))&&(!error)) {
			float sample[] = new float[4];
			state.GetAccelerometer(sample);
			System.out.println("x=" + sample[0] + ", y="+ sample[1] + ",z="+ sample[2]);
			
			if(Math.abs(sample[1])>1.5) error = true;
					else Thread.yield();
		}
		
		if(error){
			right.synchronizeWith(new RegulatedMotor[]{left});
			right.startSynchronization();
			right.stop();
			left.stop();
			right.endSynchronization();
		}

		
		
		if (GetCallback() != null)
			GetCallback().OnCompleted(result);

	}

}
