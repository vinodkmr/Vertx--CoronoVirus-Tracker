package com.vin.vertx.Util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utility {
	
	public static String getStackTrace(Throwable e) {
		StringWriter writer = new StringWriter(1024);
		e.printStackTrace( new PrintWriter( writer ) );
	    String stackTrace = writer.toString();
		return stackTrace;
	}

}
