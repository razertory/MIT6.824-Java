package rpc;

import lombok.Data;

/**
 * Rpc 请求
 * @author razertory
 * @date 2020/12/31
 */
@Data
public class RpcReq <T> {
    private String rpcId;
    private String className;
    private String method;
    private T data;
}
