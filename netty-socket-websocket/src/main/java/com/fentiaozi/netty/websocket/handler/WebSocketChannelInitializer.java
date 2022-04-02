package com.fentiaozi.netty.websocket.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author visi
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
        //若5秒没有收到客户端发送来的数据则证明心跳丢失
//        ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
        ch.pipeline().addLast("http-handler", httpRequestHandler);
        ch.pipeline().addLast("websocket-handler", webSocketServerHandler);
//        ch.pipeline().addLast(new VisiServerHandler());
//        ch.pipeline().addLast(new RequestDecoder());
//        ch.pipeline().addLast(new ResponseEncoder());
    }

}
