package org.ddb.lejos.droid;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class DroidCommandPhoto implements DroidCommand {

	private DroidCallback _callback = null;
	private int _request_id = -1;
	public DroidCommandPhoto(DroidCallback callback,int request_id) {
		_callback = callback;
		_request_id = request_id;
	}

	@Override
	public String GetName() {
		// TODO Auto-generated method stub
		return "Photo";
	}

	@Override
	public DroidCallback GetCallback() {
		// TODO Auto-generated method stub
		return _callback;
	}

	@Override
	public void ExecuteLejOS(DroidLejOS state) throws Exception{
		DroidStatusPhoto result = new DroidStatusPhoto(200,_request_id);
		Mat mat = new Mat();

		try {
			state.CapturePhoto(mat);
			if (!mat.empty()) {
				MatOfByte buf = new MatOfByte();
				Highgui.imencode(".jpg", mat, buf);
				result.image_bytes = buf.toArray();
			}
	
		} catch (Exception ex) {
		} 

		if (GetCallback() != null)
			GetCallback().OnCompleted(result);

	}

}
