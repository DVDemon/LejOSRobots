package org.ddb.lejos.server;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ddb.lejos.droid.Droid;
import org.ddb.lejos.droid.DroidCallback;
import org.ddb.lejos.droid.DroidCommandPhoto;
import org.ddb.lejos.droid.DroidStatus;
import org.ddb.lejos.droid.DroidStatusPhoto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class PhotoHandler extends HttpServlet implements DroidCallback
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DroidStatusPhoto _status = null;
	
	public PhotoHandler(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	int request_id = -1;
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			if (paramName.compareToIgnoreCase("request_id") == 0) {
				String[] paramValues = request.getParameterValues(paramName);
				for (int i = 0; i < paramValues.length; i++) {
					String paramValue = paramValues[i];
					if (i == 0)
						request_id = Integer.parseInt(paramValue);
				}
			}
		}
		
    	Droid.GetDroid().ExecuteCommand(new DroidCommandPhoto(this,request_id));
        
    	
    	boolean wait = true;
    	
    	try{
    	while(wait){  		
    		synchronized(this) {
    			wait = (_status==null);
    		}    		
    		Thread.sleep(100);
    	}
    	}catch(Exception ex){
    		
    	}
    	
    	if(_status!=null){
    	 response.setContentType("image/jpg");// or png or gif, etc
		 response.setContentLength(_status.image_bytes.length);
		 response.getOutputStream().write(_status.image_bytes);
    	} else{
    		GsonBuilder builder = new GsonBuilder();
    		Gson gson = builder.create();
    		response.setContentType("text/html");
    		response.setStatus(HttpServletResponse.SC_OK);
    		response.getWriter().print(gson.toJson(new DroidStatus(500,request_id)));
    	}
    	_status = null;
    	
    	

	}

	@Override
	public void OnCompleted(DroidStatus status) {
		synchronized(this){
			if(status instanceof DroidStatusPhoto)
			_status = (DroidStatusPhoto)status;
		}
		
	}

}
