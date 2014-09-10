package org.httpserver.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Class for initialization of main channel of server.
 * Setting handlers for channel.
 * @author Kseniia
 *
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel>  {

	 @Override
     public void initChannel(SocketChannel ch) throws Exception {
         ChannelPipeline p = ch.pipeline(); //pipeline of handlers
         p.addLast(new HttpServerCodec());  
         p.addLast(new ServerHandler());  
     }
}