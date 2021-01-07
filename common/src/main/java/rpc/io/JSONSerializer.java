/*
 * JSONSerializer.java

 */

package rpc.io;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import rpc.common.Serializer;

/**
 * @author razertory
 * @date 2021/1/6
 */
public class JSONSerializer implements Serializer {
    public byte[] serialize(Object object) throws IOException {
        return JSON.toJSONBytes(object);
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        return JSON.parseObject(bytes,clazz);
    }
}
