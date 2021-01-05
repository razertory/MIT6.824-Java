/*
 * Encoder.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author gusu
 * @date 2021/1/1
 */
public class Encoder extends MessageToByteEncoder {
    private Class<?> clz;

    public Encoder(Class<?> clz) {
        this.clz = clz;
    }

    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf)
        throws Exception {
        byte[] bytes = JSON.toJSONBytes(o);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
