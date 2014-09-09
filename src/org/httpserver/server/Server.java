package org.httpserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Основной класс http сервера, который возвращает контент 
 * на полученный http запрос 
 * @author Kseniia
 *
 */
public class Server {
	static final int port = 8080;	 //порт
	private static int currConn = 0; //текущее соединение	 
    
	/**
	 * Счетчик открытых соединений
	 * @return currConn - количество открытых соединений
	 */
	 public static int getCurrConn() {
	    	return currConn++;
		}	
	 
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
    	//Конфигурация сервера
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer());
            Channel ch = b.bind(port).sync().channel();
            currConn++;
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }    
    
}

