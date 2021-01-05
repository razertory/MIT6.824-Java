/*
 * Rpc.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc;

import java.util.UUID;

/**
 * @author gusu
 * @date 2021/1/1
 */
public class Rpc {

    RpcResp call(RpcReq req) {
        req.setRpcId(UUID.randomUUID().toString());
    }
}
