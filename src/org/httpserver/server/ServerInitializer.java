package org.httpserver.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Класс для инициализации основного канала сервера.
 * Класс устанавливает обработчики для канала.
 * @author Kseniia
 *
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel>  {

	 @Override
     public void initChannel(SocketChannel ch) throws Exception {
         ChannelPipeline p = ch.pipeline(); //пул обработчиков
         p.addLast(new HttpServerCodec());  
         p.addLast(new ServerHandler());  
     }
}