package com.fentiaozi.netty.socket.common;


import com.fentiaozi.netty.socket.util.ByteIntUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 编码器
 *
 * @author lsl
 */
@Slf4j
public class ResponseEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        String msg1 = msg.toString();
        byte[] command = ByteIntUtil.hexStringToByte(msg1);
        //byte [] command = new byte[]{(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03,0x04,(byte)0x05,(byte)0x06,(byte)0x07,(byte)0x08,(byte)0x08,(byte)0x02,(byte)0x03,(byte)0xFE,(byte)0x01,(byte)0x00,(byte)0x0D,(byte)0x0A};
        log.info("响应给客户端数据：[{}]<--------------------->[{}]", msg1, command);
        out.writeBytes(command);

        //
    }


}
