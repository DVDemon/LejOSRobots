package org.ddb.lejos.droid;

import java.util.LinkedList;
import java.util.Queue;

import org.ddb.lejos.server.GoToWallHandler;
import org.opencv.core.Core;

import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RegulatedMotor;


public class Droid{
	private static Droid _droid = null;
	

	private DroidLejOS			_state    = null;
	private Queue<DroidCommand> _commands = null;
	private boolean				_running      = true;
	private ServerQueue         _queue	  = new ServerQueue();
	
	private Droid(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);	
		_state		= new DroidLejOS();
		_commands   = new LinkedList<DroidCommand>();
		
		
		new Thread(new Runnable() {
			public void run() {
				try {
					Run();
				} catch (Exception ex) {
					System.out.println("Droid Exception");
				} finally{
					
				}
			}
		}).start();	
	}
	
	private void Run() throws Exception{
		
		boolean _continue = true;
		System.out.println("Droid Active");
		do{
			
			synchronized(this){
				_continue = _running;
			}
			
			if(_continue){
				
				if(!_commands.isEmpty()){
					DroidCommand command = null;
					synchronized(_commands){
						command =_commands.remove();
					}
					System.out.println(command.GetName());
					try{
					 command.ExecuteLejOS(_state);
					}catch(Exception ex){
					 System.out.println(ex.getMessage());	
					} finally{
						System.out.println("Done");
					}
				} else Thread.yield();
			}
			
		}while(_continue);
		System.out.println("Droid Stopped");
	}
	
	public ServerQueue GetServerQueue(){
		return _queue;
	}
	
	public synchronized static Droid GetDroid(){
		if(_droid == null) _droid = new Droid();
		return _droid;
	}
	
	public void ExecuteCommand(DroidCommand c){
		
		synchronized(_commands){
				_commands.add(c);
		}
	}
	
	public synchronized void Stop(){
		_running = false;
	}

}
