package com.fentiaozi.netty.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author fentiaozi
 */
@Component("channelInitializer")
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Resource(name = "httpRequestHandler")
    public ChannelHandler httpRequestHandler;
    @Resource(name = "webSocketServerHandler")
    public ChannelHandler webSocketServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // HTTP编码解码器
        ch.pipeline().addLast("http-codec", new HttpServerCodec());
        // 把HTTP头、HTTP体拼成完整的HTTP请求
        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
        ch.pipeline().addLast("http-handler", httpRequestHandler);
        ch.pipeline().addLast("websocket-handler", webSocketServerHandler);
        //若3秒没有收到客户端发送来的数据则证明心跳丢失
//        ch.pipeline().addLast(new IdleStateHandler(3, 0, 0, TimeUnit.SECONDS));
    }

}
