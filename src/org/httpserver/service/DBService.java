package org.httpserver.service;

import java.util.HashSet;

import org.httpserver.logic.Ip;
import org.httpserver.logic.Url;
import org.httpserver.logic.UrlData;

/**
 * ����� ��� ���������� ������ � ��
 * @author Kseniia
 *
 */
public class DBService {

	/**
	 * ����� ��������� Ip ����� � ���� ������
	 * @param ip - Ip �����
	 */
	@SuppressWarnings("unchecked")
	public static void addIpToDB(String ip){
		Ip ip_obj = new Ip();
		IpService ipservice = new IpService();
		ip_obj.setIp(ip);
		@SuppressWarnings("rawtypes")
		HashSet ipSet = new HashSet();
		ipSet.add(ip_obj);
		ipservice.addIP(ip_obj);
	}
	
	/**
	 * ����� ��������� Url � ���� ������
	 * @param url
	 */
	@SuppressWarnings("unchecked")
	public static void addUrlToDB(String url){
		Url url_obj = new Url();
		UrlService urlservice = new UrlService();
		url_obj.setUrl(url);
		url_obj.setData(UrlData.getTimestamp());
		@SuppressWarnings("rawtypes")
		HashSet urlSet = new HashSet();
		urlSet.add(url_obj);
		urlservice.addURL(url_obj);		
	}
}
