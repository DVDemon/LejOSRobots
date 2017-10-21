package org.ddb.lejos.droid;


public class DroidStatusRadar extends DroidStatus {
	public class RadarValue{
		public RadarValue(int a,float d){ angle = a;distance=d;};
		public int angle;
		public float distance;
	}
	
	public RadarValue[] values = null;
	public DroidStatusRadar(int result,int request_id) { super(result,request_id);}

}
