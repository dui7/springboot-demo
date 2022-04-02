package com.fentiaozi.netty.socket.common;

import com.fentiaozi.netty.socket.util.ByteIntUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 数据包解码器
 * <pre>
 * 数据包格式
 * +——--------——+——---——+——----——+——-------——+——-------——+——-------——+
 * |  包头   	| 包长  |  指令   | 设备id     | 告警类型   | 校验位     |
 * +——-------——+——---——+——----——+——-------——+——-------——+——-------——+
 * </pre>
 * 包头：固定为03-代表液探消息
 * 包长 ---命令 +设备id + 告警类型 + 校验位占用字节数
 * 指令 ---区分心跳命令和告警命令类型
 * 设备ID ---(唯一标识202106040001代表2021年6月4日提供的id为1的设备)
 * 告警类型 ---TY—10非金属危险TY—20非金属安全TY—30金属危险TY—40金属安全
 * 校验位：(单字节)采用校验和方式：命令，设备ID ,告警类型字段的各字节之和
 * 事例：液体容器
 * 03 07 66 00 00 00 00 0A 70
 *
 * @author lsl
 */
public class RequestDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(RequestDecoder.class);

    /**
     * 数据包基本长度
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        //创建字节数组,buffer.readableBytes可读字节长度
        byte[] bArray = new byte[buffer.readableBytes()];
        buffer.readBytes(bArray);
        //字节数组转换成十六进制字符串
        String str = ByteIntUtil.bytesToHexString(bArray);
        log.info("解码器收到的消息：,data长度:[{}],接收字符：[{}]", bArray.length, str);
        //解析出消息对象，继续往下面的handler传递
        out.add(str);
    }


}
