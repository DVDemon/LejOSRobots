package org.ddb.lejos.server;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ddb.lejos.droid.Droid;
import org.ddb.lejos.droid.DroidCommandGoToWall;
import org.ddb.lejos.droid.DroidStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GoToWallHandler extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GoToWallHandler() {
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	
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
		
		Droid.GetDroid().ExecuteCommand(new DroidCommandGoToWall(request.getRemoteAddr()));

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().print(gson.toJson(new DroidStatus(200,request_id)));

	}
    

}
