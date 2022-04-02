package com.fentiaozi.netty.socket.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author visi
 */
@Slf4j
@Component("myServerHandler")
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 心跳丢失次数
     */
    private int counter = 0;

    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        log.info("收到客户端[ip: {} ]连接",clientIp);
    }


    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开链接：{}", ctx.channel().localAddress().toString());
        String channelId = ctx.channel().id().toString();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //业务处理
        handlerMessage(ctx,msg);
        //重置心跳丢失次数
        counter = 0;
    }
    /**
     * 服务端业务处理
     * @param ctx
     * @param request
     */
    private void handlerMessage(ChannelHandlerContext ctx,String request) {
        Map<String,Object> responseData = new HashMap<>(5);
//        RedisUtils redisUtils = SpringContextUtils.getBean(RedisUtils.class);
            //获取消息头
            String headMsg = request.substring(0, 4);

    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.error("异常信息：{}", cause.getMessage());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)){
                // 空闲5s之后触发 (心跳包丢失)
                if (counter >= 3) {
                    // 连续丢失3个心跳包 (断开连接)
                    ctx.channel().close().sync();
                    log.info("已与"+ctx.channel().remoteAddress()+"断开连接");
                } else {
                    counter++;
                    log.info(ctx.channel().remoteAddress() + "丢失了第 " + counter + " 个心跳包");
                }
            }
        }
    }

}
