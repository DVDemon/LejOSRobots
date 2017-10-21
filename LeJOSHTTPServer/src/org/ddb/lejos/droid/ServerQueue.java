package org.ddb.lejos.droid;

import java.util.Map;
import java.util.Vector;

public class ServerQueue {

	protected Map<String,Vector<DroidStatusUltimate>> map;
	public ServerQueue() {
		// TODO Auto-generated constructor stub
	}
	
	public synchronized void Add(String host,DroidStatusUltimate status){
		if(map.containsKey(host)){
			Vector<DroidStatusUltimate> vector = map.get(host);
			vector.add(status);
		} else {
			Vector<DroidStatusUltimate> vector = new Vector<DroidStatusUltimate>();
			vector.add(status);
			map.put(host, vector);
		}
	}
	
	public synchronized Vector<DroidStatusUltimate> Unload(String host){
		Vector<DroidStatusUltimate> result = null;
		if(map.containsKey(host)){
			result = map.get(host);
			map.put(host, new Vector<DroidStatusUltimate>());			
		} 
		return result;
	}

}
