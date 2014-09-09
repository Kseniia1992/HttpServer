package org.httpserver.server;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpResponseStatus.MOVED_PERMANENTLY;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.httpserver.service.DBService;
import org.httpserver.service.UrlService;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpHeaders.Values;

/**
 * Обработчик http запросов
 * @author Kseniia
 *
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
		 		 
	/**
	 * ChannelHandlerContext позволяет обработчику взаимодействовать 
	 * с другими обработчиками 
	 * 
	 */
	@Override
     public void channelReadComplete(ChannelHandlerContext ctx) {
         ctx.flush(); 
     }
 	
	/**
	 * Метод - обработчик запросов.
	 * По запросу на http://somedomain/hello отдает «Hello World».
	 * По запросу на http://somedomain/redirect?url=<url> происходит 
	 * переадресация на указанный url.
	 * По запросу на http://somedomain/status выдается статистика:
	 * общее количество запросов;
	 * количество уникальных запросов (по одному на IP);
	 * счетчик запросов на каждый IP в виде таблицы с колонкам и IP,
	 * кол-во запросов, время последнего запроса;
	 * количество переадресаций по url'ам в виде таблицы, 
	 * с колонками url, кол-во переадресация
	 * количество соединений, открытых в данный момент
	 */
    @SuppressWarnings("rawtypes")
	@Override
     public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException{
    	 // Если объект HttpRequest, записать его в значение запроса.
    	 if (msg instanceof HttpRequest) {
    		   HttpRequest req = (HttpRequest) msg;
    		  
    		   String url = req.getUri();     //получить url
    		   SocketAddress remoteAddress = ctx.channel().remoteAddress(); //получить IP
    		   String ip = remoteAddress.toString().substring(1, remoteAddress.toString().indexOf(':'));    		       
    		     		 
    		  // обработка запроса http://somedomain/hello
    		   if (url.equalsIgnoreCase("/hello")){    			   
    			   String str = "<p align='center'> <font size=14px >Hello world! =)</font></p>";
    			   FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,Unpooled.copiedBuffer(str.toString(),
    								Charset.forName("UTF-8")));
    			   response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");       
    			   response.headers().set(CONNECTION, Values.KEEP_ALIVE);              
    			   DBService.addIpToDB(ip);   //добавить объект Ip в базу данных
    	    	   DBService.addUrlToDB(url); 
    			   ctx.write(response);
    		    }
    		   // обработка запроса http://somedomain/redirect?url=<url>
    		   else if (url.length()>=14 && url.substring(0,14).equalsIgnoreCase("/redirect?url=")){
    			   	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, MOVED_PERMANENTLY);
    			  	String u=url.substring(14);
    			    response.headers().set(LOCATION, u);
    			    DBService.addIpToDB(ip); 
    			    DBService.addUrlToDB(u);              //добавить объект url в базу данных
    			    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    		   }
    		   //обработка запроса http://somedomain/status
    		   else if (url.equalsIgnoreCase("/status")){
    			   DBService.addIpToDB(ip); 
    			   DBService.addUrlToDB(url);            //добавить объект url в базу данных
    			   int current = Server.getCurrConn();   //открытые соединения
    			   UrlService urlservice = new UrlService();
    			      			  
    			   StringBuilder str = new StringBuilder();	    			   
    			   str.append("<p align='center'> <font size=12px >Статус</font></p>"
    			   + "<p>Общее количество запросов:"+" "+urlservice.getAllUrlCount()+"</p>"
    			   +"<p>Количество уникальных запросов:"+" "+urlservice.getUniqueUrl()+"</p>"
    			   +"<p>Количество открытых соединений:"+" "+current+"</p>"
    			   +"Счетчик запросов по ip:"
    			   +"<table><tr><td width='80'>IP</td><td width='150'>URL</td>"
    			   + "<td width='250'>LAST TIME</td></tr>");
    			      			   
    			   @SuppressWarnings("unchecked")
    			   List<Object> list = (List<Object>) urlservice.requestCounter();
    			   Iterator ip_itr = list.iterator();    			   
    			   while(ip_itr.hasNext()) {        				   
    				   Object[] obj = (Object[]) ip_itr.next();
    				   String ipadr = String.valueOf(obj[0]);     				   
    				   String urladr = String.valueOf(obj[1]);
    				   if (urladr.length()>24){
    					   urladr=urladr.substring(0, 24);
    				   }
    				   String last_time = String.valueOf(obj[2]); 
    				   str.append("<p><tr><td width='80'>"+ipadr+"</td>"
    				   		+ "<td width='150'>"+urladr+"</td><td width='250'>"+last_time+"</td></tr></p>");      				  
    			   }	  
    			  
    			   str.append("<table><p>Счетчик переадресаций:</p></table>"
    					   +"<table><tr><td width='150'>URL</td><td width='20'>COUNT</td></tr>");
    			   @SuppressWarnings("unchecked")
    			   List<Object> urllist = (List<Object>) urlservice.urlCounter();
    			   Iterator url_itr = urllist.iterator();    
    			   while(url_itr.hasNext()){
    			 	   Object[] obj = (Object[]) url_itr.next();
    				   String url_name = String.valueOf(obj[0]);
    				   if (url_name.length()>24){
    					   url_name=url_name.substring(0, 24);
    				   }
    				   String url_count = String.valueOf(obj[1]);
    				   str.append("<p><tr><td width='150'>"+url_name+"</td><td width='20'>"+url_count+"</td></tr></p>");	   			   
    			   }
    			  		   
    			   FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,Unpooled.copiedBuffer(str.toString(),
							Charset.forName("UTF-8")));    			  
		   response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");   
		   ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    			     			  
    	}
      }
    }   
}
