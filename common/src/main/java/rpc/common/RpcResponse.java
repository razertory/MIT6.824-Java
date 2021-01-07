/*
 * RpcResponse.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc.common;

import lombok.Data;

/**
 * @author gusu
 * @date 2021/1/7
 */
@Data
public class RpcResponse {
    String requestId;
    Object data;
}
