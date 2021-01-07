/*
 * RpcEncoder.java

 */

package rpc.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> clazz;
    private Serializer serializer;

    public RpcEncoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf)
        throws Exception {

        if (clazz != null && clazz.isInstance(o)) {

            //将对象序列化成二进制数组
            byte[] bytes = serializer.serialize(o);
            //获取长度
            byteBuf.writeInt(bytes.length);
            //写入 buffer 中
            byteBuf.writeBytes(bytes);
        }
    }
}

