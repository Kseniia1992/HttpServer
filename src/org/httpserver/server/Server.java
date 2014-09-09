package org.httpserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * �������� ����� http �������, ������� ���������� ������� 
 * �� ���������� http ������ 
 * @author Kseniia
 *
 */
public class Server {
	static final int port = 8080;	 //����
	private static int currConn = 0; //������� ����������	 
    
	/**
	 * ������� �������� ����������
	 * @return currConn - ���������� �������� ����������
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
    	//������������ �������
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

