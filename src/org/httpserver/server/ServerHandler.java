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
 * ���������� http ��������
 * @author Kseniia
 *
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
		 		 
	/**
	 * ChannelHandlerContext ��������� ����������� ����������������� 
	 * � ������� ������������� 
	 * 
	 */
	@Override
     public void channelReadComplete(ChannelHandlerContext ctx) {
         ctx.flush(); 
     }
 	
	/**
	 * ����� - ���������� ��������.
	 * �� ������� �� http://somedomain/hello ������ �Hello World�.
	 * �� ������� �� http://somedomain/redirect?url=<url> ���������� 
	 * ������������� �� ��������� url.
	 * �� ������� �� http://somedomain/status �������� ����������:
	 * ����� ���������� ��������;
	 * ���������� ���������� �������� (�� ������ �� IP);
	 * ������� �������� �� ������ IP � ���� ������� � �������� � IP,
	 * ���-�� ��������, ����� ���������� �������;
	 * ���������� ������������� �� url'�� � ���� �������, 
	 * � ��������� url, ���-�� �������������
	 * ���������� ����������, �������� � ������ ������
	 */
    @SuppressWarnings("rawtypes")
	@Override
     public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException{
    	 // ���� ������ HttpRequest, �������� ��� � �������� �������.
    	 if (msg instanceof HttpRequest) {
    		   HttpRequest req = (HttpRequest) msg;
    		  
    		   String url = req.getUri();     //�������� url
    		   SocketAddress remoteAddress = ctx.channel().remoteAddress(); //�������� IP
    		   String ip = remoteAddress.toString().substring(1, remoteAddress.toString().indexOf(':'));    		       
    		     		 
    		  // ��������� ������� http://somedomain/hello
    		   if (url.equalsIgnoreCase("/hello")){    			   
    			   String str = "<p align='center'> <font size=14px >Hello world! =)</font></p>";
    			   FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,Unpooled.copiedBuffer(str.toString(),
    								Charset.forName("UTF-8")));
    			   response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");       
    			   response.headers().set(CONNECTION, Values.KEEP_ALIVE);              
    			   DBService.addIpToDB(ip);   //�������� ������ Ip � ���� ������
    	    	   DBService.addUrlToDB(url); 
    			   ctx.write(response);
    		    }
    		   // ��������� ������� http://somedomain/redirect?url=<url>
    		   else if (url.length()>=14 && url.substring(0,14).equalsIgnoreCase("/redirect?url=")){
    			   	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, MOVED_PERMANENTLY);
    			  	String u=url.substring(14);
    			    response.headers().set(LOCATION, u);
    			    DBService.addIpToDB(ip); 
    			    DBService.addUrlToDB(u);              //�������� ������ url � ���� ������
    			    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    		   }
    		   //��������� ������� http://somedomain/status
    		   else if (url.equalsIgnoreCase("/status")){
    			   DBService.addIpToDB(ip); 
    			   DBService.addUrlToDB(url);            //�������� ������ url � ���� ������
    			   int current = Server.getCurrConn();   //�������� ����������
    			   UrlService urlservice = new UrlService();
    			      			  
    			   StringBuilder str = new StringBuilder();	    			   
    			   str.append("<p align='center'> <font size=12px >������</font></p>"
    			   + "<p>����� ���������� ��������:"+" "+urlservice.getAllUrlCount()+"</p>"
    			   +"<p>���������� ���������� ��������:"+" "+urlservice.getUniqueUrl()+"</p>"
    			   +"<p>���������� �������� ����������:"+" "+current+"</p>"
    			   +"������� �������� �� ip:"
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
    			  
    			   str.append("<table><p>������� �������������:</p></table>"
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
