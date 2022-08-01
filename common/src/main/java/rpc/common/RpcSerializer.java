/*
 * Serializer.java

 */

package rpc.common;

import java.io.IOException;

/**
 * @author razertory
 * @date 2021/1/6
 */
public interface RpcSerializer {

    /**
     * java 对象转换为二进制
     *
     * @param object
     * @return
     * @throws IOException
     */
    byte[] serialize(Object object) throws IOException;


    /**
     * 二进制转换成 java 对象
     *
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException;
}
