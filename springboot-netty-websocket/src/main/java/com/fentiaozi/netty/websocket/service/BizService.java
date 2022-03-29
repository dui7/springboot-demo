package com.fentiaozi.netty.websocket.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

public interface BizService {

    void test(JSONObject param, ChannelHandlerContext ctx);

    void typeError(ChannelHandlerContext ctx);

    void send(JSONObject param, ChannelHandlerContext ctx);
}
