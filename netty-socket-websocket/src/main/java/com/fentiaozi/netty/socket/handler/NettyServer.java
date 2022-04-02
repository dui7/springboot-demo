package com.fentiaozi.netty.socket.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author visi
 */
@Slf4j
@Service
public class NettyServer {
    @Value("${netty.socket.port}")
    private int nettyServerPort;


    //配置服务端NIO线程组
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;

    @Resource(name = "myChannelInitializer")
    private ChannelHandler myChannelInitializer;

    private ChannelFuture serverChannelFuture;

    @PostConstruct
    public void build() {

        try {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)    //非阻塞模式
                    .option(ChannelOption.SO_BACKLOG, 2048)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(myChannelInitializer);

            serverChannelFuture = serverBootstrap.bind(nettyServerPort).sync();
            log.info("[netty]启动netty成功,端口:{}", nettyServerPort);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }


    @PreDestroy
    public void close() {
        serverChannelFuture.channel().close();
        Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workerGroup.shutdownGracefully();

        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
    }

}
