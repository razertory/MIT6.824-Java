package rpc;

import lombok.Data;

/**
 * Rpc 响应
 * @author razertory
 * @date 2020/12/31
 */
@Data
public class RpcResp <T>{
    private String rpcId;
    private T data;
}
