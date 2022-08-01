/*
 * SampleServer.java
 * Copyright 2021 Razertory, all rights reserved.
 * GUSU PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc.io;


import java.util.HashMap;
import java.util.Map;

/**
 * @author gusu
 * @date 2021/6/26
 */
public class SampleServer extends RpcNode {

    public Map foo(String s) {
        return new HashMap(1);
    }
}
