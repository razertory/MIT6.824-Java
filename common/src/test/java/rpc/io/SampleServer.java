/*
 * SampleServer.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package rpc.io;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import util.LogUtil;

/**
 * @author gusu
 * @date 2021/6/26
 */
public class SampleServer extends RpcNode {

    public Map foo(String s) {
        return new HashMap(1);
    }

    public Set shutDown(String name) {
        shutDownServer();
        LogUtil.log("interrupted : " + name);
        return Collections.emptySet();
    }
}
