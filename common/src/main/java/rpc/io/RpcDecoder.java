/*
 * RpcDecoder.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> clazz;
    private Serializer serializer;

    public RpcDecoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        // 读取传送过来的消息的长度
        int dataLength = byteBuf.readInt();
        // 我们读到的消息体长度为 0，这是不应该出现的情况，这里出现这情况，关闭连接。
        if (dataLength < 0) {
            channelHandlerContext.close();
        }

        //取出数据
        byte[] bytes = new byte[dataLength];
        byteBuf.readBytes(bytes);  //
        //将 byte 数据转化为我们需要的对象。
        Object o = serializer.deserialize(clazz, bytes);
        list.add(o);
    }
}
