package com.fentiaozi.netty.socket.handler;


import com.fentiaozi.netty.socket.common.RequestDecoder;
import com.fentiaozi.netty.socket.common.ResponseEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author visi
 */
@Component("myChannelInitializer")
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Resource(name = "myServerHandler")
    public ChannelHandler myServerHandler;

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline().addLast(new IdleStateHandler(6000, 0, 0, TimeUnit.SECONDS));
        channel.pipeline().addLast(new RequestDecoder());
        channel.pipeline().addLast(new ResponseEncoder());
        // 在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(myServerHandler);
    }

}
