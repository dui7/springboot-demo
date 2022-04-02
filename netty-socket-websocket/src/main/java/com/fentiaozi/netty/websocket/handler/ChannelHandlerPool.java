package com.fentiaozi.netty.websocket.handler;

import com.fentiaozi.netty.websocket.exception.SeatIdNotFindException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 通道
 *
 * @author visi
 */
@Component
@Slf4j
public class ChannelHandlerPool {

    /**
     * 所有握手信息
     */
    public static ConcurrentMap<String, WebSocketServerHandshaker> webSocketHandshakerMap = new ConcurrentHashMap<String, WebSocketServerHandshaker>();
    /**
     * 所有连接Channel
     */
    private static ChannelGroup GlobalGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 坐席连接Channel
     */
    private static ConcurrentMap<String, ChannelId> seatId2ChannelMap = new ConcurrentHashMap();
    /**
     * ChannelId与坐席对应关系
     */
    private static ConcurrentMap<ChannelId, String> channelId2SeatIdMap = new ConcurrentHashMap();
    /**
     * 用户Id和坐席id对应关系
     */
    private static ConcurrentMap<String, String> userId2SeatIdMap = new ConcurrentHashMap();
    /**
     * 坐席id和用户Id对应关系
     */
    private static ConcurrentMap<String, String> seatId2UserIdMap = new ConcurrentHashMap();
    /**
     * 坐席id和坐席组id对应关系
     */
    private static ConcurrentMap<String, String> seatId2seatGroupIdMap = new ConcurrentHashMap();

    /**
     * 添加Channel
     *
     * @param seatId
     * @param channel
     */
    public static void addChannel2(String seatId, Channel channel) {
        GlobalGroup.add(channel);
        seatId2ChannelMap.put(seatId, channel.id());
        channelId2SeatIdMap.put(channel.id(), seatId);
    }

    /**
     * 添加用户和坐席之间的关系
     *
     * @param userId
     * @param seatId
     */
    public static void addUserId2SeatId(String userId, String seatId) {
        userId2SeatIdMap.put(userId, seatId);
    }

    public static String getSeatIdByUserId(String seatId) {
        return userId2SeatIdMap.get(seatId);
    }

    /**
     * 添加坐席和用户之间的关系
     *
     * @param seatId
     * @param userId
     */
    public static void addSeatId2UserId(String seatId, String userId) {
        seatId2UserIdMap.put(seatId, userId);
    }

    public static String getUserIdBySeatId(String seatId) {
        return seatId2UserIdMap.get(seatId);
    }

    /**
     * 添加坐席和坐席组之间的关系
     *
     * @param seatId
     * @param seatGroupId
     */
    public static void addSeatId2SeatGroupIdMap(String seatId, String seatGroupId) {
        seatId2seatGroupIdMap.put(seatId, seatGroupId);
    }

    public static String getSeatGroupIdBySeatId(String seatId) {
        return seatId2seatGroupIdMap.get(seatId);
    }

    public static String getSeatId(Channel channel) {
        String id = channelId2SeatIdMap.get(channel.id());
        return id;
    }

    /**
     * 通过SeatId查找Channel
     *
     * @param seatId
     * @return
     */
    private static Channel findSeatIdChannel(String seatId) {
        if (!seatId2ChannelMap.containsKey(seatId)) {
            throw new SeatIdNotFindException("seatId: " + seatId + " not exist");
        }
        return GlobalGroup.find(seatId2ChannelMap.get(seatId));
    }

    /**
     * 移除连接Channel
     *
     * @param channel
     */
    public static void removeChannel(Channel channel) {
//        String seatId = channelId2SeatIdMap.get(channel.id());
//        String userId = getUserIdBySeatId(seatId);
//        userId2SeatIdMap.remove(userId);
//        seatId2ChannelMap.remove(channelId2SeatIdMap.get(channel.id()));
//        channelId2SeatIdMap.remove(channel.id());
//        seatId2UserIdMap.remove(seatId);
//        seatId2seatGroupIdMap.remove(seatId);
        GlobalGroup.remove(channel);
        webSocketHandshakerMap.remove(channel.id().asLongText());
    }

    /**
     * 向坐席推送消息
     *
     * @param seatId
     * @param tws
     */
    public static void send2SeatId(String seatId, String tws) {
        findSeatIdChannel(seatId).writeAndFlush(new TextWebSocketFrame(tws));
    }

    public static void removeChannelByUserId(String userId) {
        String seatId = getSeatIdByUserId(userId);
        Channel channel = findSeatIdChannel(seatId);
        removeChannel(channel);
    }

}