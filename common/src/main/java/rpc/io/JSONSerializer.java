/*
 * JSONSerializer.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc.io;

import com.alibaba.fastjson.JSON;
import java.io.IOException;

/**
 * @author gusu
 * @date 2021/1/6
 */
public class JSONSerializer implements Serializer{
    public byte[] serialize(Object object) throws IOException {
        return JSON.toJSONBytes(object);
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        return JSON.parseObject(bytes,clazz);
    }
}
