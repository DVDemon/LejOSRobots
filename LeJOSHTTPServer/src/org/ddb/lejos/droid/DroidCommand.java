package org.ddb.lejos.droid;

public interface DroidCommand {
	String GetName();
	DroidCallback GetCallback();
	void ExecuteLejOS(DroidLejOS state)  throws Exception;

}
