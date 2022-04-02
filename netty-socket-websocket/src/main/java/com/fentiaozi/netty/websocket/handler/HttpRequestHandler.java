package com.fentiaozi.netty.websocket.handler;

import com.fentiaozi.netty.websocket.util.RequestParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 通过HTTP请求建立连接，然后升级协议到webSocket
 *
 * @author visi
 */
@Component("httpRequestHandler")
@Sharable
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 读取完连接的消息后，对消息进行处理。
     * 这里仅处理HTTP请求，WebSocket请求交给下一个处理器。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            ctx.fireChannelRead(((WebSocketFrame) msg).retain());
        }
    }

    /**
     * 处理Http请求，主要是完成HTTP协议到Websocket协议的升级
     *
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //请求解析
        RequestParser requestParser = new RequestParser(req);
        //解析参数到Map
        Map<String, String> map = requestParser.parse();
        String seatId = map.get("seatId");
        String seatGroupId = map.get("seatGroupId");
        String realName = map.get("realName");
        String userId = map.get("userId");
//        String token = map.get("token");
        log.info("[WebSocket]创建连接,收到的连接请求url为: {}", req.uri());
        // 校验token,前端失败就会重新连接,这里前端获取不到这个response值,所以放弃校验token
//        boolean isVerifyPass = centralJudgmentService.verifyToken(userId, token);
//        if (Boolean.FALSE.equals(isVerifyPass)) {
//            sendHttpResponse(ctx, req,
//                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
//            return;
//        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws:/" + ctx.channel() + "/websocket", null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
        ChannelHandlerPool.webSocketHandshakerMap.put(ctx.channel().id().asLongText(), handshaker);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
//        connectSucces(ctx, userId, realName, seatId, seatGroupId);
    }

    private void connectSucces(ChannelHandlerContext ctx, String userId, String realName, String seatId, String seatGroupId) {
        ChannelHandlerPool.addChannel2(seatId, ctx.channel());
        ChannelHandlerPool.addUserId2SeatId(userId, seatId);
        ChannelHandlerPool.addSeatId2UserId(seatId, userId);
        ChannelHandlerPool.addSeatId2SeatGroupIdMap(seatId, seatGroupId);
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        boolean keepAlive = HttpUtil.isKeepAlive(req);
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 异常处理，关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
