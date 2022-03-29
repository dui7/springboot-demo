package com.fentiaozi.netty.websocket.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fentiaozi.netty.websocket.model.vo.ResponseJson;
import com.fentiaozi.netty.websocket.service.BizService;
import com.fentiaozi.netty.websocket.util.ChannelHandlerPool;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author fentiaozi
 */
@Service
public class BizServiceImpl implements BizService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BizServiceImpl.class);

    @Override
    public void test(JSONObject param, ChannelHandlerContext ctx) {
        LOGGER.info("调用了test方法");
        String responseJson = new ResponseJson().success("test").toString();
        ChannelHandlerPool.sendMessage(ctx, responseJson);
    }

    @Override
    public void typeError(ChannelHandlerContext ctx) {
        String responseJson = new ResponseJson()
                .error("该类型不存在！")
                .toString();
        ChannelHandlerPool.sendMessage(ctx, responseJson);
    }

    @Override
    public void send(JSONObject param, ChannelHandlerContext ctx) {
        LOGGER.info("调用了send方法");
        String responseJson = new ResponseJson().success().toString();
        ChannelHandlerPool.sendMessage(ctx, responseJson);
    }


}
