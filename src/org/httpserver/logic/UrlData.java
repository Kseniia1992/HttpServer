package org.httpserver.logic;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Класс для получения даты и времени(Timestamp) запроса к http серверу
 * @author Kseniia
 *
 */
public class UrlData {
	
	/**
	 * 
	 * @return t - время запроса типа Timestamp 
	 */
	public static Timestamp getTimestamp(){
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		Timestamp t = new Timestamp(now.getTime());		
		return t;		
	}
}
