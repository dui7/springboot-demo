package com.fentiaozi.netty.websocket.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fentiaozi
 */
public class ChannelHandlerPool {

    public static Map<String, WebSocketServerHandshaker> webSocketHandshakerMap = new ConcurrentHashMap();

    public static void sendMessage(ChannelHandlerContext ctx, String message) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }
}
