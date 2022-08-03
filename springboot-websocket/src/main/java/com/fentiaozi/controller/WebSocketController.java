package com.fentiaozi.controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ruciya
 * @desc
 * @date 2022/8/3
 */
@ServerEndpoint("/ws/{id}")
@RestController
@Slf4j
public class WebSocketController {
    /**
     * 存储会话
     */
    private static ConcurrentHashMap<String, WebSocketController> webSocketId2SessionMap = new ConcurrentHashMap<>();
    private String id;
    private Session session;

    /**
     * 接入连接回调
     *
     * @param session 会话对象
     * @param id      会话ID
     * @throws Exception 异常
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) throws Exception {
        this.id = id;
        this.session = session;
        webSocketId2SessionMap.put(id, this);
        // 检验后端能否正常给前端发送信息
        sendMessageToId(this.id, "我是服务端");
        log.info("[webSocket]id:{},连接成功", id);
    }

    /**
     * 关闭连接回调
     */
    @OnClose
    public void onClose() {
        webSocketId2SessionMap.remove(this.id);
        log.info("[webSocket]id:{},关闭连接", id);
    }

    /**
     * 收到客户端发来消息回调
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("[webSocket]id:{},发来消息:{}", id, message);
    }

    /**
     * 会话出现错误回调
     *
     * @param error 错误信息
     */
    @OnError
    public void onError(Throwable error) {
        log.error("[webSocket]错误:{}", error);
    }

    /**
     * 发送消息给客户端
     *
     * @param message 消息
     * @throws IOException 异常
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 给指定的会话发送消息
     *
     * @param id      会话ID
     * @param message 消息
     * @throws IOException 异常
     */
    public void sendMessageToId(String id, String message) throws IOException {
        webSocketId2SessionMap.get(id).sendMessage(message);
    }

    /**
     * 群发消息
     *
     * @param message 消息
     * @throws IOException 异常
     */
    public void sendMessageToAll(String message) throws IOException {
        for (String key : webSocketId2SessionMap.keySet()) {
            try {
                webSocketId2SessionMap.get(key).sendMessage(message);
            } catch (Exception e) {
                log.error("[webSocket]发送消息错误:{}", e.getMessage(), e);
            }
        }
    }

}

