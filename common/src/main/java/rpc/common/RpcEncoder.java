package rpc.common;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> clazz;
    private RpcSerializer rpcSerializer;

    public RpcEncoder(Class<?> clazz, RpcSerializer rpcSerializer) {
        this.clazz = clazz;
        this.rpcSerializer = rpcSerializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf)
        throws Exception {

        if (clazz != null && clazz.isInstance(o)) {

            //将对象序列化成二进制数组
            byte[] bytes = rpcSerializer.serialize(o);
            //获取长度
            byteBuf.writeInt(bytes.length);
            //写入 buffer 中
            byteBuf.writeBytes(bytes);
        }
    }

    /**
     * @author razertory
     * @date 2021/1/6
     */
    public static class JSONRpcSerializer implements RpcSerializer {

        public byte[] serialize(Object object) throws IOException {
            return JSON.toJSONBytes(object);
        }

        public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
            return JSON.parseObject(bytes, clazz);
        }
    }
}

