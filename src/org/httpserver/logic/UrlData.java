package org.httpserver.logic;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * ����� ��� ��������� ���� � �������(Timestamp) ������� � http �������
 * @author Kseniia
 *
 */
public class UrlData {
	
	/**
	 * 
	 * @return t - ����� ������� ���� Timestamp 
	 */
	public static Timestamp getTimestamp(){
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		Timestamp t = new Timestamp(now.getTime());		
		return t;		
	}
}
