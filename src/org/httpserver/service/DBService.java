package org.httpserver.service;

import java.util.HashSet;

import org.httpserver.logic.Ip;
import org.httpserver.logic.Url;
import org.httpserver.logic.UrlData;

/**
 * Класс для добавления данных в БД
 * @author Kseniia
 *
 */
public class DBService {

	/**
	 * Метод добавляет Ip адрес в базу данных
	 * @param ip - Ip адрес
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
	 * Метод добавляет Url в базу данных
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
