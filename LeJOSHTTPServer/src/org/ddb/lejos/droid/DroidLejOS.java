package org.ddb.lejos.droid;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.hardware.sensor.MindsensorsDistanceSensorV2;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class DroidLejOS {
	
	private MindsensorsDistanceSensorV2 	_sensor_us 	  = null;
	private EV3GyroSensor   _sensor_gyro  = null;
	private HiTechnicAccelerometer _accelerometer = null;
	private SampleProvider  _gyro_samples = null;
	private float			_gyro_tacho   = 0;
	private float			_angle[]	  = {0.0f};
	private RegulatedMotor  _left 		  = null;
	private RegulatedMotor  _right 		  = null;
	private EV3MediumRegulatedMotor  _radar		  = null;
	private VideoCapture 	_vid 		  = null;
	private Audio			_audio		  = null;
	private EV3				_ev3		  = null;
	
	public DroidLejOS(){
			
		_sensor_gyro = new EV3GyroSensor(SensorPort.S1);
		_radar		 = new EV3MediumRegulatedMotor(BrickFinder.getDefault().getPort("A"));
		_left 		 = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort("C"));
		_right 		 = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort("B"));
		_accelerometer = new HiTechnicAccelerometer(SensorPort.S3);
		_sensor_us 	 = new MindsensorsDistanceSensorV2(LocalEV3.get().getPort("S4"));	
		
		

		
		//if(!_sensor_us.isEnabled()) _sensor_us.enable();;
		_vid 		 = new VideoCapture(0);
		
		_ev3 		 = LocalEV3.get();
	    _audio 		 = _ev3.getAudio();
	      
		_vid.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 640);
		_vid.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 480);
		
		
		_audio.systemSound(Audio.DOUBLE_BEEP);
	}
	
	public void CapturePhoto(Mat mat) throws Exception{
		_vid.open(0);
		Thread.sleep(1500);
		//while(!_vid.isOpened()) Thread.yield();
		if(_vid.isOpened()){
		 _vid.read(mat);
		 _audio.systemSound(Audio.BEEP);
		 _vid.release();
		 
		} else _audio.systemSound(Audio.DOUBLE_BEEP);
		
	}
	

	public void GetAccelerometer(float[] sample){
		_accelerometer.fetchSample(sample, 0);
	}
	public void ResetGyro() {
	      if (_sensor_gyro != null) {
	         //Delay.msDelay(1000); //wait until the hands are off the robot
	         _sensor_gyro.reset();
	         _gyro_samples = _sensor_gyro.getAngleMode();
	         _gyro_tacho = 0;
	      }
	   }
	
	public float GetGyroAngleRaw() {
		_gyro_samples.fetchSample(_angle, 0);
	      return _angle[0];
	   }

	public float GetGyroAngle() {
	      float rawAngle = GetGyroAngleRaw();
	      return rawAngle - _gyro_tacho;
	   }
	
	public float GetDistance(){
		//_sensor_us.enable();
		try{
			
		SampleProvider sp = _sensor_us.getDistanceMode();
        
    	float[] sample = new float[sp.sampleSize()];
    	sp.fetchSample(sample, 0);
        if(!Float.isInfinite(sample[0])){
            return sample[0];
          } else return -1;
		}finally{
		//	_sensor_us.disable();
		}
	}
	
//	public EV3IRSensor GetIRSensor(){ return _sensor_ir;}
	public EV3MediumRegulatedMotor  GetRadarMotor() { return _radar;}
	public RegulatedMotor  GetLeftMotor() { return _left;}
	public RegulatedMotor  GetRightMotor() {return _right;}

}
