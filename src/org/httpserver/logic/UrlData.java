package org.httpserver.logic;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Getting data and time (Timestamp)of request to http server
 * @author Kseniia
 *
 */
public class UrlData {
	
	/**
	 * 
	 * @return t - data and time of request 
	 */
	public static Timestamp getTimestamp(){
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		Timestamp t = new Timestamp(now.getTime());		
		return t;		
	}
}
