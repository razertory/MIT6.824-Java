/*
 * RpcRequest.java

 */

package rpc.common;

import lombok.Data;

/**
 * @author razertory
 * @date 2021/1/6
 */
@Data
public class RpcRequest {

    /**
     * 请求对象的 ID
     */
    private String requestId;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 入参
     */
    private Object[] parameters;

}
