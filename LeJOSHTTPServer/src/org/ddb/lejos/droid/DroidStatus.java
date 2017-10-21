package org.ddb.lejos.droid;

public class DroidStatus {
	public DroidStatus(int result,int request_id){
		this.result = result;
		this.request_id = request_id;
	}
	public int    result     = 200;
	public int    request_id = -1;
}
